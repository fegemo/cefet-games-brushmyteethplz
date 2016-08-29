package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class MenuScreen extends BaseScreen {

    private static final int NUMBER_OF_TILED_BACKGROUND_TEXTURE = 7;
    private final TextureRegion background;

    public MenuScreen(Game game) {
        super(game);
        this.background = new TextureRegion(new Texture("menu-background.png"));
        this.background.getTexture().setWrap(
                Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.background.setRegionWidth(
                this.background.getTexture().getWidth()
                * NUMBER_OF_TILED_BACKGROUND_TEXTURE);
        this.background.setRegionHeight((int) (this.background.getTexture()
                .getHeight() * NUMBER_OF_TILED_BACKGROUND_TEXTURE
                / Config.DESIRED_ASPECT_RATIO));
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            navigateToMicroGameScreen();
        }
    }

    @Override
    public void update(float dt) {
        float speed = dt * 0.25f;
        this.background.scroll(speed, -speed);
    }

    @Override
    public void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.batch.begin();
        {
            super.batch.draw(background, 0, 0,
                    super.bounds.width, super.bounds.height);
            super.drawCenterAlignedText("Pressione qualquer tecla para jogar",
                    0.5f, super.bounds.height * 0.35f);
        }
        super.batch.end();
    }

    private void navigateToMicroGameScreen() {
        this.game.setScreen(new PlayingGamesScreen(this.game));
    }

}
