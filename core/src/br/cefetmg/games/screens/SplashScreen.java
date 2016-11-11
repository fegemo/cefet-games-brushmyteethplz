package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * A tela de <em>splash</em> (inicial, com a logomarca) do jogo.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class SplashScreen extends BaseScreen {

    /**
     * Momento em que a tela foi mostrada (em milissegundos).
     */
    private float displayWaitingTime;

    private static final int QTD_OF_FRAMES = 87;
    private static final float FRAME_PERIOD = 0.036f;
    private static final float SPRITE_SCALE_FACTOR = 1.38f;

    private long timeWhenScreenShowedUp;
    private int currentFrame;
    /**
     * Uma {@link Sprite} que contém a logo animada da empresa CEFET-GAMES.
     */
    private Sprite animatedLogo;
    private final Sound splashSound;
    private final Texture[] splashTextures;

    /**
     * Cria uma nova tela de <em>splash</em>.
     *
     * @param game O jogo dono desta tela.
     * @param previous A tela anterior a esta.
     */
    public SplashScreen(Game game, BaseScreen previous) {
        super(game, previous);
        splashSound = Gdx.audio.newSound(Gdx.files.internal("sounds/splash.mp3"));
        this.currentFrame = 0;
        this.displayWaitingTime = 0;
        transitionState = states.doNothing;
        this.splashTextures = new Texture[QTD_OF_FRAMES];
    }

    /**
     * Configura parâmetros iniciais da tela.
     */
    @Override
    public void appear() {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        for (int i = 0; i < QTD_OF_FRAMES; i++) {
            String name = String.format("images/splash/video %d.jpg", (i+1));
            splashTextures[i] = new Texture(name);
        }

        animatedLogo = new Sprite(splashTextures[0]);
        animatedLogo.setScale(SPRITE_SCALE_FACTOR);
        animatedLogo.setCenter(
                super.viewport.getWorldWidth() / 2,
                super.viewport.getWorldHeight() / 2);
        timeWhenScreenShowedUp = TimeUtils.millis();
        splashSound.play();
    }
    
    @Override
    public void cleanUp() {
        splashSound.dispose();
    }

    /**
     * Navega para a tela de Menu.
     */
    private void navigateToMenuScreen() {
        game.setScreen(new MenuScreen(game, this));
    }

    /**
     * Verifica se houve <em>input</em> do jogador.
     */
    @Override
    public void handleInput() {
        // se o jogador apertar alguma tecla, clicar com o mouse ou 
        // tocar a tela, pula direto para a próxima tela.
        if (Gdx.input.justTouched()) {
            animatedLogo.setTexture(splashTextures[QTD_OF_FRAMES - 1]);
            splashSound.stop();
            transitionState = states.doNothing;
            navigateToMenuScreen();
        }
    }

    /**
     * Atualiza a lógica da tela de acordo com o tempo.
     *
     * @param dt Tempo desde a última chamada.
     */
    @Override
    public void update(float dt) {
        displayWaitingTime += dt;
        
        //verifica se tempo de espera para exibir novo frame foi atingido
        if (displayWaitingTime > FRAME_PERIOD) {
            //verifica não chegou ao final da animação
            if (currentFrame < QTD_OF_FRAMES) {
                animatedLogo.setTexture(splashTextures[currentFrame]);
                currentFrame += 1;
                displayWaitingTime = 0;
            }
        }

        // verifica se o quadro atual é último para ir para a próxima tela.
        if ((currentFrame >= QTD_OF_FRAMES) &&
            (transitionState == states.doNothing)) {
            splashSound.stop();
            transitionState = states.fadeOut;
        }
        
        
        if (transitionState == states.fadeOut && transition.isFinished()) {
            navigateToMenuScreen();
        }
    }

    /**
     * Desenha a {@link Sprite} com a logomarca.
     */
    @Override
    public void draw() {
        batch.begin();
        animatedLogo.draw(batch);
        batch.end();
    }
}
