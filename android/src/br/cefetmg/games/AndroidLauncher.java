package br.cefetmg.games;

import android.os.Bundle;
import br.cefetmg.games.ranking.factories.LocalFileRankingFactory;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new BrushMyTeethPlzGame(new LocalFileRankingFactory()), config);
    }
}
