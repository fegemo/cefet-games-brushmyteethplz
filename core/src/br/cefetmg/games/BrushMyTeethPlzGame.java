package br.cefetmg.games;

import br.cefetmg.games.ranking.Ranking;
import br.cefetmg.games.ranking.factories.RankingFactory;
import br.cefetmg.games.screens.SplashScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class BrushMyTeethPlzGame extends Game {

    private Ranking ranking;
    private final RankingFactory rankingFactory;

    public BrushMyTeethPlzGame(RankingFactory rankingFactory) {
        this.rankingFactory = rankingFactory;
    }    
    
    @Override
    public void create() {
        Gdx.input.setCatchBackKey(true);
        this.ranking = this.rankingFactory.createRanking();
        this.setScreen(new SplashScreen(this, null));
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void render() {
        handleInput();
        super.render();
    }

    @Override
    public void dispose() {
        if (this.getScreen() != null) {
            this.getScreen().dispose();
        }
    }
    
    public Ranking getRanking() {
        return ranking;
    }
}
