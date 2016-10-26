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
    }
    public void update(Batch batch,ArrayList<Sprite> sprites){
        int i;
        for(i = 0;i < sprites.size();i++){
            actualSprite = sprites.get(i);
            actualSprite.draw(batch,1-alpha);
        }
        alpha = timeFunction(x);
        x += delta;
        Gdx.gl.glClearColor(1-alpha, 1-alpha, 1-alpha, 1);
    }
    public void update(){
        alpha = timeFunction(x);
        x += delta;
        Gdx.gl.glClearColor(1-alpha, 1-alpha, 1-alpha, 1);
    }
    public boolean isFinished(){
        if(alpha >= 1) {
            //Gdx.gl.glClearColor(1, 1, 1, 1);
            return true;
        }
        return false;
    }
    public void setDelta(float value){
        this.delta = value;
    }
    public float timeFunction(float x){
        return (float) x*x*x*x;
    }
}

