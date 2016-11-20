package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import java.util.Random;

/**
 * Created by matheus on 15/09/16.
 */
public class CarieSword extends MiniGame {

    //Quadros de animacao para os dentes no canto direito
    private class Denticao {

        public Sprite sprite;
        public TextureRegion atual;
        public Animation quebra;
        public float tempoAnimacao;

        public Denticao(Texture textura) {
            this.sprite = new Sprite(textura);
            TextureRegion[][] quadrosDaAnimacao;
            quadrosDaAnimacao = TextureRegion.split(textura, 64, 296);

            quebra = new Animation(1f, new TextureRegion[]{
                quadrosDaAnimacao[0][0],
                quadrosDaAnimacao[0][1],
                quadrosDaAnimacao[0][2],
                quadrosDaAnimacao[0][3],});

            quebra.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            tempoAnimacao = 0;
        }
    }

    //Quadros de animacao para as caries
    private class Enemy {

        public Sprite sprite;
        public Vector2 direcaoX;
        public Vector2 direcaoY;
        public float forcaX;
        public float forcaY;
        public float tempoAnimacao;
        public TextureRegion atual;
        public Animation die;
        public boolean morre;

        public Enemy(Sprite sprite, Vector2 direcaoX, Vector2 direcaoY, float forcaX, float forcaY,
                Texture animacao) {
            TextureRegion[][] quadrosDaAnimacao;
            quadrosDaAnimacao = TextureRegion.split(animacao, 244, 256);

            die = new Animation(1f, new TextureRegion[]{
                quadrosDaAnimacao[0][0],
                quadrosDaAnimacao[0][1],
                quadrosDaAnimacao[0][2],
                quadrosDaAnimacao[0][3],
                quadrosDaAnimacao[0][3]
            });

            die.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            tempoAnimacao = 0;

            this.sprite = sprite;
            this.direcaoX = direcaoX;
            this.direcaoY = direcaoY;
            this.forcaX = forcaX;
            this.forcaY = forcaY;
            this.morre = false;
        }
    };

    //estruturas relacionadas a tela e aos sons
    private final Array<Enemy> enemies;
    private final Denticao dentes;
    private final Texture cariesTexture;
    private final Texture cariesAnimation;
    private final Texture dentesTexture;
    private final Array<Sound> swordSounds;
    private final Array<Sound> bombSounds;
    private final Array<Sound> cariesSounds;

    //vriaveis relacionadas aos personagens
    private int enemiesPass;
    private int spawnedEnemies;
    private int totalEnemies;
    private float spawnInterval;
    private int limite;
    private int perdeu;

    //variaveis de cunho fisico para o jogo
    private final Vector2 gravidade;
    private final Random random;

    public CarieSword(BaseScreen screen, GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS, observer);
        this.enemies = new Array<Enemy>();

        //carrega texturas e sons
        this.cariesTexture = this.screen.assets.get("carie-sword/caries.png", Texture.class);
        this.cariesAnimation = this.screen.assets.get("carie-sword/cariesAnimacao.png", Texture.class);
        this.dentesTexture = this.screen.assets.get("carie-sword/tooth.png", Texture.class);
        this.swordSounds = new Array<Sound>(new Sound[]{
            this.screen.assets.get("carie-sword/som1.mp3", Sound.class),
            this.screen.assets.get("carie-sword/som2.mp3", Sound.class),
            this.screen.assets.get("carie-sword/som3.mp3", Sound.class)
        });
        this.bombSounds = new Array<Sound>(new Sound[]{
            this.screen.assets.get("carie-sword/bomb1.mp3", Sound.class),
            this.screen.assets.get("carie-sword/bomb2.mp3", Sound.class)
        });
        this.cariesSounds = new Array<Sound>(new Sound[]{
            this.screen.assets.get("carie-sword/caries1.mp3", Sound.class),
            this.screen.assets.get("carie-sword/caries2.mp3", Sound.class)
        });

