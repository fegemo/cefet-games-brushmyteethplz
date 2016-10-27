/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;



import br.cefetmg.games.minigames.EscoveOsDentes;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.minigames.util.GameStateObserver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Henrique Hideki Sampaio
 */
public class EscoveOsDentesFactory implements MiniGameFactory {

    /**
     * Veja {@link MiniGameFactory}.
     *
     * @param screen
     * @param observer
     * @param difficulty
     * @return
     */
    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        return new EscoveOsDentes(screen, observer, difficulty);
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
                put("escove-os-dentes/toothdirty.png", Texture.class);
                put("escove-os-dentes/brush1.png", Texture.class);
                put("escove-os-dentes/toothclean.png", Texture.class);
                put("escove-os-dentes/brush2.png", Texture.class);
                put("escove-os-dentes/fundo.png", Texture.class);
                put("escove-os-dentes/escovar.mp3", Sound.class);
                put("escove-os-dentes/limpo.wav", Sound.class);
            }
        };
    }

}

