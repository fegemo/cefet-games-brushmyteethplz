package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class ShootTheCaries extends MiniGame {

    private final Array<Sprite> enemies;
    private final Sprite target;
    private final Texture cariesTexture;
    private final Texture targetTexture;
    private int enemiesKilled;
    private int spawnedEnemies;

    private float initialEnemyScale;
    private float minimumEnemyScale;
    private int totalEnemies;
    private int spawnInterval;

    public ShootTheCaries(BaseScreen screen, float difficulty,
            long maxDuration) {
        super(screen, difficulty, maxDuration);
        this.enemies = new Array<Sprite>();
        this.cariesTexture = this.screen.assets.get(
                "shoot-the-caries/caries.png", Texture.class);
        this.targetTexture = this.screen.assets.get(
                "shoot-the-caries/target.png", Texture.class);
        this.target = new Sprite(targetTexture);
        this.target.setOriginCenter();
        this.enemiesKilled = 0;
        this.spawnedEnemies = 0;

        scheduleEnemySpawn();
    }

    private void scheduleEnemySpawn() {
        Task t = new Task() {
            @Override
            public void run() {
                spawnEnemy();
                if (++spawnedEnemies < totalEnemies) {
                    scheduleEnemySpawn();
                }
            }
        };
        // spawnInterval * 15% para mais ou para menos
        float nextSpawnMillis = this.spawnInterval * 
                (rand.nextFloat() / 3 + 0.15f);
        super.timer.scheduleTask(t, nextSpawnMillis/1000f);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.initialEnemyScale = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 1.15f, 0.8f);
        this.minimumEnemyScale = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.15f, 0.4f);
        this.spawnInterval = (int) DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, 500, 1500);
        this.totalEnemies = (int) Math.ceil((float)maxDuration / spawnInterval) - 3;

        System.out.println("initialEnemyScale = " + initialEnemyScale);
        System.out.println("minimumEnemyScale = " + minimumEnemyScale);
        System.out.println("spawnInterval = " + spawnInterval);
        System.out.println("totalEnemies = " + totalEnemies);
    }

    @Override
    public void onHandlePlayingInput() {
        // atualiza a posição do alvo de acordo com o mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        this.screen.camera.unproject(click);
        this.target.setPosition(click.x - this.target.getWidth() / 2,
                click.y - this.target.getHeight() / 2);

        // verifica se matou um inimigo
        if (Gdx.input.justTouched()) {
            // itera no array de inimigos
            for (int i = 0; i < enemies.size; i++) {
                Sprite sprite = enemies.get(i);
                // se há interseção entre o retângulo da sprite e do alvo,
                // o tiro acertou
                if (sprite.getBoundingRectangle().overlaps(
                        target.getBoundingRectangle())) {
                    // contabiliza um inimigo morto
                    this.enemiesKilled++;
                    // remove o inimigo do array
                    this.enemies.removeValue(sprite, true);
                    // se tiver matado todos os inimigos, o desafio
                    // está resolvido
                    if (this.enemiesKilled >= this.totalEnemies) {
                        super.challengeSolved = true;
                    }

                    // pára de iterar, porque senão o tiro pode pegar em mais
                    // de um inimigo
                    break;
                }
            }
        }
    }

    private void spawnEnemy() {
        Vector2 position = new Vector2(rand.nextFloat(), rand.nextFloat());
        position.scl(
                this.screen.bounds.width - cariesTexture.getWidth() * initialEnemyScale,
                this.screen.bounds.height - cariesTexture.getHeight() * initialEnemyScale);

        Sprite enemy = new Sprite(cariesTexture);
        enemy.setPosition(position.x, position.y);
        enemy.setScale(initialEnemyScale);
        enemies.add(enemy);
    }

    @Override
    public void onUpdate(float dt) {

        // vai diminuindo o tamanho das cáries existentes
        for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            // diminui só até x% do tamanho da imagem
            if (sprite.getScaleX() > minimumEnemyScale) {
                sprite.setScale(sprite.getScaleX() - 0.3f * dt);
            }
        }
    }

    @Override
    public void onDrawInstructions() {
        float y = this.screen.bounds.height * 0.75f;
        super.drawCenterAlignedText("Acerte as cáries", 1, y);
    }

    @Override
    public void onDrawGame() {

        for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            sprite.draw(this.screen.batch);
        }
        target.draw(this.screen.batch);
    }

}
