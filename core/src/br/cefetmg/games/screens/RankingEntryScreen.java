package br.cefetmg.games.screens;

import br.cefetmg.games.ranking.Ranking;
import br.cefetmg.games.minigames.util.MenuState;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.dermetfan.gdx.graphics.g2d.AnimatedSprite;

/**
 *
 * @author lindley
 */
public class RankingEntryScreen extends BaseScreen {

    private Texture letrasTexture;
    private Array<StructSprite> letters;
    private Animation letrasFrame;
    private String nome;
    private Sprite eraseButton, okButton, traco;
    private Array<StructSprite> nickname;
    private int points;
    private Ranking rank;
    private final ShapeRenderer shapes;

    public RankingEntryScreen(Game game, BaseScreen previous) {
        super(game, previous);
        shapes = new ShapeRenderer();
    }

    @Override
    public void appear() {
        Gdx.gl.glClearColor(0, 0, 0, 1);

        new Letters(new Texture("buttons_rank/letras.png"));
        this.letters = new Array<StructSprite>();
        this.nickname = new Array<StructSprite>();
        this.nome = "";
        this.rank = new Ranking();
        colocaLetrasNoArray();

        this.eraseButton = new Sprite(new Texture("buttons_rank/erase.png"));
        this.okButton = new Sprite(new Texture("buttons_rank/ok.png"));

        this.eraseButton.setPosition(330.0f, viewport.getWorldHeight() / 2.5f);
        this.okButton.setPosition(800.0f, viewport.getWorldHeight() / 2.5f);

        this.traco = new Sprite(new Texture("buttons_rank/traco.png"));

        Gdx.input.setCursorCatched(false);
    }

    @Override
    public void cleanUp() {
    }

    @Override
    public void handleInput() {
        if (transitionState == states.fadeIn
                || transitionState == states.fadeOut) {
            return;
        }

        Vector2 click = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        viewport.unproject(click);

        // verifica se clicou nos botões        
        if (Gdx.input.justTouched()) {
            // se clicar em ok
            if (okButton.getBoundingRectangle().contains(click)
                    && nickname.size == 3) {
                // guarda o nickname em uma string
                for (int i = 0; i < nickname.size; i++) {
                    nome += (char) nickname.get(i).caracterASCII;
                }
                rank.writeScoreDB(nome, points);

                // chama a tela de menu
                MenuScreen menu = new MenuScreen(super.game, this);
                super.game.setScreen(menu);
                menu.changeMenuState(MenuState.RANKING);
            }

            // se clicar em erase
            if (eraseButton.getBoundingRectangle().contains(click)) {
                if (nickname.size > 0) {
                    this.nickname.removeIndex(nickname.size - 1);
                }
            }

            // itera no array de letras
            for (int i = 0; i < letters.size; i++) {
                StructSprite spriteletras = letters.get(i);
                // se clicar em alguma letra
                if (spriteletras.getSprite().getBoundingRectangle()
                        .contains(click) && nickname.size < 3) {
                    Sprite s = new Sprite(spriteletras.getSprite());
                    s.setPosition(470 + nickname.size * 150, 520.0f);
                    s.setScale(2.0f);
                    StructSprite struct = new StructSprite(s,
                            spriteletras.getCaracter());
                    this.nickname.add(struct);
                    break;
                }
            }
        }
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void draw() {
        shapes.setProjectionMatrix(batch.getProjectionMatrix());
        shapes.setColor(Color.WHITE);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldWidth());
        shapes.end();

        batch.begin();
        super.drawCenterAlignedText("Escreva um nickname com 3 letras", 0.9f,
                super.viewport.getWorldHeight() * 0.90f);

        // desenha traços
        for (int i = 0; i < 3; i++) {
            traco.setPosition(420 + i * 150, 450.0f);
            traco.setScale(0.8f);
            traco.draw(super.batch);
        }

        // desenha nickname
        for (int i = 0; i < nickname.size; i++) {
            Sprite sprite = nickname.get(i).getSprite();
            sprite.draw(this.batch);
        }

        // desenha letras
        for (int i = 0; i < letters.size; i++) {
            Sprite sprite = letters.get(i).getSprite();
            sprite.draw(this.batch);
        }

        // desenha Botões de ok e apagar
        okButton.draw(this.batch);
        eraseButton.draw(this.batch);

        batch.end();
    }

    public String getNickname() {
        return this.nome;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    class Letters extends AnimatedSprite {

        Letters(final Texture letrasTexture) {
            super(letrasFrame = new Animation(0.1f, new Array<TextureRegion>() {
                {
                    TextureRegion[][] frames = TextureRegion.split(
                            letrasTexture, letrasTexture.getWidth() / 4,
                            letrasTexture.getHeight() / 7);
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
            super.getAnimation().setFrameDuration(1.0f);
            super.setAutoUpdate(false);
        }
    }

    private void colocaLetrasNoArray() {
        for (int i = 0; i < 26; i++) {
            float posX, posY;
            if (i < 13) {
                posX = 330 + i * 50;
                posY = 200.0f;
            } else {
                posX = 330 + (i - 13) * 50;
                posY = 100.0f;
            }
            Sprite letra = new Sprite(letrasFrame.getKeyFrame(i));
            letra.setPosition(posX, posY);
            letra.setScale(1.5f);
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
