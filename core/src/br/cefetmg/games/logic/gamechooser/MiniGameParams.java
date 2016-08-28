package br.cefetmg.games.logic.gamechooser;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class MiniGameParams {
    private Class miniGame;
    private Float difficulty;
    private Long maxDuration;

    public MiniGameParams(Class miniGame, Float difficulty, Long maxDuration) {
        this.miniGame = miniGame;
        this.difficulty = difficulty;
        this.maxDuration = maxDuration;
    }

    public Class getMiniGame() {
        return miniGame;
    }

    public void setMiniGame(Class miniGame) {
        this.miniGame = miniGame;
    }

    public Float getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Float difficulty) {
        this.difficulty = difficulty;
    }

    public Long getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Long maxDuration) {
        this.maxDuration = maxDuration;
    }
}
