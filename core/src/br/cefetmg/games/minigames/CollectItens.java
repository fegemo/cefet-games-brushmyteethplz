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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;

/**
 *
 * @author afp11
 */
public class CollectItens extends MiniGame{
    
    private final Array<Sprite> characters;    

    private final Sprite smile;
    private final Texture toothpasteTexture;
    private final Texture toothbrushTexture;
    private final Texture candyTexture;
    private final Texture lollipopTexture;
    private final Texture smileTexture;
    
    private int friendsCollected;
    private int spawnedCharacters;

    private float initialCharactersScale;
    private float minimumCharactersScale;
    private int totalCharacters;
    private int spawnInterval;
    
    private int totalToothpaste, quantAtualToothpaste;
    private int totalToothbrush, quantAtualToothbrush;
    
    private int enemies;
    private int friends;
    
    private int totalCandies, quantAtualCandies;
    private int totalLollipops, quantAtualLollipops;
    
    private int contador = 0;
    private final Sound enemiesAppearing;
    private final Sound friendsAppearing;
    private final Sound collectItem;
    private final Sound venceu;
    private final Sound perdeu;

    public CollectItens(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10000,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        this.characters = new Array<Sprite>();
        
        this.toothpasteTexture = this.screen.assets.get(
                "collect-itens/toothpaste.png", Texture.class);
        this.toothbrushTexture = this.screen.assets.get(
                "collect-itens/toothbrush.png", Texture.class);
        this.lollipopTexture = this.screen.assets.get(
                "collect-itens/lollipop.png", Texture.class);
        this.candyTexture = this.screen.assets.get(
                "collect-itens/candy.png", Texture.class);
        
        this.smileTexture = this.screen.assets.get(
                "collect-itens/smile.png", Texture.class);
        
        
        this.enemiesAppearing = screen.assets.get(
                "collect-itens/aperta.mp3", Sound.class);
        this.friendsAppearing = screen.assets.get(
                "collect-itens/aperta.mp3", Sound.class);
        
        this.collectItem = screen.assets.get(
                "collect-itens/aperta2.mp3", Sound.class);
                
        this.venceu = screen.assets.get(
                "collect-itens/aplausos.mp3", Sound.class);
        this.perdeu = screen.assets.get(
                "collect-itens/game-over.mp3", Sound.class);
        
        this.smile = new Sprite(smileTexture);
        this.smile.setOriginCenter();
        this.friendsCollected = 0;
        this.spawnedCharacters = 0;
        
        this.quantAtualToothbrush = 0;
        this.quantAtualToothpaste = 0;

        scheduleCharactersSpawn();
    }

    private void scheduleCharactersSpawn() {
        Task t = new Task() {
            @Override
            public void run() {
                
                contador++;
                
                switch (contador % 4) {
                    case 0:
                        spawnToothpaste();
                        break;
                    case 1:
                        spawnToothbrush();
                        break;
                    case 2:
                        spawnCandy();
                        break;
                    case 3:
                        spawnLollipop();
                        break;
                    default:
                        break;
                }
                
                if (++spawnedCharacters < totalCharacters) {
                    scheduleCharactersSpawn();
                }
            }
        };
        // spawnInterval * 15% para mais ou para menos
        float nextSpawnMillis = this.spawnInterval
                * (rand.nextFloat() / 3 + 0.15f);
        super.timer.scheduleTask(t, nextSpawnMillis / 1000f);
    }
    
    private void spawnLollipop() {
        // pega x e y entre 0 e 1
        Vector2 position = new Vector2(rand.nextFloat(), rand.nextFloat());
        
        if (quantAtualLollipops < totalLollipops){
            // multiplica x e y pela largura e altura da tela
            position.scl(this.screen.viewport.getScreenWidth() - 
                    lollipopTexture.getWidth()
                    * initialCharactersScale,
                    this.screen.viewport.getScreenHeight()
                    - lollipopTexture.getHeight() * 
                            initialCharactersScale);

            Sprite lollipop = new Sprite(lollipopTexture);
            lollipop.setPosition(position.x, position.y);
            lollipop.setScale(initialCharactersScale);
            this.characters.add(lollipop);
       
            this.enemiesAppearing.play();
        }
    }
    
    private void spawnCandy() {
        // pega x e y entre 0 e 1
        Vector2 position = new Vector2(rand.nextFloat(), rand.nextFloat());
        
        if (quantAtualCandies < totalCandies){
            // multiplica x e y pela largura e altura da tela
            position.scl(this.screen.viewport.getScreenWidth() - 
                    candyTexture.getWidth()
                    * initialCharactersScale,
                    this.screen.viewport.getScreenHeight()
                    - candyTexture.getHeight() * 
                            initialCharactersScale);

            Sprite candy = new Sprite(candyTexture);
            candy.setPosition(position.x, position.y);
            candy.setScale(initialCharactersScale);
            this.characters.add(candy);
            
            friends++;
       
            // toca um efeito sonoro
            this.enemiesAppearing.play();
        }
    }

