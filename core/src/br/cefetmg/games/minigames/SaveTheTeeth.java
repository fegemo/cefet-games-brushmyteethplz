/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.DifficultyCurve;
import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import java.util.Random;

/**
 *
 * @author Raphus
 */
public class SaveTheTeeth extends MiniGame {
    
    private final Texture backGroundTexture;    
    private final Sound backGroundSound;

    private int foodSpawnInterval;
    private final Texture MouthTexture;
    private final Texture CursorTexture;
    private final Mouth mouth;
    private final Cursor cursor;
    private final TextureRegion[][] mouthFrames;
    
    private final Array<Food> food;
    private final Texture GoodFoodSpritesheet;
    private final TextureRegion[][] GoodFoodTextures;
    private final Texture BadFoodSpritesheet;
    private final TextureRegion[][] BadFoodTextures;
    private float foodSpeed;
    private final Sprite bg;

    public SaveTheTeeth(BaseScreen screen, GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10000,TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS, observer);
        
        this.MouthTexture=super.screen.assets.get("save-the-teeth/boca-spritesheet.png", Texture.class);
        this.CursorTexture=super.screen.assets.get("save-the-teeth/cursor.png", Texture.class);
        this.BadFoodSpritesheet=super.screen.assets.get("save-the-teeth/bad.png", Texture.class);
        this.BadFoodTextures=TextureRegion.split(BadFoodSpritesheet, Food.BAD_WIDTH, Food.BAD_HEIGHT);
        this.GoodFoodSpritesheet=super.screen.assets.get("save-the-teeth/good.png", Texture.class);
        this.GoodFoodTextures=TextureRegion.split(GoodFoodSpritesheet, Food.GOOD_WIDTH, Food.GOOD_HEIGHT);
        this.food=new Array<Food>();
        this.mouthFrames=TextureRegion.split(MouthTexture,Mouth.FRAME_WIDTH,Mouth.FRAME_HEIGHT);
        this.mouth=new Mouth(mouthFrames[0][0],mouthFrames[0][1],mouthFrames[0][2],2);
        this.cursor=new Cursor(CursorTexture);
        this.backGroundSound = screen.assets.get(
                "save-the-teeth/fundo.wav", Sound.class);
        this.backGroundTexture = super.screen.assets.get(
                "save-the-teeth/background.jpg", Texture.class);
        
        this.bg = new Sprite(backGroundTexture);
        this.bg.setSize(super.screen.viewport.getWorldWidth(),super.screen.viewport.getWorldHeight());
        
