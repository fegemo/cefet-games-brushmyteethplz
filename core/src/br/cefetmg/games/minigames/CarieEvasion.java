package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.HashMap;
import br.cefetmg.games.minigames.util.GameStateObserver;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author dangon1 <d.aguiargoncalves@gmail.com>
 */
public class CarieEvasion extends MiniGame {

    private final Texture superToothTexture;
    private final Texture superToothTextureDead;
    private final SuperTooth superTooth;
    private final Texture tartarusTexture;
    private final Array<Sound> tartarusAppearingSound;
    private final Sound toothBreakingSound;
    private final Array<Tartarus> enemies;

    // variáveis do desafio - variam com a dificuldade do minigame
    private float minimumEnemySpeed;
    private float maximumEnemySpeed;
    private float spawnInterval;

    public CarieEvasion(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS, observer);
        this.superToothTexture = super.screen.assets.get("carie-evasion/supertooth.png", Texture.class);
        this.superToothTextureDead = super.screen.assets.get("carie-evasion/supertoothDead.png", Texture.class);
        this.superTooth = new SuperTooth(superToothTexture);
        this.tartarusTexture = super.screen.assets.get("shoo-the-tartarus/tartarus-spritesheet.png", Texture.class);
        this.tartarusAppearingSound = new Array<Sound>(3);
        this.tartarusAppearingSound.addAll(screen.assets.get("shoo-the-tartarus/appearing1.wav", Sound.class),
                screen.assets.get("shoo-the-tartarus/appearing2.wav", Sound.class),
                screen.assets.get("shoo-the-tartarus/appearing3.wav", Sound.class));
        this.toothBreakingSound = screen.assets.get("shoo-the-tartarus/tooth-breaking.wav", Sound.class);
        this.enemies = new Array<Tartarus>();
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
        goalCenter.x = MathUtils.random(super.screen.viewport.getWorldWidth());
        goalCenter.y = MathUtils.random(super.screen.viewport.getWorldHeight());

        Vector2 tartarusGoal = goalCenter;
        Vector2 tartarusPosition = new Vector2();

        tartarusPosition.x = super.screen.viewport.getWorldWidth();
        tartarusPosition.y = MathUtils.random(super.screen.viewport.getWorldHeight());

        Vector2 tartarusSpeed = tartarusGoal.sub(tartarusPosition).nor().scl(this.minimumEnemySpeed);

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
        this.minimumEnemySpeed = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 120, 230);
        this.maximumEnemySpeed = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 250, 450);
        this.spawnInterval = DifficultyCurve.LINEAR_NEGATIVE.getCurveValueBetween(difficulty, 0.05f, 0.4f);
    }

    @Override
    public void onHandlePlayingInput() {
        // atualiza a posição do alvo de acordo com o mouse
        //Gdx.input.setCursorCatched(true);
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        super.screen.viewport.unproject(click);
        this.superTooth.setCenter(click.x, click.y);

        // verifica se a cabeça da escova está próxima dos tártarus
        for (Tartarus tart : this.enemies) {
            float distance = this.superTooth.getHeadDistanceTo(tart.getX(), tart.getY());
            if (distance <= 30) {
                tart.startFleeing(this.superTooth.getHeadPosition());
            }
        }

    }

    private void toothWasHurt(Tartarus enemy) {
        this.enemies.removeValue(enemy, false);
        super.challengeFailed();
        toothBreakingSound.play();
    }

    @Override
    public void onUpdate(float dt) {
        // atualiza a escova (quadro da animação)
        //toothPaste.update(dt);

        // atualiza os inimigos (quadro de animação + colisão com dentes)
        for (int i = 0; i < this.enemies.size; i++) {
            Tartarus tart = this.enemies.get(i);
            tart.update(dt);

            // verifica se este inimigo está colidindo com algum dente
            if (tart.getBoundingRectangle().overlaps(superTooth.getBoundingRectangle())) {
                toothWasHurt(tart);
            }

        }
    }

    @Override
    public void onDrawGame() {
        for (Tartarus tart : this.enemies) {
            tart.draw(super.screen.batch);
        }
        superTooth.draw(super.screen.batch);

    }

    @Override
    public String getInstructions() {
        return "Evite a chuva de Tártaro";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class SuperTooth extends Sprite {

        static final int FRAME_WIDTH = 70;
        static final int FRAME_HEIGHT = 52;

        SuperTooth(final Texture superToothTexture) {
            super(superToothTexture);

        }

        Vector2 getHeadPosition() {
            return new Vector2(
                    this.getX(),
                    this.getY());
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getHeadPosition().dst(enemyX, enemyY);
        }

        public boolean wasHurt() {
            super.setTexture(superToothTextureDead);
            return true;
        }
    }

    class Tartarus extends MultiAnimatedSprite {

        private Vector2 speed;
        private boolean isFleeing = false;

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
            super.setAutoUpdate(false);
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

        public void startFleeing(Vector2 from) {
            if (this.isFleeing) {
                return;
            }
            this.isFleeing = true;
            Vector2 position = new Vector2(super.getX(), super.getY());
            this.speed = position.sub(from).nor().scl(maximumEnemySpeed);
            this.setColor(Color.YELLOW);
        }
    }
}
