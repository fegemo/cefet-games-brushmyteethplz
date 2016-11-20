package br.cefetmg.games.minigames;

import br.cefetmg.games.minigames.util.GameStateObserver;
import br.cefetmg.games.minigames.util.TimeoutBehavior;
import br.cefetmg.games.screens.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author lindley
 */
public final class Gallows extends MiniGame {

    private final Sprite mousePointer;
    private final Texture mouseTexture;
    private float tentativas = 0.0f;
    private final Texture gallowTexture;
    private final String[] palavras = {"DENTISTA", "FLUOR", "ESCOVA", "DENTE", "BOCA",
        "CARIE", "FIO DENTAL"};
    /*tamanho máximo 10, contando espaços*/
    private Animation gallowFrame;
    private final Sprite traco;
    private final Texture tracoTexture;
    private final Sprite tracoBranco;
    private final Texture tracoBrancoTexture;
    private String word;
    private final Texture letrasTexture;
    private Animation letrasFrame;
    //private TextureRegion[] aux;
    private final Array<StructSprite> letters;
    private final Array<Sprite> letrasCertas;
    private int qtdLetrasAcertadas;
    private final Sound somDenteQuebrando;
    private final Sound somSucesso;

    public Gallows(BaseScreen screen, GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 15f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        this.mouseTexture = this.screen.assets.get(
                "Gallows/mousePointer.png", Texture.class);
        this.mousePointer = new Sprite(mouseTexture);
        this.mousePointer.setOriginCenter();
        this.mousePointer.setScale(0.3f);
        this.gallowTexture = super.screen.assets.get(
                "Gallows/spriteTooth.png", Texture.class);
        new Gallow(gallowTexture);
        this.tentativas = 0;
        this.tracoTexture = this.screen.assets.get(
                "Gallows/traco.png", Texture.class);
        this.traco = new Sprite(tracoTexture);
        this.traco.setScale(0.6f);
        this.tracoBrancoTexture = this.screen.assets.get(
                "Gallows/tracoBranco.png", Texture.class);
        this.tracoBranco = new Sprite(tracoBrancoTexture);
        this.tracoBranco.setScale(0.6f);
        this.letrasTexture = super.screen.assets.get(
                "Gallows/letras.png", Texture.class);
        new Letters(letrasTexture);
        this.letters = new Array<StructSprite>();
        colocaLetrasNoArray();
        this.word = escolhePalavra();
        this.letrasCertas = new Array<Sprite>();
        this.qtdLetrasAcertadas = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != ' ') {
                qtdLetrasAcertadas++;
            }
        }
        this.somDenteQuebrando = this.screen.assets.get(
                "Gallows/denteQuebrando.mp3", Sound.class);
        this.somSucesso = this.screen.assets.get(
                "Gallows/sucesso.mp3", Sound.class);
    }
    
    

    @Override
    protected void configureDifficultyParameters(float difficulty) {
    }

    @Override
    public void onHandlePlayingInput() {
        // atualiza a posição do alvo de acordo com o mouse
        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        super.screen.viewport.unproject(click);
        this.mousePointer.setPosition(click.x - this.mousePointer.getHeight() * 0.3f, click.y - this.mousePointer.getHeight() * 0.7f);

        //se apertar P o jogo acaba 
        if (Gdx.input.isKeyPressed(Keys.P)) {
            super.challengeFailed();
        }

        // verifica se acertou a letr        
        if (Gdx.input.justTouched()) {
            boolean letraEstaNaPalavra = false;
            // itera no array de letras
            for (int i = 0; i < letters.size; i++) {
                StructSprite sprite = letters.get(i);
                // se há interseção entre o retângulo do mouse e da letra,
                // o tiro acertou
                if (sprite.getSprite().getBoundingRectangle().contains(click)) {

                    // remove a letra do array 
                    this.letters.removeValue(sprite, true);

                    for (int j = 0; j < word.length(); j++) {
                        //se a palavra tiver a letra, ela é colocada no array de letras acertadas                                                
                        if (word.charAt(j) == sprite.getCaracter()) {
                            Sprite s = new Sprite(sprite.getSprite());
                            s.setPosition(250 + j * 100, 320.0f);
                            letrasCertas.add(s);
                            letraEstaNaPalavra = true;
                            qtdLetrasAcertadas--;
                            somSucesso.play();
                        }
                    }
                    if (!letraEstaNaPalavra) {
                        tentativas++;
                        somDenteQuebrando.play();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
        if (tentativas == 4.0f) {
            super.challengeFailed();
        }
        if (qtdLetrasAcertadas == 0) {
            super.challengeSolved();
        }
    }

    @Override
    public void onDrawGame() {
        //desenha dente
        super.screen.batch.draw(gallowFrame.getKeyFrame(tentativas), 10.0f, 300.0f);

        //Desenha letras
        for (int i = 0; i < letters.size; i++) {
            Sprite sprite = letters.get(i).getSprite();
            sprite.draw(this.screen.batch);
        }
        //Desenha letras acertadas
        for (int i = 0; i < letrasCertas.size; i++) {
            Sprite sprite = letrasCertas.get(i);
            sprite.draw(this.screen.batch);
        }

        //desenha traços
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == ' ') {
                tracoBranco.setPosition(190 + i * 100, 300.0f);
                tracoBranco.draw(super.screen.batch);
            } else {
                traco.setPosition(190 + i * 100, 300.0f);
                traco.draw(super.screen.batch);
            }
        }

        //desenha mouse
        mousePointer.draw(super.screen.batch);
    }

    @Override
    public String getInstructions() {
        return "Descubra a palavra antes do tempo acabar";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return true;
    }

    class Gallow extends AnimatedSprite {

        Gallow(final Texture gallowTexture) {
            super(gallowFrame = new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            gallowTexture, gallowTexture.getWidth() / 6, gallowTexture.getHeight());
                    super.addAll(new TextureRegion[]{
                        frames[0][0],
                        frames[0][1],
                        frames[0][2],
                        frames[0][3],
                        frames[0][4]
                    });
                }
            }));
            //super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.getAnimation().setFrameDuration(1.0f);
            super.setAutoUpdate(false);
        }
    }

    public String escolhePalavra() {
        return palavras[rand.nextInt(palavras.length)];
    }

    class Letters extends AnimatedSprite {

        Letters(final Texture letrasTexture) {
            super(letrasFrame = new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            letrasTexture, letrasTexture.getWidth() / 4, letrasTexture.getHeight() / 7);
                    super.addAll(new TextureRegion[]{
                        frames[0][0],
                        frames[0][1],
                        frames[0][2],
                        frames[0][3],
                        frames[1][0],
                        frames[1][1],
                        frames[1][2],
                        frames[1][3],
                        frames[2][0],
                        frames[2][1],
                        frames[2][2],
                        frames[2][3],
                        frames[3][0],
                        frames[3][1],
                        frames[3][2],
                        frames[3][3],
                        frames[4][0],
                        frames[4][1],
                        frames[4][2],
                        frames[4][3],
                        frames[5][0],
                        frames[5][1],
                        frames[5][2],
                        frames[5][3],
                        frames[6][0],
                        frames[6][1],
                        frames[6][2],
                        frames[6][3]
                    });
                }
            }));
            //super.getAnimation().setPlayMode(Animation.PlayMode.LOOP);
            super.getAnimation().setFrameDuration(1.0f);
            super.setAutoUpdate(false);
        }
    }

    private void colocaLetrasNoArray() {
        for (int i = 0; i < 26; i++) {
            float posX, posY;
            if (i < 13) {
                posX = 350 + i * 50;
                posY = 200.0f;
            } else {
                posX = 350 + (i - 13) * 50;
                posY = 100.0f;
            }
            Sprite letra = new Sprite(letrasFrame.getKeyFrame(i));
            letra.setPosition(posX, posY);
            StructSprite struct = new StructSprite(letra, i + 65);
            letters.add(struct);
        }
    }

    class StructSprite {

        private Sprite sprite;
        private final int caracterASCII;

        StructSprite(Sprite s, int c) {
            this.sprite = s;
            this.caracterASCII = c;
        }

        public Sprite getSprite() {
            return sprite;
        }

        public int getCaracter() {
            return caracterASCII;
        }

        public void setSprite(Sprite s) {
            this.sprite = s;
        }
    }
}
