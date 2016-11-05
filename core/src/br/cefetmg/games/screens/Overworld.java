/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.screens;

import br.cefetmg.games.minigames.util.GameOption;
import br.cefetmg.games.minigames.util.GameType;
import br.cefetmg.games.minigames.util.Score;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import java.util.ArrayList;

/**
 *
 * @author thais
 */
public class Overworld extends BaseScreen{

    
    private Texture background, targetTexture;
    private Stage stage;
    private Sprite target;
    private Object screen;
    
    public Overworld(Game game, BaseScreen previous) {
        super(game, previous);  
        
        //TODO assets.get
        super.assets.load("overworld/map.png", Texture.class);
        background = new Texture("overworld/map.png");
        
   
    }

    @Override
    public void appear() {

    }

    @Override
    public void cleanUp() {
        //throw new UnsupportedOperationException("CLEAN_UP Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            //System.out.println("Handled Input");
            if (Gdx.input.getX() > 315 && Gdx.input.getX() < 490
                && Gdx.input.getY() > 0 && Gdx.input.getY() < 130){
                    System.out.println("DESTRUCTION: X = " + Gdx.input.getX() 
                            + "; Y = " + Gdx.input.getY());
                    //new PlayingGamesScreen(game, Overworld.this, GameOption.NORMAL, GameType.DESTRUCTION);
                
            } else if (Gdx.input.getX() > 290 && Gdx.input.getX() < 640
                && Gdx.input.getY() > 390 && Gdx.input.getY() < viewport.getWorldHeight()){  
                    System.out.println("RUNNING_PROTECTING: X = " + Gdx.input.getX()
                            + "; Y = " + Gdx.input.getY());
                /*transitionState = states.fadeOut;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        transitionState = states.doNothing;
                        game.setScreen(new PlayingGamesScreen(game, Overworld.this,
                                GameOption.NORMAL, GameType.RUNNING_PROTECTING));            
                    }
                }, 0.75f);// 750ms*/                
                
            } else if (Gdx.input.getX() > 600 && Gdx.input.getX() < 775
                && Gdx.input.getY() > 0 && Gdx.input.getY() < 130){  
                    System.out.println("HYGIENE: X = " + Gdx.input.getX()
                            + "; Y = " + Gdx.input.getY());
                    
                    
            } else if (Gdx.input.getX() > 680 && Gdx.input.getX() < viewport.getWorldWidth()
                && Gdx.input.getY() > 280 && Gdx.input.getY() < 410){  
                    System.out.println("CARING: X = " + Gdx.input.getX()
                            + "; Y = " + Gdx.input.getY());
                    
                    
            } else if (Gdx.input.getX() > 0 && Gdx.input.getX() < 260
                && Gdx.input.getY() > 260 && Gdx.input.getY() < 420){  
                    System.out.println("CANDY: X = " + Gdx.input.getX()
                            + "; Y = " + Gdx.input.getY());
            } else {
                System.out.println ("OUT: X = " + Gdx.input.getX()
                + "; Y = " + Gdx.input.getY());
            }
        }

    }

    @Override
    public void update(float dt) {
       
        
    }
    

    @Override
    public void draw() {
        batch.begin();

            batch.draw(background, 0, 0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight());
            
            drawCenterAlignedText("Siga os tijolos amarelos",
                1f, viewport.getWorldHeight() * 0.5f);
            //stage.draw();

        batch.end();
    }
    
}
