package br.cefetmg.games.ranking;

import br.cefetmg.games.minigames.util.Score;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class LocalFileRanking extends Ranking {

    public LocalFileRanking() {
        readScores();
    }
    
    @Override
    public List<Score> getScores() {
        return ranking;
    }

    @Override
    public final void readScores() {
        ranking.clear();
        try {
            FileHandle file = Gdx.files.internal("ranking.txt");
            String[] lines = file.readString().split("\n");
            for (String line : lines) {
                String[] parts = line.split(" ");
                ranking.add(new Score(parts[0], Integer.parseInt(parts[1])));
            }
            Collections.sort(ranking);
            if (observer != null) {
                observer.onRankingChanged(ranking);
            }
        } catch (RuntimeException ex) {
            Gdx.app.error("LocalFileRanking", ex.getMessage(), ex);
        }
    }

    @Override
    public void writeScore(String nickname, int gamesPlayed) {
        if (ranking.size() >= 10) {
            // Se nova pontuacao for menor que a decima do ranking, nao faz nada. 
            if (gamesPlayed <= ranking.get(ranking.size() - 1).getGames()) {
                return;
            } else {
                // Remove menor valor (pode remover mais de um valor, 
                // caso o ranking esteja com tamanho maior que 10 
                // devido a escritas simultaneas). 
                while (ranking.size() > 9) {
                    ranking.remove(ranking.size() - 1);
                }
            }
        }
        ranking.add(new Score(nickname, gamesPlayed));
        Collections.sort(ranking);

        FileHandle file = Gdx.files.internal("ranking.txt");
        try {
            StringBuilder b = new StringBuilder();
            for (Score s : ranking) {
                b.append(s.getName())
                        .append(" ")
                        .append(s.getGames())
                        .append("\n");
            }
            file.writeString(b.toString(), false);
        } catch (RuntimeException ex) {
            Gdx.app.error("Ranking", ex.getMessage(), ex);
        }
    }

}
