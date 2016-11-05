/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.GameStateObserver;
<<<<<<< HEAD
import br.cefetmg.games.minigames.util.MiniGameState;
=======
>>>>>>> 6e3805fb93c7c456be2e340295e727c54e7c1655
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author lucas
 */
public class SideWalking extends MiniGame {

    private final Texture teeth;
    private final Texture monster;
<<<<<<< HEAD
    private final Texture cloud;
=======
    private final Texture cloud;    
>>>>>>> 6e3805fb93c7c456be2e340295e727c54e7c1655
    private int spawnInterval;
    private float velocidade;
    private float pulo;
    private final float gravidade = 0.4f;
<<<<<<< HEAD
    private final float velocidadeNuvem = 2f;
=======
    private final float velocidadeNuvem = 2f;    
>>>>>>> 6e3805fb93c7c456be2e340295e727c54e7c1655
    private final float alturachao = 100f;
    private final Array<Sprite> enemies;
    private final Array<Sprite> clouds;
    private final Sound teethJumpingSound;
    private final Sound backSound;
    private final Teeth dente;
    private final Texture background;
    private int incremento;
<<<<<<< HEAD

=======
    
>>>>>>> 6e3805fb93c7c456be2e340295e727c54e7c1655
    public SideWalking(BaseScreen screen, GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 15000, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS, observer);
        this.teeth = this.screen.assets.get("side_walking/walking_tooth.png", Texture.class);
        dente = new Teeth(teeth);
        this.monster = this.screen.assets.get("side_walking/monster.png", Texture.class);
        this.cloud = this.screen.assets.get("side_walking/nuvem.png", Texture.class);
<<<<<<< HEAD

=======
                
>>>>>>> 6e3805fb93c7c456be2e340295e727c54e7c1655
        this.teethJumpingSound = this.screen.assets.get(
                "side_walking/jump.mp3", Sound.class);
        this.backSound = this.screen.assets.get(
                "side_walking/backSound.mp3", Sound.class);
<<<<<<< HEAD
=======
        backSound.loop(0.5f);        
>>>>>>> 6e3805fb93c7c456be2e340295e727c54e7c1655
        this.enemies = new Array<Sprite>();
        this.clouds = new Array<Sprite>();
        background = new Texture(Gdx.files.internal("side_walking/candy.png"));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        super.timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
<<<<<<< HEAD
                backSound.loop(0.5f);
=======
>>>>>>> 6e3805fb93c7c456be2e340295e727c54e7c1655
                spawnEnemie();
                spawnCloud();
            }
        }, 0, this.spawnInterval / 1000f);
    }
<<<<<<< HEAD

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        spawnInterval = (int) DifficultyCurve.LINEAR_NEGATIVE.getCurveValueBetween(difficulty, 2500, 4000);
        velocidade = (float) DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 4f, 12f);
=======
    
    @Override
    protected void configureDifficultyParameters(float difficulty) {
        spawnInterval = (int) DifficultyCurve.LINEAR_NEGATIVE.getCurveValueBetween(difficulty, 2500, 4000);
        velocidade = (float) DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 4f, 12f);        