        this.spawnedEnemies = 0;                            //inimigos criados
        this.enemiesPass = 0;                               //inimigos que passaram
        this.gravidade = new Vector2(0, (float) -0.015);
        this.random = new Random();
        this.perdeu = 0;                                    //controle de derrota
        this.dentes = new Denticao(dentesTexture);          //Cria nova Denticao para a tela
        this.dentes.sprite.setX(this.screen.viewport.getWorldWidth()
                - (this.dentes.sprite.getWidth() / 4) * 2);
        this.dentes.sprite.setY(0);
        this.dentes.sprite.scale(2);
    }

    @Override
    protected void onStart() {
        Gdx.input.setInputProcessor(
                new InputAdapter() {
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                screen.viewport.unproject(click);
                for (int i = 0; i < enemies.size; i++) {
                    Enemy enemie = enemies.get(i);
                    //verifica se o mouse arrastou porcima da carie
                    Rectangle aux = enemie.sprite.getBoundingRectangle();
                    if (enemie.morre == false) {
                        if (click.x > aux.getX() && click.x < aux.getX() + aux.getWidth()
                                && click.y > aux.getY() && click.y < aux.getY() + aux.getHeight()) {
                            enemie.morre = true;
                            swordSounds.random().play();
                            cariesSounds.random().play();
                        }
                    }
                }
                return false;
            }
        });
        scheduleEnemySpawn();
    }

    @Override
    protected void onEnd() {
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Responsavel pela controle de criacao do inimigo com base em um certo
     * intervalo de tempo que depende da dficuldade
     */
    private void scheduleEnemySpawn() {
        Timer.Task t = new Timer.Task() {
            @Override
            public void run() {
                spawnEnemy();
                if (++spawnedEnemies < totalEnemies) {
                    scheduleEnemySpawn();
                }
            }
        };

        float nextSpawnMillis = this.spawnInterval
                * (rand.nextFloat() / 3 + 0.15f);
        super.timer.scheduleTask(t, nextSpawnMillis);
    }

    /**
     * Responsavel por gerar os inimigos Os inimigos sao gerados com um vetor
     * apontando na direcao x, outro apontando na direcao y, e uma posicao
     * aleatoria no eixo y (0 a 200). Os inimigos tamvem possuem uma forca
     * aleatoria, tanto para o vetor x, quanto para o vetor y.
     */
    private void spawnEnemy() {
        Vector2 direcaoX = new Vector2(1, 0);
        Vector2 direcaoY = new Vector2(0, 1);
        Vector2 position = new Vector2(0, random.nextInt(200));

        //cria a estrutura inimigo
        Sprite enemy = new Sprite(cariesTexture);
        enemy.setPosition(position.x, position.y);
        Enemy novo = new Enemy(enemy, direcaoX, direcaoY, random.nextInt(13 - 9) + 9, random.nextInt(17 - 1) + 1,
                cariesAnimation);
        novo.sprite.setSize(100, 100);
        enemies.add(novo);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.totalEnemies = 20 + (int) (difficulty * 50);
        this.spawnInterval = 1 - (difficulty / 2f);
        this.limite = 15 - (int) (difficulty * 5);
    }

    @Override
    public void onHandlePlayingInput() {

    }

    @Override
    public void onUpdate(float dt) {
        for (Enemy enemy : enemies) {
            Vector2 direcaoX = new Vector2(enemy.direcaoX).scl(enemy.forcaX);
            Vector2 direcaoY = new Vector2(enemy.direcaoY).scl(enemy.forcaY);
            Vector2 resultante = new Vector2(direcaoX.add(direcaoY));
            enemy.direcaoY.add(gravidade);
            enemy.sprite.translate(resultante.x, resultante.y);
            enemy.atual = enemy.die.getKeyFrame(enemy.tempoAnimacao);

            //caso o inimigo ja tenha sido decomposto ele e removido do vetor
            if (enemy.tempoAnimacao > 4) {
                enemies.removeValue(enemy, true);
            }

            //caso o inimigo ja tenha sido acertado o tempo de animcao deve variar
            if (enemy.morre == true) {
                enemy.tempoAnimacao += Gdx.graphics.getDeltaTime() * 15;
            }

            //caso o inimigo tenha ultrapassado a fronteira de defesa, e contado o numero de inimigos que
            //passaram e o inimigo e removido do vetor
            if (enemy.sprite.getX() > this.screen.viewport.getWorldWidth()) {
                enemiesPass++;
                enemies.removeValue(enemy, true);
                bombSounds.random().play();
            }
        }

        //se o numero de inimigos que passaram for menor que o limite podemos varia a textura
        if (enemiesPass < limite) {
            this.dentes.atual = this.dentes.quebra.getKeyFrame(0 + ((float) enemiesPass / limite) * 3.75f);
        }
    }

    @Override
    public void onDrawGame() {
        //controla se perdeu, para que isso nao seja chamado em um intervalo curto do jogo
        if (perdeu == 0) {
            if (enemiesPass > limite) {
                super.challengeFailed();
                perdeu = 1;
            }
        }
        //varre o array de inimigos atualizando suas posicoes na tela
        for (Enemy enemy : enemies) {
            if (enemy.atual == null) {
                continue;
            }
            this.screen.batch.draw(enemy.atual, enemy.sprite.getX(), enemy.sprite.getY(),
                    0, 0, 
                    enemy.atual.getRegionWidth(), 
                    enemy.atual.getRegionHeight(), 0.5f, 0.5f, 0);
        }
        //atualiza o dente
        if (dentes.atual != null) {
            this.screen.batch.draw(dentes.atual, dentes.sprite.getX() - 25, dentes.sprite.getY(),
                    0, 0, dentes.atual.getRegionWidth() * 2.5f, dentes.atual.getRegionHeight() * 2.45f, 1.0f, 1.0f, 0);
        }
    }

    @Override
    public String getInstructions() {
        return "Corte as cáries com sua \"katana\"";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }

}
