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
import com.badlogic.gdx.utils.Timer.Task;

public class DentalKombat extends MiniGame{
    
    private final Vector2 playerPosition = new Vector2();
    
    private final Texture backGroundTexture;
    private final Texture toothSpritesheet;
    private final Texture cariesSheet;
    private final Texture barraDeVidaDente;
    private final Texture barraDeVidaCarie;
    private final Texture barraDeVidaMoldura;
    private final Sound toothIsHitSound;
    private final Sound toothDefendsSound;
    private final Sound cariesIsHitSound;
    private int toothHealth = 3;
    private int cariesHealth = 0;
    private int cariesMaxHealth = 0;
    private boolean stateToothAttack;
    private boolean stateToothDefend;
    private boolean stateCariesAttack;
    
    //animacoes
    private TextureRegion[][] framesCaries;
    private TextureRegion[][] framesTooth;
    private TextureRegion currentFrameTooth;
    private TextureRegion currentFrameCaries;
    private float stateTimeTooth = 0;
    private float stateTimeCaries = 0;
    private Animation toothDefend;
    private Animation toothAttack;
    private Animation caries;
    
    // constantes
    private static final int INITIAL_POSX_TOOTH = 350;
    private static final int INITIAL_POSY_TOOTH = 75;
    private static final int INITIAL_POSX_CARIE = 350;
    private static final int INITIAL_POSY_CARIE = 75;
    private final float TOOTH_MAX_HEALTH = toothHealth;
    private final float ATTACK_TIME_MILLIS = 2f;

