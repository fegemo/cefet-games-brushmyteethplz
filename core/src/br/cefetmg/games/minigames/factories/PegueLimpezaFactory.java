package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.PegueLimpeza;
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
public class PegueLimpezaFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen, GameStateObserver observer, float difficulty) {
        return new PegueLimpeza(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("pegue-limpeza/dentinhos.png", Texture.class);
                put("pegue-limpeza/dente.png", Texture.class);
                put("pegue-limpeza/map.png", Texture.class);
                put("pegue-limpeza/limpeza-sheet.png", Texture.class);
                put("pegue-limpeza/dente-final.png", Texture.class);
                put("pegue-limpeza/dente-final-loser.png", Texture.class);
                put("pegue-limpeza/pasta.png", Texture.class);
                put("pegue-limpeza/escova.png", Texture.class);
                put("pegue-limpeza/fiodental.png", Texture.class);
                //put("pegue-limpeza/appearing1.wav", Sound.class);
                //put("pegue-limpeza/appearing2.wav", Sound.class);
                //put("pegue-limpeza/appearing3.wav", Sound.class);
                //put("pegue-limpeza/tooth-breaking.wav", Sound.class);
            }
        };
    }
}
