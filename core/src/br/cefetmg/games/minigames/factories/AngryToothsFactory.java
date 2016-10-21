/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.minigames.AngryTooths;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author higor
 */
public class AngryToothsFactory implements MiniGameFactory{

    @Override
    public MiniGame createMiniGame(BaseScreen screen, GameStateObserver observer, float difficulty) {
        return new AngryTooths(screen, observer,difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>(){
            {
              put("angry-tooths/boca.png",Texture.class);
              put("angry-tooths/background.jpg",Texture.class);
              put("angry-tooths/dente_region.png",Texture.class);
              put("angry-tooths/missile.mp3",Sound.class);
            }
        };
    }
    
}
