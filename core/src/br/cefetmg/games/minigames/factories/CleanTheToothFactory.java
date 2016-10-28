package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.CleanTheTooth;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.minigames.util.GameStateObserver;
//import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class CleanTheToothFactory implements MiniGameFactory {

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
        return new CleanTheTooth(screen, observer, difficulty);
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
                put("clean-the-tooth/clean.png", Texture.class);
                put("clean-the-tooth/dirty.png", Texture.class);
                put("clean-the-tooth/toothbrush.png", Texture.class);
                put("clean-the-tooth/chargedtoothbrush.png", Texture.class);
                put("clean-the-tooth/mouth2.png", Texture.class);
                put("clean-the-tooth/toothpaste.png", Texture.class);
            }
        };
    }

}
