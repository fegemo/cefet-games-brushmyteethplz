package br.cefetmg.games.ranking;

import br.cefetmg.games.minigames.util.Score;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public abstract class Ranking {

    protected final ArrayList<Score> ranking;
    protected RankingObserver observer;

    public Ranking() {
        ranking = new ArrayList<Score>();
    }

    /**
     *
     * @return an arrayList of rank
     */
    public abstract List<Score> getScores();

    /**
     * Le todas as pontuacoes do db, ordena e exibe as 10 maiores. O ranking é
     * atualizado em tempo real sempre que há alguma mudança.
     */
    public abstract void readScores();

    /**
     * Escreve uma pontuação de jogador no banco de dados. Antes de escrever no
     * banco, verifica se o nickname ja existe. Se existir, substitui a
     * pontuacao se o novo valor for mais alto. Se o valor for menor que o
     * ultimo do ranking exibido, nao adiciona no banco, independente da
     * condicao anterior.
     *
     * @param nickname apelido do jogador.
     * @param gamesPlayed pontuação alcançada pelo jogador.
     */
    public abstract void writeScore(String nickname, final int gamesPlayed);

    /**
     * Define um objeto que é notificado quando a pontuação é alterada.
     *
     * @param observer
     */
    public void setObserver(RankingObserver observer) {
        this.observer = observer;
        readScores();
    }

}
