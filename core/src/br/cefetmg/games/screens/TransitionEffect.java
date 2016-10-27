/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.ArrayList;

/**
 *
 * @author higor
 */
public class TransitionEffect {
    private float alpha;
    private float delta;
    private float x;
    private Sprite actualSprite;
    public TransitionEffect(){
        this.alpha = 0;
        this.x = 0;
        this.delta = 0;
    }
    public void fadeOut(Batch batch,Sprite sprite){
        alpha = timeFunction(x);
        sprite.draw(batch,alpha);
        x += delta;
    }
    public void fadeIn(Batch batch,Sprite sprite){
        sprite.draw(batch,1-alpha);
        alpha = timeFunction(x);
        x += delta;
    }
    
    public boolean isFinished(){
        if(x >= 1) {
            return true;
        }
        return false;
    }
    public void setDelta(float value){
        this.delta = value;
    }
    public void setX(float value){
        this.x = value;
    }
    public float timeFunction(float x){
        return (float) x*x*x*x;
    }
}

