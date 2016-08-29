package br.cefetmg.games.logic.chooser;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.MiniGameFactory;
import br.cefetmg.games.minigames.util.StateChangeObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.Set;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class GameSequencer {

    private final int numberOfGames;
    private final Set<MiniGameFactory> availableGames;
    private final ArrayList<MiniGameFactory> previousGames;
    private final BaseScreen screen;
    private final StateChangeObserver observer;

    public GameSequencer(int numberOfGames,
            Set<MiniGameFactory> availableGames, BaseScreen screen,
            StateChangeObserver observer) {
        if (numberOfGames <= 0) {
            throw new IllegalArgumentException("Tentou-se criar um "
                    + "GameSequencer com 0 jogos. Deve haver ao menos 1.");
        }
        this.numberOfGames = numberOfGames;
        this.availableGames = availableGames;
        this.screen = screen;
        this.observer = observer;
        this.previousGames = new ArrayList<MiniGameFactory>();
    }

    public boolean hasNextGame() {
        return previousGames.size() < numberOfGames;
    }

    private float getProgress() {
        return ((float) previousGames.size()) / numberOfGames;
    }

    public MiniGame nextGame() {
        int gameIndex = MathUtils.random(availableGames.size() - 1);
        MiniGameFactory factory = (MiniGameFactory) availableGames
                .toArray()[gameIndex];
        
        previousGames.add(factory);
        return factory.createMiniGame(screen, observer, DifficultyCurve
                .LINEAR.getCurveValue(getProgress()));
    }

}
