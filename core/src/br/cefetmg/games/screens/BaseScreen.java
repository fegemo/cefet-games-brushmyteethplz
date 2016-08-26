package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public abstract class BaseScreen extends ScreenAdapter {

    public final Game game;
    public final SpriteBatch batch;
    public final OrthographicCamera camera;
    public Rectangle bounds;
    public final AssetManager assets;

    public BaseScreen(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.bounds = new Rectangle();
        this.assets = new AssetManager();
    }

    @Override
    public void resize(int width, int height) {
        this.bounds.setSize(height * Config.DESIRED_ASPECT_RATIO, height);
        this.camera.setToOrtho(false, this.bounds.width, this.bounds.height);
        this.bounds.setPosition(0, 0);
    }

    @Override
    public final void render(float dt) {
        handleInput();
        update(dt);
        draw();
    }

    public abstract void handleInput();

    public abstract void update(float dt);

    public abstract void draw();

}
