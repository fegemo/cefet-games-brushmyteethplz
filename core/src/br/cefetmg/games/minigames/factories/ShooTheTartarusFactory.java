package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.ShooTheTartarus;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.minigames.util.GameStateObserver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class ShooTheTartarusFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        return new ShooTheTartarus(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("shoo-the-tartarus/toothbrush-spritesheet.png",
                        Texture.class);
                put("shoo-the-tartarus/tartarus-spritesheet.png",
                        Texture.class);
                put("shoo-the-tartarus/tooth.png", Texture.class);
                put("shoo-the-tartarus/appearing1.wav", Sound.class);
                put("shoo-the-tartarus/appearing2.wav", Sound.class);
                put("shoo-the-tartarus/appearing3.wav", Sound.class);
                put("shoo-the-tartarus/tooth-breaking.wav", Sound.class);
            }
        };
    }
}