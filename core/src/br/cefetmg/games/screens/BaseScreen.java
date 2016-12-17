package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Uma tela do jogo.
 *
 * O jogo é dividido em várias telas (Splash, Menu, PlayingGame etc.) e o código
 * relativo a cada uma delas é uma instância de uma subclasse de BaseScreen.
 *
 * Cada BaseScreen possui uma {@link SpriteBatch} própria, bem como duas fontes
 * ({@link BitmapFont}) padrão para escrever texto na tela: Sawasdee 24pt e
 * Sawasdee 50pt.
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public abstract class BaseScreen extends ScreenAdapter {

    public final Game game;
    private final BaseScreen previous;
    public final SpriteBatch batch;
    public final OrthographicCamera camera;
    public Viewport viewport;
    public final AssetManager assets;
    private BitmapFont messagesFont;
    protected Texture transitionTexture;
    protected Sprite screenTransition;
    protected TransitionEffect transition;
    private boolean previousBackButtonState;
    private float deviceAspectRatioDivergenceFromDesired;

    protected enum states {
        fadeIn, doNothing, fadeOut, stopDrawing
    };
    protected states transitionState;
    private boolean wasJustDisposed = false;

    /**
     * Cria uma instância de tela.
     *
     * @param game O jogo do qual a nova instância pertence.
     * @param previous A tela anterior, que levou a esta. Caso esta seja a
     * primeira tela, o valor deve ser null.
     */
    public BaseScreen(Game game, BaseScreen previous) {
        this.game = game;
        this.previous = previous;
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
        this.viewport = chooseBestViewport();
        this.assets = new AssetManager();
        this.assets.load("fonts/sawasdee-24.fnt", BitmapFont.class);
        this.assets.load("fonts/sawasdee-50.fnt", BitmapFont.class);
        screenTransition = new Sprite(new Texture("images/transicao.png"), (int) viewport.getWorldWidth(), (int) viewport.getWorldHeight());
        screenTransition.setCenter(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f);
        transition = new TransitionEffect();
        transition.setDelta(0.025f);
        transitionState = states.fadeIn;
    }

    /**
     * Determina qual é o melhor tipo de viewport para ser usado considerando
     * que o jogo foi projetado para 16:9, mas o dispositivo pode ter uma razão
     * de aspecto diferente.
     *
     * Se a razão real do dispositivo for próxima da razão ideal, escolhe uma
     * FillViewport, que preenche todo o espaço do dispositivo mas pode deixar
     * alguma coisinha de fora; se a razão do dispositivo for muito diferente,
     * usa uma FitViewport e coloca o conteúdo do jogo completamente dentro da
     * tela do dispositivo, o que provoca o surgimento de barras pretas laterais
     * ou em cima/baixo.
     *
     * @return a viewport que será usada.
     */
    private Viewport chooseBestViewport() {
        int deviceWidth = Gdx.graphics.getWidth();
        int deviceHeight = Gdx.graphics.getHeight();
        float deviceAspectRatio = ((float) deviceWidth) / deviceHeight;
        this.deviceAspectRatioDivergenceFromDesired = 
                (Config.DESIRED_ASPECT_RATIO - deviceAspectRatio)
                / Config.DESIRED_ASPECT_RATIO;
        
        if (Math.abs(deviceAspectRatioDivergenceFromDesired)
                < Config.MAXIMUM_ASPECTO_RATIO_DIFFERENCE) {
            return new FillViewport(
                    Config.WORLD_WIDTH,
                    Config.WORLD_HEIGHT,
                    this.camera
            );
        } else {
            return new FitViewport(
                    Config.WORLD_WIDTH,
                    Config.WORLD_HEIGHT,
                    this.camera
            );
        }
    }

    public Rectangle getVisibleWorldBounds() {
        float factor = this.deviceAspectRatioDivergenceFromDesired;
        boolean lastingSideways =  factor > 0;
        if (lastingSideways) {
            return new Rectangle(
                    0 + (factor / 2) * Config.WORLD_WIDTH,
                    0,
                    Config.WORLD_WIDTH - (factor/2) * Config.WORLD_WIDTH,
                    Config.WORLD_HEIGHT
            );
            
        } else {
            return new Rectangle(
                    0,
                    0 + ((1/factor) / 2) * Config.WORLD_HEIGHT,
                    Config.WORLD_WIDTH,
                    Config.WORLD_HEIGHT - ((1/factor)/2) * Config.WORLD_HEIGHT
            );
        }
    }

    @Override
    public final void show() {
        if (previous != null) {
            previous.dispose();
        }
        this.appear();
    }

    /**
     * Atualiza as dimensões da tela de pintura ({@link Viewport}).
     *
     * @param width nova largura da janela.
     * @param height nova altura da janela.
     */
    @Override
    public void resize(int width, int height) {
        this.viewport.update(width, height, true);
    }

    /**
     * Invoca as funções de atualização de lógica, recepção de <em>input</em>
     * e de desenho.
     *
     * Além disso, assegura de que os desenhos
     *
     * @param dt Quanto tempo se passou desde a última vez que a função foi
     * chamada.
     */
    @Override
    public final void render(float dt) {
        if (this.assets.update()) {
            if (this.messagesFont == null) {
                this.messagesFont = this.assets.get("fonts/sawasdee-50.fnt");
            }

            // verifica se o botão voltar foi pressionado (apenas Android)
            handleBackButton();

            // chama função para gerenciar o input
            handleInput();

            // chama função para atualizar a lógica da tela
            update(dt);

            // a tela pode ter sido "disposed" durante este último update, então
            // verificamos se isso aconteceu para saber se seguimos adiante
            if (wasJustDisposed) {
                return;
            }

            // chama função para atualizar o estado da transição
            updateTransition(dt);

            // define o sistema de coordenadas (projeção) a ser usada pelo
            // spriteBatch
            this.batch.setProjectionMatrix(this.camera.combined);

            // limpa a tela para que possa ser redesenhada
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // desenha o conteúdo da tela
            draw();

            // desenha a transição de tela
            drawTransition();
        }
    }

    protected void updateTransition(float dt) {
        switch (transitionState) {
            case fadeIn:
                if (transition.isFinished()) {
                    transitionState = states.doNothing;
                    transition.setX(0);
                }
            // passa adiante (nao pus o break de propósito)
            case fadeOut:
                transition.update(dt);
                break;
        }
    }

    /**
     * Escreve um texto centralizado na tela, com uma escala {@code scale} e na
     * altura {@code y}.
     *
     * @param text O texto a ser escrito.
     * @param scale O tamanho do texto, que deve ser ]0,1].
     * @param y A altura do mundo de jogo onde o texto deve ser renderizado.
     * Deve estar entre [0 (baixo), Config.WORLD_HEIGHT-altura-do-texto].
     */
    public void drawCenterAlignedText(String text, float scale, float y) {
        if (scale > 1) {
            throw new IllegalArgumentException("Pediu-se para escrever texto "
                    + "com tamanho maior que 100% da fonte, mas isso acarreta "
                    + "em perda de qualidade do texto. Em vez disso, use uma "
                    + "fonte maior. O valor de 'scale' deve ser sempre menor "
                    + "que 1.");
        }
        final float horizontalPadding = 0.05f;
        messagesFont.setColor(Color.BLACK);
        messagesFont.getData().setScale(scale);

        final float worldWidth = this.viewport.getWorldWidth();
        messagesFont.draw(
                this.batch,
                text,
                0 + horizontalPadding * worldWidth,
                y,
                worldWidth * (1 - horizontalPadding * 2),
                Align.center,
                true);
    }

    protected void drawTransition() {
        batch.begin();
        switch (transitionState) {
            case fadeIn:
                transition.fadeIn(batch, screenTransition);
                break;
            case fadeOut:
                transition.fadeOut(batch, screenTransition);
                break;
        }
        batch.end();
    }

    @Override
    public final void dispose() {
        if (!wasJustDisposed) {
            wasJustDisposed = true;
            batch.dispose();
            if (messagesFont != null) {
                messagesFont.dispose();
            }
            this.cleanUp();
        }
    }

    /**
     * Verifica se o botão "voltar" do Android (caso estejamos nele) foi
     * pressionado e então chama o método <code>onBackButtonPressed</code>.
     */
    private void handleBackButton() {
        // esta implementação poderia ser apenas a linha abaixo, mas um bug
        // na LibGDX não permite que isso aconteça: 
        // https://github.com/libgdx/libgdx/issues/4363
        //
        // if (Gdx.input.isKeyJustPressed(Keys.BACK)) onBackButtonPressed();
        //
        // a implementação alternativa mantém o estado do botão "voltar" e 
        // apenas chama onBackButtonPressed se o estado anterior era "não 
        // pressionado" e o atual é "pressionado"
        boolean currentBackButtonState = Gdx.input.isKeyPressed(Keys.BACK);
        if (currentBackButtonState != previousBackButtonState
                && currentBackButtonState) {
            onBackButtonPressed();
        }
        previousBackButtonState = currentBackButtonState;
    }

    /**
     * Executa ações de carregamento da tela. Esta função é chamada assim que a
     * tela vai ser exibida pela primeira vez.
     *
     * Esta função deve ser usada em vez do método {@code show()}.
     */
    public abstract void appear();

    /**
     * Executa as ações de limpeza e descarregamento de recursos e é chamada
     * automaticamente quando a tela não está mais sendo usada.
     */
    public abstract void cleanUp();

    /**
     * Invocada quando o botão "voltar" (i.e., apenas no Android) foi
     * pressionada. Na implementação padrão, sai da aplicação.
     */
    protected void onBackButtonPressed() {
        Gdx.app.exit();
    }

    /**
     * Executa ações relativas ao <em>input</em> do jogador.
     *
     * Use {@code Gdx.input.*} para perguntar se eventos de <em>input</em>
     * estão acontecendo.
     */
    public abstract void handleInput();

    /**
     * Atualiza a lógica da tela.
     *
     * @param dt Quanto tempo se passou desde a última vez que a função foi
     * chamada.
     */
    public abstract void update(float dt);

    /**
     * Desenha o conteúdo da tela.
     */
    public abstract void draw();

}
