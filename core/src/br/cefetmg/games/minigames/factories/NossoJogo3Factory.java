/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.NossoJogo3;
import br.cefetmg.games.minigames.ShootTheCaries;
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
public class NossoJogo3Factory implements MiniGameFactory{

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        return new NossoJogo3(screen, observer, difficulty);
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
                put("nosso-jogo-3/toothpaste.png", Texture.class);
                put("nosso-jogo-3/toothbrush.png", Texture.class);
                put("nosso-jogo-3/lollipop.png", Texture.class);
                put("nosso-jogo-3/candy.png", Texture.class);
                put("nosso-jogo-3/smile.png", Texture.class);
            }
        };
    }
    
}
