package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 *
 * @author nicolas
 */
public class PutTheBraces extends MiniGame {

    private final Texture toothTexture;
    private final Texture breteTexture;
    private final Array<Tooth> teeth;
    private final Sound breteSound;
    private final Sprite brete;

    private int totalTeeth;
    private int numberOfBracedTeeth;

    public PutTheBraces(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        this.toothTexture = super.screen.assets.get("put-the-braces/tooth.png", Texture.class);
        this.breteTexture = super.screen.assets.get("put-the-braces/brete.png", Texture.class);
        this.breteSound = super.screen.assets.get("put-the-braces/metal.mp3", Sound.class);
        teeth = new Array<Tooth>();
        numberOfBracedTeeth = 0;
        brete = new Sprite(breteTexture);
        brete.setOriginCenter();
        this.initializeTeeth();
    }

    private void initializeTeeth() {
        TextureRegion[][] frames = TextureRegion.split(toothTexture,
                Tooth.FRAME_WIDTH, Tooth.FRAME_HEIGHT);
        for (int i = 0; i < this.totalTeeth; i++) {
            float angle = (360f / this.totalTeeth) * i;
            final float radius = 250f;
            Tooth tooth = new Tooth(
                    frames[0][0],
                    frames[0][1]);
            tooth.setCenter(
                    super.screen.viewport.getWorldWidth() / 2
                    + MathUtils.cosDeg(angle) * radius,
                    super.screen.viewport.getWorldHeight() / 2
                    + MathUtils.sinDeg(angle) * radius);
            this.teeth.add(tooth);
        }
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {

        totalTeeth = (int) (10 + difficulty * 10);
    }

    @Override
    public void onHandlePlayingInput() {
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        super.screen.viewport.unproject(click);
        brete.setPosition(click.x - this.brete.getWidth() / 2,
                click.y - this.brete.getHeight() / 2);
        if (Gdx.input.justTouched()) {
            // itera no array de dentes
            for (int i = 0; i < teeth.size; i++) {
                Tooth tooth = teeth.get(i);
                if (tooth.getBoundingRectangle().contains(click)) {
                    toothWasBraced(tooth);
                    break;
                }
            }
        }
    }

    private void toothWasBraced(Tooth tooth) {
        if (tooth.putBraces()) {
            numberOfBracedTeeth++;
            long id = breteSound.play(0.5f);
            breteSound.setPan(id, 0, 1);
        }
        if (this.numberOfBracedTeeth >= this.totalTeeth) {
            super.challengeSolved();
        }
    }

    @Override
    public void onUpdate(float dt) {

    }

    @Override
    public void onDrawGame() {
        for (Tooth tooth : this.teeth) {
            tooth.draw(super.screen.batch);
        }
        brete.draw(super.screen.batch);
    }

    @Override
    public String getInstructions() {
        return "Coloque aparelho nos dentes";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Tooth extends Sprite {

        private final TextureRegion withBraces;
        private boolean ready;

        static final int FRAME_WIDTH = 64;
        static final int FRAME_HEIGHT = 64;

        public Tooth(TextureRegion textureInitial, TextureRegion textureBraces) {
            super(textureInitial);
            this.withBraces = textureBraces;
            ready = false;
        }

        public boolean putBraces() {
            if (!ready) {
                super.setRegion(withBraces);
            }
            if (!ready) {
                ready = true;
            } else {
                return false;
            }
            return ready;
        }

    }

}
