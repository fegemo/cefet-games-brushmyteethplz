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
    private final Texture backgroundTexture;
    private final Sound toothSound;
    private final Tooth tooth;
    private final Mouth mouth;
    private final Background background;
    private final int DENTE_X = 100;
    private final int DENTE_Y = 200;
    private final int MOUTH_X = 1000;
    private final int MOUTH_Y = 200;
    private final int LIMITE_ERRO = 50;
    private Vector3 click;
    private Vector3 velocidade_inicial;
    private float velocidade_boca;
    private Vector3 posicao_inicial;
    private Vector3 posicao_final;
    private boolean trigger;
    private boolean trigger_velocidade;
    private boolean trigger_velocidade_boca;
    private boolean trigger_click;
    private float difficulty;
    
    public AngryTooths(BaseScreen screen, GameStateObserver observer,float difficulty) {
        super(screen, difficulty, 10000, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        
        this.toothSound = super.screen.assets.get("angry-tooths/missile.mp3",Sound.class);
        
        this.toothTexture = super.screen.assets.get("angry-tooths/dente_region.png",Texture.class);
        TextureRegion[][] tooth_regions = TextureRegion.split(toothTexture,
                Tooth.TOOTH_TEXTURE_W, Tooth.TOOTH_TEXTURE_H);
        this.tooth = new Tooth(tooth_regions[0][0],tooth_regions[0][1],tooth_regions[0][2]);
        this.tooth.setSize(40,60);
        this.tooth.setCenter(DENTE_X,DENTE_Y);
        
        this.trigger = false;
        this.trigger_velocidade = false;
        this.trigger_velocidade_boca = true;
        this.trigger_click = true;
        
        this.mouthTexture = super.screen.assets.get("angry-tooths/boca.png",Texture.class);
        this.mouth = new Mouth(mouthTexture);
        this.mouth.scale(0.7f);
        this.mouth.setCenter(MOUTH_X,MOUTH_Y);
        this.velocidade_boca = 0;
        
        this.backgroundTexture = super.screen.assets.get("angry-tooths/background.jpg",Texture.class);
        this.background = new Background(backgroundTexture);
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
        if(mouth.getBoundingRectangle().contains(tooth.getBoundingRectangle()) && 
                tooth.getX() > mouth.getX() + 40){
            super.challengeSolved();
        }
        if(tooth.getY() < LIMITE_ERRO){
            super.challengeFailed();
        }
        if(trigger_velocidade_boca){
            mouth.atua_dificuldade_velocidade_inicial(difficulty);
            trigger_velocidade_boca = false;
        }
        mouth.movimento_alternado();
        mouth.integra(dt);
        mouth.update(dt);
        tooth.troca_sprite();
        Gdx.input.setInputProcessor(
            new InputProcessor(){
                @Override
                public boolean keyDown(int keycode) {
                    return false;
                }

                @Override
                public boolean keyUp(int keycode) {
                    return false;
                }

                @Override
                public boolean keyTyped(char character) {
                    return false;
                }

                @Override
                public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                    if(trigger_click){
                        click = new Vector3(Gdx.input.getX(), Gdx.input.getY(),0);
                        screen.viewport.unproject(click);
                        posicao_inicial = new Vector3(click.x,click.y,0);
                    }
                    return false;
                }

                @Override
                public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                    if(posicao_inicial != null && trigger_click == true){
                        click = new Vector3(Gdx.input.getX(),Gdx.input.getY(),0);
                        screen.viewport.unproject(click);
                        posicao_final = new Vector3(click.x,click.y,0);
                        trigger = true;
                        trigger_velocidade = true;
                        if(trigger_click){
                            toothSound.play();
                            trigger_click = false;
                        }
                    }
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
                    return false;
                }
            }
       );
    }

    @Override
    public void onDrawGame(){
        this.background.draw(super.screen.batch);
        this.mouth.draw(super.screen.batch);
        this.tooth.draw(super.screen.batch);
    }

    @Override
    public String getInstructions(){
        return "Acerte a boca";
    }
    
    @Override
    public boolean shouldHideMousePointer(){
        return false;
    }
    
    class Background extends Sprite{
        public Background(final Texture backgroundTexture){
            super(backgroundTexture);
        }
    }
    
    class Tooth extends Sprite{
        private Vector3 posicao;
        private Vector3 velocidade;
        private float velocidade_escalar;
        private Vector3 gravidade; 
        static final int TOOTH_TEXTURE_H = 720;
        static final int TOOTH_TEXTURE_W = 460;
        private float rotacao;
        private float orientacao;
        private final TextureRegion seriousTooth;
        private final TextureRegion smileTooth;
        private final TextureRegion bigSmileTooth;
        
        public Tooth(final TextureRegion seriousTooth,final TextureRegion smileTooth,
                final TextureRegion bigSmileTooth){
            super(seriousTooth);
            posicao = new Vector3(DENTE_X,DENTE_Y,0);
            velocidade_escalar = 100;
            gravidade = new Vector3(0,2,0);
            rotacao = 90;
            this.seriousTooth = seriousTooth;
            this.smileTooth = smileTooth;
            this.bigSmileTooth = bigSmileTooth;
        }
         
        public void inicia_velocidade(Vector3 velocidade){
            this.velocidade =  new Vector3(velocidade);
            this.velocidade.scl(velocidade_escalar);
        }

        public void atua_gravidade(){
            if(velocidade != null)
                velocidade = velocidade.sub(gravidade);
        }
         
        public void integra(float delta) {
            if(velocidade != null){
                posicao.x += velocidade.x * delta;
                posicao.y += velocidade.y * delta;
                posicao.z += velocidade.z * delta;
            }
        }
        
        public void update(float dt) {
            super.setPosition(posicao.x,posicao.y);
        }
        
        public void troca_sprite(){
            if(super.getX() <= 300){
                super.setRegion(seriousTooth);
            }else if(super.getX() <= 600 && super.getX() > 300){
                super.setRegion(smileTooth);
            }else{
                super.setRegion(bigSmileTooth);
            }
        }

    }
    
    class Mouth extends Sprite{
        private Vector3 posicao;
        private Vector3 velocidade;
        private float MAX_DISTANCE_X;
        private float MIN_DISTANCE_X;
        private float MAX_DISTANCE_Y;
        private float MIN_DISTANCE_Y;
        private float velocidade_escalar_x;
        private float velocidade_escalar_y;
        
        public Mouth(final Texture mouth_texture){
            super(mouth_texture);
            posicao = new Vector3(MOUTH_X,MOUTH_Y,0);
            velocidade = new Vector3(0,0,0);
            MAX_DISTANCE_X = 50;
            MIN_DISTANCE_X = 150;
            MAX_DISTANCE_Y = 300;
            MIN_DISTANCE_Y = 100;
            velocidade_escalar_x = 150;
            velocidade_escalar_y = 50;
        }
        
        public void update(float dt) {
            super.setPosition(posicao.x,posicao.y);
        }
         
        public void atua_dificuldade_velocidade_inicial(float difficulty){
            velocidade.x = (difficulty*10)*velocidade_escalar_x;
            velocidade.y = (difficulty*10)*velocidade_escalar_y;
        }
        
        public void movimento_alternado(){
           if(posicao.x > MOUTH_X+MAX_DISTANCE_X){
               velocidade.x = velocidade.x*-1;
           }else if(posicao.x < MOUTH_X-MIN_DISTANCE_X){
               velocidade.x = velocidade.x*-1;
           }
           if(posicao.y > MOUTH_Y+MAX_DISTANCE_Y){
               velocidade.y = velocidade.y*-1;
           }else if(posicao.y < MOUTH_Y-MIN_DISTANCE_Y){
               velocidade.y = velocidade.y*-1;
           }
        }
         
        public void integra(float delta) {
            posicao.x += velocidade.x * delta;
            posicao.y += velocidade.y * delta;
            posicao.z += velocidade.z * delta;
        }
        
    }
}


