package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.CarieSword;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.ShooTheTartarus;
import br.cefetmg.games.minigames.ToothRunner;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by matheus on 02/11/16.
 */
public class ToothRunnerFactory implements MiniGameFactory {
    @Override
    public MiniGame createMiniGame(BaseScreen screen,
                                   GameStateObserver observer, float difficulty) {
        return new ToothRunner(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("tooth-runner/pirulito.png", Texture.class);
                put("tooth-runner/denteAnimacao.png", Texture.class);
                put("tooth-runner/passo.mp3", Sound.class);
                put("tooth-runner/pulo.mp3", Sound.class);
            }
        };
    }
}
