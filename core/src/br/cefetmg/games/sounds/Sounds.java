/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.sounds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author Biel
 */
public class Sounds {
    
    
    private final Sound sucessSound;
    private final Sound failSound;
    private final Sound gameOverSound;
    private final Sound gameWinSound;
    
     public Sounds(){
        
        this.sucessSound = Gdx.audio.newSound(Gdx.files.internal("sounds/passouMinigame.mp3"));
        this.failSound = Gdx.audio.newSound(Gdx.files.internal("sounds/perdeuVida.mp3"));
        this.gameOverSound = Gdx.audio.newSound(Gdx.files.internal("sounds/gameover.mp3"));
        this.gameWinSound = Gdx.audio.newSound(Gdx.files.internal("sounds/fim_seq.mp3"));
    }
     
     public void playSucess(){
         this.sucessSound.play();
     }
     public void playFail(){
         this.failSound.play();
     }
     public void playGameOver(){
         this.gameOverSound.play();
     }
     public void playGameWin(){
         this.gameWinSound.play();
     }
}
