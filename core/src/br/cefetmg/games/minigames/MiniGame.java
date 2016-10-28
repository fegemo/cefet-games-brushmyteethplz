package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
import br.cefetmg.games.graphics.Hud;
import br.cefetmg.games.minigames.util.MiniGameState;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public abstract class MiniGame {

    public static final int INSTRUCTIONS_TIME = 4000;

    protected final BaseScreen screen;
    protected final long initialTime;
    protected long playingInitialTime;
    protected final long maxDuration;
    protected MiniGameState state;
    protected Random rand;
    protected Timer timer;
    private boolean isPaused;

    private final BitmapFont messagesFont;
    private final AnimatedSprite countdown;
    private final Texture grayMask, pausedImage, unpausedImage;
    private final Sprite pauseUnpauseSprite;
    private boolean challengeSolved;
    private boolean lastgame;
    private GameStateObserver stateObserver;

    public MiniGame(BaseScreen screen, float difficulty, long maxDuration,
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
        this.stateObserver = observer;
        this.initialTime = TimeUtils.millis();
        this.state = MiniGameState.INSTRUCTIONS;
        this.messagesFont = this.screen.assets.get("fonts/sawasdee-50.fnt");
        this.messagesFont.getRegion().getTexture().setFilter(
                Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        Texture countdownTexture = screen.assets.get("images/countdown.png",
                Texture.class);
        this.countdown = new AnimatedSprite(new Animation(2,
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
        this.rand = new Random();
        this.timer = new Timer();
        this.timer.stop();
        this.configureDifficultyParameters(difficulty);
    }

    public final void handleInput() {
        switch (this.state) {
            case INSTRUCTIONS:
                // caso aperte o isPaused, o tempo pausa
                Vector2 clickPosition = new Vector2(
                        Gdx.input.getX(), Gdx.input.getY());
                this.screen.viewport.unproject(clickPosition);
                
                if(Gdx.input.justTouched() &&
                        pauseUnpauseSprite.getBoundingRectangle()
                                .contains(clickPosition)){
                    isPaused = !isPaused;
                    pauseUnpauseSprite.setTexture(
                            isPaused ? pausedImage : unpausedImage);
                }
                break;
                
            case PLAYING:
                onHandlePlayingInput();
                break;
        }
    }

    public final void update(float dt) {
        switch (this.state) {
            case INSTRUCTIONS:
                if (!isPaused) {
                    this.countdown.update(dt);
                    if(TimeUtils.timeSinceMillis(initialTime)
                            > INSTRUCTIONS_TIME) {
                        transitionTo(MiniGameState.PLAYING);
                    }
                }
                break;

            case PLAYING:
                if (TimeUtils.timeSinceMillis(playingInitialTime)
                        > maxDuration) {
                    transitionTo(challengeSolved
                            ? MiniGameState.WON
                            : MiniGameState.FAILED);
                }
                onUpdate(dt);
                break;
        }
    }

    private float getCurrentCountdownScale() {
        long ellapsed = TimeUtils.timeSinceMillis(initialTime);
        if (ellapsed >= 3000) {
            return 1;
        } else {
            long ellapsedThisSecond = (long) (ellapsed % 1000);
            return Math.max(1 - (float) (ellapsedThisSecond / 1000f), 0.5f);
        }
    }

    private void drawCountdown() {
        this.countdown.setCenter(
                screen.viewport.getWorldWidth() / 2f,
                screen.viewport.getWorldHeight() / 2f);
        this.countdown.setScale(getCurrentCountdownScale());
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

    private void drawButtonPause() {
        pauseUnpauseSprite.draw(this.screen.batch);
    }

    public final void draw() {
        switch (this.state) {
            case INSTRUCTIONS:
                drawButtonPause();
                drawInstructions();
                if (isPaused) {
                    drawMask();
                } else {
                    drawCountdown();
                }
                break;

            case PLAYING:
                onDrawGame();
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
                playingInitialTime = TimeUtils.millis();
                this.timer.scheduleTask(new Task() {
                    @Override
                    public void run() {
                        stateObserver.onTimeEnding(TimeUtils.millis()
                                + Config.MINIGAME_COUNTDOWN_ON_HUD_BEGIN_AT
                                + 300);
                    }
                }, (maxDuration - Config.MINIGAME_COUNTDOWN_ON_HUD_BEGIN_AT)
                        / 1000f);
                timer.start();
                break;
            case WON:
            case FAILED:
                timer.stop();
                break;
        }
        this.state = newState;
        Hud.currentState = state;
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

    public abstract void onHandlePlayingInput();

    public abstract void onUpdate(float dt);

    public abstract void onDrawGame();

    public abstract String getInstructions();

    public abstract boolean shouldHideMousePointer();
}
