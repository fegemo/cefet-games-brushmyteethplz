package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.minigames.util.GameStateObserver;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public interface MiniGameFactory {

    public MiniGame createMiniGame(BaseScreen screen, 
            GameStateObserver observer, float difficulty);
}
