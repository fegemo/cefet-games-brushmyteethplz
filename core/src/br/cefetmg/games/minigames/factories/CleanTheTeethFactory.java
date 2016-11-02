/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.CleanTheTeeth;
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
public class CleanTheTeethFactory implements MiniGameFactory {
    
    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        return new CleanTheTeeth(screen, observer, difficulty);
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
                put("clean-the-teeth/spritedente.png", Texture.class);   
                put("clean-the-teeth/spriterambo.png", Texture.class); 
                put("clean-the-teeth/fundo.png", Texture.class);
                put("clean-the-teeth/spritesheet.png", Texture.class);
                
            }
        };
    }
    
}
