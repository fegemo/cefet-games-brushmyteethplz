package br.cefetmg.games.logic.chooser;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.factories.MiniGameFactory;
import br.cefetmg.games.screens.BaseScreen;
import java.util.ArrayList;
import java.util.Set;
import br.cefetmg.games.minigames.util.GameStateObserver;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Monta uma sequência de minigames a serem jogados.
 * 
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class GameSequencer extends BaseGameSequencer{

    private final int numberOfGames;
    private final Set<MiniGameFactory> availableGames;
    private final ArrayList<MiniGameFactory> previousGames;
    private final BaseScreen screen;
    private final GameStateObserver observer;
    private Integer[] indexSequence;
    private float finalDifficulty;
    private float initialDifficulty;

    /**
     * Cria um novo sequenciador com um número de minigames igual a 
     * {@code numberOfGames}, a partir de um <em>pool</em> de minigames
     * {@code availableGames}.
     * @param numberOfGames total de jogos que será criado para o jogador.
     * @param availableGames os tipos de minigames disponíveis para o 
     * sequenciador.
     * @param initialDifficulty dificuldade usada para o primeiro MiniGame.
     * Deve estar entre 0 e 1.
     * @param finalDifficulty dificuldade usada para o último MiniGames.
     * Deve estar entre 0 e 1.
     * @param screen a tela dona destes jogos.
     * @param observer um observador da mudança de estado dos jogos.
     */
    public GameSequencer(int numberOfGames, Set<MiniGameFactory> availableGames,
            float initialDifficulty, float finalDifficulty, 
            BaseScreen screen, GameStateObserver observer) {
        super(availableGames, screen, observer);
        if (numberOfGames <= 0) {
            throw new IllegalArgumentException("Tentou-se criar um "
                    + "GameSequencer com 0 jogos. Deve haver ao menos 1.");
        }
        this.numberOfGames = numberOfGames;
        this.availableGames = availableGames;
        this.initialDifficulty = initialDifficulty;
        this.finalDifficulty = finalDifficulty;
        this.screen = screen;
        this.observer = observer;
        previousGames = new ArrayList<MiniGameFactory>();
        indexSequence = new Integer[numberOfGames];
        determineGameSequence();
        preloadAssets();
    }
    
    @Override
    public boolean hasNextGame() {
        return previousGames.size() < numberOfGames;
    }

    private void determineGameSequence() {
        
        Random r = new Random();
        int game = r.nextInt(availableGames.size());
        
        for (int i = 0; i < numberOfGames; i++) {
            game = chooseGame(game);
            indexSequence[i] = game;           
        }  
    }
    
    private int chooseGame(int oldGame){
        
        Random r = new Random();
        int newGame = oldGame;
        
        if (availableGames.size() > 1){
            while (newGame == oldGame){
                newGame = r.nextInt(availableGames.size());
            }
        } else {
            newGame = r.nextInt(availableGames.size());
        }    
        
        return newGame;
    }

    private float getSequenceProgress() {
        return Math.min(1, ((float) previousGames.size()) / (numberOfGames-1));
    }

    /**
     * Pré-carrega os <em>assets</em> dos minigames que foram selecionados.
     */
    private void preloadAssets() {
        HashMap<String, Class> allAssets = new HashMap<String, Class>();
        HashSet<Integer> allFactoriesIndices = new HashSet<Integer>(
                Arrays.asList(indexSequence));
        for (Integer i : allFactoriesIndices) {
            allAssets.putAll(((MiniGameFactory) availableGames.toArray()[i])
                    .getAssetsToPreload());
        }
        for (Entry<String, Class> asset : allAssets.entrySet()) {
            screen.assets.load(asset.getKey(), asset.getValue());
        }
    }

    /**
     * Retorna uma instância do próximo jogo.
     * @return uma instância do próximo jogo.
     */
    @Override
    public MiniGame nextGame() {
                
        MiniGameFactory factory = (MiniGameFactory) availableGames
                .toArray()[indexSequence[getGameNumber()]];
        float difficulty = DifficultyCurve.S.getCurveValueBetween(
                getSequenceProgress(), initialDifficulty, finalDifficulty);
        previousGames.add(factory);
        
        return factory.createMiniGame(screen, observer, difficulty);
    }

    /**
     * Retorna o índice deste jogo na série de jogos criados para o jogador.
     * @return o índice deste jogo na série de jogos criados para o jogador.
     */
    @Override
    public int getGameNumber() {
        return previousGames.size();
    }

}