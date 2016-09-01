package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.minigames.util.GameStateObserver;
import java.util.Map;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public interface MiniGameFactory {

    /**
     * Retorna uma instância de um MiniGame.
     * 
     * @param screen a tela "dona" do minigame.
     * @param observer alguém que se interesse por saber da alteração de 
     * estado do jogo.
     * @param difficulty dificuldade ([0,1]).
     * @return a instância do MiniGame.
     */
    public MiniGame createMiniGame(BaseScreen screen, 
            GameStateObserver observer, float difficulty);
    
    /**
     * Retorna os recursos que devem ser pré-carregados para este MiniGame.
     * @return os recursos que devem ser pré-carregados para este MiniGame. 
     */
    public Map<String, Class> getAssetsToPreload();
}
