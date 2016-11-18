package br.cefetmg.games.screens;

import br.cefetmg.games.minigames.util.GameMode;
import br.cefetmg.games.minigames.util.GameStage;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

/**
 *
 * @author thais
 */

public class Overworld extends BaseScreen {

    private Texture background;

    public Overworld(Game game, BaseScreen previous) {
        super(game, previous);
    }

    @Override
    public void appear() {
        background = new Texture("overworld/map.png");
    }

    @Override
    public void cleanUp() {
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()) {
            // traz a coordenada do clique para o sistema de coordenadas do 
            // mundo para que seja possível comparar com os "botões" de estágio
            Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            super.viewport.unproject(click);

            // percorre todos os estágios verificando se o clique foi dado
            // em uma região pertencente a um dos botões referentes a eles
            for (GameStage stage : GameStage.values()) {
                if (stage.buttonBounds.contains(click)) {
                    navigateToGamesScreen(stage);
                    break;
                }
            }
        }

    }

    @Override
    public void update(float dt) {
    }

    private void navigateToGamesScreen(final GameStage stage) {
        transitionState = states.fadeOut;
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                transitionState = states.doNothing;
                game.setScreen(new PlayingGamesScreen(game, Overworld.this,
                        GameMode.CAMPAIGN, stage));

            }
        }, 0.75f);// 750ms
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
