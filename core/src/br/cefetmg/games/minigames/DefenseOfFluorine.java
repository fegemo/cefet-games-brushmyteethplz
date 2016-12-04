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
public class DefenseOfFluorine extends MiniGame {

    private final Texture toothPasteTexture;
    private final Texture fluorineTexture;
    private final ToothPaste toothPaste;
    private final Texture tartarusTexture;
    private final Texture toothTexture;
    private final Array<Sound> tartarusAppearingSound;
    private final Sound toothBreakingSound;
    private final Array<Tartarus> enemies;
    private final Array<Tooth> teeth;
    private final Array<Fluorine> fluorines;
    private int numberOfBrokenTeeth;
    private boolean clicked = false;
    private boolean canDraw = true;

    // variáveis do desafio - variam com a dificuldade do minigame
    private float minimumEnemySpeed;
    private float maximumEnemySpeed;
    private float spawnInterval;
    private int totalTeeth;
    private final int maximumFluorines;
    private final float maximumFluorineDuration;

    public DefenseOfFluorine(BaseScreen screen, GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10f,
                TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS, observer);
        this.toothPasteTexture = super.screen.assets.get("defense-of-fluorine/toothPaste.png", Texture.class);
        this.fluorineTexture = super.screen.assets.get("defense-of-fluorine/fluorine.png", Texture.class);
        this.toothPaste = new ToothPaste(toothPasteTexture);
        this.tartarusTexture = super.screen.assets.get("shoo-the-tartarus/tartarus-spritesheet.png", Texture.class);
        this.toothTexture = super.screen.assets.get("defense-of-fluorine/teeth.png", Texture.class);
        this.tartarusAppearingSound = new Array<Sound>(3);
        this.tartarusAppearingSound.addAll(screen.assets.get("shoo-the-tartarus/appearing1.wav", Sound.class),
                screen.assets.get("shoo-the-tartarus/appearing2.wav", Sound.class),
                screen.assets.get("shoo-the-tartarus/appearing3.wav", Sound.class));
        this.toothBreakingSound = screen.assets.get("shoo-the-tartarus/tooth-breaking.wav", Sound.class);
        this.enemies = new Array<Tartarus>();
        this.teeth = new Array<Tooth>();
        this.fluorines = new Array<Fluorine>();
        this.numberOfBrokenTeeth = 0;
        this.maximumFluorines = 1000;
        this.maximumFluorineDuration = (this.spawnInterval * 4);

