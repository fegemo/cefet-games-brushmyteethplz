package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import br.cefetmg.games.minigames.util.GameStateObserver;
import com.badlogic.gdx.audio.Sound;

public class DentalKombat extends MiniGame {
    
    private final Texture backGroundTexture;
    private final Texture toothTexture;
    private final Texture cariesTexture;
    private final Sound toothIsHitSound;
    private final Sound cariesIsHitSound;
    private int cariesHealth = 5;
    private int toothHealth = 5;

    public DentalKombat (BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10000,
                TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        this.toothTexture = this.screen.assets.get(
                "shoot-the-caries/caries.png", Texture.class);
        this.cariesTexture = this.screen.assets.get(
                "shoot-the-caries/caries.png", Texture.class);
        this.toothIsHitSound = this.screen.assets.get(
                "shoot-the-caries/caries2.mp3", Sound.class);
        this.cariesIsHitSound = this.screen.assets.get(
                "shoot-the-caries/caries2.mp3", Sound.class);
        this.backGroundTexture = super.screen.assets.get(
                "dental-kombat/background.png", Texture.class);
    }
    
    @Override
    protected void configureDifficultyParameters(float difficulty) {
        
    }
    
    @Override
    public void onHandlePlayingInput() {
        
    }
    
    @Override
    public void onUpdate(float dt) {
        
    }
    
    @Override
    public void onDrawGame() {
        //tocar animacoes?
        this.screen.batch.draw(backGroundTexture, 0, 0, 1280, 720);
        this.screen.batch.draw(toothTexture, 500, 360);
    }
    
    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
    @Override
    public String getInstructions() {
        return "Derrote a cárie em combate";
    }
}
