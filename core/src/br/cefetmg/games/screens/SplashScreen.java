package br.cefetmg.games.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;

/**
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class SplashScreen extends BaseScreen {

    private long initialTime;
    private final Sprite logo;

    public SplashScreen(Game game) {
        super(game);
        this.logo = new Sprite(new Texture("images/cefet-games-logo.png"));
        this.logo.getTexture().setFilter(
                Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }
    
    @Override
    public void show() {
        initialTime = TimeUtils.millis();
        Gdx.gl.glClearColor(1, 1, 1, 1);
    }
    
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.logo.setCenter(super.bounds.width/2f, super.bounds.height/2f);
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
        super.batch.begin();
        this.logo.draw(super.batch);
        super.batch.end();
    }
}
