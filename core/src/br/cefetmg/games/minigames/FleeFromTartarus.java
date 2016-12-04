package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.HashMap;
import br.cefetmg.games.minigames.util.GameStateObserver;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class FleeFromTartarus extends MiniGame {

    private final Texture backGroundTexture;
    private final Texture tartarusTexture;
    private final Texture toothTexture;
    private final Array<Sound> tartarusAppearingSound;
    private final Sound toothBreakingSound;
    private final Sound backGroundSound;
    private final Sound gameOverSound;
    private final Array<Tartarus> enemies;
    private final Tooth tooth;
    private int numberOfBrokenTeeth;
    private final Sprite bg;

    // variáveis do desafio - variam com a dificuldade do minigame
    private float enemySpeed;
    private float spawnInterval;
    private final Vector2 window;

    public FleeFromTartarus(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS, observer);
        this.backGroundTexture = super.screen.assets.get(
                "flee-from-tartarus/background.jpg", Texture.class);
        this.toothTexture = super.screen.assets.get(
                "flee-from-tartarus/tooth.png", Texture.class);
        this.tartarusTexture = super.screen.assets.get(
                "flee-from-tartarus/tartarus-spritesheet.png", Texture.class);
        this.tartarusAppearingSound = new Array<Sound>(3);
        this.tartarusAppearingSound.addAll(screen.assets.get(
                "flee-from-tartarus/appearing1.wav", Sound.class),
                screen.assets.get(
                        "flee-from-tartarus/appearing2.wav", Sound.class),
                screen.assets.get(
                        "flee-from-tartarus/appearing3.wav", Sound.class));
        this.toothBreakingSound = screen.assets.get(
                "flee-from-tartarus/tooth-breaking.mp3", Sound.class);
        this.backGroundSound = screen.assets.get(
                "flee-from-tartarus/fundo.wav", Sound.class);
        this.gameOverSound = screen.assets.get(
                "flee-from-tartarus/gameover.wav", Sound.class);
        this.enemies = new Array<Tartarus>();
        TextureRegion[][] frames = TextureRegion.split(toothTexture,
                Tooth.FRAME_WIDTH, Tooth.FRAME_HEIGHT);
        this.tooth = new Tooth(
                frames[0][0],
                frames[0][1],
                frames[0][2],
                2);
        tooth.setCenter(
                super.screen.viewport.getWorldWidth() / 2f,
                super.screen.viewport.getWorldHeight() / 2f);
        this.numberOfBrokenTeeth = 0;

        this.window = new Vector2(super.screen.viewport.getWorldWidth(), super.screen.viewport.getWorldHeight());
        this.bg = new Sprite(backGroundTexture);
        this.bg.setSize(window.x, window.y);
        backGroundSound.play();
    }

    @Override
    protected void onStart() {
        super.timer.scheduleTask(new Task() {
            @Override
            public void run() {
                spawnEnemy();
            }

        }, 0, this.spawnInterval);
    }

    private void spawnEnemy() {
        Vector2 goalCenter = new Vector2();
        Vector2 tartarusGoal = this.tooth
                .getBoundingRectangle()
                .getCenter(goalCenter);
        Vector2 tartarusPosition = new Vector2();
        boolean appearFromSides = MathUtils.randomBoolean();
        if (appearFromSides) {
            tartarusPosition.x = MathUtils.randomBoolean()
                    ? -Tartarus.FRAME_WIDTH
                    : super.screen.viewport.getWorldWidth();
            tartarusPosition.y = MathUtils.random(
                    -Tartarus.FRAME_HEIGHT,
                    super.screen.viewport.getWorldHeight());
        } else {
            tartarusPosition.y = MathUtils.randomBoolean()
                    ? -Tartarus.FRAME_HEIGHT
                    : super.screen.viewport.getWorldHeight();
            tartarusPosition.x = MathUtils.random(
                    -Tartarus.FRAME_WIDTH,
                    super.screen.viewport.getWorldWidth());
        }
        Tartarus enemy = new Tartarus(tartarusTexture);
        enemy.setPosition(tartarusPosition.x, tartarusPosition.y);
        enemy.setSpeed();
        enemies.add(enemy);

        // toca um efeito sonoro
        Sound sound = tartarusAppearingSound.random();
        long id = sound.play(0.5f);
        sound.setPan(id, tartarusPosition.x < screen.viewport.getWorldWidth()
                ? -1 : 1, 1);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.enemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 100, 120);
        this.spawnInterval = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 1f);
    }

    @Override
    public void onHandlePlayingInput() {

        tooth.update();
        for (Tartarus tart : this.enemies) {
            tart.setSpeed();
        }
    }

    private void toothWasHurt(Tooth tooth, Tartarus enemy) {
        this.enemies.removeValue(enemy, false);
        this.numberOfBrokenTeeth += tooth.wasHurt() ? 1 : 0;

        if (this.numberOfBrokenTeeth >= 1) {
            backGroundSound.stop();
            gameOverSound.play();
            super.challengeFailed();
        }
        toothBreakingSound.play();
    }

    @Override
    public void onUpdate(float dt) {
        // atualiza a escova (quadro da animação)
        // atualiza os inimigos (quadro de animação + colisão com dentes)
        for (int i = 0; i < this.enemies.size; i++) {
            Tartarus tart = this.enemies.get(i);
            tart.update(dt);

            // verifica se este inimigo está colidindo com algum dente
            if (tart.getBoundingRectangle()
                    .overlaps(this.tooth.getBoundingRectangle())) {
                toothWasHurt(this.tooth, tart);
            }
        }
    }

    @Override
    public void onDrawGame() {
        bg.draw(super.screen.batch);
        this.tooth.draw(super.screen.batch);
        for (Tartarus tart : this.enemies) {
            tart.draw(super.screen.batch);
        }
        //     toothBrush.draw(super.screen.batch);
    }

    @Override
    public String getInstructions() {
        return "Fuja dos Tártaros";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Tartarus extends MultiAnimatedSprite {

        private Vector2 speed;

        static final int FRAME_WIDTH = 28;
        static final int FRAME_HEIGHT = 36;

        public Tartarus(final Texture tartarusSpritesheet) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(tartarusSpritesheet,
                                    FRAME_WIDTH, FRAME_HEIGHT);
                    Animation walking = new Animation(0.2f,
                            frames[0][0],
                            frames[0][1],
                            frames[0][2],
                            frames[0][1]);
                    walking.setPlayMode(Animation.PlayMode.LOOP);
                    put("walking", walking);
                }
            }, "walking");
        }

        @Override
        public void update(float dt) {
            super.update(dt);
            super.setPosition(super.getX() + this.speed.x * dt,
                    super.getY() + this.speed.y * dt);
        }

        public Vector2 getSpeed() {
            return speed;
        }

        public void setSpeed() {
            Vector2 position = new Vector2(super.getX(), super.getY());
            Vector2 click = new Vector2(tooth.getPosition());
            this.speed = click.sub(position).nor().scl(enemySpeed);
        }
    }

    class Tooth extends Sprite {

        private final TextureRegion hurt;
        private final TextureRegion broken;
        private int lives = 2;

        static final int FRAME_WIDTH = 64;
        static final int FRAME_HEIGHT = 64;

        public Tooth(TextureRegion textureOk, TextureRegion textureHurt,
                TextureRegion textureBroken, int lives) {
            super(textureOk);
            this.hurt = textureHurt;
            this.broken = textureBroken;
            this.lives = lives;
        }

        public void update() {
            Vector2 mouse = new Vector2(Gdx.input.getX(), window.y - Gdx.input.getY());
            if (mouse.x < 0) {
                mouse.x = 0;
            } else if (mouse.x > window.x - Tooth.FRAME_WIDTH) {
                mouse.x = window.x - Tooth.FRAME_WIDTH;
            }
            if (mouse.y < 0) {
                mouse.y = 0;
            } else if (mouse.y > window.y - Tooth.FRAME_HEIGHT) {
                mouse.y = window.y - Tooth.FRAME_HEIGHT;
            }

            super.setPosition(mouse.x, mouse.y);
        }

        public Vector2 getPosition() {
            return new Vector2(super.getX(), super.getY());
        }

        public boolean wasHurt() {
            lives--;
            super.setRegion(lives > 0 ? hurt : broken);
            return lives == 0;
        }
    }
}
