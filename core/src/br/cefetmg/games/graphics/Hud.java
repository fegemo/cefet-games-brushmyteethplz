package br.cefetmg.games.graphics;

import br.cefetmg.games.Config;
import br.cefetmg.games.minigames.util.MiniGameState;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.HashMap;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class Hud {

    private final Stage stage;
    private Table table;

    private Texture lifeTexture;
    private Texture clockTexture;

    private Label sequenceIndexLabel;
    private HorizontalGroup livesGroup;
    
    private Timer timer;

    private Sound timerSound;

    private Clock clock;

    public static MiniGameState currentState;

    private boolean isClocking;
    
    public Hud(BaseScreen screen) {
        stage = new Stage(screen.viewport, screen.batch);
    }

    public void create() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        timer = new Timer();
        isClocking=false;
        lifeTexture = new Texture("images/lives.png");
        clockTexture = new Texture("images/relogio.png");
        
        BitmapFont font = new BitmapFont(
                Gdx.files.internal("fonts/sawasdee-50.fnt"));

        table = new Table();
        table.bottom();
        table.setFillParent(true);

        LabelStyle labelStyle = new Label.LabelStyle(font, Color.ORANGE);
        sequenceIndexLabel = new Label(
                String.format("%03d", 1), labelStyle);
        
        livesGroup = new HorizontalGroup();
        for (int i = 0; i < Config.MAX_LIVES; i++) {
            livesGroup.addActor(new LifeHeart(lifeTexture));
        }

        clock =new Clock(clockTexture);
        
        table.padBottom(10);
        
        table.add(clock).uniformX();
        table.add(livesGroup).uniformX();
        table.add(sequenceIndexLabel).uniformX();

        stage.addActor(table);
        
        timerSound = Gdx.audio.newSound(Gdx.files.internal("ui/tick-tock.mp3"));
    }

    public void update(float dt) {
        stage.act(dt);
            
    }

    public void draw() {
        stage.draw();
    }

    public void setTime(float time) {

    }

    public void setGameIndex(int index) {
        sequenceIndexLabel.setText(String.format("%03d", index));
    }

    public void setLives(int lives) {
        if (lives < 0 || lives > Config.MAX_LIVES) {
            throw new IllegalArgumentException("A HUD está tentando mostrar "
                    + "um número de vidas menor que 0 ou maior que "
                    + Config.MAX_LIVES + ".");
        }
        for (int i = 0; i < Config.MAX_LIVES; i++) {
            LifeHeart heart = ((LifeHeart) livesGroup.getChildren().get(i));
            if (lives > i) {
                heart.alive();
            } else {              
                heart.die();            
            }
        }
    }

    public void startEndingTimer(final long endingTime) {
        timer.scheduleTask(new Task() {
            @Override
            public void run() {
                long remainingTime = endingTime - TimeUtils.millis();
                if (currentState != null && currentState.equals(MiniGameState.WON)) {
                    remainingTime = 0;
                }
                if (remainingTime > 0) {
                    //verifica se relogio esta já esta rodando
                     if(!isClocking){
                        clock.timeFinishing();
                        isClocking=true;
                    }
                    timerSound.play();
                } else {
                    clock.stopClock();
                    timerSound.stop();
                    isClocking=false;
                }

            }
        }, 0f, 1f, 4);
    }
    
    class Clock extends Actor{
    
        private final MultiAnimatedSprite sprite;
        private static final int FRAME_WIDTH = 50;
        private static final int FRAME_HEIGHT = 50;

        Clock(Texture clockTexture) {
            TextureRegion[][] frames = TextureRegion
                    .split(clockTexture, FRAME_WIDTH, FRAME_HEIGHT);
            Animation clock = new Animation(2f, frames[0][0], frames[0][1],frames[0][2],frames[0][3],frames[0][4]);
            Animation empty = new Animation(1f, frames[0][5]);
            
            clock.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
            HashMap<String, Animation> animations
                    = new HashMap<String, Animation>();
            animations.put("empty",empty);
            animations.put("clock", clock);
            sprite = new MultiAnimatedSprite(animations, "empty");
            sprite.setCenterFrames(true);
            sprite.setUseFrameRegionSize(true);
            setPosition(0, 0);
            setWidth(sprite.getWidth());
            setHeight(sprite.getHeight());
        }

        @Override
        public void draw(Batch batch, float alpha) {
            sprite.draw(batch, alpha);
        }

        @Override
        public void act(float dt) {
            super.act(dt);
            sprite.setPosition(getX(), getY());
            sprite.update(dt);
        }

        public void timeFinishing() {
            sprite.startAnimation("clock");
        }
        
        public void stopClock() {
            sprite.startAnimation("empty");
        }
    }

    class LifeHeart extends Actor {

        private final MultiAnimatedSprite sprite;
        private static final int FRAME_WIDTH = 100;
        private static final int FRAME_HEIGHT = 112;
        
        LifeHeart(Texture lifeTexture) {
            TextureRegion[][] frames = TextureRegion
                    .split(lifeTexture, FRAME_WIDTH, FRAME_HEIGHT);
            Animation alive = new Animation(1f, frames[0][5]);
            Animation dying = new Animation(.1f,new TextureRegion[]{frames[0][4], frames[0][3], frames[0][2], frames[0][1],frames[0][0]});
            HashMap<String, Animation> animations
                    = new HashMap<String, Animation>();
            animations.put("alive", alive);
            animations.put("dying", dying);
            
            sprite = new MultiAnimatedSprite(animations, "alive");
            sprite.setCenterFrames(true);
            sprite.setUseFrameRegionSize(true);
            setPosition(0, 0);
            setWidth(sprite.getWidth());
            setHeight(sprite.getHeight());
        }
        
        @Override
        public void draw(Batch batch, float alpha) {    
            sprite.draw(batch, alpha);
        }
        @Override
        public void act(float dt) {
            sprite.update(dt);
            super.act(dt);
            sprite.setPosition(getX(), getY());
            sprite.update(dt);
        }

        public void die() {
            sprite.startAnimation("dying");
        }
        
        public void alive() {
            sprite.startAnimation("alive");
        }
    }
}