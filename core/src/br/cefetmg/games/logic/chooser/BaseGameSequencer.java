/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.logic.chooser;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.factories.MiniGameFactory;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import java.util.Set;

/**
 *
 * @author lindley
 */
public abstract class BaseGameSequencer {
    protected final Set<MiniGameFactory> availableGames;
    protected final BaseScreen screen;
    protected final GameStateObserver observer;
    
    public BaseGameSequencer(Set<MiniGameFactory> availableGames, BaseScreen screen,
            GameStateObserver observer) {
        this.availableGames = availableGames;
        this.screen = screen;
        this.observer = observer;
    }
    
    public abstract boolean hasNextGame();
    public abstract MiniGame nextGame();
    public abstract int getGameNumber();    
}
