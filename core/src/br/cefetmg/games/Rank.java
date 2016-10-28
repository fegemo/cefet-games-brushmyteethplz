/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games;

import br.cefetmg.games.minigames.util.Score;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author lindley
 */
public class Rank {
    private ArrayList<Score> ranking;
    
    public Rank(){        
    }
    
    public void readRankFile() {        
        ranking = new ArrayList<Score>();
        try {
            BufferedReader getInput = new BufferedReader(new FileReader("ranking.txt"));
            String line;            
            while ((line = getInput.readLine()) != null) {
                String[] parts = line.split(" ");
                ranking.add(new Score(parts[0], Integer.parseInt(parts[1])));
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
        readRankFile();
        return this.ranking;
    }
    
     public void writeRankFile(String nickname, int gamesPlayed) {               
        readRankFile();
         if (ranking.size() < 10) {            
            ranking.add(new Score(nickname, gamesPlayed));
        } else if (ranking.get(9).getGames() < gamesPlayed) {
            ranking.remove(9);
            ranking.add(new Score(nickname, gamesPlayed));
        }
        Collections.sort(ranking);
        try {
            BufferedWriter getOutput = new BufferedWriter(new FileWriter("ranking.txt"));
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
