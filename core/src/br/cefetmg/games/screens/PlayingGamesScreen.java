package br.cefetmg.games.screens;

import br.cefetmg.games.minigames.MiniGame;
import br.cefetmg.games.minigames.ShootTheCaries;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class PlayingGamesScreen extends BaseScreen {
    
    private MiniGame currentGame;
    
    public PlayingGamesScreen(Game game) {
        super(game);
        super.assets.load("fonts/sawasdee.fnt", BitmapFont.class);
        super.assets.load("shoot-the-caries/caries.png", Texture.class);
        super.assets.load("shoot-the-caries/target.png", Texture.class);
    }

    @Override
    public void show() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
    }
    
    @Override
    public void handleInput() {
        if (currentGame != null)  {
            currentGame.handleInput();        
        }
    }

    @Override
    public void update(float dt) {
        if (super.assets.update()) {
            if (currentGame == null) {
                BitmapFont font = super.assets.get("fonts/sawasdee.fnt", BitmapFont.class);
                font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
                currentGame = new ShootTheCaries(this, 0, 10000); 
            }
            currentGame.update(dt);
        }
    }

    @Override
    public void draw() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.batch.setProjectionMatrix(super.camera.combined);
        super.batch.begin();
        if (currentGame != null)  {
            currentGame.draw();
        }
        super.batch.end();
    }
    
}
