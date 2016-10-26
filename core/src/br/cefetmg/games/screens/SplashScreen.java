package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.ArrayList;

/**
 * A tela de <em>splash</em> (inicial, com a logomarca) do jogo.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class SplashScreen extends BaseScreen {

    /**
     * Momento em que a tela foi mostrada (em milissegundos).
     */
    private long timeWhenScreenShowedUp;

    /**
     * Uma {@link Sprite} que contém a logo da empresa CEFET-GAMES.
     */
    private Sprite logo;
    
    /**
     * Um objeto para criação do efeito de transição entre telas
     */
    private TransitionEffect transition;

    /**
     * ArrayList auxiliar no processo de transição de tela
     */
    private ArrayList<Sprite> sprites;
    /**
     * Cria uma nova tela de <em>splash</em>.
     *
     * @param game O jogo dono desta tela.
     */
    public SplashScreen(Game game, BaseScreen previous) {
        super(game, previous);
    }

    /**
     * Configura parâmetros iniciais da tela.
     */
    @Override
    public void show() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        timeWhenScreenShowedUp = TimeUtils.millis();
        logo = new Sprite(new Texture("images/cefet-games-logo.png"));
        logo.getTexture().setFilter(
                Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        logo.setCenter(
                super.viewport.getWorldWidth() / 2,
                super.viewport.getWorldHeight() / 2);
        transition = new TransitionEffect();
        transition.setDelta(0.01f);
        sprites = new ArrayList<Sprite>();
        sprites.add(logo);
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
        // verifica se o tempo em que se passou na tela é maior do que o máximo
        // para que possamos navegar para a próxima tela.
        if (TimeUtils.timeSinceMillis(timeWhenScreenShowedUp)
                >= Config.TIME_ON_SPLASH_SCREEN) {
            navigateToMenuScreen();
        }
    }

    /**
     * Desenha a {@link Sprite} com a logomarca.
     */
    @Override
    public void draw() {
        batch.begin();
        transition.update(batch, sprites);
        if(transition.isFinished()){
            navigateToMenuScreen();
        }
        batch.end();
    }
}
