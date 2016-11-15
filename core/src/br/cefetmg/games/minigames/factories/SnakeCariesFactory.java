/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.SnakeCaries;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nicolas
 */
public class SnakeCariesFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen, GameStateObserver observer, float difficulty) {
        return new SnakeCaries(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("snake-caries/carie.png", Texture.class);
                put("snake-caries/fundo.png", Texture.class);
                put("snake-caries/seta-cima.png", Texture.class);
                put("snake-caries/seta-direita.png", Texture.class);
                put("snake-caries/seta-baixo.png", Texture.class);
                put("snake-caries/seta-esquerda.png", Texture.class);
                put("snake-caries/snake.png", Texture.class);
                put("snake-caries/carie-morrendo.wav", Sound.class);
                put("snake-caries/pressionado.png", Texture.class);
            }
        };
    }

}
