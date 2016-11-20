package br.cefetmg.games.minigames;

import br.cefetmg.games.Config;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import java.util.HashMap;

/**
 *
 * @author afp11
 */
public class FleeTheTartarus extends MiniGame {

    private final Array<Tartarus> enemies;
    private final Tooth tooth;
    private final Sprite background;

    private final Texture toothTexture;
    private final Texture tartarusTexture;
    private final Texture deadToothTexture;
    private final Texture backgroundTexture;

    private float spawnInterval;
    private float minimumEnemySpeed;
    private float maximumEnemySpeed;
    private final Array<Sound> tartarusAppearingSound;

    private final Sound venceu, perdeu;

    public FleeTheTartarus(BaseScreen screen,
            GameStateObserver observer, float difficulty) {

        super(screen, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS, observer);

        this.backgroundTexture = this.screen.assets.get(
                "flee-the-tartarus/fundo.png", Texture.class);
        this.background = new Sprite(this.backgroundTexture);
        this.background.setOriginCenter();

        float novaEscala = 1.4f;
        float escalaX = novaEscala * Gdx.graphics.getWidth();
        float escalaY = novaEscala * Gdx.graphics.getHeight();
        this.background.setSize(escalaX, escalaY);

        this.enemies = new Array<Tartarus>();
        this.tartarusTexture = this.screen.assets.get(
                "flee-the-tartarus/spritecarie.png", Texture.class);
        this.toothTexture = this.screen.assets.get(
                "flee-the-tartarus/dente.png", Texture.class);
        this.deadToothTexture = this.screen.assets.get(
                "flee-the-tartarus/dente-morto.png", Texture.class);
        this.tooth = new Tooth(toothTexture);
        this.tooth.setOriginCenter();
        this.tooth.setScale(0.7f);

        this.tartarusAppearingSound = new Array<Sound>(3);
        this.tartarusAppearingSound.addAll(screen.assets.get(
                "flee-the-tartarus/aperta2.mp3", Sound.class),
                screen.assets.get(
                        "flee-the-tartarus/aperta2.mp3", Sound.class),
                screen.assets.get(
                        "flee-the-tartarus/aperta2.mp3", Sound.class));

        this.venceu = screen.assets.get(
                "flee-the-tartarus/venceu.mp3", Sound.class);

        this.perdeu = screen.assets.get(
                "flee-the-tartarus/game-over.mp3", Sound.class);
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

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 30, 60);
        this.maximumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 70, 120);
        this.spawnInterval = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 1.5f);
    }

    @Override
    public void onHandlePlayingInput() {
        // atualiza a posição do alvo de acordo com o mouse
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        super.screen.viewport.unproject(click);
        this.tooth.setCenter(click.x, click.y);

        for (Tartarus t : this.enemies) {
            if (t.getBoundingRectangle().overlaps(tooth.getBoundingRectangle())) {
                tooth.setTexture(deadToothTexture);
                perdeu.play();
                super.challengeFailed();
            }
        }
    }

    @Override
    public void onUpdate(float dt) {

        // atualiza os inimigos (quadro de animação + colisão com dentes)
        for (int i = 0; i < this.enemies.size; i++) {
            Tartarus t = this.enemies.get(i);
            t.update(dt);
        }

    }

    @Override
    public void onDrawGame() {

        this.background.draw(this.screen.batch);

        tooth.draw(this.screen.batch);

        for (Tartarus tart : this.enemies) {
            tart.draw(super.screen.batch);
        }

    }

    @Override
    public String getInstructions() {
        return "Evite as Cáries e sobreviva!";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Tooth extends Sprite {

        static final int FRAME_WIDTH = 60;
        static final int FRAME_HEIGHT = 140;

        public Tooth(final Texture tooth) {
            super(tooth);
        }

        Vector2 getToothPosition() {
            return new Vector2(
                    this.getX() + this.getWidth() * 0.5f,
                    this.getY() + this.getHeight() * 0.8f);
        }

    }

    class Tartarus extends MultiAnimatedSprite {

        private Vector2 speed;

        static final int FRAME_WIDTH = 91;
        static final int FRAME_HEIGHT = 86;

        public Tartarus(final Texture tartarusSpritesheet) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(tartarusSpritesheet,
                                    FRAME_WIDTH, FRAME_HEIGHT);
                    Animation walking = new Animation(0.2f,
                            frames[0][0],
                            frames[0][1],
                            frames[1][0],
                            frames[0][0]);
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

        public void setSpeed(Vector2 speed) {
            this.speed = speed;
        }

    }

    private void spawnEnemy() {

        Vector2 tartarusPosition = new Vector2();

        boolean appearFromSides = MathUtils.randomBoolean();
        if (appearFromSides) {
            boolean appearFromLeft = MathUtils.randomBoolean();
            if (appearFromLeft) {
                tartarusPosition.x = 0;
                tartarusPosition.y = MathUtils.random(
                        Config.WORLD_HEIGHT);
            } else {
                tartarusPosition.x = Gdx.graphics.getWidth();
                tartarusPosition.y = MathUtils.random(
                        Config.WORLD_HEIGHT);
            }
        } else {
            boolean appearFromBottom = MathUtils.randomBoolean();
            if (appearFromBottom) {
                tartarusPosition.y = 0;
                tartarusPosition.x = MathUtils.random(
                        Config.WORLD_WIDTH);
            } else {
                tartarusPosition.y = Gdx.graphics.getHeight();
                tartarusPosition.x = MathUtils.random(
                        Config.WORLD_WIDTH);
            }
        }

        Vector2 obj = new Vector2(tooth.getToothPosition());

        Vector2 tartarusSpeed = obj.sub(tartarusPosition).nor()
                .scl(2 * this.maximumEnemySpeed);

        Tartarus enemy = new Tartarus(tartarusTexture);
        enemy.setPosition(tartarusPosition.x, tartarusPosition.y);
        enemy.setSpeed(tartarusSpeed);
        enemy.setScale(0.7f);
        enemies.add(enemy);

        tartarusAppearingSound.random().play(); //toca sempre a mesma musica

    }
}
