package br.cefetmg.games.minigames.util;

/**
 * Estado de um @{link MiniGame}.
 * 
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public enum MiniGameState {
    /** Exibindo instruções. */
    INSTRUCTIONS,
    /** Durante o jogo. */
    PLAYING,
    /** Fim do minigame com vitória do jogador. */
    WON,
    /** Fim do minigame com derrota do jogador. */
    FAILED
}
