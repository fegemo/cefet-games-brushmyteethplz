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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class CleanTheTooth extends MiniGame {

    private final Sprite target;
    private final Array<Tooth> dentes;
    private final Texture bocaTexture;
    private final Sprite boca;
    private final Texture targetTexture;
    private final Texture denteTexture;
    private final Texture denteSujoTexture;
    private int cleanTeeth;
    private int totalTeeth;
    private int spawnInterval;

    public CleanTheTooth(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10000,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        this.bocaTexture = this.screen.assets.get(
                "clean-the-tooth/mouth2.png", Texture.class);
        this.targetTexture = this.screen.assets.get(
                "clean-the-tooth/toothbrush.png", Texture.class);
        this.denteTexture = this.screen.assets.get(
                "clean-the-tooth/clean.png", Texture.class);
        this.denteSujoTexture = this.screen.assets.get(
                "clean-the-tooth/dirty.png", Texture.class);
        this.boca = new Sprite(bocaTexture);
        boca.setCenterX(super.screen.viewport.getWorldWidth()/2);
        boca.setCenterY(super.screen.viewport.getWorldHeight()/2);
//        this.boca.setPosition(super.screen.viewport.getWorldWidth()/2, super.screen.viewport.getWorldHeight()/2);
        this.target = new Sprite(targetTexture);
        this.target.setOriginCenter();
        this.cleanTeeth = 0;
        this.dentes = new Array<Tooth>();
        
        for(int i=0;i<this.totalTeeth/2;i++){
            Tooth d = new Tooth(this.denteSujoTexture,this.denteTexture);
            d.setPosition(340 + 90*i,500);
            d.rotate(180);
            this.dentes.add(d);
        }
        
        for(int i = this.totalTeeth/2;i<this.totalTeeth;i++){
            Tooth d = new Tooth(this.denteSujoTexture,this.denteTexture);
            if(i == this.totalTeeth/2 || i == 13){
               d.setPosition(340 + 90*(i-this.totalTeeth/2),240); 
            }
            else{
                d.setPosition(340 + 90*(i-this.totalTeeth/2),210);
            }
            this.dentes.add(d);
        }
        
    }


    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.spawnInterval = (int) DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, 500, 1500);
        this.totalTeeth = (int) Math.ceil((float) maxDuration
                / spawnInterval) - 2;
        if(this.totalTeeth > 14){
            this.totalTeeth = 14;
        }
    }

    @Override
    public void onHandlePlayingInput() {
        // atualiza a posição do alvo de acordo com o mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        super.screen.viewport.unproject(click);
        this.target.setPosition(click.x - this.target.getWidth() / 2,
                click.y - this.target.getHeight() / 2);
        if (Gdx.input.justTouched()) {
            // itera no array de dentes
            for (int i = 0; i < dentes.size; i++) {
                Tooth sprite = dentes.get(i);
                // se há interseção entre o retângulo da sprite e do alvo,
                // o dente foi clicado
                if (sprite.getBoundingRectangle().overlaps(
                        target.getBoundingRectangle())) {
                    // contabiliza um dente limpo se dente estiver sujo
                    if(!sprite.getIsClean()){
                        this.cleanTeeth++;
                    // muda textura do dente
                        sprite.cleanTooth();
                    }
                    // se tiver limpado todos os dentes, o desafio
                    // está resolvido
                    if (this.cleanTeeth >= this.totalTeeth) {
                        super.challengeSolved();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public String getInstructions() {
        return "Limpe os dentes";
    }

    @Override
    public void onDrawGame() {
        boca.draw(super.screen.batch);
        for (Tooth tooth : this.dentes) {
            tooth.draw(super.screen.batch);
        }
        target.draw(super.screen.batch);
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
     class Tooth extends Sprite {

    
        private final Texture clean;
      

        static final int FRAME_WIDTH = 80;
        static final int FRAME_HEIGHT = 80;
        private boolean isClean;

        public Tooth(Texture dirty,
                Texture clean) {
            
            super(dirty);
            this.clean = clean;
            this.isClean = false;
        }
        
        private void cleanTooth(){
            this.setTexture(this.clean);
            this.isClean = true;
        }
        
        private boolean getIsClean(){
            return this.isClean;
        }
        
    }
}
