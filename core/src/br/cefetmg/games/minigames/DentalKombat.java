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
    
    private Vector2 playerPosition = new Vector2();
    
    private final Texture backGroundTexture;
    private final Texture toothSpritesheet;
    private final Texture cariesSheet;
    private final Sound toothIsHitSound;
    private final Sound cariesIsHitSound;
    private int toothHealth = 3;
    private int cariesHealth = 3;
    private boolean stateToothAttack;
    private boolean stateToothDefend;
    
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
    private static final int INITIAL_POSX_CARIE = 750;
    private static final int INITIAL_POSY_CARIE = 75;

    public DentalKombat (BaseScreen screen,
            GameStateObserver observer, float difficulty){
        super(screen, difficulty, 10000,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        
        this.toothSpritesheet = this.screen.assets.get(
                "dental-kombat/toothSpritesheet.png", Texture.class);
        this.cariesSheet = this.screen.assets.get(
                "dental-kombat/caries.png", Texture.class);
        this.toothIsHitSound = this.screen.assets.get(
                "shoot-the-caries/caries2.mp3", Sound.class);
        this.cariesIsHitSound = this.screen.assets.get(
                "shoot-the-caries/caries2.mp3", Sound.class);
        this.backGroundTexture = super.screen.assets.get(
                "dental-kombat/background.png", Texture.class);
        
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
        // spawnInterval * 15% para mais ou para menos
        // TROCAR INTERVALO DO RAND
        float nextSpawnMillis = (rand.nextFloat() + 1.5f) * 2;
        //System.out.println("Tempo: "+ nextSpawnMillis);
        super.timer.scheduleTask(t, nextSpawnMillis);
    }

    private void cariesAttack(){
        // Realiza a animação de ataque da carie
        //ARRUMAR ANIMACAO DE ATAQUE DA CARIE
//        stateTimeCaries += Gdx.graphics.getDeltaTime();
//        this.currentFrameCaries = this.caries.getKeyFrame(this.stateTimeCaries);
        this.currentFrameCaries = this.framesCaries[0][1];
        // Se o jogador não estiver defendendo perde vida
        if (this.stateToothDefend != true){
            this.toothHealth = this.toothHealth - 1;
            //        
            System.out.println("Tooth Health: " + toothHealth);
            System.out.println("Carie Health: " + cariesHealth);            
        }
        // Se o jogador estiver atacando ao mesmo tempo que a cárie, cancela o ataque do dente.
        if (this.stateToothAttack)
            this.stateToothAttack = false;
        // Prepara o próximo ataque da carie
        scheduleEnemyAttack();
        // Volta para o frame original da carie
        // ARRUMAR ANIMACAO DE ATAQUE DA CARIE
//        this.stateTimeCaries = 0;
//        this.currentFrameCaries = this.caries.getKeyFrame(this.stateTimeCaries);
        this.currentFrameCaries = this.framesCaries[0][0];
        System.out.println("Tooth Health: " + toothHealth);
        System.out.println("Carie Health: " + cariesHealth);
    }
    
    
    @Override
    protected void configureDifficultyParameters(float difficulty) {
        if (difficulty != -1) {
            this.cariesHealth = 3;
        }
        //<editor-fold defaultstate="collapsed" desc="Implementar dificuldades">
        if (difficulty >= 0.5) {
            this.cariesHealth = 3;
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
            this.stateToothDefend = true;
            //vai para o proximo frame da animacao
            this.stateTimeTooth += Gdx.graphics.getDeltaTime();
            this.currentFrameTooth = this.toothDefend.getKeyFrame(this.stateTimeTooth);
        }
        
        else { 
            //Se clicou na frente ou Direita, ataque
            if (click.x > super.screen.viewport.getScreenWidth()/2 && Gdx.input.isTouched()
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
                    //        
                    System.out.println("Tooth Health: " + toothHealth);
                    System.out.println("Carie Health: " + cariesHealth);                    
                }
                // Se acabou a animação de defesa volta ao estado inicial
                if (this.stateToothDefend)
                    this.stateToothDefend = false;
            }
            
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
        this.framesTooth = TextureRegion.split(toothSpritesheet, toothSpritesheet.getWidth()/6, toothSpritesheet.getHeight()/2);
        this.framesCaries = TextureRegion.split(cariesSheet, cariesSheet.getWidth()/2, cariesSheet.getHeight());      
        
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
        });
        //defend.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        
        // Inicializa as variáveis
        this.currentFrameTooth = this.framesTooth[0][0];
        this.currentFrameCaries = this.framesCaries[0][0];
        this.stateToothAttack = false;
        this.stateToothDefend = false;
    }
}
