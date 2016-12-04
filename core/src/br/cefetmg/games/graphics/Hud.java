package br.cefetmg.games.graphics;

import br.cefetmg.games.Config;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import java.util.HashMap;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class Hud {

    private final Stage stage;
    private Table table;
    private Label sequenceIndexLabel;
    private HorizontalGroup livesGroup;
    
    private Texture lifeTexture;
    private Texture clockTexture;
    private Sound timerSound;
    private Clock clock;
    
    //Variável de referência para quantidade de dentes inteiros by Bruno e Carlos
    private int dentes;
    
    public Hud(BaseScreen screen) {
        stage = new Stage(screen.viewport, screen.batch);
    }

    public void create() {
        lifeTexture = new Texture("images/lives.png");
        clockTexture = new Texture("images/relogio.png");
        
        // dentes é atribuída com a quantidade de dentes ou vidas inicial by 
        // Bruno e Carlos
        dentes = Config.MAX_LIVES;
        
        BitmapFont font = new BitmapFont(
                Gdx.files.internal("fonts/sawasdee-50.fnt"));

        table = new Table();
        table.bottom();
        table.setFillParent(true);

        sequenceIndexLabel = new Label(
                String.format("%03d", 1), new LabelStyle(font, Color.ORANGE));
        
        livesGroup = new HorizontalGroup();
        for (int i = 0; i < Config.MAX_LIVES; i++) {
            livesGroup.addActor(new LifeHeart(lifeTexture));
        }

        clock =new Clock(clockTexture);
        
        table.padBottom(10);
        
        table.add(clock).uniformX();
        table.add(livesGroup).uniformX();
        table.add(sequenceIndexLabel).uniformX();

        stage.addActor(table);
        
        timerSound = Gdx.audio.newSound(Gdx.files.internal("ui/tick-tock.mp3"));
        
        // como não há animação para os dentes inteiros eles são apenas 
        // desenhados uma vez na tela by Bruno e Carlos
        for (int i = 0; i < Config.MAX_LIVES; i++) {
            LifeHeart heart = ((LifeHeart) livesGroup.getChildren().get(i));
            heart.alive();
        }
    }

    public void update(float dt) {
        stage.act(dt);
    }

    public void draw() {
        stage.draw();
    }

    public void setGameIndex(int index) {
        sequenceIndexLabel.setText(String.format("%03d", index));
    }

    public void setLives(int lives) {
        if (lives < 0 || lives > Config.MAX_LIVES) {
            throw new IllegalArgumentException("A HUD está tentando mostrar "
                    + "um número de vidas menor que 0 ou maior que "
                    + Config.MAX_LIVES + ".");
        }
        //Contabiliza os dentes de acordo com as vidas(lives) e executa a animação de um dente quebrando
        //para apenas um dente inteiro disponível em sequência by Bruno e Carlos
        if(lives < dentes && dentes != 0){
            LifeHeart heart = ((LifeHeart) livesGroup.getChildren().get(dentes-1));
            heart.die();
            dentes--;
        }
                
    }

    public void startEndingTimer() {
        clock.startTicking();
    }
    
    public void cancelEndingTimer() {
        clock.stopTicking();
    }

    public void pauseEndingTimer() {
        clock.pauseTicking();
    }

    public void resumeEndingTimer() {
        clock.resumeTicking();
    }
    
    class Clock extends Actor {
    
        private final MultiAnimatedSprite sprite;
        private static final int FRAME_WIDTH = 50;
        private static final int FRAME_HEIGHT = 50;
        private float timeTickingThisSecond;
        private boolean isTicking;
        private int ticksDone;

        Clock(Texture clockTexture) {
            TextureRegion[][] frames = TextureRegion
                    .split(clockTexture, FRAME_WIDTH, FRAME_HEIGHT);
            Animation clock = new Animation(1f, 
                    frames[0][0], 
                    frames[0][1],
                    frames[0][2],
                    frames[0][3],
                    frames[0][4]);
            Animation empty = new Animation(1f, frames[0][5]);
            
            clock.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
            HashMap<String, Animation> animations
                    = new HashMap<String, Animation>();
            animations.put("empty",empty);
            animations.put("clock", clock);
            sprite = new MultiAnimatedSprite(animations, "empty");
            sprite.setCenterFrames(true);
            sprite.setUseFrameRegionSize(true);
            setWidth(sprite.getWidth());
            setHeight(sprite.getHeight());
            resetTicking();
        }

        @Override
        public void draw(Batch batch, float alpha) {
            sprite.draw(batch, alpha);
        }

        @Override
        public void act(float dt) {
            super.act(dt);
            sprite.setPosition(getX(), getY());
            if (isTicking) {
                sprite.update(dt);
                timeTickingThisSecond += dt;
                if (timeTickingThisSecond > 1f) {
                    timeTickingThisSecond -= 1f;
                    timerSound.play();
                    
                    if (++ticksDone > 3) {
                        stopTicking();
                    }
                }
            }
        }

        void resetTicking() {
            this.isTicking = false;
            this.ticksDone = 0;
            this.timeTickingThisSecond = 0;
        }
        
        void startTicking() {
            this.isTicking = true;
            timerSound.play();
            sprite.startAnimation("clock");
        }
        
        void resumeTicking() {
            this.isTicking = true;
        }
        
        void pauseTicking() {
            this.isTicking = false;
        }
        
        void stopTicking() {
            resetTicking();
            sprite.startAnimation("empty");
            sprite.update(1);
            timerSound.stop();
        }
    }

    class LifeHeart extends Actor {

        private final MultiAnimatedSprite sprite;
        private static final int FRAME_WIDTH = 100;
        private static final int FRAME_HEIGHT = 112;
        
        LifeHeart(Texture lifeTexture) {
            TextureRegion[][] frames = TextureRegion
                    .split(lifeTexture, FRAME_WIDTH, FRAME_HEIGHT);
            Animation alive = new Animation(1f, frames[0][5]);
            //O intervalo de tempo da animação foi incrementado by Bruno e Carlos
            Animation dying = new Animation(.3f,new TextureRegion[]{frames[0][4], frames[0][3], frames[0][2], frames[0][1],frames[0][0]});
            HashMap<String, Animation> animations
                    = new HashMap<String, Animation>();
            animations.put("alive", alive);
            animations.put("dying", dying);
            
            sprite = new MultiAnimatedSprite(animations, "alive");
            sprite.setCenterFrames(true);
            sprite.setUseFrameRegionSize(true);
            setPosition(0, 0);
            setWidth(sprite.getWidth());
            setHeight(sprite.getHeight());
        }
        
        @Override
        public void draw(Batch batch, float alpha) {    
            sprite.draw(batch, alpha);
        }
        @Override
        public void act(float dt) {
            sprite.update(dt);
            super.act(dt);
            sprite.setPosition(getX(), getY());
            sprite.update(dt);
        }

        public void die() {
            sprite.startAnimation("dying");
        }
        
        public void alive() {
            sprite.startAnimation("alive");
        }
    }
}