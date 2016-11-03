package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.CarieEvasion;
import br.cefetmg.games.minigames.DefenseOfFluorine;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.NinjaTooth;
import br.cefetmg.games.minigames.ShooTheTartarus;
import br.cefetmg.games.screens.BaseScreen;
import br.cefetmg.games.minigames.util.GameStateObserver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dangon1 <d.aguiargoncalves@gmail.com>
 */
public class NinjaToothFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        return new NinjaTooth(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("ninja-tooth/tartarus-spritesheet.png",
                        Texture.class);
                  put("ninja-tooth/bullet-spritesheet.png",
                        Texture.class);
                put("ninja-tooth/aim.png",
                        Texture.class);
                put("ninja-tooth/ninjatooth.png", Texture.class);
                put("ninja-tooth/ninjatooth-dead.png", Texture.class); 
                put("ninja-tooth/appearing1.wav", Sound.class);
                put("ninja-tooth/appearing2.wav", Sound.class);
                put("ninja-tooth/appearing3.wav", Sound.class);
                put("ninja-tooth/tooth-breaking.wav", Sound.class);
            }
        };
    }
}