    private void spawnToothpaste() {
        // pega x e y entre 0 e 1
        Vector2 position = new Vector2(rand.nextFloat(), rand.nextFloat());
        
        if (quantAtualToothpaste < totalToothpaste){
            // multiplica x e y pela largura e altura da tela
            position.scl(this.screen.viewport.getScreenWidth() - 
                    toothpasteTexture.getWidth()
                    * initialCharactersScale,
                    this.screen.viewport.getScreenHeight()
                    - toothpasteTexture.getHeight() * 
                            initialCharactersScale);

            Sprite toothpaste = new Sprite(toothpasteTexture);
            toothpaste.setPosition(position.x, position.y);
            toothpaste.setScale(initialCharactersScale);
            this.characters.add(toothpaste);
       
            // toca um efeito sonoro
            this.friendsAppearing.play();
            
            friends++;
        }
    }
    
    private void spawnToothbrush() {
        // pega x e y entre 0 e 1
        Vector2 position = new Vector2(rand.nextFloat(), rand.nextFloat());
        
        if (quantAtualToothbrush < totalToothbrush){
            // multiplica x e y pela largura e altura da tela
            position.scl(this.screen.viewport.getScreenWidth() - 
                    toothbrushTexture.getWidth()
                    * initialCharactersScale,
                    this.screen.viewport.getScreenHeight()
                    - toothbrushTexture.getHeight() * 
                            initialCharactersScale);

            Sprite toothbrush = new Sprite(toothbrushTexture);
            toothbrush.setPosition(position.x, position.y);
            toothbrush.setScale(initialCharactersScale);
            this.characters.add(toothbrush);
        }
        // toca um efeito sonoro
        this.friendsAppearing.play();
        
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.initialCharactersScale = DifficultyCurve.LINEAR
                .getCurveValueBetween(difficulty, 1.15f, 0.8f);
        this.minimumCharactersScale = DifficultyCurve.LINEAR_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 0.4f);
        this.spawnInterval = (int) DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, 500, 1500);
        this.totalCharacters = (int) Math.ceil((float) maxDuration
                / spawnInterval) - 3;
        this.enemies = (int) Math.ceil((float) maxDuration
                / spawnInterval) - 3;
               
        if ((totalCharacters % 2) == 0){
            this.totalToothbrush = totalCharacters / 2;
        } else {
            this.totalToothbrush = (totalCharacters + 1) / 2;
        }
        this.totalToothpaste = totalCharacters - this.totalToothbrush;
        if ((totalCandies % 2) == 0){
            this.totalCandies = enemies / 2;
        } else {
            this.totalCandies = (enemies + 1) / 2;
        }
        this.totalLollipops = enemies - this.totalCandies; 
        
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
            this.collectItem.play();
            // itera no array de inimigos
            for (int i = 0; i < characters.size; i++) {
                Sprite sprite = characters.get(i);
                // se há interseção entre o retângulo da sprite e do alvo,
                // o tiro acertou
                if (sprite.getBoundingRectangle().overlaps(smile.getBoundingRectangle())) {
                    if ((sprite.getTexture() == toothbrushTexture) || sprite.getTexture() == toothpasteTexture){
                        // contabiliza um inimigo morto
                        this.friendsCollected++;
                    } else {
                        this.perdeu.play();
                        this.challengeFailed();
                    }
                    // remove o inimigo do array
                    characters.removeValue(sprite, true);
                    // se tiver matado todos os inimigos, o desafio
                    // está resolvido
                    if (this.friendsCollected >= friends) {
                        this.venceu.play();
                        super.challengeSolved();
                    }

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
        for (int i = 0; i < characters.size; i++) {
            Sprite sprite = characters.get(i);
            // diminui só até x% do tamanho da imagem
            if (sprite.getScaleX() > minimumCharactersScale) {
                sprite.setScale(sprite.getScaleX() - 0.1f * dt);
            }
        }  

    }

    @Override
    public String getInstructions() {
        return "Colete os itens que cuidam do sorriso!";
    }

    @Override
    public void onDrawGame() {
       
        for (int i = 0; i < characters.size; i++) {
            Sprite sprite = characters.get(i);
            sprite.draw(this.screen.batch);
        }
        smile.draw(this.screen.batch);
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

}

