package br.cefetmg.games.screens;

import br.cefetmg.games.logic.gamechooser.MiniGameChooser;
import br.cefetmg.games.logic.gamechooser.MiniGameParams;
import br.cefetmg.games.logic.gamechooser.SequentialMiniGameChooser;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.ShooTheTartarus;
import br.cefetmg.games.minigames.ShootTheCaries;
import br.cefetmg.games.minigames.util.MiniGameState;
import br.cefetmg.games.minigames.util.StateChangeObserver;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class PlayingGamesScreen extends BaseScreen implements StateChangeObserver {

    private MiniGame currentGame;
    private final MiniGameChooser chooser;

    public PlayingGamesScreen(Game game) {
        super(game);
        super.assets.load("fonts/sawasdee-24.fnt", BitmapFont.class);
        super.assets.load("fonts/sawasdee-50.fnt", BitmapFont.class);
        super.assets.load("fonts/sawasdee-100.fnt", BitmapFont.class);
        super.assets.load("fonts/sawasdee-150.fnt", BitmapFont.class);
        super.assets.load("images/countdown.png", Texture.class);
        super.assets.load("images/gray-mask.png", Texture.class);
        super.assets.load("shoot-the-caries/caries.png", Texture.class);
        super.assets.load("shoot-the-caries/target.png", Texture.class);
        super.assets.load("shoo-the-tartarus/toothbrush-spritesheet.png",
                Texture.class);
        super.assets.load("shoo-the-tartarus/tartarus-spritesheet.png",
                Texture.class);
        super.assets.load("shoo-the-tartarus/tooth.png", Texture.class);
        super.assets.load("debug-rectangle.png", Texture.class);

        chooser = new SequentialMiniGameChooser(10);
        Set<Class> available = new HashSet<Class>();
        available.add(ShootTheCaries.class);
        available.add(ShooTheTartarus.class);
        chooser.setAvailableGames(available);
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.input.setCursorCatched(true);
    }

    @Override
    public void handleInput() {
        if (currentGame != null) {
            currentGame.handleInput();
        }
    }

    @Override
    public void update(float dt) {
        if (super.assets.update()) {
            if (currentGame == null) {
                nextGame();
            }
            currentGame.update(dt);
        }
    }

    @Override
    public void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.batch.setProjectionMatrix(super.camera.combined);
        super.batch.begin();
        if (currentGame != null) {
            currentGame.draw();
        }
        super.batch.end();
    }

    private void nextGame() {
        MiniGameParams next = chooser.next();
        try {
            currentGame = (MiniGame) next
                    .getMiniGame()
                    .getDeclaredConstructor(BaseScreen.class, Float.class, 
                            Long.class, StateChangeObserver.class)
                    .newInstance(this, next.getDifficulty(), 
                            next.getMaxDuration(), this);
        } catch (Exception ex) {
            Logger.getLogger(PlayingGamesScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void onStateChanged(MiniGameState state) {
        switch (state) {
            case WON:
            case FAILED:
                Timer.instance().scheduleTask(new Task() {
                    @Override
                    public void run() {
                        nextGame();
                    }

                }, 1.5f);
                break;
        }
    }

}
