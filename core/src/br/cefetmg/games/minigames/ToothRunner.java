package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/**
 * Created by matheus on 02/11/16.
 */
public class ToothRunner extends MiniGame {

    public class Dente {

        public Sprite sprite;
        public float sobe;
        public boolean chao;
        public float impulso;
        public Animation corre;
        public float tempoAnimacao;
        public TextureRegion atual;

        public Dente(Texture textura) {
            this.sprite = new Sprite(textura);
            //public TextureRegion atual;
            this.chao = true;
            this.impulso = 25;
            this.sobe = this.impulso;

            TextureRegion[][] quadrosDaAnimacao;
            quadrosDaAnimacao = TextureRegion.split(textura, 610, 755);

            corre = new Animation(1f, new TextureRegion[]{
                quadrosDaAnimacao[0][0],
                quadrosDaAnimacao[0][1],
                quadrosDaAnimacao[0][2],});

            corre.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            tempoAnimacao = 0;
        }
    }

    private class Enemie {

        public Sprite sprite;
        public Vector2 direcaoX;

        public Enemie(Texture textura, Vector2 direcaoX) {
            this.direcaoX = direcaoX;
            this.sprite = new Sprite(textura);
        }
    };

    private final Dente dente;
    private final ArrayList<Enemie> pirulito;
    private int maxEnemies;
    private final Texture pirulitoTexture;
    private final Texture denteAnimation;
    private final Sound passo;
    private final Sound pulo;

    private int velocidade;
    private final Vector2 gravidade;
    private int delta;

    private final Texture bg;

    public ToothRunner(BaseScreen screen, GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS, observer);

        delta = 0;
        this.pirulito = new ArrayList<Enemie>();
        this.pirulitoTexture = this.screen.assets.get("tooth-runner/pirulito.png", Texture.class);
        this.denteAnimation = this.screen.assets.get("tooth-runner/denteAnimacao.png", Texture.class);
        this.passo = this.screen.assets.get("tooth-runner/passo.mp3", Sound.class);
        this.pulo = this.screen.assets.get("tooth-runner/pulo.mp3", Sound.class);

        this.gravidade = new Vector2(0, (float) 1);

        bg = new Texture(Gdx.files.internal("tooth-runner/fundo.png"));
        bg.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        this.dente = new Dente(denteAnimation);
        this.dente.sprite.setSize(100, 100);
        this.dente.sprite.setX(100);
        this.dente.sprite.setY(100);
    }

    @Override
    protected void onStart() {
        createEnemies();
    }

    void createEnemies() {
        int distancia = 0;
        for (int i = 0; i < maxEnemies; i++) {
            Enemie aux;
            aux = new Enemie(pirulitoTexture, new Vector2(-1, 0));
            aux.sprite.setSize(100, 100 + rand.nextInt(30));
            aux.sprite.setX(this.screen.viewport.getWorldWidth() - aux.sprite.getWidth() + distancia);
            distancia += this.screen.viewport.getWorldWidth() / 3 + 100;
            aux.sprite.setY(100);
            this.pirulito.add(aux);
        }
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        maxEnemies = 3;
        this.velocidade = 8 + (int) (difficulty * 6);
    }

    @Override
    public void onHandlePlayingInput() {
        if (Gdx.input.justTouched()) {
            if (dente.chao) {
                dente.chao = false;
                pulo.play();
                dente.tempoAnimacao = 0;
            } else {
                dente.sobe = -15;
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
        //atualizacao de logica
        dente.atual = dente.corre.getKeyFrame(dente.tempoAnimacao);
        if (!dente.chao) {
            if (dente.sprite.getY() + dente.sobe < 100) {
                dente.sprite.setY(100);
                dente.chao = true;
                dente.sobe = dente.impulso;
            } else {
                dente.sprite.setY(dente.sprite.getY() + dente.sobe);
            }

            dente.sobe -= gravidade.y;
        }

        //atualiza pirulitos
        for (int i = 0; i < pirulito.size(); i++) {
            Enemie enemie = pirulito.get(i);
            enemie.sprite.setX(enemie.sprite.getX() - this.velocidade);
            if (enemie.sprite.getX() + enemie.sprite.getWidth() < 0) {
                enemie.sprite.setSize(enemie.sprite.getWidth(), 100 + rand.nextInt(30));
                enemie.sprite.setX(this.screen.viewport.getWorldWidth() + rand.nextInt(100) + 350);
            }
        }

        //verificar colisao
        for (int i = 0; i < pirulito.size(); i++) {
            Enemie enemie = pirulito.get(i);
            Rectangle aux = dente.sprite.getBoundingRectangle();
            aux.setSize(90, 90);
            if (dente.sprite.getBoundingRectangle().overlaps(enemie.sprite.getBoundingRectangle())) {
                super.challengeFailed();
            }
        }

        int aux = (int) dente.tempoAnimacao;

        if (this.dente.chao) {
            dente.tempoAnimacao += Gdx.graphics.getDeltaTime() * 7;
        }

        if ((int) dente.tempoAnimacao > aux) {
            passo.play();
        }

        delta += this.velocidade;
    }

    @Override
    public void onDrawGame() {
        //controla se perdeu, para que isso nao seja chamado em um intervalo curto do jogo

        this.screen.batch.draw(bg, 0, 0, delta, 0, (int) this.screen.viewport.getWorldWidth(), (int) this.screen.viewport.getWorldHeight());

        for (int i = 0; i < pirulito.size(); i++) {
            Enemie enemie = pirulito.get(i);
            enemie.sprite.draw(this.screen.batch);
        }

        if (dente.atual != null) {
            this.screen.batch.draw(dente.atual, dente.sprite.getX() - 10, dente.sprite.getY(), 0, 0, dente.sprite.getWidth() + 10, dente.sprite.getHeight() + 20, 1.0f, 1.0f, 0);
        }
    }

    @Override
    public String getInstructions() {
        return "Salte os pirulitos";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }
}
