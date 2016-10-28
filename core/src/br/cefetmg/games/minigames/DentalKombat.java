package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import br.cefetmg.games.minigames.util.GameStateObserver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DentalKombat extends MiniGame {
    
    private Vector2 playerPosition = new Vector2();
    
    private final Texture backGroundTexture;
    private final Texture toothSheet;
    private final Texture cariesSheet;
    private final Texture cariesPunchTemporaria;
    private final Sound toothIsHitSound;
    private final Sound cariesIsHitSound;
    private int cariesHealth;
    private int toothHealth = 3;
    
    //animacoes
    private TextureRegion[][] framesToothDefend;
    private TextureRegion currentFrameTooth;
    private float stateTimeTooth = 0;
    private Animation defend;

    public DentalKombat (BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10000,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        this.toothSheet = this.screen.assets.get(
                "dental-kombat/spritesheet.png", Texture.class);
        this.cariesSheet = this.screen.assets.get(
                "shoot-the-caries/caries.png", Texture.class);
        this.toothIsHitSound = this.screen.assets.get(
                "shoot-the-caries/caries2.mp3", Sound.class);
        this.cariesIsHitSound = this.screen.assets.get(
                "shoot-the-caries/caries2.mp3", Sound.class);
        this.backGroundTexture = super.screen.assets.get(
                "dental-kombat/background.png", Texture.class);
        this.cariesPunchTemporaria = super.screen.assets.get(
                "dental-kombat/cariesPunch.png", Texture.class);
        
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
            stateTimeTooth += Gdx.graphics.getDeltaTime();
            currentFrameTooth = defend.getKeyFrame(stateTimeTooth);
        }
        
        //Se clicou na frente ou Direita, ataque
        else { 
            if (click.x > super.screen.viewport.getScreenWidth()/2 && Gdx.input.justTouched()
                || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            System.out.println("Ataque");
            }
        
            else {
            
            stateTimeTooth = 0;
            currentFrameTooth = framesToothDefend[0][0];
            }
        }
        
    }
    
    @Override
    public void onUpdate(float dt) {
        
    }
    
    @Override
    public void onDrawGame() {
        playerPosition.x = 300;
        playerPosition.y = 200;
        //this.screen.batch.draw(backGroundTexture, 0, 0, 1280, 720);
        //this.screen.batch.draw(framesToothDefend[0][1], playerPosition.x, playerPosition.y);
        this.screen.batch.draw(currentFrameTooth, playerPosition.x, playerPosition.y);
        
    }
    
    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
    
    @Override
    public String getInstructions() {
        return "Derrote a cárie em combate";
    }
    private void inicializarAnimacoes() {
        framesToothDefend = TextureRegion.split(toothSheet, toothSheet.getWidth()/6, toothSheet.getHeight());
        
        defend = new Animation(0.025f, new TextureRegion[] {
            framesToothDefend[0][0],
            framesToothDefend[0][1],
            framesToothDefend[0][2],
            framesToothDefend[0][3],
            framesToothDefend[0][4],
            framesToothDefend[0][5],
        });
        //defend.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        currentFrameTooth = framesToothDefend[0][0];
    }
}
