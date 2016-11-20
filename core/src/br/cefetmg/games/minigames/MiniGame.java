package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
import br.cefetmg.games.minigames.util.MiniGameState;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.Random;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.screens.MenuScreen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public abstract class MiniGame {

    protected final BaseScreen screen;
    protected long remainingTime;
    protected float maxDuration;
    private float timeSpentOnInstructions;
    private float timeSpentPlaying;
    protected MiniGameState state;
    protected Random rand;
    protected Timer timer;
    private boolean isPaused;

    private final BitmapFont messagesFont;
    private final AnimatedSprite countdown;
    private final Texture grayMask, pausedImage, unpausedImage,
            goBackTexture, leaveTexture;
    private final Sprite pauseUnpauseSprite, goBackSprite, leaveSprite;
    private boolean challengeSolved;
    private GameStateObserver stateObserver;
    private long timeWhenPausedLastTime;
    private InputProcessor miniGameInputProcessor;

    public MiniGame(BaseScreen screen, float difficulty, float maxDuration,
            TimeoutBehavior endOfGameSituation, final GameStateObserver observer) {
        if (difficulty < 0 || difficulty > 1) {
            throw new IllegalArgumentException(
                    "A dificuldade (difficulty) de um minigame deve ser um "
                    + "número entre 0 e 1. Você passou o número " + difficulty
                    + ".");
        }

        this.screen = screen;
        this.challengeSolved = endOfGameSituation
                == TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS;
        this.maxDuration = maxDuration;
        this.timeSpentPlaying = 0;
        this.timeSpentOnInstructions = 0;
        this.stateObserver = observer;
        this.state = MiniGameState.INSTRUCTIONS;
        this.messagesFont = this.screen.assets.get("fonts/sawasdee-50.fnt");
        this.messagesFont.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        Texture countdownTexture = screen.assets.get("images/countdown.png",
                Texture.class);
        this.countdown = new AnimatedSprite(new Animation(1,
                new TextureRegion[]{
                    // 3
                    new TextureRegion(countdownTexture, 0, 0, 180, 200),
                    // 2
                    new TextureRegion(countdownTexture, 180, 0, 180, 200),
                    // 1
                    new TextureRegion(countdownTexture, 360, 0, 180, 200),
                    // Começar!
                    new TextureRegion(countdownTexture, 0, 200, 540, 200),}
        ));
        this.countdown.setAutoUpdate(false);
        this.countdown.setUseFrameRegionSize(true);
        this.countdown.setCenterFrames(true);
        this.countdown.setCenter(
                screen.viewport.getWorldWidth() / 2f,
                screen.viewport.getWorldHeight() / 2f);
        this.countdown.getAnimation().setPlayMode(Animation.PlayMode.NORMAL);
        this.grayMask = screen.assets.get("images/gray-mask.png",
                Texture.class);
        this.pausedImage = screen.assets.get("images/pausedImage.png",
                Texture.class);
        this.unpausedImage = screen.assets.get("images/unpausedImage.png",
                Texture.class);

        this.pauseUnpauseSprite = new Sprite(unpausedImage, 100, 100);
        this.pauseUnpauseSprite.setPosition(10, 10);

        this.goBackTexture = new Texture("buttons_menu/button_voltar.png");
        this.goBackSprite = new Sprite(goBackTexture, 166, 77);
        this.goBackSprite.setPosition(
                screen.viewport.getWorldWidth() / 2f - 100,
                screen.viewport.getWorldHeight() / 2f);
        this.leaveTexture = new Texture("buttons_menu/button_sair.png");
        this.leaveSprite = new Sprite(leaveTexture, 166, 77);
        this.leaveSprite.setPosition(
                screen.viewport.getWorldWidth() / 2f - 100,
                screen.viewport.getWorldHeight() / 2f - 100);

        this.rand = new Random();
        this.timer = new Timer();
        this.timer.stop();
        this.configureDifficultyParameters(difficulty);
    }

    public final void handleInput() {
        Vector2 clickPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        this.screen.viewport.unproject(clickPosition);

        if (Gdx.input.justTouched()) {
            // se o botão pause/play foi clicado, ou então o botão voltar quando
            // já estamos em pausa (i.e., o botão está visível)...
            if (pauseUnpauseSprite.getBoundingRectangle()
                    .contains(clickPosition)
                    || (isPaused && goBackSprite.getBoundingRectangle()
                    .contains(clickPosition))) {
                // alterna entre pausado/jogando
                isPaused = !isPaused;
                pauseUnpauseSprite.setTexture(isPaused
                        ? pausedImage : unpausedImage);
                
                // se pausou quando o relojinho de fim do tempo já está 
                // aparecendo, precisa avisar a HUD sobre isso
                if (timeSpentPlaying > maxDuration
                        - Config.MINIGAME_COUNTDOWN_ON_HUD_BEGIN_AT) {
                    stateObserver.onGamePausedOrUnpaused(isPaused);
                }

                // acabou de pausar
                if (isPaused) {
                    // interrompe o timer do minigame, salvando o momento em
                    // que o jogo foi pausado
                    this.timer.stop();
                    this.timeWhenPausedLastTime = TimeUtils.nanosToMillis(
                            TimeUtils.nanoTime());
                    
                    // libera o cursor do mouse
                    Gdx.input.setCursorCatched(false);
                    
                    // salva um possível processador de input do minigame e o
                    // desabilita até que o jogo seja despausado
                    this.miniGameInputProcessor = Gdx.input.getInputProcessor();
                    Gdx.input.setInputProcessor(null);
                }
                // acabou de despausar
                else {
                    // retoma o timer, atrasando-o pelo tempo que o jogo ficou
                    // pausado
                    this.timer.start();
                    this.timer.delay(TimeUtils.nanosToMillis(
                            TimeUtils.nanoTime()) - this.timeWhenPausedLastTime);
                    
                    // recupera o possível processador de input do minigame
                    Gdx.input.setInputProcessor(this.miniGameInputProcessor);
                    
                    // se a pausa foi feita durante o jogo (fora das instruções
                    // ou do final do jogo), oculta novamente o cursor
                    if (state == MiniGameState.PLAYING) {
                        Gdx.input.setCursorCatched(shouldHideMousePointer());
                    }
                }
            }

            // jogador clicou em "sair" da tela de jogo: volta para menu
            if (leaveSprite.getBoundingRectangle()
                    .contains(clickPosition) && isPaused) {
                this.screen.game.setScreen(
                        new MenuScreen(this.screen.game, this.screen));
            }
        }

        // deixa o MiniGame lidar com o input apenas se estivermos no estado
        // de jogo propriamente dito e sem pausa
        if (this.state == MiniGameState.PLAYING && !isPaused) {
            onHandlePlayingInput();
        }
    }

    private void updateCountdown(float dt) {
        this.countdown.setScale(getCurrentCountdownScale());
        this.countdown.update(dt);
    }

    public final void update(float dt) {
        if (isPaused) {
            return;
        }

        switch (this.state) {
            case INSTRUCTIONS:
                this.timeSpentOnInstructions += dt;
                updateCountdown(dt);
                if (timeSpentOnInstructions
                        > Config.TIME_SHOWING_MINIGAME_INSTRUCTIONS) {
                    transitionTo(MiniGameState.PLAYING);
                }

                break;

            case PLAYING:
                timeSpentPlaying += dt;
                if (timeSpentPlaying > maxDuration) {
                    transitionTo(challengeSolved
                            ? MiniGameState.WON
                            : MiniGameState.FAILED);
                }
                onUpdate(dt);
                break;
        }
    }

    private float getCurrentCountdownScale() {
        if (timeSpentOnInstructions >= 3) {
            return 1;
        } else {
            float ellapsedThisSecond = timeSpentOnInstructions % 1;
            return Math.max(1 - ellapsedThisSecond, 0.5f);
        }
    }

    private void drawCountdown() {
        this.countdown.draw(this.screen.batch);
    }

    private void drawInstructions() {
        float y = this.screen.viewport.getWorldHeight() * 0.75f;
        this.screen.drawCenterAlignedText(this.getInstructions(), 1, y);
    }

    protected void drawMessage(String message, float scale) {
        messagesFont.setColor(Color.BLACK);
        this.screen.drawCenterAlignedText(message, scale,
                this.screen.viewport.getWorldHeight() / 2);
    }

    private void drawMask() {
        this.screen.batch.draw(grayMask,
                0, 0,
                this.screen.viewport.getWorldWidth(),
                this.screen.viewport.getWorldHeight());
    }

    private void drawPauseButton() {
        pauseUnpauseSprite.draw(this.screen.batch);
    }

    private void drawPauseMenu() {
        goBackSprite.draw(this.screen.batch);
        leaveSprite.draw(this.screen.batch);
    }

    public final void draw() {
        switch (this.state) {
            case INSTRUCTIONS:
                drawInstructions();
                drawCountdown();
                drawPauseButton();
                break;

            case PLAYING:
                onDrawGame();
                if (isPaused) {
                    drawMask();
                    drawPauseMenu();
                }
                drawPauseButton();
                break;

            case FAILED:
            case WON:
                onDrawGame();
                drawMask();
                drawMessage(this.state == MiniGameState.FAILED ? "Falhou!"
                        : "Conseguiu!", 1);
                Gdx.gl.glClearColor(1, 1, 1, 1);
                break;
        }
    }

    private void transitionTo(MiniGameState newState) {
        switch (newState) {
            case PLAYING:
                this.onStart();

                this.timer.scheduleTask(new Task() {
                    @Override
                    public void run() {
                        stateObserver.onTimeEnding();
                    }
                }, (maxDuration - Config.MINIGAME_COUNTDOWN_ON_HUD_BEGIN_AT));

                timer.start();
                break;

            case WON:
            case FAILED:
                this.onEnd();
                timer.stop();
                break;
        }
        this.state = newState;
        this.stateObserver.onStateChanged(state);
    }

    protected void challengeFailed() {
        this.challengeSolved = false;
        transitionTo(MiniGameState.FAILED);
    }

    protected void challengeSolved() {
        this.challengeSolved = true;
        transitionTo(MiniGameState.WON);
    }

    protected abstract void configureDifficultyParameters(float difficulty);

    protected void onStart() {
        // intentionally left for MiniGames to implement
    }

    protected void onEnd() {
        // intentionally left for MiniGames to implement
    }

    public abstract void onHandlePlayingInput();

    public abstract void onUpdate(float dt);

    public abstract void onDrawGame();

    public abstract String getInstructions();

    public abstract boolean shouldHideMousePointer();
}
