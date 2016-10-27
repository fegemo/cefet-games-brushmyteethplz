package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.CarieSword;
import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.ShooTheTartarus;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by matheus on 15/09/16.
 */
public class CarieSwordFactory implements MiniGameFactory {
    @Override
    public MiniGame createMiniGame(BaseScreen screen,
                                   GameStateObserver observer, float difficulty) {
        return new CarieSword(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {
            {
                put("carie-sword/caries.png", Texture.class);
                put("carie-sword/cariesAnimacao.png", Texture.class);
                put("carie-sword/tooth.png", Texture.class);
                put("carie-sword/som1.mp3", Sound.class);
                put("carie-sword/som2.mp3", Sound.class);
                put("carie-sword/som3.mp3", Sound.class);
                put("carie-sword/bomb1.mp3", Sound.class);
                put("carie-sword/bomb2.mp3", Sound.class);
                put("carie-sword/caries1.mp3", Sound.class);
                put("carie-sword/caries2.mp3", Sound.class);
            }
        };
    }
}