>>>>>>> 6e3805fb93c7c456be2e340295e727c54e7c1655
    }

    @Override
    public void onHandlePlayingInput() {
        if (Gdx.input.justTouched()) {
            if (dente.getY() == alturachao) {//se o dente estiver tocando o chão                 
                pulo = 18f;
                dente.setY(dente.getY() + pulo);
                teethJumpingSound.play(1f);
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
<<<<<<< HEAD
        if (state == MiniGameState.WON) {
            backSound.pause();
        }
        if (dente.getY() > alturachao) {
            pulo -= gravidade;
            dente.setY(dente.getY() + pulo);
        }
        if (dente.getY() < alturachao) {
            dente.setY(alturachao);
        }
        for (int i = 0; i < enemies.size; i++) {
=======
        if (dente.getY() > alturachao) {
            pulo -= gravidade;
            dente.setY(dente.getY() + pulo);           
        }
        if(dente.getY() < alturachao){
            dente.setY(alturachao);
        }
        for(int i = 0; i<enemies.size; i++){
>>>>>>> 6e3805fb93c7c456be2e340295e727c54e7c1655
            Sprite mst = enemies.get(i);
            mst.setX(mst.getX() - velocidade);
            if (dente.getBoundingRectangle().overlaps(mst.getBoundingRectangle())) {
                backSound.stop();
<<<<<<< HEAD
                super.challengeFailed();
                break;//Deveria chegar à linha acima e imterromper, porém por algum motivo
                //continua o loop. Por isso o break.
            }
            if (mst.getX() + mst.getWidth() < 0) {
                enemies.removeIndex(i);
            }
        }
        for (int i = 0; i < clouds.size; i++) {
            Sprite cld = clouds.get(i);
            cld.setX(cld.getX() - velocidadeNuvem);
            if (cld.getX() + cld.getWidth() < 0) {
                clouds.removeIndex(i);
            }
        }
        incremento += velocidade;
    }

    @Override
    public void onDrawGame() {
        this.screen.batch.draw(background, 0, 0, incremento, 0, (int) this.screen.viewport.getWorldWidth(), (int) this.screen.viewport.getWorldHeight());
=======
                super.challengeFailed();                
                break;//Deveria chegar à linha acima e imterromper, porém por algum motivo
                //continua o loop. Por isso o break.
            }           
            if (mst.getX() + mst.getWidth() < 0) {
                enemies.removeIndex(i);                
            }
        }      
        for(int i = 0; i<clouds.size; i++){
            Sprite cld = clouds.get(i);
            cld.setX(cld.getX() - velocidadeNuvem);
            if (cld.getX() + cld.getWidth() < 0) {
                clouds.removeIndex(i);                
            }
        }
        incremento += velocidade; 
    }

    @Override
    public void onDrawGame() {        
        this.screen.batch.draw(background, 0, 0, incremento , 0, (int)this.screen.viewport.getWorldWidth(), (int)this.screen.viewport.getWorldHeight());
>>>>>>> 6e3805fb93c7c456be2e340295e727c54e7c1655

        for (Sprite mst : this.enemies) {
            mst.draw(this.screen.batch);
        }
        for (Sprite cld : this.clouds) {
            cld.draw(this.screen.batch);
        }
        dente.draw(this.screen.batch);
    }

    @Override
    public String getInstructions() {
        return "Clique para saltar os obstáculos";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
<<<<<<< HEAD

    private void spawnEnemie() {
        Sprite enemy = new Sprite(monster);
        Random r = new Random();
        float scale = (r.nextInt(6) + 2) / 10.0f;
        enemy.setScale(scale);
        enemy.setPosition(1300, 15 + alturachao - ((enemy.getHeight() * (1 - scale)) / 2));
        enemies.add(enemy);
    }

    private void spawnCloud() {
        Sprite cld = new Sprite(cloud);
        cld.setPosition(1300, screen.viewport.getWorldHeight() - alturachao * 1.5f);
        Random r = new Random();
        cld.setScale((r.nextInt(8) + 5) / 10.0f);
        clouds.add(cld);
    }

    class Teeth extends MultiAnimatedSprite {

=======
    
    private void spawnEnemie(){
        Sprite enemy = new Sprite(monster);        
        Random r = new Random();
        float scale = (r.nextInt(6)+2)/10.0f;        
        enemy.setScale(scale);
        enemy.setPosition(1300, 15 + alturachao - ((enemy.getHeight()*(1-scale))/2));
        enemies.add(enemy);
    }
    
    private void spawnCloud(){
        Sprite cld = new Sprite(cloud);
        cld.setPosition(1300, screen.viewport.getWorldHeight() - alturachao*1.5f);
        Random r = new Random();
        cld.setScale((r.nextInt(8)+5)/10.0f);
        clouds.add(cld);
    }
    
    class Teeth extends MultiAnimatedSprite {
>>>>>>> 6e3805fb93c7c456be2e340295e727c54e7c1655
        public Teeth(final Texture thoothTexture) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(thoothTexture,
<<<<<<< HEAD
                                    thoothTexture.getWidth() / 2, thoothTexture.getHeight());
                    Animation walking = new Animation(0.2f,
                            frames[0][0],
                            frames[0][1]);
                    walking.setFrameDuration(0.08f);
                    walking.setPlayMode(Animation.PlayMode.LOOP);
=======
                                    thoothTexture.getWidth()/2, thoothTexture.getHeight());
                    Animation walking = new Animation(0.2f,
                            frames[0][0],
                            frames[0][1]);
                    walking.setFrameDuration(0.08f);                    
                    walking.setPlayMode(Animation.PlayMode.LOOP);                    
>>>>>>> 6e3805fb93c7c456be2e340295e727c54e7c1655
                    put("walking", walking);
                }
            }, "walking");
            super.setPosition(30f, alturachao);
<<<<<<< HEAD
        }
=======
        }              
>>>>>>> 6e3805fb93c7c456be2e340295e727c54e7c1655
    }
}