    public DentalKombat (BaseScreen screen,
            GameStateObserver observer, float difficulty){
        super(screen, difficulty, 10f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        
        this.toothSpritesheet = this.screen.assets.get(
                "dental-kombat/toothSpritesheet.png", Texture.class);
        this.cariesSheet = this.screen.assets.get(
                "dental-kombat/caries.png", Texture.class);
        this.backGroundTexture = super.screen.assets.get(
                "dental-kombat/background.png", Texture.class);
        this.barraDeVidaDente = super.screen.assets.get(
                "dental-kombat/barraDeVida.png", Texture.class);
        this.barraDeVidaCarie = super.screen.assets.get(
                "dental-kombat/barraDeVida.png", Texture.class);
        this.barraDeVidaMoldura = super.screen.assets.get(
                "dental-kombat/barraDeVidaMoldura.png", Texture.class);
        this.toothIsHitSound = this.screen.assets.get(
                "dental-kombat/pain.mp3", Sound.class);
        this.toothDefendsSound = this.screen.assets.get(
                "dental-kombat/punch2.mp3", Sound.class);
        this.cariesIsHitSound = this.screen.assets.get(
                "dental-kombat/punch1.mp3", Sound.class);
        
        configureDifficultyParameters(difficulty);
        inicializarAnimacoes();
        scheduleEnemyAttack();
    }
    // Prepara o próximo ataque da carie
    private void scheduleEnemyAttack() {
        Task t = new Task() {
            @Override
            public void run() {
                cariesAttack();
            }
        };
        super.timer.scheduleTask(t, ATTACK_TIME_MILLIS);
    }

    private void cariesAttack(){
        // Realiza a animação de ataque da carie
        this.currentFrameCaries = this.framesCaries[0][1];
        this.stateCariesAttack = true;
        
        // Se o jogador estiver atacando ao mesmo tempo que a cárie, cancela o ataque do dente.
        if (this.stateToothAttack)
            this.stateToothAttack = false;
        // Prepara o próximo ataque da carie
        scheduleEnemyAttack();
        // Volta para o frame original da carie
        this.currentFrameCaries = this.framesCaries[0][0];
    }
    
    
    @Override
    protected void configureDifficultyParameters(float difficulty) {
        if (difficulty <= 0.33) {
            this.cariesMaxHealth = 5;
            this.cariesHealth = 5;
        }
        else if ((difficulty > 0.33) && (difficulty <= 0.66) ){
            this.cariesMaxHealth = 8;  
            this.cariesHealth = 8;            
        }
        else {
            this.cariesMaxHealth = 12;
            this.cariesHealth = 12;
        }
    }
    
    @Override
    public void onHandlePlayingInput() {
        //Saber onde o player apertou
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        super.screen.viewport.unproject(click);
        
        if (stateToothAttack == false)
        {
            //Se clicou para tras ou apertou Esquerda, defesa
            if (click.x <= super.screen.viewport.getScreenWidth()/2 && Gdx.input.isTouched() 
                    || Gdx.input.isKeyPressed(Input.Keys.LEFT))
            {
                this.stateToothDefend = true;
                //vai para o proximo frame da animacao
                this.stateTimeTooth += Gdx.graphics.getDeltaTime();
                this.currentFrameTooth = this.toothDefend.getKeyFrame(this.stateTimeTooth);
            }

            else { 
                //Se clicou na frente ou Direita, ataque
                if (click.x > super.screen.viewport.getScreenWidth()/2 && Gdx.input.justTouched()
                        || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {  
                    // Variável que controla se o jogador realizou um ataque
                    this.stateToothAttack = true;
                    //vai para o proximo frame da animacao
                    this.stateTimeTooth += Gdx.graphics.getDeltaTime();
                    this.currentFrameTooth = this.toothAttack.getKeyFrame(this.stateTimeTooth);
                }
                // Se não está clicando em lugar nenhum volta para a animação inicial
                else {             
                    this.stateTimeTooth = 0;
                    this.currentFrameTooth = this.framesTooth[0][0];
                    // Se acabou a animação de atacar diminui a vida da carie e volta ao estado inicial
                    if (this.stateToothAttack) {
                        this.stateToothAttack = false;
                        this.cariesHealth = this.cariesHealth - 1;                    
                    }
                    // Se acabou a animação de defesa volta ao estado inicial
                    if (this.stateToothDefend)
                        this.stateToothDefend = false;
                }

            }
        }
        else if (toothAttack.isAnimationFinished(stateTimeTooth) == false)
        {
            this.stateTimeTooth += Gdx.graphics.getDeltaTime();
            this.currentFrameTooth = this.toothAttack.getKeyFrame(this.stateTimeTooth);
        }
        else
        {
            stateToothAttack = false;
            cariesHealth -= 1;
            cariesIsHitSound.play();
        }
    }
    
    @Override
    public void onUpdate(float dt) {
        // Se a vida da Carie tiver acabado ganhou o desafio
        if (this.cariesHealth == 0) {
            super.challengeSolved();
        } 
        // Se a vida do Dente tiver acabado perdeu o desafio
        if (this.toothHealth == 0){
            super.challengeFailed();
        }
        
        if (this.stateCariesAttack == true)
        {
            if (this.caries.isAnimationFinished(stateTimeCaries) == false)
            {
            stateTimeCaries += Gdx.graphics.getDeltaTime();
            this.currentFrameCaries = this.caries.getKeyFrame(this.stateTimeCaries);
            }
            else
            {
                stateCariesAttack = false;
                currentFrameCaries = framesCaries[2][3];
                stateTimeCaries = 0;
                // Se o jogador não estiver defendendo perde vida
                if (this.stateToothDefend != true){
                    this.toothHealth = this.toothHealth - 1;         
                    toothIsHitSound.play();
                }
                else
                {
                    toothDefendsSound.play();
                }
            }
        }
    }
    
    @Override
    public void onDrawGame() {
        playerPosition.x = INITIAL_POSX_TOOTH;
        playerPosition.y = INITIAL_POSY_TOOTH;
        this.screen.batch.draw(backGroundTexture, 0, 0, 1280, 720);
        
        this.screen.batch.draw(barraDeVidaMoldura, 0, 628);
        this.screen.batch.draw(barraDeVidaMoldura, 720, 628, 547, 92, 0, 0, 547, 92, true, false);
        this.screen.batch.draw(barraDeVidaDente, 79, 641, ((float)toothHealth)/TOOTH_MAX_HEALTH*449, 68);
        this.screen.batch.draw(barraDeVidaCarie, ((float)(cariesMaxHealth - cariesHealth))/cariesMaxHealth*449 + 740, 641, 
                                                ((float)cariesHealth)/cariesMaxHealth*449, 68);        
        
        
        this.screen.batch.draw(currentFrameTooth, playerPosition.x, playerPosition.y);
        this.screen.batch.draw(currentFrameCaries, INITIAL_POSX_CARIE, INITIAL_POSY_CARIE);
        
        
        
    }
    
    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
    
    @Override
    public String getInstructions() {
        return "Lute! (Aperte tras e frente)";
    }
    private void inicializarAnimacoes() {
        this.framesTooth = TextureRegion.split(toothSpritesheet, toothSpritesheet.getWidth()/6, toothSpritesheet.getHeight()/2);
        this.framesCaries = TextureRegion.split(cariesSheet, cariesSheet.getWidth()/4, cariesSheet.getHeight()/5);      
        
        this.toothDefend = new Animation(0.025f, new TextureRegion[] {
            this.framesTooth[0][0],
            this.framesTooth[0][1],
            this.framesTooth[0][2],
            this.framesTooth[0][3],
            this.framesTooth[0][4],
            this.framesTooth[0][5],
        });
        
        this.toothAttack = new Animation(0.025f, new TextureRegion[] {
            this.framesTooth[1][0],
            this.framesTooth[1][1],
            this.framesTooth[1][2],
            this.framesTooth[1][3],
        });
        
        this.caries = new Animation(0.025f, new TextureRegion[] {
            this.framesCaries[0][0],
            this.framesCaries[0][1],
            this.framesCaries[0][2],
            this.framesCaries[1][0],
            this.framesCaries[1][1],
            this.framesCaries[1][2],
            this.framesCaries[2][0],
            this.framesCaries[2][1],
            this.framesCaries[2][2],
            this.framesCaries[3][0],
            this.framesCaries[3][1],
            this.framesCaries[3][2],
            this.framesCaries[4][0],
            this.framesCaries[4][1],
            this.framesCaries[4][2],
            this.framesCaries[0][3],
            this.framesCaries[1][3],
            this.framesCaries[2][3]
        });
        
        // Inicializa as variáveis
        this.currentFrameTooth = this.framesTooth[0][0];
        this.currentFrameCaries = this.framesCaries[2][3];
        this.stateToothAttack = false;
        this.stateToothDefend = false;
        this.stateCariesAttack = false;
    }
}
