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
    private Sound splashSound;
    private Texture[] splashTextures;

    /**
     * Cria uma nova tela de <em>splash</em>.
     *
     * @param game O jogo dono desta tela.
     */
    public SplashScreen(Game game, BaseScreen previous) {
        super(game, previous);
        splashSound = Gdx.audio.newSound(Gdx.files.internal("sounds/splash.mp3"));
        this.currentFrame = 0;
        this.displayWaitingTime = 0;

        this.splashTextures = new Texture[QTD_OF_FRAMES];

        for (int i = 0; i < QTD_OF_FRAMES; i++) {
            String name = "images/splash/video ".concat(String.valueOf(i + 1).concat(".jpg"));
            splashTextures[i] = new Texture(name);
        }

    }

    /**
     * Configura parâmetros iniciais da tela.
     */
    @Override
    public void show() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        timeWhenScreenShowedUp = TimeUtils.millis();
        animatedLogo = new Sprite(splashTextures[0]);
        animatedLogo.setScale(SPRITE_SCALE_FACTOR);
        animatedLogo.setCenter(
                super.viewport.getWorldWidth() / 2,
                super.viewport.getWorldHeight() / 2);
        splashSound.play();

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
                animatedLogo = new Sprite(splashTextures[currentFrame]);
                animatedLogo.setScale(SPRITE_SCALE_FACTOR);
                currentFrame += 1;
                animatedLogo.setCenter(
                        super.viewport.getWorldWidth() / 2,
                        super.viewport.getWorldHeight() / 2);
                displayWaitingTime = 0;
            }
        }

        // verifica se o tempo em que se passou na tela é maior do que o máximo
        // para que possamos navegar para a próxima tela.
        if (TimeUtils.timeSinceMillis(timeWhenScreenShowedUp)
                >= Config.TIME_ON_SPLASH_SCREEN) {
            splashSound.stop();
            transition.update(dt);
            if (transition.isFinished()) {
                navigateToMenuScreen();
            }
        }
    }

    /**
     * Desenha a {@link Sprite} com a logomarca.
     */
    @Override
    public void draw() {
        batch.begin();
        animatedLogo.draw(batch);
        transition.fadeOut(batch, screenTransition);
        batch.end();
    }
}
