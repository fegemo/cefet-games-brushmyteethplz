package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.ShooTheTartarus;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.minigames.util.GameStateObserver;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class ShooTheTartarusFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen, 
            GameStateObserver observer, float difficulty) {
        return new ShooTheTartarus(screen, observer, difficulty);
    }
    
}
