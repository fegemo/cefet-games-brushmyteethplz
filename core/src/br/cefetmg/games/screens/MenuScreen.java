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
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.ArrayList;

//imports Carlos e Bruno

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
    
    //****linhas apagadas by Bruno e Carlos
    //private static final int NUMBER_OF_TILED_BACKGROUND_TEXTURE = 7;
    //private TextureRegion background;
    //****fim
    
    private final Sound introMusic;
    
    //****linhas apagadas by Bruno e Carlos
    //***Alterações para o modo survival by Lindley and Lucas Viana
    //private final Sprite normalButton, survivalButton, rankingButton;
    //****fim
    private ActualMenuScreen actualScreen;
    private final Rank rank;
    //***Fim do bloco de alterações
    
    //****Textures e stage by Bruno e Carlos
    private TextureRegion background,backgroundRanking,buttonIniciarTexture,buttonCreditosTexture,buttonSairTexture,
                buttonSurvivalTexture,buttonNormalTexture,buttonRankingTexture,buttonVoltarTexture; 
    private Stage stage, stageRanking;
    private Button buttonIniciar, buttonSair, buttonCreditos, buttonSurvival, buttonNormal,buttonRanking,buttonVoltar;
    //****fim
    
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
        
        //****linhas apagadas by Bruno e Carlos
        //***Alterações para o modo survival by Lindley and Lucas Viana
        //Carrega texturas para os botões do menu
        //normalButton = new Sprite(new Texture("buttons_menu/Normal.png"));
        //survivalButton = new Sprite(new Texture("buttons_menu/Survival.png"));
        //rankingButton = new Sprite(new Texture("buttons_menu/Ranking.png"));

        //Define as posições dos botões
        //normalButton.setPosition(40 * viewport.getWorldWidth() / 200.0f, viewport.getWorldHeight() / 2.5f);
        //survivalButton.setPosition(25 + 80 * viewport.getWorldWidth() / 200.0f, viewport.getWorldHeight() / 2.5f);
        //rankingButton.setPosition(50 + 120 * viewport.getWorldWidth() / 200.0f, viewport.getWorldHeight() / 2.5f);
        //****fim
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
        
        background = new TextureRegion(new Texture("menu_background_m.jpg"));
        backgroundRanking = new TextureRegion(new Texture("menu_background_r.jpg"));
        
        //****Instanciando Menu by Bruno e Carlos
        stage = new Stage(); 
        stageRanking = new Stage();
        
	// Create a table that fills the screen. Everything else will go inside this table. 
        final Table table = new Table(); 
        table.align(1); 
        table.padBottom(160); 
        table.setFillParent(true); 
        
        final Table tableGameMode = new Table();
        tableGameMode.align(1); 
        tableGameMode.padBottom(160); 
        tableGameMode.setFillParent(true);
        tableGameMode.setVisible(false);

        buttonIniciarTexture = new TextureRegion(new Texture("buttons_menu/button_iniciar.png")); 
        buttonIniciar = new ImageButton(new TextureRegionDrawable(buttonIniciarTexture)); 
        table.add(buttonIniciar);
        
        buttonNormalTexture = new TextureRegion(new Texture("buttons_menu/Normal.png")); 
        buttonNormal = new ImageButton(new TextureRegionDrawable(buttonNormalTexture));
        buttonNormal.pad(80);
        tableGameMode.add(buttonNormal);
        
        buttonSurvivalTexture = new TextureRegion(new Texture("buttons_menu/Survival.png")); 
        buttonSurvival = new ImageButton(new TextureRegionDrawable(buttonSurvivalTexture));
        buttonSurvival.pad(80);
        tableGameMode.add(buttonSurvival);
        
        buttonRankingTexture = new TextureRegion(new Texture("buttons_menu/button_ranking.png")); 
        buttonRanking = new ImageButton(new TextureRegionDrawable(buttonRankingTexture)); 
        table.add(buttonRanking);

        buttonCreditosTexture = new TextureRegion(new Texture("buttons_menu/button_creditos.png")); 
        buttonCreditos = new ImageButton(new TextureRegionDrawable(buttonCreditosTexture)); 
        table.add(buttonCreditos);  

        buttonSairTexture = new TextureRegion(new Texture("buttons_menu/button_sair.png")); 
        buttonSair = new ImageButton(new TextureRegionDrawable(buttonSairTexture)); 
        table.add(buttonSair);
        
        buttonVoltarTexture = new TextureRegion(new Texture("buttons_menu/button_voltar.png")); 
        buttonVoltar = new ImageButton(new TextureRegionDrawable(buttonVoltarTexture));
        buttonVoltar.align(2);
        
        
        buttonIniciar.addListener(new ChangeListener() { 
            public void changed (ChangeListener.ChangeEvent event, Actor actor) { 
                table.setVisible(false);
                tableGameMode.setVisible(true);
            } 
        }); 
        
        buttonSurvival.addListener(new ChangeListener() { 
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                introMusic.stop();
                navigateToMicroGameScreen(GameOption.SURVIVAL); 
            } 
        });
        
        buttonNormal.addListener(new ChangeListener() { 
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                introMusic.stop();
                navigateToMicroGameScreen(GameOption.NORMAL);
            } 
        });
        
        buttonRanking.addListener(new ChangeListener() { 
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                actualScreen = ActualMenuScreen.RANKING;
                Gdx.input.setInputProcessor(stageRanking);
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
        
        buttonVoltar.addListener(new ChangeListener() { 
            public void changed (ChangeListener.ChangeEvent event, Actor actor) { 
                actualScreen = ActualMenuScreen.MENU;
                Gdx.input.setInputProcessor(stage);
            } 
        }); 

        stage.addActor(table); 
        stage.addActor(tableGameMode);
        stageRanking.addActor(buttonVoltar);
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Recebe <em>input</em> do jogador.
     */
    @Override
    public void handleInput() {
        
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

        //***Alterações para o modo survival by Lindley and Lucas Viana
        //Desenhar ranking ou opcaos de jogo
        if (actualScreen == ActualMenuScreen.MENU) {
            /****linhas apagadas por Bruno e Carlos
            drawCenterAlignedText("Selecione o modo de jogo",
                    1f, viewport.getWorldHeight() * 0.35f);

            //Desenha botões;
            normalButton.draw(this.batch);
            survivalButton.draw(this.batch);
            rankingButton.draw(this.batch);
            fim****/
            batch.draw(background, 0, 0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight());
            
            drawCenterAlignedText("Pressione qualquer tecla para jogar", 
                1f, viewport.getWorldHeight() * 0.35f); 
            stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); 
            stage.draw(); 
            
        } else {
            batch.draw(backgroundRanking, 0, 0,
                viewport.getWorldWidth(),
                viewport.getWorldHeight());
            
            ArrayList<Score> ranking = rank.getRanking();
            for (int i = 0; i < ranking.size(); ++i) {
                drawCenterAlignedText(ranking.get(i).getName()
                        + " .......... " + ranking.get(i).getGames(),
                        1.0f, viewport.getWorldHeight() - 50f * (i + 1));
            }
            buttonVoltar.draw(batch, 1);
             
        }
        /**/
        
        batch.end();
    }

    /**
     * Navega para a tela de jogo.
     */
    private void navigateToMicroGameScreen(final GameOption option) {
        transitionState = states.fadeOut;
        Timer.schedule(new Task() {
            @Override
            public void run() {
                transitionState = states.doNothing;
                game.setScreen(
                        new PlayingGamesScreen(game, MenuScreen.this, option));
            }
        }, 0.75f);// 750ms
    }
}