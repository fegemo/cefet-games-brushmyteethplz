package br.cefetmg.games.ranking;

import br.cefetmg.games.minigames.util.Score;
import java.util.List;

/**
 * Um objeto que é notificado sobre alterações no ranking.
 * 
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public interface RankingObserver {
    void onRankingChanged(List<Score> ranking);
}
