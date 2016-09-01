package br.cefetmg.games.screens;

import br.cefetmg.games.graphics.Hud;
import br.cefetmg.games.logic.chooser.GameSequencer;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.factories.ShooTheTartarusFactory;
import br.cefetmg.games.minigames.factories.ShootTheCariesFactory;
import br.cefetmg.games.minigames.factories.MiniGameFactory;
import br.cefetmg.games.minigames.util.MiniGameState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.Arrays;
import java.util.HashSet;
import br.cefetmg.games.minigames.util.GameStateObserver;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class PlayingGamesScreen extends BaseScreen
        implements GameStateObserver {

    private MiniGame currentGame;
    private final GameSequencer sequencer;
    private final Hud hud;
    private PlayScreenState state;
    private int lives;

    public PlayingGamesScreen(Game game, BaseScreen previous) {
        super(game, previous);
        super.assets.load("images/countdown.png", Texture.class);
        super.assets.load("images/gray-mask.png", Texture.class);

        this.state = PlayScreenState.PLAYING;
        this.lives = 3;
        this.sequencer = new GameSequencer(5, new HashSet<MiniGameFactory>(
                Arrays.asList(
                        new ShootTheCariesFactory(),
                        new ShooTheTartarusFactory())
        ), this, this);
        this.hud = new Hud(this);
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.input.setCursorCatched(true);
        hud.create();
    }

    @Override
    public void handleInput() {
        if (this.currentGame != null) {
            this.currentGame.handleInput();
        }

        if (this.state != PlayScreenState.PLAYING) {
            if (Gdx.input.justTouched()) {
                // volta para o menu principal
                super.game.setScreen(new MenuScreen(super.game, previous));
            }
        }
    }

    @Override
    public void update(float dt) {
        if (super.assets.update()) {
            if (this.currentGame == null) {
                advance();
            }
            this.currentGame.update(dt);
            hud.update(dt);
        }
    }

    @Override
    public void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.batch.begin();
        if (this.currentGame != null) {
            this.currentGame.draw();
        }
        if (this.state != PlayScreenState.PLAYING) {
            drawEndGame();
        }
        super.batch.end();
        hud.draw();
    }

    private void advance() {
        if (this.state != PlayScreenState.PLAYING) {
            return;
        }

        if (this.sequencer.hasNextGame()) {
            this.currentGame = this.sequencer.nextGame();
            hud.setGameIndex(sequencer.getGameNumber());
            Gdx.input.setCursorCatched(currentGame.shouldHideMousePointer());
        } else {
            // mostra mensagem de vitória
            this.transitionTo(PlayScreenState.FINISHED_WON);
        }
    }

    private void drawEndGame() {
        super.drawCenterAlignedText("Pressione qualquer tecla para voltar "
                + "ao Menu", 0.5f, super.viewport.getWorldHeight() * 0.35f);
    }

    private void loseLife() {
        this.lives--;
        hud.setLives(lives);
        if (this.lives == 0) {
            transitionTo(PlayScreenState.FINISHED_GAME_OVER);
        }
    }

    private void transitionTo(PlayScreenState newState) {
        switch (newState) {
            case FINISHED_GAME_OVER:
                break;

        }
        this.state = newState;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void onStateChanged(MiniGameState state) {
        switch (state) {
            case WON:
            case FAILED:
                if (state == MiniGameState.FAILED) {
                    loseLife();
                }
                Timer.instance().scheduleTask(new Task() {
                    @Override
                    public void run() {
                        advance();
                    }

                }, 1.5f);
                break;
        }
    }

    @Override
    public void onTimeEnding(long endingTime) {
        this.hud.startEndingTimer(endingTime);
    }

    enum PlayScreenState {
        PLAYING,
        FINISHED_GAME_OVER,
        FINISHED_WON
    }

}
