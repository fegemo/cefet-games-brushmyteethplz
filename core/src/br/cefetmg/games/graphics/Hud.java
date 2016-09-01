package br.cefetmg.games.graphics;

import br.cefetmg.games.Config;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
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

    private Label sequenceIndexLabel;
    private HorizontalGroup livesGroup;
    private Label timeLabel;

    private Timer timer;

    public Hud(BaseScreen screen) {
        stage = new Stage(screen.viewport, screen.batch);
    }

    public void create() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        timer = new Timer();
        lifeTexture = new Texture("ui/lives.png");
        BitmapFont font = new BitmapFont(
                Gdx.files.internal("fonts/sawasdee-50.fnt"));

        table = new Table();
        table.bottom();
        table.setFillParent(true);

        LabelStyle labelStyle = new Label.LabelStyle(font, Color.BLACK);
        sequenceIndexLabel = new Label(
                String.format("%03d", 1), labelStyle);
        timeLabel = new Label("", labelStyle);

        livesGroup = new HorizontalGroup();
        for (int i = 0; i < Config.MAX_LIVES; i++) {
            livesGroup.addActor(new LifeHeart(lifeTexture));
        }

        table.padBottom(10);
        table.add(timeLabel).uniformX();
        table.add(livesGroup).uniformX();
        table.add(sequenceIndexLabel).uniformX();

        stage.addActor(table);
        stage.setDebugAll(false);
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
        System.out.println("chamou na hud");
        timer.scheduleTask(new Task() {
            
            @Override
            public void run() {
                long remainingTime = endingTime - TimeUtils.millis();
                System.out.println("remainingTime = " + remainingTime);
                if (remainingTime > 0) {
                    timeLabel.setText(String.format("%02d",
                            (int) Math.round(remainingTime / 1000f)));
                } else {
                    timeLabel.setText("");
                }
                
            }
        }, 0f, 1f, 4);
    }

    class LifeHeart extends Actor {

        private final MultiAnimatedSprite sprite;
        private static final int FRAME_WIDTH = 100;
        private static final int FRAME_HEIGHT = 112;

        LifeHeart(Texture lifeTexture) {
            TextureRegion[][] frames = TextureRegion
                    .split(lifeTexture, FRAME_WIDTH, FRAME_HEIGHT);
            Animation alive = new Animation(1f, frames[3][4]);
            Animation dying = new Animation(0.1f,
                    frames[3][4], frames[3][3], frames[3][2], frames[3][1],
                    frames[3][0], frames[2][7], frames[2][6], frames[2][5],
                    frames[2][4], frames[2][3], frames[2][2], frames[2][1],
                    frames[2][0], frames[1][7], frames[1][6], frames[1][5],
                    frames[1][4], frames[1][3], frames[1][2], frames[1][1],
                    frames[1][0], frames[0][7], frames[0][6], frames[0][5],
                    frames[0][4], frames[0][3], frames[0][2], frames[0][1],
                    frames[0][0]
            );
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
