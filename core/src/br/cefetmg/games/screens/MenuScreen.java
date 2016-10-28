package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import br.cefetmg.games.Rank;
import br.cefetmg.games.minigames.util.ActualMenuScreen;
import br.cefetmg.games.minigames.util.GameOption;
import br.cefetmg.games.minigames.util.Score;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/**
 * Uma tela de Menu Principal do jogo.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class MenuScreen extends BaseScreen {

    private static final int NUMBER_OF_TILED_BACKGROUND_TEXTURE = 7;
    private TextureRegion background;
    private final Sound introMusic;
    
    //***Alterações para o modo survival by Lindley and Lucas Viana
    private final Sprite normalButton, survivalButton, rankingButton;
    private ActualMenuScreen actualScreen;
    private Rank rank;
    //***Fim do bloco de alterações

    /**
     * Cria uma nova tela de menu.
     *
     * @param game o jogo dono desta tela.
     * @param previous a tela anterior a esta.
     */
    public MenuScreen(Game game, BaseScreen previous) {
        super(game, previous);
        
        //Define a música tema
        introMusic = Gdx.audio.newSound(Gdx.files.internal("sounds/menu.mp3"));

         //***Alterações para o modo survival by Lindley and Lucas Viana
        //Carrega texturas para os botões do menu
        normalButton = new Sprite(new Texture("buttons_menu/Normal.png"));
        survivalButton = new Sprite(new Texture("buttons_menu/Survival.png"));
        rankingButton = new Sprite(new Texture("buttons_menu/Ranking.png"));

        //Define as posições dos botões
        normalButton.setPosition(40 * viewport.getWorldWidth() / 200.0f, viewport.getWorldHeight() / 2.5f);
        survivalButton.setPosition(25 + 80 * viewport.getWorldWidth() / 200.0f, viewport.getWorldHeight() / 2.5f);
        rankingButton.setPosition(50 + 120 * viewport.getWorldWidth() / 200.0f, viewport.getWorldHeight() / 2.5f);

        //Inicializa tela
        actualScreen = ActualMenuScreen.MENU;
        
        rank = new Rank();
        //***Fim do bloco de alterações
    }

    /**
     * Configura parâmetros da tela e instancia objetos.
     */
    @Override
    public void show() {
        Gdx.gl.glClearColor(1, 1, 1, 1);

        introMusic.play();
        // instancia a textura e a região de textura (usada para repetir)
        background = new TextureRegion(new Texture("menu-background.png"));
        // configura a textura para repetir caso ela ocupe menos espaço que o
        // espaço disponível
        background.getTexture().setWrap(
                Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        // define a largura da região de desenho de forma que ela seja repetida
        // um número de vezes igual a NUMBER_OF_TILED_BACKGROUND_TEXTURE 
        background.setRegionWidth(
                background.getTexture().getWidth()
                * NUMBER_OF_TILED_BACKGROUND_TEXTURE);
        // idem para altura, porém será repetida um número de vezes igual a 
        // NUMBER_OF_TILED_BACKGROUND_TEXTURE * razãoDeAspecto
        background.setRegionHeight(
                (int) (background.getTexture().getHeight()
                * NUMBER_OF_TILED_BACKGROUND_TEXTURE
                / Config.DESIRED_ASPECT_RATIO));
    }

    /**
     * Recebe <em>input</em> do jogador.
     */
    @Override
    public void handleInput() {
        /**
         * Tratamento do click do mouse :: Lindley e Lucas Viana
         */
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);        
        
        // se qualquer interação é feita (teclado, mouse pressionado, tecla
        // tocada), navega para a próxima tela (de jogo)
        if (Gdx.input.justTouched()) {
            if (actualScreen == ActualMenuScreen.MENU) {
                if (normalButton.getBoundingRectangle().contains(click)) {
                    introMusic.stop();
                    navigateToMicroGameScreen(GameOption.NORMAL);
                } else if (survivalButton.getBoundingRectangle().contains(click)) {
                    introMusic.stop();
                    navigateToMicroGameScreen(GameOption.SURVIVAL);
                } else if (rankingButton.getBoundingRectangle().contains(click)) {
                    actualScreen = ActualMenuScreen.RANKING;
                }
            } else {
                actualScreen = ActualMenuScreen.MENU;
            }
        }
    }

    /**
     * Atualiza a lógica da tela.
     *
     * @param dt Tempo desde a última atualização.
     */
    @Override
    public void update(float dt) {
        float speed = dt * 0.25f;
        background.scroll(speed, -speed);
        switch (transitionState) {
            case fadeIn:
            case fadeOut:
                transition.update(dt);
                break;
        }
    }

    /**
     * Desenha o conteúdo da tela de Menu.
     */
    @Override
    public void draw() {
        batch.begin();
        batch.draw(background, 0, 0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight());
        //***Alterações para o modo survival by Lindley and Lucas Viana
        //Desenhar ranking ou opcaos de jogo
        if (actualScreen == ActualMenuScreen.MENU) {
            drawCenterAlignedText("Selecione o modo de jogo",
                    1f, viewport.getWorldHeight() * 0.35f);

            //Desenha botões;
            normalButton.draw(this.batch);
            survivalButton.draw(this.batch);
            rankingButton.draw(this.batch);

        } else {
            ArrayList<Score> ranking = rank.getRanking();
            for (int i = 0; i < ranking.size(); ++i) {
                drawCenterAlignedText(ranking.get(i).getName()
                        + " .......... " + ranking.get(i).getGames(),
                        1.0f, viewport.getWorldHeight() - 50f * (i + 1));
            }
        }
        /**/
        batch.end();
    }

    /**
     * Navega para a tela de jogo.
     */
    private void navigateToMicroGameScreen(GameOption option) {
        game.setScreen(new PlayingGamesScreen(game, this, option));
    }

}