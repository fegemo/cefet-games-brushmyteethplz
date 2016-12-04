package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;

/**
 *
 * @author lucas Viana
 */
public final class SmashIt extends MiniGame {

    private final Texture hole_texture;
    private final Texture carie_texture;
    private final Sprite tooth_texture;
    private final Sprite drill_sprite;
    private final Sound drill_sound;
    private final Carie[] caries;
    private int caries_max;
    private int score = 0;
    private float spawnInterval;
    private final int n_lines = 4;
    private final int caries_hole = n_lines * n_lines;
    private final float dy = screen.viewport.getWorldHeight() / 6;
    private final float dx = screen.viewport.getWorldWidth() / 6;

    public SmashIt(BaseScreen screen, GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        hole_texture = this.screen.assets.get("smash-it/hole.png", Texture.class);
        carie_texture = this.screen.assets.get("smash-it/carie.png", Texture.class);
        tooth_texture = new Sprite(this.screen.assets.get("smash-it/tooth.png", Texture.class));
        tooth_texture.setBounds(0, 0, 1280, 720);
        drill_sprite = new Sprite(this.screen.assets.get("smash-it/tool.png", Texture.class));
        drill_sound = screen.assets.get("smash-it/drill.mp3", Sound.class);
        caries = new Carie[caries_hole];
        configureDifficultyParameters(difficulty);
        initCaries();
    }

    @Override
    protected void onStart() {
        super.timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                spawnCarie();
            }
        }, 0, this.spawnInterval);
    }

    private void initCaries() {
        for (int i = 0, k = 0; i < n_lines; ++i) {
            for (int j = 0; j < n_lines; ++j, ++k) {
                caries[k] = new Carie(hole_texture, carie_texture);
                caries[k].getSprite().setCenter((i + 1.5f) * dx, (j + 2f) * dy);
                caries[k].getSprite().setScale(1 / 2f);
            }
        }
    }

    private void spawnCarie() {
        int index = rand.nextInt(caries_hole);
        if (!caries[index].isAlive()) {
            caries[index].born();
        }
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        spawnInterval = DifficultyCurve.LINEAR_NEGATIVE.getCurveValueBetween(difficulty, 0.3f, 0.8f);
        caries_max = (int) DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 5, 20);
    }

    @Override
    public void onHandlePlayingInput() {
        Vector2 leftClick = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        super.screen.viewport.unproject(leftClick);
        drill_sprite.setCenter(leftClick.x + drill_sprite.getHeight() / 2f, leftClick.y);
        if (Gdx.input.justTouched()) {
            drill_sound.play();
            for (int i = 0; i < caries.length; ++i) {
                if (caries[i].getSprite().getBoundingRectangle().contains(leftClick) && caries[i].isAlive()) {
                    caries[i].gotHit();
                    if (++score == caries_max) {
                        super.challengeSolved();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
    }

    @Override
    public void onDrawGame() {
        tooth_texture.draw(screen.batch);
        for (int i = 0; i < caries_hole; ++i) {
            caries[i].getSprite().draw(screen.batch);
        }
        drill_sprite.draw(screen.batch);
    }

    @Override
    public String getInstructions() {
        return "Obture " + caries_max + " cáries!";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    public class Carie {

        private final Texture hole_texture;
        private final Texture alive_texture;
        private final Sprite actual_texture;
        private boolean is_alive;

        public Carie(Texture ht, Texture at) {
            actual_texture = new Sprite(ht);
            hole_texture = ht;
            alive_texture = at;
            is_alive = false;
        }

        public void gotHit() {
            is_alive = false;
            actual_texture.setTexture(hole_texture);
        }

        public void born() {
            is_alive = true;
            actual_texture.setTexture(alive_texture);
        }

        public boolean isAlive() {
            return is_alive;
        }

        public Sprite getSprite() {
            return actual_texture;
        }
    }
}
