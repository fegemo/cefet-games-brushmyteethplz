package br.cefetmg.games;

import br.cefetmg.games.screens.SplashScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class BrushMyTeethPlzGame
        extends Game {

    @Override
    public void create() {
        this.setScreen(new SplashScreen(this, null));
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void render() {
        handleInput();
        super.render();
    }

    @Override
    public void dispose() {
        if (this.getScreen() != null) {
            this.getScreen().dispose();
        }
    }
}
