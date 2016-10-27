package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.Flee;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.MouthLanding;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;/**
 *
 * @author Lucas de Aguilar
 */
public class MouthLandingFactory implements MiniGameFactory{

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        return new MouthLanding(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("mouth-landing/rocket-tooth.png", Texture.class);
                put("mouth-landing/mouth.png", Texture.class);
                put("mouth-landing/fire1.wav", Sound.class);
                put("mouth-landing/fire2.wav", Sound.class);
                put("mouth-landing/fire3.wav", Sound.class);
                put("mouth-landing/fail.wav", Sound.class);
                put("mouth-landing/sucess.wav", Sound.class);
                put("shoo-the-tartarus/appearing1.wav", Sound.class);
                put("shoo-the-tartarus/appearing2.wav", Sound.class);
                put("shoo-the-tartarus/appearing3.wav", Sound.class);
                put("shoo-the-tartarus/tooth-breaking.wav", Sound.class);
            }
        };
    }
    
}
