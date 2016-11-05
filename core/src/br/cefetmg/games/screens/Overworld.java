/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.screens;

import br.cefetmg.games.minigames.util.GameOption;
import br.cefetmg.games.minigames.util.GameType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
/**
 *
 * @author thais
 */


public class Overworld extends BaseScreen{

    public static final boolean DEV_MODE = true;
    
    private final Texture background;
    //targetTexture;
    private Stage stage;
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
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){

            if (Gdx.input.getX() > 0 && Gdx.input.getX() < 260
                && Gdx.input.getY() > 260 && Gdx.input.getY() < 420){  
                    if(DEV_MODE){ 
                        System.out.println("CANDY: X = " + Gdx.input.getX()
                            + "; Y = " + Gdx.input.getY());
                    }
                    navigateToGamesScreen(GameType.LEARNING_ABOUT_CANDY);
            
            } else if (Gdx.input.getX() > 315 && Gdx.input.getX() < 490
                && Gdx.input.getY() > 0 && Gdx.input.getY() < 130){
                    if(DEV_MODE){
                        System.out.println("DESTRUCTION: X = " + Gdx.input.getX() 
                            + "; Y = " + Gdx.input.getY());
                    }
                    navigateToGamesScreen(GameType.DESTRUCTION); 
                   
            } else if (Gdx.input.getX() > 290 && Gdx.input.getX() < 640
                && Gdx.input.getY() > 390 && Gdx.input.getY() < viewport.getWorldHeight()){  
                    if(DEV_MODE){
                        System.out.println("RUNNING_PROTECTING: X = " + Gdx.input.getX()
                                     + "; Y = " + Gdx.input.getY());
                    }
                    navigateToGamesScreen(GameType.RUNNING_PROTECTING);
                
            } else if (Gdx.input.getX() > 600 && Gdx.input.getX() < 775
                && Gdx.input.getY() > 0 && Gdx.input.getY() < 130){  
                    if(DEV_MODE){
                        System.out.println("HYGIENE: X = " + Gdx.input.getX()
                            + "; Y = " + Gdx.input.getY());
                    }
                    navigateToGamesScreen(GameType.LEARNING_ABOUT_HYGIENE);
                    
                    
            } else if (Gdx.input.getX() > 680 && Gdx.input.getX() < viewport.getWorldWidth()
                && Gdx.input.getY() > 280 && Gdx.input.getY() < 410){  
                    if(DEV_MODE){
                        System.out.println("CARING: X = " + Gdx.input.getX()
                            + "; Y = " + Gdx.input.getY());
                    }
                    navigateToGamesScreen(GameType.CARING_FOR_TEETH);
                    
                    
            } else {
                if(DEV_MODE){
                    System.out.println ("OUT: X = " + Gdx.input.getX()
                        + "; Y = " + Gdx.input.getY());
                }

            }
        }

    }
    
    private void navigateToGamesScreen(final GameType gt) {
        transitionState = states.fadeOut;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                transitionState = states.doNothing;
                //menuMusic.stop();
                game.setScreen(new PlayingGamesScreen(game, Overworld.this, 
                        GameOption.NORMAL, gt));
   
            }
        }, 0.75f);// 750ms
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
