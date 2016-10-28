package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import br.cefetmg.games.minigames.util.GameOption;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
/**
 * Uma tela de Menu Principal do jogo.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class MenuScreen extends BaseScreen {

    private TextureRegion background,buttonIniciarTexture,buttonCreditosTexture,buttonSairTexture;
    private Stage stage;
    private Button buttonIniciar, buttonSair, buttonCreditos;

    /**
     * Cria uma nova tela de menu.
     *
     * @param game o jogo dono desta tela.
     */
    public MenuScreen(Game game, BaseScreen previous) {
        super(game, previous);
    }
    
    /**
     * Configura parâmetros da tela e instancia objetos.
     */
    
    @Override
    public void show() {
        Gdx.gl.glClearColor(1, 1, 1, 1);

        background = new TextureRegion(new Texture("menu_background.png"));
  
        stage = new Stage();
        

	// Create a table that fills the screen. Everything else will go inside this table.
	Table table = new Table();
        table.align(1);
        table.padBottom(100);
	table.setFillParent(true);
        

        buttonIniciarTexture = new TextureRegion(new Texture("buttons_menu/button_iniciar.png"));
        buttonIniciar = new ImageButton(new TextureRegionDrawable(buttonIniciarTexture));
        table.add(buttonIniciar);
//        table.row();
        
        
        buttonCreditosTexture = new TextureRegion(new Texture("buttons_menu/button_creditos.png"));
        buttonCreditos = new ImageButton(new TextureRegionDrawable(buttonCreditosTexture));
        table.add(buttonCreditos);
//        table.row();
        
        
        buttonSairTexture = new TextureRegion(new Texture("buttons_menu/button_sair.png"));
        buttonSair = new ImageButton(new TextureRegionDrawable(buttonSairTexture));
        table.add(buttonSair);
        

	buttonIniciar.addListener(new ChangeListener() {
		public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                    navigateToMicroGameScreen(GameOption.NORMAL);
		}
	});
        
        
        
        buttonCreditos.addListener(new ChangeListener() {
		public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                    //TODO
		}
	});
        
        buttonSair.addListener(new ChangeListener() {
		public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                    Gdx.app.exit();
		}
	});
        
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Recebe <em>input</em> do jogador.
     */
    @Override
    public void handleInput() {
        // se qualquer interação é feita (teclado, mouse pressionado, tecla
        // tocada), navega para a próxima tela (de jogo)
    }

    /**
     * Atualiza a lógica da tela.
     *
     * @param dt Tempo desde a última atualização.
     */
    @Override
    public void update(float dt) {
    }

    /**
     * Desenha o conteúdo da tela de Menu.
     */
    @Override
    public void draw() {
        batch.begin();
        batch.draw(background,0,0,viewport.getWorldWidth(),
                viewport.getWorldHeight());
        drawCenterAlignedText("Pressione qualquer tecla para jogar",
                1f, viewport.getWorldHeight() * 0.35f);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
	stage.draw();
        batch.end();
    }

    /**
     * Navega para a tela de jogo.
     */
    private void navigateToMicroGameScreen(GameOption option) {
        game.setScreen(new PlayingGamesScreen(game, this, option));
    }


}