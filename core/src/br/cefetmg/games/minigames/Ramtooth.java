package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import java.util.HashMap;

/**
 *
 * @author afp11
 */
public class Ramtooth extends MiniGame{
    
    private final Array<Tooth> teeth;
    private DenteRamtooth ramtooth;
    
    private final Texture toothTexture;
    private final Texture ramtoothTexture;
 
    private int alvo;
    private int numberOfCleanTeeth = 0;
    
    private int spawnInterval;
    private float minimumEnemySpeed;
    private float maximumEnemySpeed;
    
    private final Texture texturaFundo;
    private final Sprite fundo;
    
    private final Texture texturaTiro;
    private final Array<Shot> shots;
    
    private final Sound somTiroPastaDeDente;
    private final Sound somDenteFicandoBranco;
          
    public Ramtooth(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10f, 
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        
        this.texturaFundo = this.screen.assets.get(
                "ramtooth/fundo2.png", Texture.class);
        this.fundo = new Sprite(this.texturaFundo);
        this.fundo.setOriginCenter();
        
        float escalaFundo = 1.4f;
        float escalaX = Gdx.graphics.getWidth() * escalaFundo;
        float escalaY = Gdx.graphics.getHeight() * escalaFundo;
        this.fundo.setSize(escalaX, escalaY);

        this.teeth = new Array<Tooth>();
        this.toothTexture = this.screen.assets.get(
                "ramtooth/spritedente.png", Texture.class);  
        
        this.ramtoothTexture = this.screen.assets.get(
                "ramtooth/spriterambo.png", Texture.class);
        this.ramtooth = new DenteRamtooth(this.ramtoothTexture);
        
        this.shots = new Array<Shot>();
        this.texturaTiro = this.screen.assets.get(
                "ramtooth/spritetiro.png", Texture.class);
        
        this.somTiroPastaDeDente = screen.assets.get(
                "ramtooth/tiro.mp3", Sound.class);   
        this.somDenteFicandoBranco = screen.assets.get(
                "ramtooth/dente-branco.mp3", Sound.class);
    }

