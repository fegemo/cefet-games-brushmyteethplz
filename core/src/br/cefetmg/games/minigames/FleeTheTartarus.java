/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
 * @author afp11
 */
public class FleeTheTartarus extends MiniGame {
    
    private final Array<Tartarus> enemies;
    private final Tooth tooth;
    
    private final Texture toothTexture;
    private final Texture tartarusTexture;

    private int spawnInterval;
    private float minimumEnemySpeed;
    private float maximumEnemySpeed;
    private final Array<Sound> tartarusAppearingSound;
    
    private final Sound venceu, perdeu;

    
    public FleeTheTartarus(BaseScreen screen,
            GameStateObserver observer, float difficulty){
        
        super(screen, difficulty, 10000,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        
        this.enemies = new Array<Tartarus>();
        this.tartarusTexture = this.screen.assets.get(
                "flee-the-tartarus/tartarus-spritesheet.png", Texture.class);
        this.toothTexture = this.screen.assets.get(
                "flee-the-tartarus/dente.png", Texture.class);
        this.tooth = new Tooth(toothTexture);
        this.tooth.setOriginCenter();
        
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
        
        super.timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                spawnEnemy();
            }

        }, 0, this.spawnInterval / 1000f);
        
        this.spawnEnemy();
        
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.minimumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 30, 60);
        this.maximumEnemySpeed = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 70, 120);
        this.spawnInterval = (int) DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 500, 1500);
    }

    @Override
    public void onHandlePlayingInput() {
        // atualiza a posição do alvo de acordo com o mouse
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        super.screen.viewport.unproject(click);
        this.tooth.setCenter(click.x, click.y);

        for (Tartarus t : this.enemies) {
            float distance = tooth.getToothDistanceTo(t.getX(), t.getY());
            if (distance <= 100) {
                perdeu.play();
                super.challengeFailed();
            }
        }
        
        if ((this.initialTime + this.maxDuration + 3000) <= System.currentTimeMillis()){
            venceu.play();
            super.challengeSolved();
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
        
        tooth.draw(this.screen.batch);
        
        for (Tartarus tart : this.enemies) {
            tart.draw(super.screen.batch);
        }
        
    }

    @Override
    public String getInstructions() {
        return "Fuja das cáries até acabar o tempo!";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
    class Tooth extends Sprite {
        
        static final int FRAME_WIDTH = 60;
        static final int FRAME_HEIGHT = 140;

        public Tooth (final Texture tooth) {
            super(tooth);
        }

        Vector2 getToothPosition() {
            return new Vector2(
                this.getX() + this.getWidth() * 0.5f,
                this.getY() + this.getHeight() * 0.8f);
        }

        float getToothDistanceTo(float enemyX, float enemyY) {
            return getToothPosition().dst(enemyX, enemyY);
        }
    }
    
    class Tartarus extends MultiAnimatedSprite {

        private Vector2 speed;

        static final int FRAME_WIDTH = 84;
        static final int FRAME_HEIGHT = 108;

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

    }
    
    private void spawnEnemy() {

        Vector2 tartarusPosition = new Vector2();
        
        boolean appearFromSides = MathUtils.randomBoolean();
        if (appearFromSides) {
            boolean appearFromLeft = MathUtils.randomBoolean();
            if (appearFromLeft){
                tartarusPosition.x = 0;
                tartarusPosition.y = MathUtils.random(
                        -Tartarus.FRAME_HEIGHT,
                        super.screen.viewport.getScreenHeight());
            } else {
                tartarusPosition.x = Tartarus.FRAME_WIDTH;
                tartarusPosition.y = MathUtils.random(
                        -Tartarus.FRAME_HEIGHT,
                        super.screen.viewport.getScreenHeight());
            }
        } else {
            boolean appearFromBottom = MathUtils.randomBoolean();
            if (appearFromBottom){
                tartarusPosition.y = 0;
                tartarusPosition.x = MathUtils.random(
                        -Tartarus.FRAME_WIDTH,
                        super.screen.viewport.getScreenWidth());
            } else {
                tartarusPosition.y = Tartarus.FRAME_HEIGHT;
                tartarusPosition.x = MathUtils.random(
                        -Tartarus.FRAME_WIDTH,
                        super.screen.viewport.getScreenWidth());
            }
        }
       
        
        Vector2 obj = new Vector2(tooth.getToothPosition());
        
        Vector2 tartarusSpeed = obj.sub(tartarusPosition).nor()
                .scl(5*this.maximumEnemySpeed);

        Tartarus enemy = new Tartarus(tartarusTexture);
        enemy.setPosition(tartarusPosition.x, tartarusPosition.y);
        enemy.setSpeed(tartarusSpeed);
        enemies.add(enemy);
        
        tartarusAppearingSound.random().play(); //toca sempre a mesma musica

    }
}
