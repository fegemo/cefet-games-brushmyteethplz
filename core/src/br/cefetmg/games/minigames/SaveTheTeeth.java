/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

/**
 *
 * @author Raphus
 */
public class SaveTheTeeth extends MiniGame {
    
    private int foodSpawnInterval;
    private final Texture MouthTexture;
    private final Texture CursorTexture;
    private final Texture GoodFoodTexture;
    private final Texture BadFoodTexture;
    private Mouth mouth;
    private final Array<Food> food;

    public SaveTheTeeth(BaseScreen screen, GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10000,TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS, observer);
        
        this.MouthTexture=super.screen.assets.get("save-the-teeth/boca.png", Texture.class);
        this.CursorTexture=super.screen.assets.get("save-the-teeth/cursor.png", Texture.class);
        this.BadFoodTexture=super.screen.assets.get("save-the-teeth/bad.png", Texture.class);
        this.GoodFoodTexture=super.screen.assets.get("save-the-teeth/good.png", Texture.class);
        this.food=new Array<Food>();
        this.mouth=new Mouth(MouthTexture);
        
        super.timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                throwFood();
            }
        }, 0, this.foodSpawnInterval / 1000f);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.foodSpawnInterval=(int)DifficultyCurve.LINEAR_NEGATIVE.getCurveValueBetween(difficulty, 500, 1500);
    }

    @Override
    public void onHandlePlayingInput() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onUpdate(float dt) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onDrawGame() {
        mouth.draw(super.screen.batch);
    }

    @Override
    public String getInstructions() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return "Deixe apenas comidas saudáveis passarem";
    }

    @Override
    public boolean shouldHideMousePointer() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return false;
    }
    
    private void throwFood(){
    }
    
    class Mouth extends Sprite{
        final int FRAME_WIDTH = 625;
        final int FRAME_HEIGHT = 581;
        final int radius=30;
        
        Mouth(final Texture MouthTexture){
            super(MouthTexture);
            mouth.setCenter(screen.viewport.getWorldWidth()/2.0f,screen.viewport.getWorldHeight()/2.0f);
        }
    }
    
    class Food extends Sprite{
    }
}
