/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.NossoJogo1;
import br.cefetmg.games.minigames.ShooTheTartarus;
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
public class NossoJogo1Factory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        return new NossoJogo1(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("nosso-jogo-1/tartarus-spritesheet.png",
                        Texture.class);
                put("nosso-jogo-1/dente.png", Texture.class);
                /*put("shoo-the-tartarus/appearing1.wav", Sound.class);
                put("shoo-the-tartarus/appearing2.wav", Sound.class);
                put("shoo-the-tartarus/appearing3.wav", Sound.class);
                put("shoo-the-tartarus/tooth-breaking.wav", Sound.class);*/
            }
        };
    }
    
}
