/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import java.util.Iterator;
import java.util.Random;

/**
 *
 * @author afp11
 */
public class NossoJogo2 extends MiniGame {

    //inimigos
    private final Array<Sprite> candies;
    //private final Array<Sprite> chicletes;
    
    //amigos
    private final Array<Sprite> toothbrushAndToothpaste;
    //private final Array<Sprite> toothbrush;
    //private final Array<Sprite> toothpaste;
    
    private final Sprite smile;
    
    private final Texture candiesTexture;
    private final Texture lollipopsTexture;
    
    //private final Texture toothbrushAndToothpasteTexture;
    private final Texture toothbrushTexture;
    private final Texture toothpasteTexture;
    
    private final Texture smileTexture;
    
    private int spawnedCharacters;

    private float initialCharacterScale;
    private float minimumCharacterScale;
    
    private int friendsCollected;
    private int totalEnemies;
    private int totalFriends = 1;
    
    private int spawnInterval;

    public NossoJogo2(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10000,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        
        //INIMIGOS
        this.candies = new Array<Sprite>();
        this.candiesTexture = this.screen.assets.get(
                "nosso-jogo-2/candy.png", Texture.class);
        
        //this.chicletes = new Array<Sprite>();
        this.lollipopsTexture = this.screen.assets.get(
                "nosso-jogo-2/lollipop.png", Texture.class);
        
        //AMIGOS
        this.toothbrushAndToothpaste = new Array<Sprite>();
        //this.toothbrush = new Array<Sprite>();
        this.toothbrushTexture = this.screen.assets.get(
                "nosso-jogo-2/toothbrush.png", Texture.class);
        
        //this.toothpaste = new Array<Sprite>();
        this.toothpasteTexture = this.screen.assets.get(
                "nosso-jogo-2/toothpaste.png", Texture.class);
        
        //SMILLE
        this.smileTexture = this.screen.assets.get(
                "nosso-jogo-2/smile.png", Texture.class);
        this.smile = new Sprite(smileTexture);
        this.smile.setOriginCenter();
              
        this.totalFriends = 0;
        this.totalEnemies = 0;
        this.spawnedCharacters = 0;

        scheduleCharactersSpawn();
    }

    private void scheduleCharactersSpawn() {
        
        Timer.Task t = new Timer.Task() {
            @Override
            public void run() {
                spawnCharacters();
                if (spawnedCharacters < (totalEnemies + totalFriends)) {
                    scheduleCharactersSpawn();
                }
            }
        };
        // spawnInterval * 15% para mais ou para menos
        float nextSpawnMillis = this.spawnInterval
                * (rand.nextFloat() / 3 + 0.15f);
        super.timer.scheduleTask(t, nextSpawnMillis / 1000f);
    }

