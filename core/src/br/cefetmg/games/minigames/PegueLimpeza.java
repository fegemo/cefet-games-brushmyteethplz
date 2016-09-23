package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.Dentinho;
import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer.Task;
import br.cefetmg.games.minigames.util.GameStateObserver;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class PegueLimpeza extends MiniGame {

    //private final Sound cariesAppearingSound;
    //private final Sound cariesDyingSound;
    private SpriteBatch batch;
    private Dentinho dentinho;
    private Texture map, fimWin, fimLoss, limpeza1, limpeza2, limpeza3;
    private Sprite jogador;
    private float mapX, mapY;

    private float numeroDeLimpezasInicial;
    private float pegos, dificuldade;

    public PegueLimpeza(BaseScreen screen, GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10000, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        this.batch = new SpriteBatch();
        this.map = new Texture("pegue-limpeza/map.png");
        this.fimWin = new Texture("pegue-limpeza/dente-final.png");
        this.fimLoss = new Texture("pegue-limpeza/dente-final-loser.png");
        this.limpeza1 = new Texture("pegue-limpeza/pasta.png");
        this.limpeza2 = new Texture("pegue-limpeza/escova.png");
        this.limpeza3 = new Texture("pegue-limpeza/fiodental.png");
        this.dificuldade = difficulty;
        this.mapY = 150.0f;
        this.mapX = 350.0f;
        this.pegos = 0;
        this.dentinho = new Dentinho((85.0f+mapX), (8.0f+mapY));
        //this.cariesAppearingSound = this.screen.assets.get("shoot-the-caries/caries1.mp3", Sound.class);
        //this.cariesDyingSound = this.screen.assets.get("shoot-the-caries/caries2.mp3", Sound.class);
    }


    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.numeroDeLimpezasInicial=difficulty;
        if(this.numeroDeLimpezasInicial<=1){
        	this.numeroDeLimpezasInicial=1;
        }
        if(this.numeroDeLimpezasInicial<=2){
        	this.numeroDeLimpezasInicial=2;
        }
        if(this.numeroDeLimpezasInicial<=3){
        	this.numeroDeLimpezasInicial=3;
        }
    }

    @Override
    public void onHandlePlayingInput() {
        
    }

    @Override
    public void onUpdate(float dt) {
    	//define se o dente encosta no chão
    	if(dentinho.jogadorY<=(8.0f+mapY)){
    		dentinho.jogadorY=(8.0f+mapY);
    		dentinho.vY=0;
    		dentinho.pulo=0;
    		dentinho.queda=0;
    		// seta barreira direita chao para o dente
    		if(dentinho.jogadorX>(140.0f+mapX)){
    			dentinho.jogadorX=(140.0f+mapX);
    			dentinho.vX=0;
    		}
    		// seta barreira esquerda chao para o dente
    		if(dentinho.jogadorX<(70.0f+mapX)){
    			dentinho.jogadorX=(70.0f+mapX);
    			dentinho.vX=0;
    		}
    	}
    	//define se o dente encosta no teto da primeira prateleira
    	//retirado o limite do teto pois estava muito dificil e complexo
    	/*else if(dentinho.jogadorY<(45.0f+mapY)){
    		if(dentinho.jogadorX>=(125.0f+mapX)){
    			dentinho.pulo=0;
    			dentinho.vY-=1;
    			dentinho.queda=1;
    		}
    	}*/
    	//define se o dente encosta no chão da primeira prateleira
    	else if(dentinho.jogadorY>=(47.0f+mapY) && dentinho.jogadorY<=(51.0f+mapY)){
    		if(dentinho.jogadorX>=(125.0f+mapX) && dentinho.jogadorX<=(190.0f+mapX)){
    			dentinho.jogadorY=(49.0f+mapY);
    			dentinho.vY=0;
    			dentinho.pulo=0;
    			dentinho.queda=0;
    			
    		}
    		else if(dentinho.jogadorX<(125.0f+mapX) || dentinho.jogadorX>(190.0f+mapX)){
    			dentinho.queda=1;
    		}	
    	}
    	//define se o dente encosta no teto da segunda prateleira
    	//retirado o limite do teto pois estava muito dificil e complexo
    	/*else if(dentinho.jogadorY<(84.0f+mapY) && dentinho.jogadorY>(81.0f+mapY)){
    		if(dentinho.jogadorX<=(121.0f+mapX) && dentinho.jogadorX>=(70.0f+mapX)){
    			dentinho.pulo=0;
    			dentinho.vY-=1;
    			dentinho.queda=1;
    		}
    	}*/
    	//define se o dente encosta no chao da segunda prateleira
    	else if(dentinho.jogadorY>=(96.0f+mapY) && dentinho.jogadorY<=(99.0f+mapY)){
    		if(dentinho.jogadorX<=(121.0f+mapX) && dentinho.jogadorX>=(70.0f+mapX)){
    			dentinho.jogadorY=(97.0f+mapY);
    			dentinho.vY=0;
    			dentinho.pulo=0;
    			dentinho.queda=0;
    		}
    		else if(dentinho.jogadorX<(70.0f+mapX) || dentinho.jogadorX>(121.0f+mapX)){
    			dentinho.queda=1;
    		}	
    	}
    	//define se o dente encosta no teto da terceira prateleira
    	//retirado o limite do teto pois estava muito dificil e complexo
    	/*else if(dentinho.jogadorY<(126.0f+mapY) && dentinho.jogadorY>(123.0f+mapY)){
    		if(dentinho.jogadorX<=(53.0f+mapX) && dentinho.jogadorX>=(0.0f+mapX)){
    			dentinho.pulo=0;
    			dentinho.vY-=1;
    			dentinho.queda=1;
    		}
    	}*/
    	//define se o dente encosta no chao da terceira prateleira
    	else if(dentinho.jogadorY>=(135.0f+mapY) && dentinho.jogadorY<=(138.0f+mapY)){
    		if(dentinho.jogadorX<=(53.0f+mapX) && dentinho.jogadorX>=(0.0f+mapX)){
    			dentinho.jogadorY=(137.0f+mapY);
    			dentinho.vY=0;
    			dentinho.pulo=0;
    			dentinho.queda=0;
    		}
    		else if(dentinho.jogadorX<(0.0f+mapX) || dentinho.jogadorX>(54.0f+mapX)){
    			dentinho.queda=1;
    		}	
    	}
    	//retirado o limite do teto pois estava muito dificil e complexo
    	//define se o dente encosta no chao da quarta prateleira
    	else if(dentinho.jogadorY>=(173.0f+mapY) && dentinho.jogadorY<=(176.0f+mapY)){
    		if(dentinho.jogadorX<=(180.0f+mapX) && dentinho.jogadorX>=(70.0f+mapX)){
    			dentinho.jogadorY=(175.0f+mapY);
    			dentinho.vY=0;
    			dentinho.pulo=0;
    			dentinho.queda=0;
    		}
    		else if(dentinho.jogadorX<(180.0f+mapX) || dentinho.jogadorX>(70.0f+mapX)){
    			dentinho.queda=1;
    		}	
    	}
    	if(this.numeroDeLimpezasInicial>=1){
    		if(dentinho.jogadorX==(165+mapX) && dentinho.jogadorY==(49+mapY)){
    			pegos+=1;
    		}
    	}
    	if(this.numeroDeLimpezasInicial>=2){
    		if(dentinho.jogadorX==(30+mapX) && dentinho.jogadorY==(137+mapY)){
    			pegos+=1;
    		}
    	}
    	if(this.numeroDeLimpezasInicial>=3){
    		if(dentinho.jogadorX==(160+mapX) && dentinho.jogadorY==(175+mapY)){
    			pegos+=1;
    		}
    	}
    	if (this.numeroDeLimpezasInicial == this.pegos) {
            super.challengeSolved();
        }
        dentinho.update();
    }

    @Override
    public String getInstructions() {
        return "Pegue as coisas para limpar o dente e vencer a carie";
    }

    @Override
    public void onDrawGame() {
    	Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        onUpdate(1);
        batch.begin();
        // desenhos sao realizados aqui
        batch.draw(map, mapX, mapY);
        if(this.numeroDeLimpezasInicial>=1 && pegos==0){
        	batch.draw(limpeza1,165+mapX,49+mapY);
        }
        if(this.numeroDeLimpezasInicial>=2 && pegos==1){
        	batch.draw(limpeza2,30+mapX,137+mapY);
        }
        if(this.numeroDeLimpezasInicial>=3 && pegos==2){
        	batch.draw(limpeza3,160+mapX,175+mapY);
        }
        dentinho.render(batch);
        batch.end();
    	
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

}
