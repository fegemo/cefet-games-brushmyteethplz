package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import br.cefetmg.games.graphics.Hud;
import br.cefetmg.games.logic.chooser.GameSequencer;
import br.cefetmg.games.minigames.factories.*;
import br.cefetmg.games.minigames.util.MiniGameState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.Arrays;
import java.util.HashSet;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.sounds.Sounds;
import br.cefetmg.games.logic.chooser.BaseGameSequencer;
import br.cefetmg.games.logic.chooser.InfiniteGameSequencer;
import br.cefetmg.games.minigames.util.GameMode;
import br.cefetmg.games.minigames.util.GameStage;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class PlayingGamesScreen extends BaseScreen
        implements GameStateObserver {

    public static final boolean DEV_MODE = true;

    private MiniGame currentGame;
    private final BaseGameSequencer sequencer;
    private final Hud hud;
    private PlayScreenState state;
    private int lives;
    private final Sounds sound;
    private final GameMode mode;

    public PlayingGamesScreen(Game game, BaseScreen previous,
            GameMode mode, GameStage gameType) {
        super(game, previous);
        super.assets.load("images/countdown.png", Texture.class);
        super.assets.load("images/gray-mask.png", Texture.class);
        super.assets.load("images/pausedImage.png", Texture.class);
        super.assets.load("images/unpausedImage.png", Texture.class);

        this.state = PlayScreenState.PLAYING;
        this.lives = Config.MAX_LIVES;
        this.mode = mode;
        this.sequencer = initSequencer(mode, gameType);

        this.hud = new Hud(this);
        this.sound = new Sounds();
    }

    private BaseGameSequencer initSequencer(GameMode mode, GameStage stage) {
        if (mode == GameMode.CAMPAIGN) {
            return new GameSequencer(5,
                    new HashSet<MiniGameFactory>(stage.miniGames),
                    stage.initialDifficulty, stage.finalDifficulty, this, this);
        } else {
            return new InfiniteGameSequencer(new HashSet<MiniGameFactory>(
                    Arrays.asList(
                            // flávio
                            new ShootTheCariesFactory(),
                            new ShooTheTartarusFactory(),
                            // gabriel e juan
                            new SaveTheTeethFactory(),
                            new FleeFromTartarusFactory(),
                            // higor e matheus
                            new AngryToothsFactory(),
                            new CarieSwordFactory(),
                            new ToothRunnerFactory(),
                            // nicolas e henrique
                            new PutTheBracesFactory(),
                            new EscoveOsDentesFactory(),
                            new SnakeCariesFactory(),
                            // lucas carvalhais e lucas de aguilar
                            new FleeFactory(),
                            new MouthLandingFactory(),
                            // lindley e lucas
                            new GallowsFactory(),
                            new SmashItFactory(),
                            new SideWalkingFactory(),
                            // amanda e vinícius
                            new FleeTheTartarusFactory(),
                            new CollectItensFactory(),
                            new RamtoothFactory(),
                            // daniel
                            new CarieEvasionFactory(),
                            new DefenseOfFluorineFactory(),
                            new NinjaToothFactory(),
                            // carlos e bruno
                            new CleanTheToothFactory(),
                            // matheus ibrahim e luis gustavo
                            new DentalKombatFactory()
                    )
            ), this, this);
        }
    }

    @Override
    public void appear() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        hud.create();
    }

    @Override
    public void cleanUp() {
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void handleInput() {
        if (this.currentGame != null) {
            this.currentGame.handleInput();
        }

        if (this.state == PlayScreenState.FINISHED_WON
                || this.state == PlayScreenState.FINISHED_GAME_OVER) {
            if (Gdx.input.justTouched()) {
                // começa transição para voltar para o menu principal
                transitionState = states.fadeOut;
            }
        }
    }

    @Override
    public void update(float dt) {
        if (super.assets.update()) {
            if (this.state == PlayScreenState.PLAYING
                    && this.currentGame == null) {
                advance();
            }
            this.currentGame.update(dt);
            hud.update(dt);

            if (transitionState == states.fadeOut && transition.isFinished()) {
                switch (this.mode) {
                    case CAMPAIGN:
                        super.game.setScreen(new MenuScreen(super.game, this));
                        break;

                    case SURVIVAL:
                        RankingEntryScreen ranking = new RankingEntryScreen(
                                super.game, this);
                        ranking.setPoints(sequencer.getGameNumber());
                        super.game.setScreen(ranking);
                        break;
                }
            }
        }
    }

    @Override
    public void draw() {
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
        if (this.state == PlayScreenState.FINISHED_WON
                || this.state == PlayScreenState.FINISHED_GAME_OVER) {
            // se deu gameover ou terminou a sequencia com sucesso,
            // não deixa avançar para próximo minigame
            return;
        }

        if (this.sequencer.hasNextGame()) {
            this.currentGame = this.sequencer.nextGame();
            hud.setGameIndex(sequencer.getGameNumber());

            Gdx.input.setCursorPosition(
                    (int) Gdx.graphics.getWidth() / 2,
                    (int) Gdx.graphics.getHeight() / 2);
        } else {
            // mostra mensagem de vitória
            this.transitionTo(PlayScreenState.FINISHED_WON);
            Gdx.input.setCursorCatched(false);
        }
    }

    private void drawEndGame() {
        if (mode == GameMode.CAMPAIGN) {
            super.drawCenterAlignedText("Toque para voltar ao Menu",
                    0.5f, super.viewport.getWorldHeight() * 0.35f);
        }
    }

    private void loseLife() {
        this.lives--;
        hud.setLives(lives);
        if (this.lives == 0) {
            sound.playGameOver();
            transitionTo(PlayScreenState.FINISHED_GAME_OVER);
        } else if (this.sequencer.hasNextGame()) {
            sound.playFail();
        } else {
            sound.playGameWin();
        }
    }

    private void transitionTo(PlayScreenState newState) {
        switch (newState) {
            case FINISHED_GAME_OVER:
                Gdx.input.setCursorCatched(false);
                break;

        }
        this.state = newState;
    }

    @Override
    public void onStateChanged(MiniGameState state) {
        switch (state) {
            case PLAYING:
                Gdx.input.setCursorCatched(
                        this.currentGame.shouldHideMousePointer());
                break;

            case WON:
                if (this.sequencer.hasNextGame()) {
                    sound.playSucess();
                    Gdx.input.setCursorCatched(false);
                } else {
                    sound.playGameWin();
                }
            // deixa passar para próximo caso

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

                Gdx.input.setCursorCatched(false);
                this.hud.cancelEndingTimer();
                break;
        }
    }

    @Override
    public void onTimeEnding() {
        this.hud.startEndingTimer();
    }

    @Override
    public void onGamePausedOrUnpaused(boolean justPaused) {
        if (justPaused) {
            this.hud.pauseEndingTimer();
        } else {
            this.hud.resumeEndingTimer();
        }
    }

    enum PlayScreenState {
        PLAYING,
        FINISHED_GAME_OVER,
        FINISHED_WON
    }

}
