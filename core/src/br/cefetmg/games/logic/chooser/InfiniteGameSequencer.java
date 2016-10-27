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
import com.badlogic.gdx.math.MathUtils;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;
import br.cefetmg.games.minigames.util.DifficultyCurve;

/**
 * @author lindley
 */
public class InfiniteGameSequencer extends BaseGameSequencer {

    private int gamesPlayed;    

    public InfiniteGameSequencer(Set<MiniGameFactory> availableGames, BaseScreen screen,
            GameStateObserver observer) {
        super(availableGames, screen, observer);
        if (availableGames.isEmpty()) {
            throw new IllegalArgumentException("Tentou-se criar um modo survival"
                    + " com 0 jogos. Deve haver ao menos 1.");
        }
        this.gamesPlayed = 0;
        //readRankFile();
        preloadAssets();
    }

    /**
     * Pré-carrega os <em>assets</em> dos minigames que foram selecionados.
     */
    private void preloadAssets() {
        HashMap<String, Class> allAssets = new HashMap<String, Class>();

        for (int i = 0; i < availableGames.size(); i++) {
            allAssets.putAll(((MiniGameFactory) availableGames.toArray()[i])
                    .getAssetsToPreload());
        }
        for (Entry<String, Class> asset : allAssets.entrySet()) {
            screen.assets.load(asset.getKey(), asset.getValue());
        }
    }

    /**
     * Retorna uma instância do próximo jogo.
     *
     * @return uma instância do próximo jogo.
     */
    @Override
    public MiniGame nextGame() {
        MiniGameFactory factory = (MiniGameFactory) availableGames
                .toArray()[MathUtils.random(availableGames.size() - 1)];
        gamesPlayed++;
        return factory.createMiniGame(screen, observer,
                DifficultyCurve.LINEAR.getCurveValue(getGameDifficult()));
    }

    /**
     * Retorna um número entre 0 e 1 indicando a dificuldade do jogo. Acima do
     * 30º a dificuldade é máxima.
     *
     * @return dificuldade entre 0 e 1.
     */
    private float getGameDifficult() {
        if (getGameNumber() < 30) {
            return getGameNumber() / 30.0f;
        } else {
            return 1.0f;
       }
    }

    /**
     * Retorna o índice deste jogo.
     *
     * @return o índice deste jogo.
     */
    @Override
    public int getGameNumber() {
        return gamesPlayed;
    }

    @Override
    public boolean hasNextGame() {
        return true;
    }    
}
