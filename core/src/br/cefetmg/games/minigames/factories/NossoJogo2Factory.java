/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.NossoJogo2;
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
public class NossoJogo2Factory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        return new NossoJogo2(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("nosso-jogo-2/candy.png", Texture.class);
                put("nosso-jogo-2/lollipop.png", Texture.class);
                put("nosso-jogo-2/toothbrush.png", Texture.class);
                put("nosso-jogo-2/toothpaste.png", Texture.class);
                put("nosso-jogo-2/smile.png", Texture.class);
                /*put("nosso-jogo-2/caries1.mp3", Sound.class);
                put("nosso-jogo-2/caries2.mp3", Sound.class);*/
            }
        };
    }
    
}
