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
    private final Texture cloud;    
    private int spawnInterval;
    private float velocidade;
    private final float velocidadePulo = 4f;
    private final float velocidadeNuvem = 2f;
    private boolean pulando = false;
    private final float alturachao = 150f;
    private final Array<Sprite> enemies;
    private final Array<Sprite> clouds;
    private final Sound teethJumpingSound;
    private final Sound backSound;
    private final Teeth dente;
    
    public SideWalking(BaseScreen screen, GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 15000, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS, observer);
        this.teeth = this.screen.assets.get("side_walking/walking_tooth.png", Texture.class);
        dente = new Teeth(teeth);
        this.monster = this.screen.assets.get("side_walking/monster.png", Texture.class);
        this.cloud = this.screen.assets.get("side_walking/nuvem.png", Texture.class);
                
        this.teethJumpingSound = this.screen.assets.get(
                "side_walking/jump.mp3", Sound.class);
        this.backSound = this.screen.assets.get(
                "side_walking/backSound.mp3", Sound.class);
        backSound.loop(0.5f);        
        this.enemies = new Array<Sprite>();
        this.clouds = new Array<Sprite>();
        super.timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                spawnEnemie();
                spawnCloud();
            }
        }, 0, this.spawnInterval / 1000f);
    }
    
    @Override
    protected void configureDifficultyParameters(float difficulty) {
        spawnInterval = (int) DifficultyCurve.LINEAR_NEGATIVE.getCurveValueBetween(difficulty, 2500, 4000);
        velocidade = (float) DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 4f, 12f);        
    }

    @Override
    public void onHandlePlayingInput() {
        if (Gdx.input.justTouched()) {
            if (dente.getY() == alturachao) {//se o dente estiver tocando o chão 
                pulando = true;
                dente.setY(dente.getY() + velocidadePulo);
                teethJumpingSound.play(1f);
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
        if (dente.getY() > alturachao) {
            if (pulando) {
                dente.setY(dente.getY() + velocidadePulo);
                if (dente.getY() >= alturachao + 2 * dente.getHeight()) {
                    pulando = false;
                }
            } else {
                dente.setY(dente.getY() - velocidadePulo);
            }
        }
        for(int i = 0; i<enemies.size; i++){
            Sprite mst = enemies.get(i);
            mst.setX(mst.getX() - velocidade);
            if (dente.getBoundingRectangle().overlaps(mst.getBoundingRectangle())) {
                backSound.stop();
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
    }

    @Override
    public void onDrawGame() {        
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
    
    private void spawnEnemie(){
        Sprite enemy = new Sprite(monster);
        enemy.setPosition(1300, alturachao);
        Random r = new Random();
        enemy.setScale((r.nextInt(6)+2)/10.0f);
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
        public Teeth(final Texture thoothTexture) {
            super(new HashMap<String, Animation>() {
                {
                    TextureRegion[][] frames = TextureRegion
                            .split(thoothTexture,
                                    thoothTexture.getWidth()/2, thoothTexture.getHeight());
                    Animation walking = new Animation(0.2f,
                            frames[0][0],
                            frames[0][1]);
                    walking.setFrameDuration(0.08f);                    
                    walking.setPlayMode(Animation.PlayMode.LOOP);                    
                    put("walking", walking);
                }
            }, "walking");
            super.setPosition(30f, alturachao);
        }              
    }
}
