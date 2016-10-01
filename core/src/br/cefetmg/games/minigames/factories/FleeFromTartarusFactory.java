package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.FleeFromTartarus;
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
public class FleeFromTartarusFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        return new FleeFromTartarus(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("flee-from-tartarus/tartarus-spritesheet.png",
                        Texture.class);
                put("flee-from-tartarus/tooth.png", Texture.class);
                put("flee-from-tartarus/background.jpg", Texture.class);
                put("flee-from-tartarus/appearing1.wav", Sound.class);
                put("flee-from-tartarus/appearing2.wav", Sound.class);
                put("flee-from-tartarus/appearing3.wav", Sound.class);
                put("flee-from-tartarus/fundo.wav", Sound.class);
                put("flee-from-tartarus/gameover.wav", Sound.class);
                put("flee-from-tartarus/tooth-breaking.mp3", Sound.class);
            }
        };
    }
}
