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
public class CollectItens extends MiniGame {

    private final Array<Sprite> characters;

    private final Sprite fundo;
    private final Texture texturaFundo;

    private final Sprite boca;
    private final Texture toothpasteTexture;
    private final Texture toothbrushTexture;
    private final Texture candyTexture;
    private final Texture lollipopTexture;
    private final Texture goodMouthTexture;
    private final Texture badMouthTexture;

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
    private final Sound venceu;
    private final Sound perdeu;

    private final Sound collectGoodItem;
    private final Sound collectBadItem;

    public CollectItens(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10f,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        this.characters = new Array<Sprite>();

        this.texturaFundo = this.screen.assets.get(
                "collect-itens/fundo.png", Texture.class);

        this.toothpasteTexture = this.screen.assets.get(
                "collect-itens/toothpaste.png", Texture.class);
        this.toothbrushTexture = this.screen.assets.get(
                "collect-itens/toothbrush.png", Texture.class);
        this.lollipopTexture = this.screen.assets.get(
                "collect-itens/lollipop.png", Texture.class);
        this.candyTexture = this.screen.assets.get(
                "collect-itens/candy.png", Texture.class);

        this.goodMouthTexture = this.screen.assets.get(
                "collect-itens/bocaboa.png", Texture.class);
        this.badMouthTexture = this.screen.assets.get(
                "collect-itens/bocaruim.png", Texture.class);

        this.enemiesAppearing = screen.assets.get(
                "collect-itens/aperta.mp3", Sound.class);
        this.friendsAppearing = screen.assets.get(
                "collect-itens/aperta.mp3", Sound.class);

        this.collectGoodItem = screen.assets.get(
                "collect-itens/pegaitembom.mp3", Sound.class);

        this.collectBadItem = screen.assets.get(
                "collect-itens/pegaitemruim.mp3", Sound.class);

        this.venceu = screen.assets.get(
                "collect-itens/aplausos.mp3", Sound.class);
        this.perdeu = screen.assets.get(
                "collect-itens/game-over.mp3", Sound.class);

        this.boca = new Sprite(goodMouthTexture);

        this.fundo = new Sprite(texturaFundo);

        this.boca.setOriginCenter();
        this.friendsCollected = 0;
        this.spawnedCharacters = 0;
        this.friends = 0;

        this.quantAtualToothbrush = 0;
        this.quantAtualToothpaste = 0;

    }

    @Override
    protected void onStart() {
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
        super.timer.scheduleTask(t, nextSpawnMillis);
    }

    private void spawnLollipop() {
        // pega x e y entre 0 e 1
        Vector2 position = new Vector2(rand.nextFloat(), rand.nextFloat());

        if (quantAtualLollipops < totalLollipops) {
            // multiplica x e y pela largura e altura da tela
            position.scl(this.screen.viewport.getScreenWidth()
                    - lollipopTexture.getWidth()
                    * initialCharactersScale,
                    this.screen.viewport.getScreenHeight()
                    - lollipopTexture.getHeight()
                    * initialCharactersScale);

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

        if (quantAtualCandies < totalCandies) {
            // multiplica x e y pela largura e altura da tela
            position.scl(this.screen.viewport.getScreenWidth()
                    - candyTexture.getWidth()
                    * initialCharactersScale,
                    this.screen.viewport.getScreenHeight()
                    - candyTexture.getHeight()
                    * initialCharactersScale);

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

        if (quantAtualToothpaste < totalToothpaste) {
            // multiplica x e y pela largura e altura da tela
            position.scl(this.screen.viewport.getScreenWidth()
                    - toothpasteTexture.getWidth()
                    * initialCharactersScale,
                    this.screen.viewport.getScreenHeight()
                    - toothpasteTexture.getHeight()
                    * initialCharactersScale);

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

        if (quantAtualToothbrush < totalToothbrush) {
            // multiplica x e y pela largura e altura da tela
            position.scl(this.screen.viewport.getScreenWidth()
                    - toothbrushTexture.getWidth()
                    * initialCharactersScale,
                    this.screen.viewport.getScreenHeight()
                    - toothbrushTexture.getHeight()
                    * initialCharactersScale);

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
                .getCurveValueBetween(difficulty, 0.5f, 1.5f);
        this.totalCharacters = (int) Math.ceil(maxDuration / spawnInterval) - 3;
        this.enemies = (int) Math.ceil(maxDuration / spawnInterval) - 3;

        if ((totalCharacters % 2) != 0) {
            totalCharacters++;
        }

        this.totalToothbrush = totalCharacters / 2;
        this.totalToothpaste = totalCharacters - this.totalToothbrush;

        if ((totalCandies % 2) == 0) {
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
        this.boca.setPosition(click.x - this.boca.getWidth() / 2,
                click.y - this.boca.getHeight() / 2);

        if (Gdx.input.justTouched()) {
            for (int i = 0; i < characters.size; i++) {
                Sprite sprite = characters.get(i);
                if (sprite.getBoundingRectangle().overlaps(boca.getBoundingRectangle())) {

                    if ((sprite.getTexture() == toothbrushTexture)
                            || sprite.getTexture() == toothpasteTexture) {

                        this.collectGoodItem.play();

                        this.friendsCollected++;

                    } else {

                        this.collectBadItem.play();

                        this.boca.setTexture(this.badMouthTexture);

                        this.perdeu.play();
                        this.challengeFailed();

                    }

                    characters.removeValue(sprite, true);

                    if (this.friendsCollected >= friends) {

                        this.venceu.play();
                        super.challengeSolved();

                    }
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

        this.fundo.draw(this.screen.batch);

        for (int i = 0; i < characters.size; i++) {
            Sprite sprite = characters.get(i);
            sprite.draw(this.screen.batch);
        }
        boca.draw(this.screen.batch);
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

}
