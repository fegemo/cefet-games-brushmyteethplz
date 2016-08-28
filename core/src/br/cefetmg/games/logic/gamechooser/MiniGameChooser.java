package br.cefetmg.games.logic.gamechooser;

import java.util.Set;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public interface MiniGameChooser {
    MiniGameParams next();
    int getNumberOfPlannedGames();
    void setAvailableGames(Set<Class> availableGames);
}
