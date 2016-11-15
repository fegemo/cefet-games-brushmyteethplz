/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.Ramtooth;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author afp11
 */
public class RamtoothFactory implements MiniGameFactory {
    
    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        return new Ramtooth(screen, observer, difficulty);
    }

    /**
     * Veja {@link MiniGameFactory}.
     *
     * @return
     */
    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("ramtooth/spritedente.png", Texture.class);   
                put("ramtooth/spriterambo.png", Texture.class); 
                put("ramtooth/fundo2.png", Texture.class);
                put("ramtooth/spritetiro.png", Texture.class);
                
                put("ramtooth/dente-branco.mp3", Sound.class);
                put("ramtooth/tiro.mp3", Sound.class);
                
            }
        };
    }
    
}
