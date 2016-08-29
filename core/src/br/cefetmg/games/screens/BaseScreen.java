package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;

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
    private BitmapFont messagesFont;

    public BaseScreen(Game game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.bounds = new Rectangle();
        this.assets = new AssetManager();
        this.assets.load("fonts/sawasdee-24.fnt", BitmapFont.class);
        this.assets.load("fonts/sawasdee-50.fnt", BitmapFont.class);
    }

    @Override
    public void resize(int width, int height) {
        this.bounds.setSize(height * Config.DESIRED_ASPECT_RATIO, height);
        this.camera.setToOrtho(false, this.bounds.width, this.bounds.height);
        this.bounds.setPosition(0, 0);
    }

    @Override
    public final void render(float dt) {
        if (this.assets.update()) {
            if (this.messagesFont == null) {
                this.messagesFont = this.assets.get("fonts/sawasdee-50.fnt");
            }
            handleInput();
            update(dt);
            draw();
        }
    }
    
    public void drawCenterAlignedText(String text, float scale, float y) {
        if (scale > 1) {
            throw new IllegalArgumentException("Pediu-se para escrever texto "
                    + "com tamanho maior que 100% da fonte, mas isso acarreta "
                    + "em perda de qualidade do texto. Em vez disso, use uma "
                    + "fonte maior. O valor de 'scale' deve ser sempre menor "
                    + "que 1.");
        }
        final float horizontalPadding = 0.05f;
        messagesFont.setColor(Color.BLACK);
        messagesFont.getData().setScale(scale);
        messagesFont.draw(this.batch,
                text,
                0 + horizontalPadding * this.bounds.width, y,
                this.bounds.width * (1 - horizontalPadding * 2),
                Align.center, true);
    }

    public abstract void handleInput();

    public abstract void update(float dt);

    public abstract void draw();

}
