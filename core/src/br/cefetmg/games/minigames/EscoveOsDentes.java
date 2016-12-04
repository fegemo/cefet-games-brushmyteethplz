package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import br.cefetmg.games.minigames.util.GameStateObserver;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author Henrique Hideki Sampaio
 */
public class EscoveOsDentes extends MiniGame {

    private final Array<Sprite> Tooths;
    private final Array<Escova> Escovar;
    private final Sprite escova;
    private final Sprite fundo2;
    private final Texture fundo;
    private final Texture toothTexture;
    private final Texture dente;
    private final Texture bolhas;
    private final Texture EscovaTexture;
    private final Sound BrushingSound;
    private final Sound itsCleanSound;
    private int cleanTooth;
    private int spawnedTooth;

    private float initialToothScale;
    private float minimumToothScale;
    private int totalTooths;
    private float spawnInterval;
    private int numEscovada = 8;

    public class Escova {

        public int overlapdentro = 0;
        public int overlapfora = 0;
        public int count = 0;
        public int direcao = 0;
        public int escovado = 0;
        private int tempo = 0;
        private boolean dentro;
    }

    public EscoveOsDentes(BaseScreen screen, GameStateObserver observer, float difficulty) {

        super(screen, difficulty, 10f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);

        this.Escovar = new Array<Escova>();
        this.Tooths = new Array<Sprite>();
        this.toothTexture = this.screen.assets.get("escove-os-dentes/toothdirty.png", Texture.class);
        this.dente = this.screen.assets.get("escove-os-dentes/toothclean.png", Texture.class);
        this.bolhas = this.screen.assets.get("escove-os-dentes/brush2.png", Texture.class);
        this.fundo = this.screen.assets.get("escove-os-dentes/fundo.png", Texture.class);
        this.EscovaTexture = this.screen.assets.get("escove-os-dentes/brush1.png", Texture.class);
        this.BrushingSound = this.screen.assets.get("escove-os-dentes/escovar.mp3", Sound.class);
        this.itsCleanSound = this.screen.assets.get("escove-os-dentes/limpo.wav", Sound.class);
        this.escova = new Sprite(EscovaTexture);
        this.fundo2 = new Sprite(fundo);
        this.escova.setOriginCenter();
        this.cleanTooth = 0;
        this.spawnedTooth = 0;
        this.escova.setScale((float) 0.25);
        this.escova.rotate(30);
        this.fundo2.setOriginCenter();
        this.fundo2.setSize(super.screen.viewport.getWorldWidth(), super.screen.viewport.getWorldHeight());
    }

    @Override
    protected void onStart() {
        scheduleEnemySpawn();
    }

    private void scheduleEnemySpawn() {
        Task t = new Task() {
            @Override
            public void run() {
                spawnEnemy();
                if (++spawnedTooth < totalTooths) {
                    scheduleEnemySpawn();
                }
            }
        };
        // spawnInterval * 15% para mais ou para menos
        float nextSpawnMillis = this.spawnInterval * (rand.nextFloat() / 3 + 0.15f);
        super.timer.scheduleTask(t, nextSpawnMillis);
    }