    private void spawnCharacters() {
        Vector2 position = new Vector2(rand.nextFloat(), rand.nextFloat());
        
        //sorteia um tipo de personagem
        int maximo = 3, minimo = 0;
        Random random = new Random();
        int c = random.nextInt((maximo - minimo) + 1) + minimo;
        
        //desenha o personagem
        if (c == 0){
            position.scl(this.screen.viewport.getScreenWidth() - 
                    candiesTexture.getWidth()* initialCharacterScale,
                    this.screen.viewport.getScreenHeight()
                    - candiesTexture.getHeight() * initialCharacterScale);
            Sprite candy = new Sprite(candiesTexture);
            candy.setPosition(position.x, position.y);
            candy.setScale(initialCharacterScale);
            candies.add(candy);
            spawnedCharacters++;
        } else if (c == 1){
            position.scl(this.screen.viewport.getScreenWidth() - 
                    lollipopsTexture.getWidth()* initialCharacterScale,
                    this.screen.viewport.getScreenHeight()
                    - lollipopsTexture.getHeight() * initialCharacterScale);
            Sprite lollipop = new Sprite(lollipopsTexture);
            lollipop.setPosition(position.x, position.y);
            lollipop.setScale(initialCharacterScale);
            candies.add(lollipop);
            spawnedCharacters++;
        } else if (c == 2) {
            position.scl(this.screen.viewport.getScreenWidth() - 
                    toothbrushTexture.getWidth()* initialCharacterScale,
                    this.screen.viewport.getScreenHeight()
                    - toothbrushTexture.getHeight() * initialCharacterScale);
            Sprite tbrush = new Sprite(toothbrushTexture);
            tbrush.setPosition(position.x, position.y);
            tbrush.setScale(initialCharacterScale);
            toothbrushAndToothpaste.add(tbrush);            
            spawnedCharacters++;

        } else {
            position.scl(this.screen.viewport.getScreenWidth() - 
                    toothpasteTexture.getWidth()* initialCharacterScale,
                    this.screen.viewport.getScreenHeight()
                    - toothpasteTexture.getHeight() * initialCharacterScale);
            Sprite tpaste = new Sprite(toothpasteTexture);
            tpaste.setPosition(position.x, position.y);
            tpaste.setScale(initialCharacterScale);
            toothbrushAndToothpaste.add(tpaste);
            spawnedCharacters++;
        }
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.initialCharacterScale = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 1.15f, 0.8f);
        this.minimumCharacterScale = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.15f, 0.4f);
        this.spawnInterval = (int) DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, 500, 1500);
        this.totalEnemies = (int) Math.ceil((float) maxDuration
                / spawnInterval) - 3;
        this.totalEnemies = (int) Math.ceil((float) maxDuration
                / spawnInterval) - 3;
        
        /*int maximo = 15, minimo = 3;
        Random random = new Random();
        this.totalFriends = random.nextInt((maximo - minimo) + 1) + minimo;*/
               
    }

    @Override
    public void onHandlePlayingInput() {
        // atualiza a posição do alvo de acordo com o mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        super.screen.viewport.unproject(click);
        this.smile.setPosition(click.x - this.smile.getWidth() / 2,
                click.y - this.smile.getHeight() / 2);

        // verifica se matou um inimigo
        if (Gdx.input.justTouched()) {
            // itera no array de inimigos
            for (int i = 0; i < candies.size; i++) {
                Sprite sprite = candies.get(i);
                // se há interseção entre o retângulo da sprite e do alvo,
                // o tiro acertou
                if (sprite.getBoundingRectangle().overlaps(
                        smile.getBoundingRectangle())) {
                    // contabiliza um inimigo morto
                    super.challengeFailed();
                    // pára de iterar, porque senão o tiro pode pegar em mais
                    // de um inimigo
                    break;
                }
            }
            // itera no array de inimigos
            for (int i = 0; i < toothbrushAndToothpaste.size; i++) {
                Sprite sprite = toothbrushAndToothpaste.get(i);
                // se há interseção entre o retângulo da sprite e do alvo,
                // o tiro acertou
                if (sprite.getBoundingRectangle().overlaps(
                        smile.getBoundingRectangle())) {
                    // contabiliza um inimigo morto
                    this.friendsCollected++;
                    // remove o inimigo do array
                    this.toothbrushAndToothpaste.removeValue(sprite, true);
                    //cariesDyingSound.play();
       
                    // pára de iterar, porque senão o tiro pode pegar em mais
                    // de um inimigo
                    break;
                }
            }
        }
    }

    @Override
    public void onUpdate(float dt) {

        // vai diminuindo o tamanho das cáries existentes
        for (int i = 0; i < candies.size; i++) {
            Sprite sprite = candies.get(i);
            // diminui só até x% do tamanho da imagem
            if (sprite.getScaleX() > minimumCharacterScale) {
                sprite.setScale(sprite.getScaleX() - 0.3f * dt);
            }
        }
        // vai diminuindo o tamanho das cáries existentes
        for (int i = 0; i < toothbrushAndToothpaste.size; i++) {
            Sprite sprite = toothbrushAndToothpaste.get(i);
            // diminui só até x% do tamanho da imagem
            if (sprite.getScaleX() > minimumCharacterScale) {
                sprite.setScale(sprite.getScaleX() - 0.3f * dt);
            }
        }
        
        if (friendsCollected > totalFriends){
            super.challengeSolved();
        }
        
    }

    @Override
    public String getInstructions() {
        return "Colete os itens que cuidam do sorriso!";
    }

    @Override
    public void onDrawGame() {
         
        /*for (int i = 0; i < candies.size; i++) {
            Sprite sprite = candies.get(i);
            sprite.draw(this.screen.batch);
        }
        
        for (int i = 0; i < toothbrushAndToothpaste.size; i++) {
            Sprite sprite = toothbrushAndToothpaste.get(i);
            sprite.draw(this.screen.batch);
        }*/
        
        smile.draw(this.screen.batch);
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
}
