/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.SaveTheTeeth;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Raphus
 */
public class SaveTheTeethFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen, GameStateObserver observer, float difficulty) {
        return new SaveTheTeeth(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("save-the-teeth/fundo.wav", Sound.class);
                put("save-the-teeth/background.jpg", Texture.class);
                put("save-the-teeth/boca-spritesheet.png", Texture.class);
                put("save-the-teeth/bad.png", Texture.class);
                put("save-the-teeth/cursor.png", Texture.class);
                put("save-the-teeth/good.png", Texture.class);
            }
        };
    }
    
}
