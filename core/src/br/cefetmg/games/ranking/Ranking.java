package br.cefetmg.games.ranking;

import br.cefetmg.games.Config;
import br.cefetmg.games.minigames.util.Score;
import java.util.ArrayList;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import java.util.Iterator;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Lindley, Carlos, Bruno
 */
public class Ranking {

    private final ArrayList<Score> ranking;
    private final Firebase firebase;
    private RankingObserver observer;
    
    public Ranking() {
        ranking = new ArrayList<Score>();
        firebase = new Firebase(Config.RANKING_DATABASE_ENDPOINT);
        readRankDB();
    }
    
    public void setObserver(RankingObserver observer) {
        this.observer = observer;
    }

    /**
     * Le todas as pontuacoes do db, ordena e exibe as 10 maiores. O ranking é
     * atualizado em tempo real sempre que há alguma mudança.
     */
    public final void readRankDB() {
        ranking.clear();
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = iter.iterator();
                while (iterator.hasNext()) {
                    DataSnapshot data = iterator.next();
                    Score score = new Score(data.getKey(),
                            data.getValue(Integer.class));
                    ranking.add(score);
                }
                ranking.sort(new Comparator<Score>() {
                    @Override
                    public int compare(Score s1, Score s2) {
                        if (s1.getGames() < s2.getGames()) {
                            return 1;
                        } else if (s1.getGames() == s2.getGames()) {
                            return 0;
                        } else {
                            return -1;
                        }
                    }
                });

                if (observer != null) {
                    observer.onRankingChanged(ranking);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + 
                        firebaseError.getCode());
            }
        });
    }

    /**
     *
     * @return an arrayList of rank
     */
    public List<Score> getRanking() {

        // Ranking pode ter mais de 10 scores devido a escritas simultaneas. 
        // Caso isso ocorra,
        // retorna só os 10 primeiros valores.
        if (ranking.size() > 10) {
            return this.ranking.subList(0, 9);
        } else {
            return this.ranking;
        }
    }

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
    public void writeScoreDB(String nickname, final int gamesPlayed) {
        System.out.println(ranking.size());
        if (ranking.size() >= 10) {
            // Se nova pontuacao for menor que a decima do ranking, nao faz nada.
            if (gamesPlayed <= ranking.get(ranking.size() - 1).getGames()) {
                return;
            } else {
                // Remove menor valor (pode remover mais de um valor, 
                // caso o ranking esteja com tamanho maior que 10
                // devido a escritas simultaneas).
                while (ranking.size() > 9) {
                    Firebase ref = firebase.child(
                            ranking.get(ranking.size() - 1).getName());
                    ref.removeValue();
                    ranking.remove(ranking.size() - 1);
                }
            }
        }
        final Firebase ref = firebase.child(nickname);
        // O listener abaixo le a pontuacao atual do nickname para decidir se
        // ela sera substituida. Se o nickname ainda nao existe no ranking, ele
        // é adicionado.
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    ref.setValue(gamesPlayed);
                } else {
                    int current_value = dataSnapshot.getValue(Integer.class);
                    if (current_value < gamesPlayed) {
                        ref.setValue(gamesPlayed);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: "
                        + firebaseError.getCode());
            }
        });
    }
}
