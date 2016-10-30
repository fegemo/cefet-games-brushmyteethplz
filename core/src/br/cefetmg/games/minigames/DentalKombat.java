package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import br.cefetmg.games.minigames.util.GameStateObserver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DentalKombat extends MiniGame{
    
    private Vector2 playerPosition = new Vector2();
    
    private final Texture backGroundTexture;
    private final Texture toothDefendSheet;
    private final Texture toothAttackSheet;
    private final Texture cariesSheet;
    private final Sound toothIsHitSound;
    private final Sound cariesIsHitSound;
    private int toothHealth = 3;
    private int cariesHealth = 3;
    
    //animacoes
    private TextureRegion[][] framesToothDefend;
    private TextureRegion[][] framesToothAttack;
    private TextureRegion[][] framesCaries;
    private TextureRegion currentFrameTooth;
    private TextureRegion currentFrameCaries;
    private float stateTimeTooth = 0;
    private Animation toothDefend;
    private Animation toothAttack;
    private Animation caries;
    
    // constantes
    private static final int INITIAL_POSX_TOOTH = 350;
    private static final int INITIAL_POSY_TOOTH = 75;
    private static final int INITIAL_POSX_CARIE = 600;
    private static final int INITIAL_POSY_CARIE = 75;

    public DentalKombat (BaseScreen screen,
            GameStateObserver observer, float difficulty){
        super(screen, difficulty, 10000,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        
        this.toothDefendSheet = this.screen.assets.get(
                "dental-kombat/toothDefendSpritesheet.png", Texture.class);
        this.toothAttackSheet = this.screen.assets.get(
                "dental-kombat/toothAttackSpritesheet.png", Texture.class);
        this.cariesSheet = this.screen.assets.get(
                "dental-kombat/caries.png", Texture.class);
        this.toothIsHitSound = this.screen.assets.get(
                "shoot-the-caries/caries2.mp3", Sound.class);
        this.cariesIsHitSound = this.screen.assets.get(
                "shoot-the-caries/caries2.mp3", Sound.class);
        this.backGroundTexture = super.screen.assets.get(
                "dental-kombat/background.png", Texture.class);
        
        inicializarAnimacoes();
    }
    
    @Override
    protected void configureDifficultyParameters(float difficulty) {
        if (difficulty != -1) {
            cariesHealth = 3;
        }
        //<editor-fold defaultstate="collapsed" desc="Implementar dificuldades">
        if (difficulty >= 0.5) {
            cariesHealth = 3;
        }
//</editor-fold>
    }
    
    @Override
    public void onHandlePlayingInput() {
        //Saber onde o player apertou
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        super.screen.viewport.unproject(click);
        //Se clicou para tras ou apertou Esquerda, defesa
        if (click.x <= super.screen.viewport.getScreenWidth()/2 && Gdx.input.isTouched() 
                || Gdx.input.isKeyPressed(Input.Keys.LEFT))
        {
            //vai para o proximo frame da animacao
            stateTimeTooth += Gdx.graphics.getDeltaTime();
            currentFrameTooth = toothDefend.getKeyFrame(stateTimeTooth);
        }
        
        else { 
            //Se clicou na frente ou Direita, ataque
            if (click.x > super.screen.viewport.getScreenWidth()/2 && Gdx.input.isTouched()
                    || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {  
                //vai para o proximo frame da animacao
                stateTimeTooth += Gdx.graphics.getDeltaTime();
                currentFrameTooth = toothAttack.getKeyFrame(stateTimeTooth);
                cariesHealth = cariesHealth - 1;
            }
            // Se não está clicando em lugar nenhum volta para a animação inicial
            else {             
                stateTimeTooth = 0;
                currentFrameTooth = framesToothDefend[0][0];
            }
            
        }
        
    }
    
    @Override
    public void onUpdate(float dt) {
        // se tiver matado a carie venceu o desafio
        if (this.cariesHealth == 0) {
            super.challengeSolved();
        }        
    }
    
    @Override
    public void onDrawGame() {
        playerPosition.x = INITIAL_POSX_TOOTH;
        playerPosition.y = INITIAL_POSY_TOOTH;
        this.screen.batch.draw(backGroundTexture, 0, 0, 1280, 720);
        this.screen.batch.draw(currentFrameTooth, playerPosition.x, playerPosition.y);
        this.screen.batch.draw(currentFrameCaries, INITIAL_POSX_CARIE, INITIAL_POSY_CARIE);
        
    }
    
    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
    
    @Override
    public String getInstructions() {
        return "Derrote a carie em combate";
    }
    private void inicializarAnimacoes() {
        framesToothDefend = TextureRegion.split(toothDefendSheet, toothDefendSheet.getWidth()/6, toothDefendSheet.getHeight());
        framesToothAttack = TextureRegion.split(toothAttackSheet, toothAttackSheet.getWidth()/6, toothAttackSheet.getHeight());
        framesCaries = TextureRegion.split(cariesSheet, cariesSheet.getWidth()/2, cariesSheet.getHeight());      
        
        toothDefend = new Animation(0.025f, new TextureRegion[] {
            framesToothDefend[0][0],
            framesToothDefend[0][1],
            framesToothDefend[0][2],
            framesToothDefend[0][3],
            framesToothDefend[0][4],
            framesToothDefend[0][5],
        });
        
        toothAttack = new Animation(0.025f, new TextureRegion[] {
            framesToothAttack[0][0],
            framesToothAttack[0][1],
            framesToothAttack[0][2],
            framesToothAttack[0][3],
            framesToothAttack[0][4],
            framesToothAttack[0][5],
        });
        
        caries = new Animation(0.025f, new TextureRegion[] {
            framesCaries[0][0],
            framesCaries[0][1],
        });
        //defend.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        currentFrameTooth = framesToothDefend[0][0];
        currentFrameCaries = framesCaries[0][0];
    }
}