    @Override
    protected void onStart() {
        super.timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                spawnTooth();
            }

        }, 0, this.spawnInterval);
        
        this.spawnRamtooth();
    }
    
    private void spawnRamtooth(){
        
        Vector2 ramboPosition = new Vector2();

        ramboPosition.x = 0;
        ramboPosition.y = (Gdx.graphics.getHeight() / 2);
        
        DenteRamtooth rambo = new DenteRamtooth(ramtoothTexture);
        rambo.setPosition(ramboPosition.x, ramboPosition.y);
        rambo.setSpeed(new Vector2());
        
        float novaEscala = 0.4f;
        rambo.setScale(rambo.getScaleX()*novaEscala, 
                rambo.getScaleY()*novaEscala);
        
        this.ramtooth = rambo;
           
    }
    
    private void spawnTooth(){
        
        TextureRegion[][] frames = TextureRegion.split(toothTexture,
                Tooth.FRAME_WIDTH, Tooth.FRAME_HEIGHT);
        
        Vector2 toothPosition = new Vector2();

        toothPosition.x = Config.WORLD_WIDTH;
        toothPosition.y = MathUtils.random(Gdx.graphics.getHeight() - 200);
        
        Vector2 toothGoal = new Vector2(0, toothPosition.y);
        Vector2 toothSpeed = toothGoal.sub(toothPosition)
                .nor().scl(this.minimumEnemySpeed);
        
        Tooth dirtyTooth = new Tooth(frames[1][0], frames[0][0], 0);
        
        dirtyTooth.setPosition(toothPosition.x, toothPosition.y);
        dirtyTooth.setSpeed(toothSpeed);
        
        float novaEscala = 0.5f;
        dirtyTooth.setScale(dirtyTooth.getScaleX()*novaEscala, 
                dirtyTooth.getScaleY()*novaEscala);
        
        teeth.add(dirtyTooth);
    }
    
    private void spawnShot(float posY){
        
        Vector2 shotPosition = new Vector2();

        shotPosition.x = 200;        
        shotPosition.y = posY + 180;
        
        Vector2 shotGoal = new Vector2(Config.WORLD_WIDTH, shotPosition.y);
        Vector2 shotSpeed = shotGoal.sub(shotPosition)
                .nor().scl(10000);
        
        Shot shot = new Shot(texturaTiro);

        shot.setPosition(shotPosition.x, shotPosition.y);
        shot.setSpeed(shotSpeed);
                
        float novaEscala = 0.9f;
        shot.setScale(shot.getScaleX()*novaEscala, 
                shot.getScaleY()*novaEscala);
        
        shots.add(shot);
        
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 30, 60);
        this.maximumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 70, 120);
        this.spawnInterval = (int) DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 1.5f);
        this.alvo = (int) Math.ceil(maxDuration / spawnInterval) - 3;
    }

    @Override
    public void onHandlePlayingInput() {
        // atualiza a posição do alvo de acordo com o mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        super.screen.viewport.unproject(click);
        this.ramtooth.setPosition(0, click.y);
        
        if (Gdx.input.justTouched()){
            spawnShot(click.y);
            this.somTiroPastaDeDente.play();
        }
        
        if (numberOfCleanTeeth >= alvo){
            super.challengeSolved();
        }        
    }

    @Override
    public void onUpdate(float dt) {
        
        ramtooth.update(dt);

        for (int i = 0; i < this.teeth.size; i++) {
            Tooth t = this.teeth.get(i);
            t.update(dt);
        }
        
        for (int i = 0; i < shots.size; i++) {
            for (int j = 0; j < teeth.size; j++){
                if (shots.get(i).getBoundingRectangle().overlaps(teeth.get(j).getBoundingRectangle())){
                    if (teeth.get(j).acertos < 1){
                        teeth.get(j).changeState();
                        this.somDenteFicandoBranco.play();
                        shots.removeIndex(i);
                        break;
                    } 
                }
            }
        }
        
        for (int j = 0; j < teeth.size; j++){
            if (ramtooth.getBoundingRectangle().overlaps(teeth.get(j).getBoundingRectangle())){
                if (teeth.get(j).acertos == 0){
                    super.challengeFailed();
                } else {
                    teeth.removeIndex(j);
                }
            }
        }
        
        for (int i = 0; i < this.shots.size; i++){
            Shot s = this.shots.get(i);
            s.update(dt);
        }
    }

    @Override
    public void onDrawGame() {
        
        fundo.draw(this.screen.batch);

        ramtooth.draw(super.screen.batch);
      
        for (Tooth t : this.teeth) {
            t.draw(super.screen.batch);
        }
        
        for (Shot s : this.shots){
            s.draw(super.screen.batch);
        }
               
    }

    @Override
    public String getInstructions() {
        return "Limpe " + this.alvo + " dentes antes que o tempo acabe "
                + "e evite dente sujo no Ramtooth!";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
    class Tooth extends Sprite {

        private final TextureRegion denteLimpo;
        private int acertos = 0;

        static final int FRAME_WIDTH = 136;
        static final int FRAME_HEIGHT = 144;
        
        private Vector2 speed;

        public Tooth(TextureRegion denteSujo, TextureRegion denteLimpo,
                int acertos) {
            super(denteSujo);
            this.denteLimpo = denteLimpo;
            this.acertos = acertos;
        }

        public void changeState() {
            acertos++;
            if (this.acertos < 2){
                super.setRegion(denteLimpo);
                numberOfCleanTeeth++;
            } 
        }
        
        public void update(float dt) {
            super.setPosition(super.getX() + this.speed.x * dt * 5,
                    super.getY());
        }
        
        public Vector2 getSpeed() {
            return speed;
        }

        public void setSpeed(Vector2 speed) {
            this.speed = speed;
        }
    }
    
    class DenteRamtooth extends MultiAnimatedSprite {

        private Vector2 speed;

        static final int FRAME_WIDTH = 433;
        static final int FRAME_HEIGHT = 377;

        public DenteRamtooth(final Texture spriteSheet) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(spriteSheet,
                                    FRAME_WIDTH, FRAME_HEIGHT);
                    Animation walking = new Animation(0.2f,
                            frames[0][1],
                            frames[0][0],
                            frames[1][0]);
                    walking.setPlayMode(Animation.PlayMode.LOOP);
                    put("walking", walking);
                }
            }, "walking");
        }
        
        @Override
        public void update(float dt) {
            super.update(dt);
            super.setPosition(super.getX(), super.getY());
        }
        
        public Vector2 getSpeed() {
            return speed;
        }

        public void setSpeed(Vector2 speed) {
            this.speed = speed;
        }
        
    }
    
    class Shot extends MultiAnimatedSprite {

        private Vector2 speed;

        static final int FRAME_WIDTH = 54;
        static final int FRAME_HEIGHT = 33;

        public Shot(final Texture spriteSheet) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(spriteSheet,
                                    FRAME_WIDTH, FRAME_HEIGHT);
                    Animation flying = new Animation(0.2f,
                            frames[0][0],
                            frames[1][0],
                            frames[2][0]);
                    flying.setPlayMode(Animation.PlayMode.LOOP);
                    put("flying", flying);
                }
            }, "flying");
        }
        
        @Override
        public void update(float dt) {
            super.update(dt);
            float novoX = super.getX() + 8;
            super.setPosition(novoX, super.getY());
        }
        
        public Vector2 getSpeed() {
            return speed;
        }

        public void setSpeed(Vector2 speed) {
            this.speed = new Vector2(speed.x, speed.y);
        }
        
    }
    
}
