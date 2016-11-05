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
                put("collect-itens/bocacomer.png", Texture.class);
                put("collect-itens/bocaboa.png", Texture.class);
                put("collect-itens/bocaruim.png", Texture.class);
                put("collect-itens/fundo.png", Texture.class);
                
                put("collect-itens/aperta.mp3", Sound.class);
                put("collect-itens/aplausos.mp3", Sound.class);
                put("collect-itens/aperta2.mp3", Sound.class);
                put("collect-itens/game-over.mp3", Sound.class);
                
            }
        };
    }

    /**
     * Created by matheus on 02/11/16.
     */

    public static class CarieSwordFactory implements MiniGameFactory {
        @Override
        public MiniGame createMiniGame(BaseScreen screen,
                                       GameStateObserver observer, float difficulty) {
            return new CarieSword(screen, observer, difficulty);
        }

        @Override
        public Map<String, Class> getAssetsToPreload() {
            return new HashMap<String, Class>() {
                {
                    put("carie-sword/caries.png", Texture.class);
                    put("carie-sword/cariesAnimacao.png", Texture.class);
                    put("carie-sword/tooth.png", Texture.class);
                    put("carie-sword/som1.mp3", Sound.class);
                    put("carie-sword/som2.mp3", Sound.class);
                    put("carie-sword/som3.mp3", Sound.class);
                    put("carie-sword/bomb1.mp3", Sound.class);
                    put("carie-sword/bomb2.mp3", Sound.class);
                    put("carie-sword/caries1.mp3", Sound.class);
                    put("carie-sword/caries2.mp3", Sound.class);
                }
            };
        }
    }
}
