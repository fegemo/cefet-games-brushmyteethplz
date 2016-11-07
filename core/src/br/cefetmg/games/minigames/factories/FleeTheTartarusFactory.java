/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.FleeTheTartarus;
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
public class FleeTheTartarusFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        return new FleeTheTartarus(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("flee-the-tartarus/spritecarie.png",
                        Texture.class);
                put("flee-the-tartarus/dente.png", Texture.class);
                put("flee-the-tartarus/dente-morto.png", Texture.class);
                put("flee-the-tartarus/fundo.png", Texture.class);
                
                put("flee-the-tartarus/aperta2.mp3", Sound.class);
                put("flee-the-tartarus/venceu.mp3", Sound.class);
                put("flee-the-tartarus/game-over.mp3", Sound.class); 
            }
        };
    }
    
}
