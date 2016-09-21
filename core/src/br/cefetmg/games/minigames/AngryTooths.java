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
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author higor
 */
public class AngryTooths extends MiniGame{

    private final Texture toothTexture;
    private final Texture mouthTexture;
    private final Tooth tooth;
    private final Mouth mouth;
    private final int DENTE_X = 100;
    private final int DENTE_Y = 200;
    private final int MOUTH_X = 1000;
    private final int MOUTH_Y = 200;
    private Vector3 click;
    private Vector3 velocidade_inicial;
    private float velocidade_boca;
    private Vector3 posicao_inicial;
    private Vector3 posicao_final;
    private boolean trigger;
    private boolean trigger_velocidade;
    private boolean trigger_velocidade_boca;
    private float difficulty;
    
    public AngryTooths(BaseScreen screen, GameStateObserver observer,float difficulty) {
        super(screen, difficulty, 10000, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        
        this.toothTexture = super.screen.assets.get("angry-tooths/dente.png",Texture.class);
        this.tooth = new Tooth(toothTexture);
        this.tooth.setSize(40,40);
        this.tooth.setCenter(DENTE_X,DENTE_Y);
        
        this.trigger = false;
        this.trigger_velocidade = false;
        this.trigger_velocidade_boca = true;
        
        this.mouthTexture = super.screen.assets.get("angry-tooths/boca.png",Texture.class);
        this.mouth = new Mouth(mouthTexture);
        this.mouth.setCenter(MOUTH_X,MOUTH_Y);
        this.velocidade_boca = 0;
    }
    
    @Override
    public void configureDifficultyParameters(float difficulty){
        this.difficulty = difficulty;
        trigger_velocidade_boca = true;
    }

    @Override
    public void onHandlePlayingInput(){
    }

    
    
    @Override
    public void onUpdate(float dt){
        if(trigger_velocidade){
            velocidade_inicial = posicao_inicial.sub(posicao_final);
            velocidade_inicial.scl((float) 0.005);
            tooth.inicia_velocidade(velocidade_inicial);
            trigger_velocidade = false;
        }
        if(trigger){
            tooth.atua_gravidade();
            tooth.integra(dt);
            tooth.update(dt);
        }
        if(tooth.getBoundingRectangle().overlaps(mouth.getBoundingRectangle())){
            super.challengeSolved();
        }
        if(trigger_velocidade_boca){
            mouth.atua_dificuldade_velocidade_inicial(difficulty);
            trigger_velocidade_boca = false;
        }
        mouth.movimento_alternado();
        mouth.integra(dt);
        mouth.update(dt);
        Gdx.input.setInputProcessor(
            new InputProcessor(){
                @Override
                public boolean keyDown(int keycode) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public boolean keyUp(int keycode) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public boolean keyTyped(char character) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                    click = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
                    screen.viewport.unproject(click);
                    posicao_inicial = new Vector3(click.x,click.y,0);
                    return false;
                }

                @Override
                public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                    click = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
                    screen.viewport.unproject(click);
                    posicao_final = new Vector3(click.x,click.y,0);
                    trigger = true;
                    trigger_velocidade = true;
                    return false;
                }

                @Override
                public boolean touchDragged(int screenX, int screenY, int pointer) {
                    return false;
                }

                @Override
                public boolean mouseMoved(int screenX, int screenY) {
                    return false;
                }

                @Override
                public boolean scrolled(int amount) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            }
       );
    }

    @Override
    public void onDrawGame(){
        this.tooth.draw(super.screen.batch);
        this.mouth.draw(super.screen.batch);
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
        private Vector3 posicao;
        private Vector3 velocidade;
        private float velocidade_escalar;
        private Vector3 gravidade; 
        private float rotacao;
        private float orientacao;
        
        public Tooth(final Texture toothTexture){
            super(toothTexture);
            posicao = new Vector3(DENTE_X,DENTE_Y,0);
            velocidade_escalar = 50;
            gravidade = new Vector3(0,2,0);
            rotacao = 90;
        }
         
        public void inicia_velocidade(Vector3 velocidade){
            this.velocidade =  new Vector3(velocidade);
            this.velocidade.scl(velocidade_escalar);
        }

        public void atua_gravidade(){
            velocidade = velocidade.sub(gravidade);
        }
         
        public void integra(float delta) {
            posicao.x += velocidade.x * delta;
            posicao.y += velocidade.y * delta;
            posicao.z += velocidade.z * delta;
        }
        
        public void update(float dt) {
            super.setPosition(posicao.x,posicao.y);
        }

    }
    
    class Mouth extends Sprite{
        private Vector3 posicao;
        private Vector3 velocidade;
        private float MAX_DISTANCE;
        private float MIN_DISTANCE;
        private float velocidade_escalar;
        
        public Mouth(final Texture mouth_texture){
            super(mouth_texture);
            posicao = new Vector3(MOUTH_X,MOUTH_Y,0);
            velocidade = new Vector3(0,0,0);
            MAX_DISTANCE = 50;
            MIN_DISTANCE = 150;
            velocidade_escalar = 70;
        }
        
        public void update(float dt) {
            super.setPosition(posicao.x,posicao.y);
        }
         
        public void atua_dificuldade_velocidade_inicial(float difficulty){
            velocidade.x = (difficulty*10)*velocidade_escalar;
        }
        
        public void movimento_alternado(){
           if(posicao.x > MOUTH_X+MAX_DISTANCE){
               velocidade = velocidade.scl(-1);
           }else if(posicao.x < MOUTH_X-MIN_DISTANCE){
               velocidade = velocidade.scl(-1);
           }
        }
         
        public void integra(float delta) {
            posicao.x += velocidade.x * delta;
            posicao.y += velocidade.y * delta;
            posicao.z += velocidade.z * delta;
        }
        
    }
}


