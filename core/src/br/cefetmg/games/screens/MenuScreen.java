package br.cefetmg.games.screens;

import br.cefetmg.games.Config;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import br.cefetmg.games.ranking.Ranking;
import br.cefetmg.games.ranking.RankingObserver;
import br.cefetmg.games.minigames.util.MenuState;
import br.cefetmg.games.minigames.util.GameMode;
import br.cefetmg.games.minigames.util.Score;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Uma tela de Menu Principal do jogo.
 *
 * @author Flávio Coutinho - fegemo <coutinho@decom.cefetmg.br>
 */
public class MenuScreen extends BaseScreen implements RankingObserver {

    private final Music menuMusic;
    private MenuState menuState;
    private Ranking ranking;
    private Texture background3Teeth, background2Teeth;
    private TextureRegion buttonIniciarTexture, buttonCreditosTexture,
            buttonSairTexture, buttonSurvivalTexture, buttonNormalTexture,
            buttonRankingTexture, buttonVoltarTexture;
    private Stage stage, stageRanking, stageCredits;
    private Button buttonIniciar, buttonSair, buttonCreditos, buttonSurvival,
            buttonNormal, buttonRanking, buttonVoltar;
    private Skin skin;
    private Label rankingLabel;
    private BitmapFont monoFont, titleFont;
    private ChangeListener backToMenuListener;

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
    }

    /**
     * Configura parâmetros da tela e instancia objetos.
     */
    @Override
    public void appear() {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        background3Teeth = new Texture("menu_background_m.jpg");
        background2Teeth = new Texture("menu_background_r.jpg");

        stage = new Stage(viewport);
        stageRanking = new Stage(viewport);
        stageCredits = new Stage(viewport);

        skin = new Skin(new FileHandle("ui/uiskin.json"));
        monoFont = new BitmapFont(new FileHandle("fonts/ubuntu-mono.fnt"));
        titleFont = new BitmapFont(new FileHandle("fonts/sawasdee-50.fnt"));

        initMainMenu();
        initRanking();
        initCredits();
        
        menuMusic.setLooping(true);
        menuMusic.play();

        changeMenuState(MenuState.MENU);

        ranking = new Ranking();
        ranking.setObserver(this);
    }
    
    private void initMainMenu() {
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
        tableGameMode.add(buttonNormal).spaceRight(160);

        buttonSurvivalTexture = new TextureRegion(
                new Texture("buttons_menu/Survival.png"));
        buttonSurvival = new ImageButton(
                new TextureRegionDrawable(buttonSurvivalTexture));
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
        buttonVoltar.setVisible(false);
        
        buttonIniciar.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                table.setVisible(false);
                tableGameMode.setVisible(true);
                buttonVoltar.setVisible(true);
            }
        });

        buttonSurvival.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                menuMusic.stop();
                navigateToMicroGameScreen(GameMode.SURVIVAL);
            }
        });

        buttonNormal.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                menuMusic.stop();
                navigateToMicroGameScreen(GameMode.CAMPAIGN);
            }
        });

        buttonRanking.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                changeMenuState(MenuState.RANKING);
            }
        });

        buttonCreditos.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                changeMenuState(MenuState.CREDITS);
            }
        });

        buttonSair.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        backToMenuListener = new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                changeMenuState(MenuState.MENU);
                table.setVisible(true);
                tableGameMode.setVisible(false);
                buttonVoltar.setVisible(false);
            }
        };
        
        buttonVoltar.addListener(backToMenuListener);

        Image backgroundImage3Teeth = new Image(background3Teeth);
        backgroundImage3Teeth.setFillParent(true);

        stage.addActor(backgroundImage3Teeth);
        stage.addActor(table);
        stage.addActor(tableGameMode);
        stage.addActor(buttonVoltar);
    }
    
    private void initRanking() {
        rankingLabel = new Label(Config.RANKING_WAITING_FOR, skin);
        rankingLabel.setAlignment(Align.center);
        rankingLabel.setStyle(new LabelStyle(monoFont, Color.BLACK));
        rankingLabel.setFontScale(1);
        rankingLabel.setWrap(true);

        final ImageButton backFromRankingButton = new ImageButton(
                new TextureRegionDrawable(buttonVoltarTexture));
        backFromRankingButton.align(2).addListener(backToMenuListener);

        final Label titleLabel = new Label("Ranking", skin);
        titleLabel.setStyle(new LabelStyle(titleFont, Color.BLACK));

        final Table tableRanking = new Table();
        tableRanking.setFillParent(true);
        tableRanking
                .padLeft(buttonVoltar.getWidth())
                .padRight(buttonVoltar.getWidth())
                .add(titleLabel)
                .row();
        tableRanking
                .add(rankingLabel)
                .fill()
                .expand();

        Image backgroundImage2Teeth = new Image(background2Teeth);
        backgroundImage2Teeth.setFillParent(true);

        stageRanking.addActor(backgroundImage2Teeth);
        stageRanking.addActor(tableRanking);
        stageRanking.addActor(backFromRankingButton);
    }
    
    private void initCredits() {
        String creditsText = getCreditsText();

        final ImageButton backFromCreditsButton = new ImageButton(
                new TextureRegionDrawable(buttonVoltarTexture));
        backFromCreditsButton.align(2).addListener(backToMenuListener);

        final Label creditsLabel = new Label(creditsText, skin);
        creditsLabel.setAlignment(Align.center);
        creditsLabel.setStyle(new LabelStyle(monoFont, Color.BLACK));
        creditsLabel.setFontScale(1F);
        creditsLabel.setWrap(true);
        
        final Label titleLabel = new Label("Créditos", skin);
        titleLabel.setStyle(new LabelStyle(titleFont, Color.BLACK));

        final ScrollPane scroller = new ScrollPane(creditsLabel);

        final Table tableCredits = new Table();
        tableCredits.setFillParent(true);
        tableCredits
                .padLeft(buttonVoltar.getWidth())
                .padRight(buttonVoltar.getWidth())
                .add(titleLabel)
                .row();
        tableCredits.add(scroller)
                .fill()
                .expand();

        Image backgroundImage2Teeth = new Image(background2Teeth);
        backgroundImage2Teeth.setFillParent(true);

        stageCredits.addActor(backgroundImage2Teeth);
        stageCredits.addActor(tableCredits);
        stageCredits.addActor(backFromCreditsButton);
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
        stageCredits.act(dt);
    }

    /**
     * Desenha o conteúdo da tela de Menu.
     */
    @Override
    public void draw() {
        batch.begin();

        // desenha o menu propriamente dito ou o ranking
        switch (menuState) {
            case MENU:
                stage.draw();
                break;

            case RANKING:
                stageRanking.draw();
                break;

            case CREDITS:
                stageCredits.draw();
                break;
        }

        batch.end();
    }

    public void changeMenuState(final MenuState newMenuState) {
        switch (newMenuState) {
            case MENU:
                Gdx.input.setInputProcessor(stage);
                break;

            case RANKING:
                Gdx.input.setInputProcessor(stageRanking);
                break;

            case CREDITS:
                Gdx.input.setInputProcessor(stageCredits);
                break;
        }
        this.menuState = newMenuState;
    }

    /**
     * Navega para a tela de jogo.
     */
    private void navigateToMicroGameScreen(final GameMode option) {
        transitionState = states.fadeOut;
        Timer.schedule(new Task() {
            @Override
            public void run() {
                transitionState = states.doNothing;
                menuMusic.stop();
                if (option == GameMode.CAMPAIGN){
                    game.setScreen(
                        new Overworld(game, MenuScreen.this));
                } else {
                    game.setScreen(
                        new PlayingGamesScreen(game, MenuScreen.this, option, null));
                }
            }
        }, 0.75f);// 750ms
    }
    
    private String getCreditsText() {
        StringBuilder creditsText = new StringBuilder();
        BufferedReader fileReader = null;
        try {
            fileReader = new BufferedReader(
                    new FileReader(Config.CREDITS_FILE_NAME));
            String line;
            while ((line = fileReader.readLine()) != null) {
                creditsText = creditsText.append(line).append("\n");
            }
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
            creditsText = new StringBuilder(Config.CREDITS_DEFAULT_MESSAGE);
        } finally {
            try {
                if (fileReader != null && fileReader.ready()) {
                    fileReader.close();
                }
            } catch (IOException ex) {
                creditsText = new StringBuilder(Config.CREDITS_DEFAULT_MESSAGE);
            }
        }
        return creditsText.toString();
    }

    @Override
    public void onRankingChanged(List<Score> ranking) {
        StringBuilder rankingText = new StringBuilder();
        
        for (Score score : ranking) {
            rankingText
                    .append(score.getName())
                    .append(" .......... ")
                    .append(score.getGames())
                    .append("\n");
        }
        
        rankingLabel.setText(rankingText);
    }
}
