/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.SideWalking;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lucas
 */
public class SideWalkingFactory implements MiniGameFactory {

     @Override
    public MiniGame createMiniGame(BaseScreen screen, GameStateObserver observer, float difficulty) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new SideWalking(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new HashMap<String, Class>() {
            {
                put("side_walking/monster.png", Texture.class);
                put("side_walking/nuvem.png", Texture.class);     
                put("side_walking/walking_tooth.png", Texture.class); 
                put("side_walking/backSound.mp3", Sound.class);
                put("side_walking/jump.mp3", Sound.class);  
                put("side_walking/candy.png", Texture.class);
            }
        };      
    }
}
