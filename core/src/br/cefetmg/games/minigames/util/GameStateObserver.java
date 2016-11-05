package br.cefetmg.games.minigames.util;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public interface GameStateObserver {
    void onStateChanged(MiniGameState state);
    void onTimeEnding(long remainingTime);
}
