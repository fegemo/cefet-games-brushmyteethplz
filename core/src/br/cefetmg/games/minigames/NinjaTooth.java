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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.HashMap;
import br.cefetmg.games.minigames.util.GameStateObserver;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author dangon1 <d.aguiargoncalves@gmail.com>
 */
public class NinjaTooth extends MiniGame {

    private final Texture superToothTexture;
    private final Texture superToothTextureDead;
    private final SuperTooth superTooth;
    private final Aim aim;
    private final Texture aimTexture;
    private final Texture bulletTexture;
    private final Texture tartarusTexture;
    private final Array<Sound> tartarusAppearingSound;
    private final Sound toothBreakingSound;
    private final Array<Tartarus> enemies;

    // variáveis do desafio - variam com a dificuldade do minigame
    private float minimumEnemySpeed;
    private float maximumEnemySpeed;

    private final float bulletFloatSpeed;
    private float spawnInterval;

    public NinjaTooth(BaseScreen screen, GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS, observer);
        this.superToothTexture = super.screen.assets.get("ninja-tooth/ninjatooth.png", Texture.class);
        this.bulletTexture = super.screen.assets.get("ninja-tooth/bullet-spritesheet.png", Texture.class);
        this.aimTexture = super.screen.assets.get("ninja-tooth/aim.png", Texture.class);
        this.bulletFloatSpeed = 100;
        this.superToothTextureDead = super.screen.assets.get("ninja-tooth/ninjatooth-dead.png", Texture.class);
        this.superTooth = new SuperTooth(superToothTexture);
        this.aim = new Aim(aimTexture);
        this.tartarusTexture = super.screen.assets.get("ninja-tooth/tartarus-spritesheet.png", Texture.class);
        this.tartarusAppearingSound = new Array<Sound>(3);
        this.tartarusAppearingSound.addAll(screen.assets.get("ninja-tooth/appearing1.wav", Sound.class),
                screen.assets.get("ninja-tooth/appearing2.wav", Sound.class),
                screen.assets.get("ninja-tooth/appearing3.wav", Sound.class));
        this.toothBreakingSound = screen.assets.get("ninja-tooth/tooth-breaking.wav", Sound.class);
        this.enemies = new Array<Tartarus>();
        this.superTooth.setCenter(screen.viewport.getWorldWidth() / 2, screen.viewport.getWorldHeight() / 2);
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
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());

        super.screen.viewport.unproject(click);

        aim.setPosition(click.x, click.y);
        if (Gdx.input.justTouched()) {
            superTooth.shoot(click);
        }

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

    private void killTartarus(Bullet b, Tartarus enemy) {
        this.enemies.removeValue(enemy, false);
        superTooth.bullets.removeValue(b, false);

        toothBreakingSound.play();
    }

    @Override
    public void onUpdate(float dt) {
        // atualiza os inimigos (quadro de animação + colisão com dentes)
        if (superTooth.getHeadPosition().x > super.screen.viewport.getWorldWidth()
                || superTooth.getHeadPosition().x < 0
                || superTooth.getHeadPosition().y > super.screen.viewport.getWorldHeight()
                || superTooth.getHeadPosition().y < 0) {

            superTooth.wasHurt();
            super.challengeFailed();
            toothBreakingSound.play();
        }

        for (int i = 0; i < this.enemies.size; i++) {
            Tartarus tart = this.enemies.get(i);
            tart.update(dt);

            // verifica se este inimigo está colidindo com algum dente
            if (tart.getBoundingRectangle().overlaps(superTooth.getBoundingRectangle())) {
                toothWasHurt(tart);
            }
            // bullet collision
            for (Bullet b : superTooth.bullets) {

                if (b.getBoundingRectangle().overlaps(tart.getBoundingRectangle())) {
                    killTartarus(b, tart);
                }
            }
        }
        superTooth.update(dt);
    }

    @Override
    public void onDrawGame() {
        //draw enemies
        for (Tartarus tart : this.enemies) {
            tart.draw(super.screen.batch);
        }

        //draw ninja tooth
        superTooth.draw(super.screen.batch);

        //draw ninja shurikens
        for (Bullet b : superTooth.bullets) {
            b.draw(super.screen.batch);
        }

        //draw aim
        aim.draw(super.screen.batch);
    }

    @Override
    public String getInstructions() {
        return "Proteja-se dos Tártaros com shurikens";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Aim extends Sprite {

        static final int FRAME_WIDTH = 70;
        static final int FRAME_HEIGHT = 52;

        Aim(final Texture aimTexture) {
            super(aimTexture);
        }
    }

    class SuperTooth extends Sprite {

        static final int FRAME_WIDTH = 70;
        static final int FRAME_HEIGHT = 52;

        private Vector2 speed;

        public Array<Bullet> bullets = new Array<Bullet>();

        SuperTooth(final Texture superToothTexture) {
            super(superToothTexture);
            bullets = new Array<Bullet>();
            speed = new Vector2();

        }

        public void update(float dt) {
            super.setPosition(super.getX() + this.speed.x * dt,
                    super.getY() + this.speed.y * dt);
            for (Bullet b : bullets) {
                b.update(dt);
            }
        }

        Vector2 getHeadPosition() {
            return new Vector2(
                    this.getX(),
                    this.getY());
        }

        void setHeadPosition(float x, float y) {
            this.setX(x);
            this.setY(y);
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getHeadPosition().dst(enemyX, enemyY);
        }

        public boolean wasHurt() {
            super.setTexture(superToothTextureDead);
            return true;
        }

        public void shoot(Vector2 click) {
            Bullet bullet = new Bullet(bulletTexture);

            Vector2 direction = new Vector2(superTooth.getHeadPosition().sub(click)).nor();
            Vector2 directionBullet = new Vector2(click.sub(bullet.getHeadPosition())).nor();

            bullet.setPosition(superTooth.getHeadPosition().x, superTooth.getHeadPosition().y);

            Vector2 toothSpeed = direction.scl(bulletFloatSpeed);
            superTooth.setSpeed(toothSpeed);

            Vector2 bulletSpeed = directionBullet.scl(bulletFloatSpeed);
            bullet.setSpeed(bulletSpeed);
            bullets.add(bullet);
        }

        public Vector2 getSpeed() {
            return speed;
        }

        public void setSpeed(Vector2 speed) {
            this.speed = speed;
        }

    }

    class Bullet extends MultiAnimatedSprite {

        private Vector2 speed;
        private float orientation;

        static final int FRAME_WIDTH = 29;
        static final int FRAME_HEIGHT = 36;
        
        static final int ANGULAR_SPEED = 300;

        public Bullet(final Texture bulletSpriteSheet) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(bulletSpriteSheet,
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
            super.setPosition(super.getX() + this.speed.x * dt, super.getY() + this.speed.y * dt);
            super.setRotation(orientation += ANGULAR_SPEED * dt);
        }

        public Vector2 getSpeed() {
            return speed;
        }

        public void setSpeed(Vector2 speed) {
            this.speed = speed;
        }

        public Vector2 getHeadPosition() {
            return new Vector2(this.getX(), this.getY());
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
