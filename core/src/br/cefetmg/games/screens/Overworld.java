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
    private final Texture background;
    private float fase1_X_max, fase1_X_min, fase1_Y_max, fase1_Y_min,
            fase2_X_max, fase2_X_min, fase2_Y_max, fase2_Y_min,
            fase3_X_max, fase3_X_min, fase3_Y_max, fase3_Y_min,
            fase4_X_max, fase4_X_min, fase4_Y_max, fase4_Y_min,
            fase5_X_max, fase5_X_min, fase5_Y_max, fase5_Y_min;
    public Overworld(Game game, BaseScreen previous) {
        super(game, previous);  
        
        //TODO assets.get
        super.assets.load("overworld/map.png", Texture.class);
        background = new Texture("overworld/map.png");
        fase1_X_max=260;
        fase1_X_min=0;
        fase1_Y_max=420;
        fase1_Y_min=260;
        fase2_X_max=490;
        fase2_X_min=315;
        fase2_Y_max=130;
        fase2_Y_min=0;
        fase3_X_max=640;
        fase3_X_min=290;
        fase3_Y_max=viewport.getWorldHeight();
        fase3_Y_min=390;
        fase4_X_max=775;
        fase4_X_min=600;
        fase4_Y_max=130;
        fase4_Y_min=0;
        fase5_X_max=viewport.getWorldWidth();
        fase5_X_min=680;
        fase5_Y_max=410;
        fase5_Y_min=280;
    }

    @Override
    public void appear() {

    }

    @Override
    public void cleanUp() {

    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){
            if (Gdx.input.getX() > fase1_X_min && Gdx.input.getX() < fase1_X_max
                && Gdx.input.getY() > fase1_Y_min && Gdx.input.getY() < fase1_Y_max){
                    navigateToGamesScreen(GameType.LEARNING_ABOUT_CANDY);
            
            } else if (Gdx.input.getX() > fase2_X_min && Gdx.input.getX() < fase2_X_max
                && Gdx.input.getY() > fase2_Y_min && Gdx.input.getY() < fase2_Y_max){
                    navigateToGamesScreen(GameType.DESTRUCTION);
                   
            } else if (Gdx.input.getX() > fase3_X_min && Gdx.input.getX() < fase3_X_max
                && Gdx.input.getY() > fase3_Y_min && Gdx.input.getY() < fase3_Y_max){
                    navigateToGamesScreen(GameType.RUNNING_PROTECTING);
                
            } else if (Gdx.input.getX() > fase4_X_min && Gdx.input.getX() < fase4_X_max
                && Gdx.input.getY() > fase4_Y_min && Gdx.input.getY() < fase4_Y_max){
                    navigateToGamesScreen(GameType.LEARNING_ABOUT_HYGIENE);
                    
                    
            } else if (Gdx.input.getX() > fase5_X_min && Gdx.input.getX() < fase5_X_max
                && Gdx.input.getY() > fase5_Y_min && Gdx.input.getY() < fase5_Y_max){
                    navigateToGamesScreen(GameType.CARING_FOR_TEETH);
            }
        }

    }
    
    private void navigateToGamesScreen(final GameType gt) {
        transitionState = states.fadeOut;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                transitionState = states.doNothing;
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
        batch.end();
    }
    
}
