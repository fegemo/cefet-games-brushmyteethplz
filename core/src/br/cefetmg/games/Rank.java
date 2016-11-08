package br.cefetmg.games;

import br.cefetmg.games.minigames.util.Score;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import java.util.Iterator;

/**
 *
 * @author lindley
 */
public class Rank {

    private ArrayList<Score> ranking;
    private Firebase firebase;

    public Rank() {
        firebase = new Firebase("https://escove-meus-dentes.firebaseio.com/");
        readRankDB();
    }

    public final void readRankDB(){
        ranking = new ArrayList<Score>();
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = iter.iterator();
                while(iterator.hasNext()){
                    DataSnapshot data = iterator.next();
                    Score score = new Score(data.getKey(), data.getValue(Integer.class));
                    ranking.add(score);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getCode());
            }
        });
    }
    
    public final void readRankFile() {
        ranking = new ArrayList<Score>();
        try {
            BufferedReader getInput = new BufferedReader(
                    new FileReader("ranking.txt"));
            String line;
            while ((line = getInput.readLine()) != null) {
                String[] parts = line.split(" ");
                ranking.add(new Score(parts[0], Integer.parseInt(parts[1])));
//                String s = firebase.child("AAA").toString();
//                ranking.add(new Score(s, Integer.parseInt(parts[1])));
            }
            getInput.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        }
    }

    /**
     *
     * @return an arrayList of rank
     */
    public ArrayList<Score> getRanking() {
        return this.ranking;
    }

    public void writeRankFile(String nickname, int gamesPlayed) {
        if (ranking.size() < 10) {
            ranking.add(new Score(nickname, gamesPlayed));
        } else if (ranking.get(9).getGames() < gamesPlayed) {
            ranking.remove(9);
            ranking.add(new Score(nickname, gamesPlayed));
        }
        Collections.sort(ranking);
        try {
            BufferedWriter getOutput = new BufferedWriter(
                    new FileWriter("ranking.txt"));
            for (Score s : ranking) {
                getOutput.write(s.getName() + " " + s.getGames() + "\n");
            }
            getOutput.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        }
    }
}