        super.timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                throwFood();
            }
        }, 0, this.foodSpawnInterval / 1000f);
        backGroundSound.play();
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.foodSpawnInterval=(int)DifficultyCurve.LINEAR_NEGATIVE.getCurveValueBetween(difficulty, 500, 1500);
        this.foodSpeed=DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 200, 500);
    }

    @Override
    public void onHandlePlayingInput() {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        super.screen.viewport.unproject(mousePos);
        this.cursor.setCenter(mousePos.x+34.0f,mousePos.y+13.0f);
        
        if (Gdx.input.justTouched()){
            for(int i=0;i<food.size;i++){
                Sprite s = food.get(i);
                if(s.getBoundingRectangle().overlaps(cursor.getBoundingRectangle())){
                    if(food.get(i).getIsGood()){
                        if(mouth.touchedGoodFood() == 0){
                            backGroundSound.stop();
                            super.challengeFailed();
                        }
                    }
                    food.removeIndex(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
        for(int i=0;i<food.size;i++){
            Food f=food.get(i);
            f.update(dt);
            if(mouth.foodEntered(f)){
                food.removeIndex(i);
                if(mouth.lives == 0){
                    super.challengeFailed();
                }
            }
        }
    }

    @Override
    public void onDrawGame() {
        bg.draw(super.screen.batch);
        mouth.draw(super.screen.batch);
        cursor.draw(super.screen.batch);
        for(Food f : this.food){
            f.draw(super.screen.batch);
        }
    }

    @Override
    public String getInstructions() {
        return "Deixe apenas comidas saudáveis passarem";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }
    
    private void throwFood(){
        Vector2 foodPos=new Vector2();
        Vector2 fSpeed;
        Vector2 goalCenter = new Vector2();
        Vector2 goal=this.mouth.getBoundingRectangle().getCenter(goalCenter);
        float xOffset;
        float yOffset;
        boolean isGood=MathUtils.randomBoolean();
        TextureRegion fTexture;
        Random rand = new Random();
        
        if(isGood){
            xOffset=Food.GOOD_WIDTH;
            yOffset=Food.GOOD_HEIGHT;
            fTexture=GoodFoodTextures[0][rand.nextInt(2)];
        }else{
            xOffset=Food.BAD_WIDTH;
            yOffset=Food.BAD_HEIGHT;
            fTexture=BadFoodTextures[0][rand.nextInt(2)];
        }
        
        if(MathUtils.randomBoolean()) {
            foodPos.x = MathUtils.randomBoolean() ? -xOffset : super.screen.viewport.getWorldWidth();
            foodPos.y = MathUtils.random(-yOffset,super.screen.viewport.getWorldHeight());
        } else {
            foodPos.y = MathUtils.randomBoolean() ? -yOffset : super.screen.viewport.getWorldHeight();
            foodPos.x = MathUtils.random(-xOffset,super.screen.viewport.getWorldWidth());
        }

        //fSpeed=new Vector2(goalCenter.x,goalCenter.y);
        //fSpeed.nor().scl(this.foodSpeed);
        fSpeed=goal.sub(foodPos).nor().scl(this.foodSpeed);
        
        Food f=new Food(fTexture,isGood);
        f.setSpeed(fSpeed);
        f.setPosition(foodPos.x,foodPos.y);
        food.add(f);
    }
    
    class Mouth extends Sprite{
        final static int FRAME_WIDTH = 270;
        final static int FRAME_HEIGHT = 390;
        final float radius=50f;
        private final TextureRegion tHit;
        private final TextureRegion tBad;
        private int lives;
        
        Mouth(TextureRegion tInit, TextureRegion tHit, TextureRegion tBad, int lives){
            super(tInit);
            this.tHit=tHit;
            this.tBad=tBad;
            this.lives=lives;
            this.setCenter(screen.viewport.getWorldWidth()/2.0f,screen.viewport.getWorldHeight()/2.0f);
        }
        
        public boolean foodEntered(Food f){
            if(f.getBoundingRectangle().overlaps(this.getBoundingRectangle())){
                if(!f.getIsGood()){
                    lives--;
                    if(lives == 1){
                        super.setRegion(tHit);
                    }else{
                        super.setRegion(tBad);
                    }
                }
                return true;
            }else{
                return false;
            }
        }
        
        public int touchedGoodFood(){
            lives--;
            if(lives == 1){
                super.setRegion(tHit);
            }else{
                super.setRegion(tBad);
            }
            
            return lives;
        }
    }
    
    class Cursor extends Sprite{
        private final int headXoffset=42;
        private final int headYoffset=36;
                
        Cursor(final Texture CursorTexture){
            super(CursorTexture);
        }
        
        Vector2 getPointerPos(){
            Vector2 pos=new Vector2();
            
            pos.x=this.getX()+this.headXoffset;
            pos.y=this.getY()+this.headYoffset;
            
            return pos;
        }
    }
    
    class Food extends Sprite{
        final static int BAD_WIDTH = 120;
        final static int BAD_HEIGHT = 120;
        final static int GOOD_WIDTH = 115;
        final static int GOOD_HEIGHT = 118;
        private final boolean isGood;
        private Vector2 speed;
        
        Food(TextureRegion text,boolean isGood){
            super(text);
            this.isGood=isGood;
        }

        public Vector2 getSpeed() {
            return speed;
        }

        public void setSpeed(Vector2 speed) {
            this.speed = speed;
        }

        public boolean getIsGood() {
            return isGood;
        }
        
        public void update(float dt){
            super.setPosition(super.getX()+this.speed.x*dt, super.getY()+this.speed.y*dt);
        }
    }
}
