package br.cefetmg.games.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class MenuScreen extends BaseScreen {
    
    public MenuScreen(Game game) {
        super(game);
    }
    
    @Override
    public void show() {
        Gdx.gl.glClearColor(1, 0, 1, 1);
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            navigateToMicroGameScreen();
        }
    }

    @Override
    public void update(float dt) {
        
    }

    @Override
    public void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
    }

    private void navigateToMicroGameScreen() {
        this.game.setScreen(new PlayingGamesScreen(this.game));
    }
    
}
