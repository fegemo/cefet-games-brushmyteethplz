package br.cefetmg.games.screens;

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
import br.cefetmg.games.logic.chooser.BaseGameSequencer;
import br.cefetmg.games.logic.chooser.InfiniteGameSequencer;
import br.cefetmg.games.minigames.util.GameOption;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class PlayingGamesScreen extends BaseScreen
        implements GameStateObserver {

    private MiniGame currentGame;
    private final BaseGameSequencer sequencer;
    private final Hud hud;
    private PlayScreenState state;
    private int lives;
    private final Sounds sound;
    private final GameOption option;

    public PlayingGamesScreen(Game game, BaseScreen previous, GameOption option) {
        super(game, previous);
        super.assets.load("images/countdown.png", Texture.class);
        super.assets.load("images/gray-mask.png", Texture.class);
        super.assets.load("images/pausedImage.png", Texture.class);
        super.assets.load("images/unpausedImage.png", Texture.class);

        this.state = PlayScreenState.PLAYING;
        this.lives = 3;
        this.sound = new Sounds();
                this.option = option;

        if (this.option == GameOption.NORMAL) {
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
        } else {
            this.sequencer = new InfiniteGameSequencer(new HashSet<MiniGameFactory>(
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
                        new CleanTheToothFactory())
            ), this, this);
        } 
        this.hud = new Hud(this);
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        hud.create();
    }

    @Override
    public void handleInput() {
        if (this.currentGame != null) {
            this.currentGame.handleInput();
        }

        if (this.state == PlayScreenState.FINISHED_WON ||
                this.state == PlayScreenState.FINISHED_GAME_OVER) {
            if (Gdx.input.justTouched()) {
                // começa transição para voltar para o menu principal
                transitionState =  states.fadeOut;
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
                super.game.setScreen(new MenuScreen(super.game, this));		
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
            drawEndGame();
        }
        super.batch.end();
        hud.draw();
    }

    private void advance() {
        if (this.state == PlayScreenState.FINISHED_WON ||
                this.state == PlayScreenState.FINISHED_GAME_OVER) {
            if (option == GameOption.SURVIVAL) {
                RankScreen ranque;
                super.game.setScreen(ranque = new RankScreen(super.game, this)); 
                ranque.setPoints(sequencer.getGameNumber());
            }
            
            // se deu gameover ou terminou a sequencia com sucesso,
            // não deixa avançar para próximo minigame
            return;
        }
        
        if (this.sequencer.hasNextGame()) {
            this.currentGame = this.sequencer.nextGame();
            hud.setGameIndex(sequencer.getGameNumber());

            Gdx.input.setCursorPosition(
                    (int)Gdx.graphics.getWidth() / 2, 
                    (int)Gdx.graphics.getHeight() / 2);
        } else {
            // mostra mensagem de vitória
            this.transitionTo(PlayScreenState.FINISHED_WON);
            Gdx.input.setCursorCatched(false);
        }
    }

    private void drawEndGame() {
        if(option == GameOption.NORMAL){
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
    public void dispose() {
        super.dispose();
        Gdx.input.setCursorCatched(false);
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
