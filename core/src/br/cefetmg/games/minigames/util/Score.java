/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.util;

/**
 *
 * @author Lucas
 * Classe que define um socre do ranking
 */
public class Score implements Comparable<Score> {
    private final String name;
    private final int games;
    
    public Score(String n, int g) {
        name = n;
        games = g;
    }
    
    public String getName() {
        return name;
    }
    
    public int getGames() {
        return games;
    }

    @Override
    public int compareTo(Score s) {
        return s.games - games;
    }
}