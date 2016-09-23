package br.cefetmg.games.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author fegemo
 */
public class Dentinho {
    int pulo, queda;
    private Texture spriteSheet, normal;
    private TextureRegion[][] quadrosDaAnimacao;
    private Animation frente, direita, esquerda, tras, parado, posicao;
    float tempoDaAnimacao, i, vX, vY, jogadorX, jogadorY;
    
    
    public Dentinho(float jogadorX, float jogadorY) {
        this.jogadorX = jogadorX;
        this.jogadorY = jogadorY;
        normal = new Texture("pegue-limpeza/dente.png");
        spriteSheet = new Texture("pegue-limpeza/dentinhos.png");
        quadrosDaAnimacao = TextureRegion.split(spriteSheet,35,23);
        frente = animacaoMovimento(0);
        direita = animacaoMovimento(1);
        tras = animacaoMovimento(2);
        esquerda = animacaoMovimento(3);        
        parado = animacaoParado(0); 
        tempoDaAnimacao = 0;
        pulo = 0;
        queda=0;
        vX=0.0f;
        vY=0.0f;
    }

    public void update() {
    	if(pulo==1){
    		vY-=0.1;
    		if(vY==0){
    			queda=1;
    			pulo=0;
    		}
    	}
    	else if(queda==1){
    		vY-=0.1;
    	}
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        	vX-=0.2;
        	if(vX<(-1)){
        		vX=-1;
        	}
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            vX+=0.2;
            if(vX>1){
            	vX=1;
            }
        }
        else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
        	if(pulo==0){
	        	pulo=1;
	        	vY=4.5f;
        	}
        }
        if(vX>0){
    		posicao=direita;
    	}
    	else  if(vX<0){
    		posicao=esquerda;
    	}
    	else{
    		posicao=parado;
    	}
    }
    
    public void render(SpriteBatch batch) {
    	tempoDaAnimacao += Gdx.graphics.getDeltaTime();
    	TextureRegion quadroCorrente = posicao.getKeyFrame(tempoDaAnimacao);
    	jogadorX+=vX;
    	jogadorY+=vY;
        batch.draw(quadroCorrente, jogadorX, jogadorY);
    }
    
    public Animation animacaoMovimento(int linha){
    	Animation movimento;
    	movimento = new Animation(0.1f, new TextureRegion[] {
                quadrosDaAnimacao[linha][0],
                quadrosDaAnimacao[linha][1],
                quadrosDaAnimacao[linha][2],
                quadrosDaAnimacao[linha][3],
                quadrosDaAnimacao[linha][4],
              });
        movimento.setPlayMode(PlayMode.LOOP_PINGPONG);
        return(movimento);
    }
    
    public Animation animacaoParado(int linha){
    	Animation movimento;
    	movimento = new Animation(0.1f, new TextureRegion[] {
    		quadrosDaAnimacao[linha][2],
    	});
    	movimento.setPlayMode(PlayMode.LOOP_PINGPONG);
    	return(movimento);
    }
    
}