    private void spawnEnemy() {
        // pega x e y entre 0 e 1
        Vector2 position = new Vector2(rand.nextFloat(), rand.nextFloat());
        // multiplica x e y pela largura e altura da tela
        position.scl(this.screen.viewport.getWorldWidth() - toothTexture.getWidth() * initialToothScale,
                this.screen.viewport.getWorldHeight() - toothTexture.getHeight() * initialToothScale
        );

        Sprite enemy = new Sprite(toothTexture);
        Escova novo = new Escova();
        enemy.setPosition(position.x, position.y);
        enemy.setScale(minimumToothScale);
        Tooths.add(enemy);
        Escovar.add(novo);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.initialToothScale = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 1.15f, 0.8f);
        this.minimumToothScale = DifficultyCurve.LINEAR_NEGATIVE.getCurveValueBetween(difficulty, 0.15f, 0.4f);
        this.spawnInterval = DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 1.5f);
        this.totalTooths = (int) Math.ceil(maxDuration / spawnInterval) - 3;
        this.numEscovada = (int) Math.ceil(maxDuration / spawnInterval) - 3;
    }

    @Override
    public void onHandlePlayingInput() {
        // atualiza a posição do alvo de acordo com o mouse
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        super.screen.viewport.unproject(click);
        this.escova.setPosition(click.x - this.escova.getWidth() / 2, click.y - this.escova.getHeight() / 2);

        // itera no array de inimigos
        for (int i = 0; i < Tooths.size; i++) {
            Sprite sprite = Tooths.get(i);
            Escova aux = Escovar.get(i);

            // se há interseção entre o retângulo da sprite e da escova,
            // a escova encostou
            if (sprite.getBoundingRectangle().overlaps(escova.getBoundingRectangle())) {
                // verifica se escova em cima do dente ou nao 
                aux.overlapdentro = 1;
                if (!aux.dentro) {
                    // toca um efeito sonoro
                    BrushingSound.play(0.5f);
                    aux.count += 1;
                    aux.dentro = true;
                    this.escova.setTexture(bolhas);
                }
            } else {

                aux.overlapfora = 1;
                if (aux.dentro) {
                    aux.count += 1;
                    aux.dentro = false;
                    this.escova.setTexture(EscovaTexture);
                }

                if (aux.count >= this.numEscovada && aux.escovado != 1) {
                    // contabiliza um dente limpo
                    this.cleanTooth++;
                    sprite.setTexture(dente);
                    // seta dente como escovado
                    aux.escovado = 1;
                    itsCleanSound.play();
                    // se tiver limpado todo os dentes, o desafio
                    // está resolvido
                    if (this.cleanTooth >= this.totalTooths) {
                        super.challengeSolved();
                    }
                    // pára de iterar, porque senão pode pegar em mais
                    // de um dente
                    break;
                }
            }
        }
    }

    @Override
    public void onUpdate(float dt) {

        for (int i = 0; i < Tooths.size; i++) {
            Sprite sprite = Tooths.get(i);
            Escova aux = Escovar.get(i);

            aux.tempo++;
            if (aux.tempo == 20 && aux.count < 6) {
                aux.direcao = rand.nextInt(8);
                aux.tempo = 0;
            }
            if (aux.count == this.numEscovada) {
                aux.direcao = 11;
                sprite.setTexture(dente);
            }

            switch (aux.direcao) {
                case 1:
                    sprite.translate(3, 3);
                    break;
                case 2:
                    sprite.translate(-3, -3);
                    break;
                case 3:
                    sprite.translateX(3);
                    break;
                case 4:
                    sprite.translateX(-3);
                    break;
                case 5:
                    sprite.translateY(3);
                    break;
                case 6:
                    sprite.translateY(-3);
                    break;
                case 7:
                    sprite.translate(-3, 3);
                    break;
                case 8:
                    sprite.translate(3, -3);
                    break;
                default:
                    break;
            }

            if (sprite.getY() <= -80) {
                aux.direcao = rand.nextInt(8);
                sprite.translateY(3);
            }
            if (sprite.getY() > this.screen.viewport.getWorldHeight() - 160) {
                aux.direcao = rand.nextInt(8);
                sprite.translateY(-3);
            }
            if (sprite.getX() >= this.screen.viewport.getWorldWidth() - 160) {
                aux.direcao = rand.nextInt(8);
                sprite.translateX(-3);
            }
            if (sprite.getX() <= -80) {
                aux.direcao = rand.nextInt(8);
                sprite.translateX(3);
            }
        }
    }

    @Override
    public String getInstructions() {
        return "Escove os dentes";
    }

    @Override
    public void onDrawGame() {
        fundo2.draw(this.screen.batch);
        for (int i = 0; i < Tooths.size; i++) {
            Sprite sprite = Tooths.get(i);
            sprite.draw(this.screen.batch);
        }
        escova.draw(this.screen.batch);
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

}
