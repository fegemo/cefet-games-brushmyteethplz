package br.cefetmg.games.minigames.factories;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.SmashIt;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

public class SmashItFactory implements MiniGameFactory {

    @Override
    public MiniGame createMiniGame(BaseScreen screen, GameStateObserver observer, float difficulty) {
        return new SmashIt(screen, observer, difficulty);
    }

    @Override
    public Map<String, Class> getAssetsToPreload() {
        return new HashMap<String, Class>() {{
            put("smash-it/carie.png", Texture.class);
            put("smash-it/hole.png", Texture.class);
            put("smash-it/tool.png", Texture.class);
            put("smash-it/tooth.png", Texture.class);
            put("smash-it/loop.png", Texture.class);
            put("smash-it/drill.mp3", Sound.class);
        }};
    }
    
}