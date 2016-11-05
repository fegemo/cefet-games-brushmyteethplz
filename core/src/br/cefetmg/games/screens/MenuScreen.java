package br.cefetmg.games.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import br.cefetmg.games.Rank;
import br.cefetmg.games.minigames.util.ActualMenuScreen;
import br.cefetmg.games.minigames.util.GameOption;
import br.cefetmg.games.minigames.util.Score;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.plaf.basic.BasicMenuUI.ChangeHandler;

/**
 * Uma tela de Menu Principal do jogo.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class MenuScreen extends BaseScreen {

    private final Music menuMusic;
    private ActualMenuScreen actualScreen;
    private final Rank rank;
    private Texture background, backgroundRanking;
    private TextureRegion buttonIniciarTexture, buttonCreditosTexture,
            buttonSairTexture, buttonSurvivalTexture, buttonNormalTexture,
            buttonRankingTexture, buttonVoltarTexture;
    private Stage stage, stageRanking, stageCredits;
    private Button buttonIniciar, buttonSair, buttonCreditos, buttonSurvival,
            buttonNormal, buttonRanking, buttonVoltar;
    
    /**
     * Cria uma nova tela de menu.
     *
     * @param game o jogo dono desta tela.
     * @param previous a tela anterior a esta.
     */
    public MenuScreen(Game game, BaseScreen previous) {
        super(game, previous);

        //Define a música tema
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu.mp3"));

        //Inicializa tela
        actualScreen = ActualMenuScreen.MENU;
        rank = new Rank();
    }

    /**
     * Configura parâmetros da tela e instancia objetos.
     */
    @Override
    public void appear() {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        background = new Texture("menu_background_m.jpg");
        backgroundRanking = new Texture("menu_background_r.jpg");

        stage = new Stage();
        stageRanking = new Stage();
        stageCredits = new Stage();
        
        // creates a table that fills the screen. 
        // everything else will go inside this table. 
        final Table table = new Table();
        table.align(1);
        table.padBottom(160);
        table.setFillParent(true);

        final Table tableGameMode = new Table();
        tableGameMode.align(1);
        tableGameMode.padBottom(160);
        tableGameMode.setFillParent(true);
        tableGameMode.setVisible(false);

        buttonIniciarTexture = new TextureRegion(
                new Texture("buttons_menu/button_iniciar.png"));
        buttonIniciar = new ImageButton(
                new TextureRegionDrawable(buttonIniciarTexture));
        table.add(buttonIniciar);

        buttonNormalTexture = new TextureRegion(
                new Texture("buttons_menu/Normal.png"));
        buttonNormal = new ImageButton(
                new TextureRegionDrawable(buttonNormalTexture));
        buttonNormal.pad(80);
        tableGameMode.add(buttonNormal);

        buttonSurvivalTexture = new TextureRegion(
                new Texture("buttons_menu/Survival.png"));
        buttonSurvival = new ImageButton(
                new TextureRegionDrawable(buttonSurvivalTexture));
        buttonSurvival.pad(80);
        tableGameMode.add(buttonSurvival);

        buttonRankingTexture = new TextureRegion(
                new Texture("buttons_menu/button_ranking.png"));
        buttonRanking = new ImageButton(
                new TextureRegionDrawable(buttonRankingTexture));
        table.add(buttonRanking);

        buttonCreditosTexture = new TextureRegion(
                new Texture("buttons_menu/button_creditos.png"));
        buttonCreditos = new ImageButton(
                new TextureRegionDrawable(buttonCreditosTexture));
        table.add(buttonCreditos);

        buttonSairTexture = new TextureRegion(
                new Texture("buttons_menu/button_sair.png"));
        buttonSair = new ImageButton(
                new TextureRegionDrawable(buttonSairTexture));
        table.add(buttonSair);

        buttonVoltarTexture = new TextureRegion(
                new Texture("buttons_menu/button_voltar.png"));
        buttonVoltar = new ImageButton(
                new TextureRegionDrawable(buttonVoltarTexture));
        buttonVoltar.align(2);

        buttonIniciar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                table.setVisible(false);
                tableGameMode.setVisible(true);
            }
        });

        buttonSurvival.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                menuMusic.stop();
                navigateToMicroGameScreen(GameOption.SURVIVAL);
            }
        });

        buttonNormal.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                menuMusic.stop();
                navigateToMicroGameScreen(GameOption.NORMAL);
            }
        });

        buttonRanking.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                actualScreen = ActualMenuScreen.RANKING;
                Gdx.input.setInputProcessor(stageRanking);
            }
        });

        buttonCreditos.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                actualScreen = ActualMenuScreen.CREDITS;
                Gdx.input.setInputProcessor(stageCredits);
            }
        });

        buttonSair.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        buttonVoltar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                actualScreen = ActualMenuScreen.MENU;
                Gdx.input.setInputProcessor(stage);
            }
        });

        stage.addActor(table);
        stage.addActor(tableGameMode);
        stageRanking.addActor(buttonVoltar);
        stageCredits.addActor(buttonVoltar);
        Gdx.input.setInputProcessor(stage);
        final Skin skin = new Skin(new FileHandle("ui/uiskin.json"));
        
        String Creditos = "";
        try {
            BufferedReader getInput = new BufferedReader(
                    new FileReader("creditos.txt"));
            String line;
            while ((line = getInput.readLine()) != null) {
                Creditos = Creditos.concat(line);
                Creditos = Creditos.concat("\n");
            }
            getInput.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        }
        
        BitmapFont font = new BitmapFont(new FileHandle("fonts/sawasdee-24.fnt"));
        final Label text = new Label(Creditos, skin);
        text.setAlignment(Align.center);
        text.setStyle(new LabelStyle(font, Color.BLACK));
        text.setFontScale(1F);
        text.setWrap(true);
        
        final Table scrollTable = new Table();
        scrollTable.add(text);
        scrollTable.row();

        final ScrollPane scroller = new ScrollPane(scrollTable);
        // Pesquisar como fazer scroller rolar automaticamente!
        
        final Table table2 = new Table();
        table2.setFillParent(true);
        table2.add(scroller).fill().expand();
        stageCredits.addActor(table2);
        
        menuMusic.setLooping(true);
        menuMusic.play();
    }
    
    @Override
    public void cleanUp() {
        if (stage != null) {
            stage.dispose();
        }
        if (stageRanking != null) {
            stageRanking.dispose();
        }
        if (stageCredits != null) {
            stageCredits.dispose();
        }
        if (menuMusic != null) {
            menuMusic.dispose();
        }
        Gdx.input.setInputProcessor(null);
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
        stage.act(dt);
    }

    /**
     * Desenha o conteúdo da tela de Menu.
     */
    @Override
    public void draw() {
        batch.begin();

        // desenha o menu propriamente dito ou o ranking
        switch (actualScreen) {
            case MENU:
                batch.draw(background, 0, 0,
                        viewport.getWorldWidth(),
                        viewport.getWorldHeight());

                drawCenterAlignedText("Toque/clique para jogar",
                        1f, viewport.getWorldHeight() * 0.35f);
                stage.draw();
                break;

            case RANKING:
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
                break;
            case CREDITS:
                batch.draw(backgroundRanking, 0, 0,
                        viewport.getWorldWidth(),
                        viewport.getWorldHeight());
                
                this.stageCredits.getActors().get(1).act(Gdx.graphics.getDeltaTime());
                this.stageCredits.getActors().get(1).draw(batch, 1);
                
                buttonVoltar.draw(batch, 1);
                
                break;
        }
        
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
                menuMusic.stop();
                game.setScreen(
                        new PlayingGamesScreen(game, MenuScreen.this, option));
            }
        }, 0.75f);// 750ms
    }

}
