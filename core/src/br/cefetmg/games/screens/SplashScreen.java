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
    private static final int NUMBER_IMAGES=87;
    private long timeWhenScreenShowedUp;
    private int frame;
    private float tempoDeEspera;
    /**
     * Uma {@link Sprite} que contém a logo da empresa CEFET-GAMES.
     */
    private Sprite logo;
     private Sound audio;
    private Texture[] splashTextures;

    /**
     * Cria uma nova tela de <em>splash</em>.
     *
     * @param game O jogo dono desta tela.
     */
    public SplashScreen(Game game, BaseScreen previous) {
        super(game, previous);
        
        audio = Gdx.audio.newSound(Gdx.files.internal("sounds/splash.mp3"));
        frame=0;
        tempoDeEspera=0;
        splashTextures=new Texture[NUMBER_IMAGES];
        for(int i=0;i<NUMBER_IMAGES;i++){
            String name="images/splash/video ".concat(String.valueOf(i+1).concat(".jpg"));
            splashTextures[i]= new Texture(name);
        }
        
    }

    /**
     * Configura parâmetros iniciais da tela.
     */
    @Override
    public void show() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        timeWhenScreenShowedUp = TimeUtils.millis();
        logo = new Sprite(splashTextures[0]);
        logo.setScale(1.38f);
        logo.setCenter(
                    super.viewport.getWorldWidth() / 2,
                    super.viewport.getWorldHeight() / 2);
        audio.play();
        
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
        tempoDeEspera+=dt;
        if(tempoDeEspera>0.036){
            if(frame<NUMBER_IMAGES){
                logo = new Sprite(splashTextures[frame]);
                logo.setScale(1.38f);
                frame+=1;
                logo.setCenter(
                    super.viewport.getWorldWidth() / 2,
                    super.viewport.getWorldHeight() / 2);
                tempoDeEspera=0;
            }
        }
        if (TimeUtils.timeSinceMillis(timeWhenScreenShowedUp)
                >= Config.TIME_ON_SPLASH_SCREEN) {
            audio.stop();
            navigateToMenuScreen();
            
        }
    }

    /**
     * Desenha a {@link Sprite} com a logomarca.
     */
    @Override
    public void draw() {
        batch.begin();
        logo.draw(batch);
        batch.end();
  
    }
}