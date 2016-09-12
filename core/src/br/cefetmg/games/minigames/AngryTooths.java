/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.graphics.MultiAnimatedSprite;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import java.util.HashMap;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;
import br.cefetmg.games.minigames.util.GameStateObserver;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author higor
 */
public class AngryTooths extends MiniGame{

    private final Texture toothTexture;
    private final Tooth tooth;
    public AngryTooths(BaseScreen screen, GameStateObserver observer,float difficulty) {
        super(screen, difficulty, 10000, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        this.toothTexture = super.screen.assets.get("angry-tooths/dente.png",Texture.class);
        this.tooth = new Tooth(toothTexture);
        this.tooth.setSize(120, 120);
    }
    
    @Override
    public void configureDifficultyParameters(float difficulty){}

    @Override
    public void onHandlePlayingInput(){
        Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        this.tooth.setCenter(click.x,-1*click.y);
    }

    @Override
    public void onUpdate(float dt){
    }

    @Override
    public void onDrawGame(){
        this.tooth.draw(super.screen.batch);
    }

    @Override
    public String getInstructions(){
        return "Mova o dente";
    }
    
    @Override
    public boolean shouldHideMousePointer(){
        return true;
    }
    
    class Tooth extends Sprite{
        public Tooth(final Texture toothTexture){
            super(toothTexture);
        }
    }
}


