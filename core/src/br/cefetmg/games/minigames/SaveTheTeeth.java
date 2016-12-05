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
import com.badlogic.gdx.utils.Timer.Task;

/**
 *
 * @author Raphus
 */
public class SaveTheTeeth extends MiniGame {

    private final Texture backGroundTexture;
    private final Sound backGroundSound;
    private final Sound hurtSound;
    private final Sound niceSound;

    private float foodSpawnInterval;
    private final Texture MouthTexture;
    private final Texture MouthTexture2;
    private final Texture MouthTexture3;
    private final Texture CursorTexture;
    private final Mouth mouth;
    private final Cursor cursor;
    private final TextureRegion[][] mouthFrames;
    private final TextureRegion[][] mouthFrames2;
    private final TextureRegion[][] mouthFrames3;

    private final Array<Food> food;
    private final Texture GoodFoodSpritesheet;
    private final TextureRegion[][] GoodFoodTextures;
    private final Texture BadFoodSpritesheet;
    private final TextureRegion[][] BadFoodTextures;
    private float foodSpeed;
    private final Sprite bg;

    public SaveTheTeeth(BaseScreen screen, GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 10f, TimeoutBehavior.WINS_WHEN_MINIGAME_ENDS, observer);

        this.MouthTexture = super.screen.assets.get("save-the-teeth/boca-spritesheet.png", Texture.class);
        this.MouthTexture2 = super.screen.assets.get("save-the-teeth/boca-spritesheet-meio.png", Texture.class);
        this.MouthTexture3 = super.screen.assets.get("save-the-teeth/boca-spritesheet-fim.png", Texture.class);
        this.CursorTexture = super.screen.assets.get("save-the-teeth/cursor.png", Texture.class);
        this.BadFoodSpritesheet = super.screen.assets.get("save-the-teeth/bad.png", Texture.class);
        this.BadFoodTextures = TextureRegion.split(BadFoodSpritesheet, Food.BAD_WIDTH, Food.BAD_HEIGHT);
        this.GoodFoodSpritesheet = super.screen.assets.get("save-the-teeth/good.png", Texture.class);
        this.GoodFoodTextures = TextureRegion.split(GoodFoodSpritesheet, Food.GOOD_WIDTH, Food.GOOD_HEIGHT);
        this.food = new Array<Food>();
        this.mouthFrames = TextureRegion.split(MouthTexture, Mouth.FRAME_WIDTH, Mouth.FRAME_HEIGHT);
        this.mouthFrames2 = TextureRegion.split(MouthTexture2, Mouth.FRAME_WIDTH, Mouth.FRAME_HEIGHT);
        this.mouthFrames3 = TextureRegion.split(MouthTexture3, Mouth.FRAME_WIDTH, Mouth.FRAME_HEIGHT);
        this.mouth = new Mouth(mouthFrames[0][0], mouthFrames[0][1], mouthFrames[0][2], 
                               mouthFrames2[0][0], mouthFrames2[0][1], mouthFrames2[0][2], 
                               mouthFrames3[0][0], mouthFrames3[0][1], mouthFrames3[0][2], 2);
        this.cursor = new Cursor(CursorTexture);
        this.hurtSound = screen.assets.get(
                "save-the-teeth/Hurt.wav", Sound.class);
        this.niceSound = screen.assets.get(
                "save-the-teeth/Nice.wav", Sound.class);
        this.backGroundSound = screen.assets.get(
                "save-the-teeth/fundo.wav", Sound.class);
        this.backGroundTexture = super.screen.assets.get(
                "save-the-teeth/background.jpg", Texture.class);