        this.initializeTeeth();
    }

    @Override
    protected void onStart() {
        super.timer.scheduleTask(new Task() {
            @Override
            public void run() {
                spawnEnemy();
            }

        }, 0, this.spawnInterval);

        super.timer.scheduleTask(new Task() {
            @Override
            public void run() {
                fluorines.clear();
            }

        }, 0, this.maximumFluorineDuration);
    }

    private void initializeTeeth() {
        TextureRegion[][] frames = TextureRegion.split(toothTexture,
                Tooth.FRAME_WIDTH, Tooth.FRAME_HEIGHT);

        switch (this.totalTeeth) {
            case 1: // coloca o único dente no centro da tela
            {
                Tooth tooth = new Tooth(
                        frames[0][0],
                        frames[0][1],
                        2);
                tooth.setCenter(
                        super.screen.viewport.getWorldWidth() / 2f,
                        super.screen.viewport.getWorldHeight() / 2f);
                this.teeth.add(tooth);
            }
            break;
            case 2:
                // coloca os dois dentes verticalmente no centro, mas o
                // primeiro em 25% da largura e o segundo em 75%
                for (int i = 0; i < this.totalTeeth; i++) {
                    Tooth tooth = new Tooth(
                            frames[0][0],
                            frames[0][1],
                            2);
                    tooth.setCenter(
                            // 3/7 e 4/7 da largura da tela
                            super.screen.viewport.getWorldWidth() / 7f * (i + 3),
                            super.screen.viewport.getWorldHeight() / 2f);
                    this.teeth.add(tooth);
                }
                break;
            case 3:
            default:
                // coloca os 3 ou mais dentes em um círculo ao redor do centro
                for (int i = 0; i < this.totalTeeth; i++) {
                    float angle = (360f / this.totalTeeth) * i;
                    final float radius = 90f;
                    Tooth tooth = new Tooth(
                            frames[0][0],
                            frames[0][1],
                            2);
                    tooth.setCenter(
                            super.screen.viewport.getWorldWidth() / 2
                            + MathUtils.cosDeg(angle) * radius,
                            super.screen.viewport.getWorldHeight() / 2
                            + MathUtils.sinDeg(angle) * radius);
                    this.teeth.add(tooth);
                }
                break;
        }
    }

    private void spawnEnemy() {
        Vector2 goalCenter = new Vector2();
        Vector2 tartarusGoal = this.teeth.random().getBoundingRectangle().getCenter(goalCenter);
        Vector2 tartarusPosition = new Vector2();
        boolean appearFromSides = MathUtils.randomBoolean();
        if (appearFromSides) {
            tartarusPosition.x = MathUtils.randomBoolean() ? -Tartarus.FRAME_WIDTH : super.screen.viewport.getWorldWidth();
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
                .scl(this.minimumEnemySpeed);

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
        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 30, 60);
        this.maximumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 70, 120);
        this.spawnInterval = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 1.5f);
        this.totalTeeth = (int) Math.ceil(DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 0, 2)) + 1;
    }

    @Override
    public void onHandlePlayingInput() {
        // atualiza a posição do alvo de acordo com o mouse
        //Gdx.input.setCursorCatched(true);
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        super.screen.viewport.unproject(click);
        this.toothPaste.setCenter(click.x, click.y);

        if (Gdx.input.isTouched()) {
            if (this.fluorines.size >= maximumFluorines) {
                canDraw = false;
            }
            drawFluorine(click);
        } else if (!Gdx.input.isTouched()) {
            if (!canDraw) {
                this.fluorines.clear();
            }
            canDraw = true;
        }

        // verifica se a cabeça da escova está próxima dos tártarus
        for (Tartarus tart : this.enemies) {
            for (Fluorine fluor : this.fluorines) {
                float distance = fluor.getHeadDistanceTo(tart.getX(), tart.getY());
                if (distance <= 30) {
                    tart.startFleeing(fluor.getHeadPosition());
                }
            }
        }
    }

    public void drawFluorine(Vector3 click) {

        if (canDraw) {
            if (this.fluorines.size >= maximumFluorines) {
                this.fluorines.clear();
            }
            Fluorine fluorine = new Fluorine(fluorineTexture);
            fluorine.setCenter((click.x + 90), (click.y + 90));
            if (this.fluorines.size <= maximumFluorines) {
                this.fluorines.add(fluorine);
                clicked = true;
                canDraw = true;
            } else {
                clicked = false;
                canDraw = false;
            }
        }
    }

    private void toothWasHurt(Tooth tooth, Tartarus enemy) {
        this.enemies.removeValue(enemy, false);
        this.numberOfBrokenTeeth += tooth.wasHurt() ? 1 : 0;

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
            for (Tooth tooth : this.teeth) {
                if (tart.getBoundingRectangle()
                        .overlaps(tooth.getBoundingRectangle())) {
                    toothWasHurt(tooth, tart);
                }
            }
        }
    }

    @Override
    public void onDrawGame() {
        for (Tooth tooth : this.teeth) {
            tooth.draw(super.screen.batch);
        }
        for (Tartarus tart : this.enemies) {
            tart.draw(super.screen.batch);
        }
        toothPaste.draw(super.screen.batch);
        for (Fluorine fluor : this.fluorines) {
            if (clicked == true) {
                fluor.draw(super.screen.batch);
            }
        }
    }

    @Override
    public String getInstructions() {
        return "Proteja os dentes com Flúor";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class ToothPaste extends Sprite {

        static final int FRAME_WIDTH = 120;
        static final int FRAME_HEIGHT = 280;

        ToothPaste(final Texture toothPasteTexture) {
            super(toothPasteTexture);

        }

        Vector2 getHeadPosition() {
            return new Vector2(
                    this.getX() + this.getWidth() * 0.5f,
                    this.getY() + this.getHeight() * 0.8f);
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getHeadPosition().dst(enemyX, enemyY);
        }
    }

    class Fluorine extends Sprite {

        static final int FRAME_WIDTH = 49;
        static final int FRAME_HEIGHT = 44;

        Fluorine(final Texture fluorineTexture) {
            super(fluorineTexture);

        }

        Vector2 getHeadPosition() {
            return new Vector2(
                    this.getX(),
                    this.getY());
        }

        float getHeadDistanceTo(float enemyX, float enemyY) {
            return getHeadPosition().dst(enemyX, enemyY);
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

    class Tooth extends Sprite {

        private final TextureRegion broken;
        private int lives = 1;

        static final int FRAME_WIDTH = 85;
        static final int FRAME_HEIGHT = 109;

        public Tooth(TextureRegion textureOk, TextureRegion textureBroken, int lives) {
            super(textureOk);
            this.broken = textureBroken;
            this.lives = lives;
        }

        public boolean wasHurt() {
            lives--;
            super.setRegion(broken);
            return true;
        }
    }
}
