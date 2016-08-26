package br.cefetmg.games.minigames;

import br.cefetmg.games.logic.play.MiniGameState;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import java.util.Random;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public abstract class MiniGame {
    
    public static final int INSTRUCTIONS_TIME = 4000;
    
    protected final BaseScreen screen;
    protected final int difficulty;
    protected final long initialTime;
    protected long playingInitialTime;
    protected final long maxDuration;
    protected MiniGameState state;
    protected boolean challengeSolved;
    protected Random rand;
    protected Timer timer;
    
    private final BitmapFont messagesFont;
    
    public MiniGame(BaseScreen screen, int difficulty, long maxDuration)  {
        this.screen = screen;
        this.difficulty = difficulty;
        this.maxDuration = maxDuration;
        this.initialTime = TimeUtils.millis();
        this.state = MiniGameState.INSTRUCTIONS;
        this.messagesFont = this.screen.assets.get("fonts/sawasdee.fnt");
        this.challengeSolved = false;
        this.rand = new Random();
        this.timer = new Timer();
        this.timer.stop();
    }
    
    public final void handleInput() {
        switch (this.state) {
            case INSTRUCTIONS:
                // se apertar qualquer tecla durante as instruções, pula para
                // o jogo
                if (Gdx.input.justTouched()) {
                    transitionTo(MiniGameState.PLAYING);
                }
                break;
            case PLAYING:
                onHandleInput();
                break;
        }
    }
    
    public final void update(float dt) {
        switch (this.state) {
            case INSTRUCTIONS:
                if (TimeUtils.timeSinceMillis(initialTime) > 
                        INSTRUCTIONS_TIME) {
                    transitionTo(MiniGameState.PLAYING);
                }
                break;
            case PLAYING:
                if (TimeUtils.timeSinceMillis(initialTime) > 
                        INSTRUCTIONS_TIME + maxDuration) {
                    transitionTo(challengeSolved
                            ? MiniGameState.WON 
                            : MiniGameState.FAILED);
                }
                onUpdate(dt);
                break;
                
        }
    }
    
    private String getCurrentCountdownMessage() {
        long now = TimeUtils.timeSinceMillis(initialTime);
        if (now >= 3000) {
            return "Começar!";
        } else if (now >= 2000) {
            return "1";
        } else if (now >= 1000) {
            return "2";
        } else {
            return "3";
        }
    }
    
    private float getCurrentCountdownScale() {
        long now = TimeUtils.timeSinceMillis(initialTime);
        long ellapsedThisSecond = (long) (now - Math.floor(now));
        return (1 - (float) (ellapsedThisSecond / 1000f)) * 3 + 1;
    }
    
    private void drawCountdown() {
        String countdown = getCurrentCountdownMessage();
        
        drawMessage(countdown, getCurrentCountdownScale());
    }
    
    protected void drawCenterAlignedText(String text, float scale, float y) {
        messagesFont.getData().setScale(scale * 0.1f);
        final float horizontalPadding = 0.05f;
        messagesFont.draw(this.screen.batch,
                text,
                0 + horizontalPadding, y, 
                this.screen.bounds.width*(1-horizontalPadding*2),
                Align.center, true
        );
    }
    
    
    protected void drawMessage(String message, float scale) {
        messagesFont.setColor(Color.TEAL);
        drawCenterAlignedText(message, scale, this.screen.bounds.getHeight()/2);
    }
            
    
    public final void draw() {
        switch (this.state) {
            case INSTRUCTIONS:
                drawCountdown();
                onDrawInstructions();
                break;
            case PLAYING:
                onDrawGame();
                break;
            case FAILED:
                onDrawGame();
                drawMessage("Falhou!", 1);
                break;
            case WON:
                onDrawGame();
                drawMessage("Conseguiu!", 1);
                break;                
        }
    }
    
    public abstract void onHandleInput();
    public abstract void onUpdate(float dt);
    public abstract void onDrawInstructions();
    public abstract void onDrawGame();
    
    public abstract void onChallengeSolved();
    public abstract void onChallengeFailed();

    private void transitionTo(MiniGameState newState) {
        switch (newState) {
            case PLAYING:
                playingInitialTime = TimeUtils.millis();
                timer.start();
                break;
            case WON:
            case FAILED:
                timer.stop();
                break;
        }
        this.state = newState;
    }
    
}
