/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.Gallows;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lindley
 */
public class GallowsFactory implements MiniGameFactory{

    @Override
    public MiniGame createMiniGame(BaseScreen screen, GameStateObserver observer, float difficulty) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new Gallows(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return new HashMap<String, Class>() {
            {
                put("Gallows/mousePointer.png", Texture.class);
                put("Gallows/spriteTooth.png", Texture.class);
                put("Gallows/traco.png", Texture.class);
                put("Gallows/tracoBranco.png", Texture.class);
                put("Gallows/letras.png", Texture.class);
                put("Gallows/denteQuebrando.mp3", Sound.class);
                put("Gallows/sucesso.mp3", Sound.class);                
            }
        };      
    }
    
}