        this.bg = new Sprite(backGroundTexture);
        this.bg.setSize(super.screen.viewport.getWorldWidth(), super.screen.viewport.getWorldHeight());
    }

    @Override
    protected void onStart() {
        super.timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                throwFood();
            }
        }, 0, this.foodSpawnInterval);
        backGroundSound.loop();
    }
    
    @Override
    protected void onEnd() {
        backGroundSound.stop();        
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.foodSpawnInterval = DifficultyCurve.LINEAR_NEGATIVE.getCurveValueBetween(difficulty, 0.5f, 1.5f);
        this.foodSpeed = DifficultyCurve.LINEAR.getCurveValueBetween(difficulty, 200, 500);
    }

    @Override
    public void onHandlePlayingInput() {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        super.screen.viewport.unproject(mousePos);
        this.cursor.setCenter(mousePos.x + 34.0f, mousePos.y + 13.0f);

        if (Gdx.input.justTouched()) {
            for (int i = 0; i < food.size; i++) {
                Sprite s = food.get(i);
                if (s.getBoundingRectangle().overlaps(cursor.getBoundingRectangle())) {
                    if (food.get(i).getIsGood()) {
                        if (mouth.touchedGoodFood() == 0) {
                            super.challengeFailed();
                        }else
                            hurtSound.play();
                    }else niceSound.play();
                    food.removeIndex(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
        for (int i = 0; i < food.size; i++) {
            Food f = food.get(i);
            f.update(dt);
            if (mouth.foodEntered(f)) {
                food.removeIndex(i);
                if (mouth.lives == 0) {
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
        for (Food f : this.food) {
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

    private void throwFood() {
        Vector2 foodPos = new Vector2();
        Vector2 fSpeed;
        Vector2 goalCenter = new Vector2();
        Vector2 goal = this.mouth.getBoundingRectangle().getCenter(goalCenter);
        float xOffset;
        float yOffset;
        boolean isGood = MathUtils.randomBoolean();
        TextureRegion fTexture;

        if (isGood) {
            xOffset = Food.GOOD_WIDTH;
            yOffset = Food.GOOD_HEIGHT;
            fTexture = GoodFoodTextures[0][rand.nextInt(2)];
        } else {
            xOffset = Food.BAD_WIDTH;
            yOffset = Food.BAD_HEIGHT;
            fTexture = BadFoodTextures[0][rand.nextInt(2)];
        }

        if (MathUtils.randomBoolean()) {
            foodPos.x = MathUtils.randomBoolean() ? -xOffset : super.screen.viewport.getWorldWidth();
            foodPos.y = MathUtils.random(-yOffset, super.screen.viewport.getWorldHeight());
        } else {
            foodPos.y = MathUtils.randomBoolean() ? -yOffset : super.screen.viewport.getWorldHeight();
            foodPos.x = MathUtils.random(-xOffset, super.screen.viewport.getWorldWidth());
        }

        //fSpeed=new Vector2(goalCenter.x,goalCenter.y);
        //fSpeed.nor().scl(this.foodSpeed);
        fSpeed = goal.sub(foodPos).nor().scl(this.foodSpeed);

        Food f = new Food(fTexture, isGood);
        f.setSpeed(fSpeed);
        f.setPosition(foodPos.x, foodPos.y);
        food.add(f);
    }

    class Mouth extends Sprite {

        final static int FRAME_WIDTH = 135;
        final static int FRAME_HEIGHT = 195;
        private final TextureRegion tHit;
        private final TextureRegion tInit;
        private final TextureRegion tBad;
        private final TextureRegion tHit2;
        private final TextureRegion tInit2;
        private final TextureRegion tBad2;
        private final TextureRegion tHit3;
        private final TextureRegion tInit3;
        private final TextureRegion tBad3;
        private int lives;
        protected Timer timer;

        Mouth(TextureRegion tInit, TextureRegion tHit, TextureRegion tBad, 
              TextureRegion tInit2, TextureRegion tHit2, TextureRegion tBad2, 
              TextureRegion tInit3, TextureRegion tHit3, TextureRegion tBad3, int lives) {
            super(tInit);
            this.tHit = tHit;
            this.tInit = tInit;
            this.tBad = tBad;
            this.tHit2 = tHit2;
            this.tInit2 = tInit2;
            this.tBad2 = tBad2;
            this.tHit3 = tHit3;
            this.tInit3 = tInit3;
            this.tBad3 = tBad3;
            this.lives = lives;
            this.setCenter(screen.viewport.getWorldWidth() / 2.0f, screen.viewport.getWorldHeight() / 2.0f);
            this.timer = new Timer();
        }

        public boolean foodEntered(Food f) {
            if (f.getBoundingRectangle().overlaps(this.getBoundingRectangle())) {
                if (!f.getIsGood()) {
                    lives--;
                    hurtSound.play();
                    if (lives == 1) {
                        super.setRegion(tHit);
                    } else {
                        super.setRegion(tBad);
                    }
                }else niceSound.play();
                if (lives == 2)
                    movimentaBoca(tInit,tInit2,tInit3);
                else if(lives == 1)
                    movimentaBoca(tHit,tHit2,tHit3);                
                else
                    movimentaBoca(tBad,tBad2,tBad3);
                return true;
            } else {
                return false;
            }
        }

        public int touchedGoodFood() {
            lives--;
            hurtSound.play();
            if (lives == 1) {
                super.setRegion(tHit);
            } else {
                super.setRegion(tBad);
            }
            return lives;
        }
        
        public void movimentaBoca(final TextureRegion t1, TextureRegion t2, final TextureRegion t3) {     
            super.setRegion(t2);                    
            this.timer.scheduleTask(new Task() {
                @Override
                public void run() {
                    setaRegiao(t3);
                }

            }, 0.1f);
                
            this.timer.scheduleTask(new Task() {
                @Override
                public void run() {
                    setaRegiao(t1);
                }

            }, 0.2f);
        }
        
        public void setaRegiao(TextureRegion t){
            super.setRegion(t);                   
        }
    }

    class Cursor extends Sprite {

        private final int headXoffset = 42;
        private final int headYoffset = 36;

        Cursor(final Texture CursorTexture) {
            super(CursorTexture);
        }

        Vector2 getPointerPos() {
            Vector2 pos = new Vector2();

            pos.x = this.getX() + this.headXoffset;
            pos.y = this.getY() + this.headYoffset;

            return pos;
        }
    }

    class Food extends Sprite {

        final static int BAD_WIDTH = 120;
        final static int BAD_HEIGHT = 120;
        final static int GOOD_WIDTH = 115;
        final static int GOOD_HEIGHT = 118;
        private final boolean isGood;
        private Vector2 speed;

        Food(TextureRegion text, boolean isGood) {
            super(text);
            this.isGood = isGood;
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

        public void update(float dt) {
            super.setPosition(super.getX() + this.speed.x * dt, super.getY() + this.speed.y * dt);
        }
    }
}
