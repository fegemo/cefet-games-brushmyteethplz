package br.cefetmg.games.minigames;

import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import java.util.HashMap;

/**
 *
 * @author Lucas Batista
 */
public class Flee extends MiniGame {

    private final Texture toothTexture;
    private final Tooth tooth;
    private final Texture tartarusTexture;
    private final Array<Sound> tartarusAppearingSound;
    private final Sound toothBreakingSound;
    private final Array<Tartarus> enemies;

    // variáveis do desafio - variam com a dificuldade do minigame
    private float enemySpeed;
    private float spawnInterval;

    public Flee(BaseScreen screen, GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS, observer);
        this.toothTexture = super.screen.assets.get(
                "flee/toothLiveDeath.png", Texture.class);
        TextureRegion[][] frames = TextureRegion.split(toothTexture,
                Tooth.FRAME_WIDTH, Tooth.FRAME_HEIGHT);
        this.tooth = new Tooth(frames[0][0], frames[0][1]);
        this.tartarusTexture = super.screen.assets.get(
                "flee/enemy.png", Texture.class);
        this.tartarusAppearingSound = new Array<Sound>(3);
        this.tartarusAppearingSound.addAll(screen.assets.get(
                "shoo-the-tartarus/appearing1.wav", Sound.class),
                screen.assets.get(
                        "shoo-the-tartarus/appearing2.wav", Sound.class),
                screen.assets.get(
                        "shoo-the-tartarus/appearing3.wav", Sound.class));
        this.toothBreakingSound = screen.assets.get(
                "shoo-the-tartarus/tooth-breaking.wav", Sound.class);
        this.enemies = new Array<Tartarus>();
    }

    @Override
    protected void onStart() {
        super.timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                spawnEnemy();
            }
        }, 0, this.spawnInterval);
    }

    private void spawnEnemy() {
        Vector2 goalCenter = new Vector2();
        Vector2 tartarusGoal = this.tooth.getBoundingRectangle().getCenter(goalCenter);
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
        Vector2 tartarusSpeed = tartarusGoal
                .sub(tartarusPosition)
                .nor()
                .scl(this.enemySpeed);

        Tartarus enemy = new Tartarus(tartarusTexture);
        enemy.setPosition(tartarusPosition.x, tartarusPosition.y);
        enemy.setSpeed(tartarusSpeed);
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
                .getCurveValueBetween(difficulty, 75, 130);
        this.spawnInterval = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 1f);
    }

    @Override
    public void onHandlePlayingInput() {
        // atualiza a posição do alvo de acordo com o mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        super.screen.viewport.unproject(click);
        this.tooth.setCenter(click.x, click.y);

        // verifica se o dente está próximo do inimigo
        for (Tartarus tart : this.enemies) {
            if (tart.getBoundingRectangle()
                    .overlaps(tooth.getBoundingRectangle())) {
                tooth.wasHurt();
                tart.hit = true;
                toothBreakingSound.play();
                super.challengeFailed();
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
        // atualiza os inimigos (quadro de animação + colisão com dentes)
        for (int i = 0; i < this.enemies.size; i++) {
            Tartarus tart = this.enemies.get(i);
            tart.update(dt);
        }
    }

    @Override
    public void onDrawGame() {
        for (Tartarus tart : this.enemies) {
            tart.draw(super.screen.batch);
        }
        tooth.draw(super.screen.batch);
    }

    @Override
    public String getInstructions() {
        return "Não encoste nos Tártaros";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Tooth extends Sprite {

        private final TextureRegion hurt;

        static final int FRAME_WIDTH = 80;
        static final int FRAME_HEIGHT = 120;

        public Tooth(TextureRegion textureOk, TextureRegion textureHurt) {
            super(textureOk);
            this.hurt = textureHurt;
        }

        void wasHurt() {
            super.setRegion(hurt);
        }
    }

    class Tartarus extends MultiAnimatedSprite {

        private Vector2 speed;
        private boolean hit = false;

        static final int FRAME_WIDTH = 50;
        static final int FRAME_HEIGHT = 70;

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
            if (hit == false) {
                super.setPosition(super.getX() + this.speed.x * dt,
                        super.getY() + this.speed.y * dt);
            }
        }

        public Vector2 getSpeed() {
            return speed;
        }

        public void setSpeed(Vector2 speed) {
            this.speed = speed;
        }
    }
}
