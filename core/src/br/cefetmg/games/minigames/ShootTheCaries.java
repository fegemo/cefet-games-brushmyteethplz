package br.cefetmg.games.minigames;

import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class ShootTheCaries extends MiniGame {

    private static final int SPAWN_INTERVAL = 1000;
    
    private final Array<Sprite> enemies;
    private final Sprite target;
    private final Texture cariesTexture;
    private final Texture targetTexture;
    private final int numberOfEnemies;
    private int enemiesKilled;
    
    public ShootTheCaries(BaseScreen screen, int difficulty, long maxDuration) {
        super(screen, difficulty, maxDuration);
        this.enemies = new Array<Sprite>();
        this.cariesTexture = this.screen.assets.get(
                "shoot-the-caries/caries.png", Texture.class);
        this.targetTexture = this.screen.assets.get(
                "shoot-the-caries/target.png", Texture.class);
        this.target = new Sprite(targetTexture);
        this.target.setOriginCenter();
        this.numberOfEnemies = (int)(maxDuration / SPAWN_INTERVAL) - 4;
        this.enemiesKilled = 0;


        Task t = new Task() {
            @Override
            public void run() {
                System.out.println("spawned Enemy");
                spawnEnemy();
                System.out.println("enemies.size = " + enemies.size);
            }
        };
        super.timer.scheduleTask(t, 0, SPAWN_INTERVAL/1000f, this.numberOfEnemies);
        
        Gdx.gl.glClearColor(1,1,1,1);
    }

    @Override
    public void onHandleInput() {
        // atualiza a posição do alvo de acordo com o mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        this.screen.camera.unproject(click);
        this.target.setPosition(click.x-this.target.getWidth()/2,
                click.y-this.target.getHeight()/2);
        
        // verifica se matou um inimigo
        if (Gdx.input.justTouched()) {
            for (int i = 0; i < enemies.size; i++) {
                Sprite sprite = enemies.get(i);
                if (sprite.getBoundingRectangle().overlaps(
                        target.getBoundingRectangle())) {
                    this.enemiesKilled++;
                    this.enemies.removeValue(sprite, true);
                    if (this.enemiesKilled >= this.numberOfEnemies) {
                        super.challengeSolved = true;
                    }
                }
            }
        }
    }
    
    private void spawnEnemy() {
        final float initialScale = 1.15f;
        Vector2 position = new Vector2(rand.nextFloat(), rand.nextFloat());
        position.scl(
                this.screen.bounds.width-cariesTexture.getWidth()*initialScale,
                this.screen.bounds.height-cariesTexture.getHeight()*initialScale);
        
        Sprite enemy = new Sprite(cariesTexture);
        enemy.setPosition(position.x, position.y);
        enemy.setScale(initialScale);
        enemies.add(enemy);                
    }

    @Override
    public void onUpdate(float dt) {       
        
        // vai diminuindo o tamanho das cáries existentes
        for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            sprite.setScale(sprite.getScaleX() - 0.3f * dt);
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

    @Override
    public void onChallengeSolved() {
        
    }

    @Override
    public void onChallengeFailed() {
        
    }

    
    
}


