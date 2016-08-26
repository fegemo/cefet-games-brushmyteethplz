package br.cefetmg.games.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.TimeUtils;

/**
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class SplashScreen extends BaseScreen {

    private long initialTime;

    public SplashScreen(Game game) {
        super(game);
    }
    
    @Override
    public void show() {
        initialTime = TimeUtils.millis();
        Gdx.gl.glClearColor(1, 1, 1, 1);
    }
    
    private void navigateToMenuScreen() {
        this.game.setScreen(new MenuScreen(this.game));
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            navigateToMenuScreen();
        }
    }

    @Override
    public void update(float dt) {
        if (TimeUtils.timeSinceMillis(initialTime) >= 3000) {
            navigateToMenuScreen();
        }
    }

    @Override
    public void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
    }
}
