package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 *
 * @author Lucas de Aguilar, Luis Gustavo
 */
public class MouthLanding extends MiniGame {

    private final double GRAVITY_FACTOR;
    private final Tooth tooth;
    private final Texture toothTexture;
    private final Texture mouthTexture;
    private final Mouth mouth;
    private final Vector2 toothPosition = new Vector2();
    private double velNow;
    private double verticalUpForceFactor;
    private int rightForce;
    private int leftForce;
    private final Sound sucessSound;
    private final Sound failSound;
    private final Sound fire1;
    private final Sound fire2;
    private final Sound fire3;
    private int counter;
    private final Texture targetTexture;
    private final Sprite target;
    private int velocidadeMaxima;

    public MouthLanding(BaseScreen screen, GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        this.mouthTexture = super.screen.assets.get("mouth-landing/mouth.png", Texture.class);
        this.toothTexture = super.screen.assets.get("mouth-landing/rocket-tooth.png", Texture.class);
        this.sucessSound = screen.assets.get("mouth-landing/sucess.wav", Sound.class);
        this.failSound = screen.assets.get("mouth-landing/fail.wav", Sound.class);
        this.fire1 = screen.assets.get("mouth-landing/fire1.wav", Sound.class);
        this.fire2 = screen.assets.get("mouth-landing/fire2.wav", Sound.class);
        this.fire3 = screen.assets.get("mouth-landing/fire3.wav", Sound.class);
        this.targetTexture = screen.assets.get("mouth-landing/target.png", Texture.class);
        this.target = new Sprite(targetTexture);

        TextureRegion[][] framesTooth = TextureRegion.split(toothTexture, Tooth.FRAME_WIDTH, Tooth.FRAME_HEIGHT);
        this.tooth = new Tooth(framesTooth[0][0], framesTooth[0][1], framesTooth[0][2]);
        TextureRegion[][] framesMouth = TextureRegion.split(mouthTexture, Mouth.FRAME_WIDTH, Mouth.FRAME_HEIGHT);
        this.mouth = new Mouth(framesMouth[0][0]);

        toothPosition.x = 640;
        toothPosition.y = 600;
        verticalUpForceFactor = 0.035;
        velNow = 10;
        counter = 0;
        this.GRAVITY_FACTOR = 1.0206665992736816;
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        if (difficulty <= 0.33) {
            this.velocidadeMaxima = 150;
        } else if (difficulty <= 0.66) {
            this.velocidadeMaxima = 100;
        } else if (difficulty <= 1.0) {
            this.velocidadeMaxima = 75;
        }
    }

    @Override
    public void onHandlePlayingInput() {
        System.out.println(counter);

        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        super.screen.viewport.unproject(click);

        if ((click.y > toothPosition.y) && (Gdx.input.isTouched())) {
            tooth.isFlying();
            verticalUpForceFactor = 0.035;
            counter++;
            if (counter == 12) {
                fire1.play();
            } else if (counter == 24) {
                fire2.play();
            } else if (counter == 36) {
                fire3.play();
                counter = 0;
            }
        } else {
            tooth.isFalling();
            verticalUpForceFactor = 0;
        }

        if ((click.x < toothPosition.x) && (Gdx.input.isTouched())) {
            leftForce = 2;
        } else {
            leftForce = 0;
        }

        if ((click.x > toothPosition.x) && (Gdx.input.isTouched())) {
            rightForce = 2;
        } else {
            rightForce = 0;
        }
    }

    @Override
    public void onUpdate(float dt) {
        double posBefore = toothPosition.y;

        if (velNow <= 5) {
            velNow = 10;
        }

        toothPosition.y = (float) (posBefore - (velNow * GRAVITY_FACTOR * dt) + velNow * verticalUpForceFactor * dt);

        velNow = (posBefore - toothPosition.y) / dt;

        toothPosition.x = toothPosition.x + rightForce - leftForce;

        tooth.setCenter(toothPosition.x, toothPosition.y);

        if (toothPosition.y <= 100) {
            if (velNow <= this.velocidadeMaxima) {
                sucessSound.play();
                super.challengeSolved();
            } else {
                tooth.wasHurt();
                failSound.play();
                super.challengeFailed();
            }

        }
    }

    @Override
    public void onDrawGame() {
        mouth.draw(super.screen.batch);
        target.draw(super.screen.batch);
        tooth.draw(super.screen.batch);

        target.setPosition(550, 50);
        tooth.setPosition(toothPosition.x, toothPosition.y);
    }

    @Override
    public String getInstructions() {
        return "Pouse o dente voador com cuidado";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Tooth extends Sprite {

        private final TextureRegion hurt;
        private final TextureRegion flying;
        private final TextureRegion falling;

        static final int FRAME_WIDTH = 84;
        static final int FRAME_HEIGHT = 151;

        public Tooth(TextureRegion textureFlying, TextureRegion textureFalling, TextureRegion textureHurt) {
            super(textureFalling);
            this.flying = textureFlying;
            this.hurt = textureHurt;
            this.falling = textureFalling;
        }

        void wasHurt() {
            super.setRegion(hurt);
        }

        void isFlying() {
            super.setRegion(flying);
        }

        void isFalling() {
            super.setRegion(falling);
        }
    }

    class Mouth extends Sprite {

        private final TextureRegion background;

        static final int FRAME_WIDTH = 1280;
        static final int FRAME_HEIGHT = 720;

        public Mouth(TextureRegion background) {
            super(background);
            this.background = background;
        }
    }

}
