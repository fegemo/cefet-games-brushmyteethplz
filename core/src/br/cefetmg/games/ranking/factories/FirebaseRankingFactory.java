package br.cefetmg.games.ranking.factories;

import br.cefetmg.games.ranking.FirebaseRanking;
import br.cefetmg.games.ranking.Ranking;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class FirebaseRankingFactory implements RankingFactory {

    @Override
    public Ranking createRanking() {
        return new FirebaseRanking();
    }
        
}
