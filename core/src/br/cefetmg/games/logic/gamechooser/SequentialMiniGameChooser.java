package br.cefetmg.games.logic.gamechooser;

import br.cefetmg.games.minigames.ShooTheTartarus;
import java.util.Set;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class SequentialMiniGameChooser implements MiniGameChooser {

    private int numberOfPlannedGames;
    private Set<Class> availableGames;

    public SequentialMiniGameChooser(int total) {
        this.numberOfPlannedGames = total;
    }

    @Override
    public MiniGameParams next() {
        MiniGameParams params = new MiniGameParams(ShooTheTartarus.class, 0f, 10000L);
        return params;
    }

    @Override
    public int getNumberOfPlannedGames() {
        return numberOfPlannedGames;
    }

    @Override
    public void setAvailableGames(Set<Class> availableGames) {
        this.availableGames = availableGames;
    }

}
