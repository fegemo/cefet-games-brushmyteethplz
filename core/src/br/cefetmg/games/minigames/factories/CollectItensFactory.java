/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.CarieSword;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.CollectItens;
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
public class CollectItensFactory implements MiniGameFactory{

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        return new CollectItens(screen, observer, difficulty);
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
                put("collect-itens/toothpaste.png", Texture.class);
                put("collect-itens/toothbrush.png", Texture.class);
                put("collect-itens/lollipop.png", Texture.class);
                put("collect-itens/candy.png", Texture.class);
                put("collect-itens/bocaboa.png", Texture.class);
                put("collect-itens/bocaruim.png", Texture.class);
                put("collect-itens/fundo.png", Texture.class);
                
                put("collect-itens/aperta.mp3", Sound.class);
                put("collect-itens/aplausos.mp3", Sound.class);
                put("collect-itens/game-over.mp3", Sound.class);
                put("collect-itens/pegaitembom.mp3", Sound.class);
                put("collect-itens/pegaitemruim.mp3", Sound.class);
                
                
            }
        };
    }
}
