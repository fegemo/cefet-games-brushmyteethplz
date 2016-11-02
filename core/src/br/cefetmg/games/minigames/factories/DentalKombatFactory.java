package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.DentalKombat;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.ShootTheCaries;
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
public class DentalKombatFactory implements MiniGameFactory {

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
        return new DentalKombat(screen, observer, difficulty);
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
                put("dental-kombat/caries.png", Texture.class);
                put("dental-kombat/background.png", Texture.class);
                put("shoot-the-caries/caries2.mp3", Sound.class);
                put("dental-kombat/toothSpritesheet.png", Texture.class);
                put("dental-kombat/barraDeVida.png", Texture.class);
                put("dental-kombat/barraDeVidaMoldura.png", Texture.class);
                put("dental-kombat/pain.mp3", Sound.class);
                put("dental-kombat/punch1.mp3", Sound.class);
                put("dental-kombat/punch2.mp3", Sound.class);
            }
        };
    }

}
