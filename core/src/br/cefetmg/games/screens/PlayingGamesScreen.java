package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import br.cefetmg.games.graphics.Hud;
import br.cefetmg.games.logic.chooser.GameSequencer;
import br.cefetmg.games.minigames.factories.MouthLandingFactory;
import br.cefetmg.games.minigames.factories.FleeFromTartarusFactory;
import br.cefetmg.games.minigames.factories.EscoveOsDentesFactory;
import br.cefetmg.games.minigames.factories.AngryToothsFactory;
import br.cefetmg.games.minigames.factories.CarieSwordFactory;
import br.cefetmg.games.minigames.factories.GallowsFactory;
import br.cefetmg.games.minigames.factories.SmashItFactory;
import br.cefetmg.games.minigames.factories.CarieEvasionFactory;
import br.cefetmg.games.minigames.factories.DefenseOfFluorineFactory;
import br.cefetmg.games.minigames.factories.CleanTheToothFactory;
import br.cefetmg.games.minigames.factories.ShooTheTartarusFactory;
import br.cefetmg.games.minigames.factories.ShootTheCariesFactory;
import br.cefetmg.games.minigames.factories.MiniGameFactory;
import br.cefetmg.games.minigames.factories.SaveTheTeethFactory;
import br.cefetmg.games.minigames.factories.PutTheBracesFactory;
import br.cefetmg.games.minigames.factories.FleeTheTartarusFactory;
import br.cefetmg.games.minigames.factories.CollectItensFactory;
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
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.factories.FleeFactory;
import br.cefetmg.games.sounds.Sounds;
import com.badlogic.gdx.graphics.g2d.Sprite;

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
    private final Sounds sound;
    private boolean shouldTransitionToMenuScreen;
    
    public PlayingGamesScreen(Game game, BaseScreen previous) {
        super(game, previous);
        super.assets.load("images/countdown.png", Texture.class);
        super.assets.load("images/gray-mask.png", Texture.class);

        this.state = PlayScreenState.PLAYING;
        this.lives = 3;
        this.sound = new Sounds();
        this.sequencer = new GameSequencer(5, new HashSet<MiniGameFactory>(
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
                        // nicolas e henrique
                        new PutTheBracesFactory(),
                        new EscoveOsDentesFactory(),
                        // lucas
                        new FleeFactory(),
                        new MouthLandingFactory(),
                        // lindley e lucas
                        new GallowsFactory(),
                        new SmashItFactory(),
                        // amanda e vinícius
                        new FleeTheTartarusFactory(),
                        new CollectItensFactory(),
                        // daniel
                        new CarieEvasionFactory(),
                        new DefenseOfFluorineFactory(),
                        // carlos e bruno
                        new CleanTheToothFactory()
                )
        ), 0, 1, this, this);
        this.hud = new Hud(this);
        shouldTransitionToMenuScreen = false;
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
                shouldTransitionToMenuScreen = true;
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
            
            switch (transitionState) {
                case fadeIn:
                case fadeOut:
                    transition.update(dt);
                    break;
            }
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
            if(shouldTransitionToMenuScreen){
                transition.fadeOut(batch, screenTransition);
                if(transition.isFinished()) {
                    shouldTransitionToMenuScreen = false;
                    super.game.setScreen(new MenuScreen(super.game, previous));
                }
            }
            drawEndGame();
        }
        super.batch.end();
        hud.draw();
    }

    private void advance() {
        if (this.state != PlayScreenState.PLAYING) {
            return;
        }
        
        int posX = Math.round(Gdx.graphics.getWidth() / 2);
        int posY = Math.round(Gdx.graphics.getHeight() / 2);
        
        if (this.sequencer.hasNextGame()) {
            this.currentGame = this.sequencer.nextGame();
            hud.setGameIndex(sequencer.getGameNumber());
            Gdx.input.setCursorCatched(currentGame.shouldHideMousePointer());
   
            Gdx.input.setCursorPosition(posX, posY);
            
        } else {
            // mostra mensagem de vitória
            this.transitionTo(PlayScreenState.FINISHED_WON);
            Gdx.input.setCursorCatched(false);
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
    public void dispose() {
        super.dispose();
        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void onStateChanged(MiniGameState state) {
        switch (state) {
            case WON:
                if (this.sequencer.hasNextGame()) {
                    sound.playSucess();
                    Gdx.input.setCursorCatched(false);
                } else {
                    sound.playGameWin();
                }
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
