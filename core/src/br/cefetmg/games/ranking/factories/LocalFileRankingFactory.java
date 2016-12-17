package br.cefetmg.games.ranking.factories;

import br.cefetmg.games.ranking.LocalFileRanking;
import br.cefetmg.games.ranking.Ranking;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class LocalFileRankingFactory implements RankingFactory {

    @Override
    public Ranking createRanking() {
        return new LocalFileRanking();
    }
    
}
