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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author nicolas
 */
enum OrientacaoSnake {
    Top,
    Right,
    Down,
    Left
}

public class SnakeCaries extends MiniGame {

    private final Array<Sprite> enemies;
    private Sprite snake;

    private Sprite setaLeft;
    private Sprite setaRight;
    private Sprite setaTop;
    private Sprite setaDown;
    private final Sprite fundo;

    private final Texture cariesTexture;
    private final Texture setaTopTexture;
    private final Texture setaRightTexture;
    private final Texture setaLeftTexture;
    private final Texture setaDownTexture;
    private final Texture setaPretaTexture;
    private final Texture snakeTexture;
    private final Texture fundoTexture;

    private Vector2 snakeVelocity;
    private float lenSnakeVelocity;

    private int enemiesKilled;

    private int totalEnemies;
    private int spawnInterval;

    private final Sound cariesDyingSound;

    private OrientacaoSnake orientacao;

    public SnakeCaries(BaseScreen screen,
            GameStateObserver observer, float difficulty) {
        super(screen, difficulty, 11f, TimeoutBehavior.FAILS_WHEN_MINIGAME_ENDS, observer);
        this.enemies = new Array<Sprite>();
        this.fundoTexture = this.screen.assets.get(
                "snake-caries/fundo.png", Texture.class);
        this.cariesTexture = this.screen.assets.get(
                "snake-caries/carie.png", Texture.class);
        this.setaTopTexture = this.screen.assets.get(
                "snake-caries/seta-cima.png", Texture.class);
        this.setaRightTexture = this.screen.assets.get(
                "snake-caries/seta-direita.png", Texture.class);
        this.setaDownTexture = this.screen.assets.get(
                "snake-caries/seta-baixo.png", Texture.class);
        this.setaLeftTexture = this.screen.assets.get(
                "snake-caries/seta-esquerda.png", Texture.class);
        this.snakeTexture = this.screen.assets.get(
                "snake-caries/snake.png", Texture.class);
        this.setaPretaTexture = this.screen.assets.get(
                "snake-caries/pressionado.png", Texture.class);
        this.cariesDyingSound = this.screen.assets.get("snake-caries/carie-morrendo.wav", Sound.class);
        this.enemiesKilled = 0;
        this.fundo = new Sprite(fundoTexture);
        this.fundo.setPosition(0, 0);
        generateSetas();
        generateSnake();
        this.orientacao = OrientacaoSnake.Right;
    }

    @Override
    protected void onStart() {
        spawnEnemies();
    }

    private void generateSnake() {
        Vector2 position = new Vector2(this.screen.viewport.getWorldWidth() / 2 - snakeTexture.getWidth(),
                this.screen.viewport.getWorldHeight() / 2 - snakeTexture.getHeight());
        this.snake = new Sprite(snakeTexture);
        snake.setPosition(position.x, position.y);
    }

    private void generateSetas() {
        this.setaTop = new Sprite(setaTopTexture);
        this.setaRight = new Sprite(setaRightTexture);
        this.setaDown = new Sprite(setaDownTexture);
        this.setaLeft = new Sprite(setaLeftTexture);
        float setasWidth = setaTopTexture.getWidth();
        float setasHeight = setaDownTexture.getHeight();
        Vector2 setasCenter = new Vector2(this.screen.viewport.getWorldWidth() - setaRightTexture.getHeight() - setasWidth / 2 - 10, setaDownTexture.getHeight() + setasWidth / 2 + 100);
        //posiciona setas
        this.setaTop.setPosition(setasCenter.x - setasWidth / 2, setasCenter.y + setasWidth / 2);
        this.setaRight.setPosition(setasCenter.x + setasWidth / 2, setasCenter.y - setasWidth / 2);
        this.setaDown.setPosition(setasCenter.x - setasWidth / 2, setasCenter.y - setasWidth / 2 - setasHeight);
        this.setaLeft.setPosition(setasCenter.x - setasWidth / 2 - setasHeight, setasCenter.y - setasWidth / 2);
    }

    private void spawnEnemies() {
        for (int i = 0; i < this.totalEnemies; i++) {
            spawnEnemy();
        }
    }

    private void spawnEnemy() {
        // pega x e y entre 0 e 1
        Vector2 position = new Vector2(rand.nextFloat(), rand.nextFloat());
        // multiplica x e y pela largura e altura da tela
        position.scl(
                this.screen.viewport.getWorldWidth() - cariesTexture.getWidth(),
                this.screen.viewport.getWorldHeight() - cariesTexture.getHeight());
        Sprite enemy = new Sprite(cariesTexture);
        enemy.setPosition(position.x, position.y);
        enemies.add(enemy);
    }

    @Override
    protected void configureDifficultyParameters(float difficulty) {
        this.spawnInterval = (int) DifficultyCurve.S_NEGATIVE
                .getCurveValueBetween(difficulty, 0.5f, 1.5f);
        this.totalEnemies = (int) Math.ceil(maxDuration / spawnInterval) - 4;
        this.lenSnakeVelocity = (float) this.totalEnemies + 2;
        this.snakeVelocity = new Vector2(this.lenSnakeVelocity, 0);
    }

    @Override
    public void onHandlePlayingInput() {
        //verifica seta Tocada
        this.setaTop.setTexture(setaTopTexture);
        this.setaDown.setTexture(setaDownTexture);
        this.setaRight.setTexture(setaRightTexture);
        this.setaLeft.setTexture(setaLeftTexture);
        if (Gdx.input.justTouched()) {
            Vector2 touchedPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            super.screen.viewport.unproject(touchedPoint);
            if (setaTop.getBoundingRectangle().contains(touchedPoint)) {
                this.setaTop.setTexture(setaPretaTexture);
                switch (orientacao) {
                    case Left:
                        snake.rotate90(true);
                        mudaOrientacao(OrientacaoSnake.Top);
                        break;
                    case Right:
                        snake.rotate90(false);
                        mudaOrientacao(OrientacaoSnake.Top);
                        break;
                    default:
                        break;
                }
            } else if (setaRight.getBoundingRectangle().contains(touchedPoint)) {
                this.setaRight.setTexture(setaPretaTexture);
                switch (orientacao) {
                    case Down:
                        snake.rotate90(false);
                        mudaOrientacao(OrientacaoSnake.Right);
                        break;
                    case Top:
                        snake.rotate90(true);
                        mudaOrientacao(OrientacaoSnake.Right);
                        break;
                    default:
                        break;
                }
            } else if (setaDown.getBoundingRectangle().contains(touchedPoint)) {
                this.setaDown.setTexture(setaPretaTexture);
                switch (orientacao) {
                    case Left:
                        snake.rotate90(false);
                        mudaOrientacao(OrientacaoSnake.Down);
                        break;
                    case Right:
                        snake.rotate90(true);
                        mudaOrientacao(OrientacaoSnake.Down);
                        break;
                    default:
                        break;
                }
            } else if (setaLeft.getBoundingRectangle().contains(touchedPoint)) {
                this.setaLeft.setTexture(setaPretaTexture);
                switch (orientacao) {
                    case Down:
                        snake.rotate90(true);
                        mudaOrientacao(OrientacaoSnake.Left);
                        break;
                    case Top:
                        snake.rotate90(false);
                        mudaOrientacao(OrientacaoSnake.Left);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onUpdate(float dt) {
        this.fundo.setSize(this.screen.viewport.getWorldWidth(), this.screen.viewport.getWorldHeight());
        this.snake.setPosition(this.snake.getX() + this.snakeVelocity.x, this.snake.getY() + this.snakeVelocity.y);
        //verifica se snake esta fora de fronteira
        switch (orientacao) {
            case Top:
                if (snake.getHeight() + snake.getY() >= this.screen.viewport.getWorldHeight()) {
                    this.snakeVelocity.y = 0;
                }
                break;
            case Right:
                if (snake.getWidth() + snake.getX() >= this.screen.viewport.getWorldWidth()) {
                    this.snakeVelocity.x = 0;
                }
                break;
            case Down:
                if (snake.getY() <= 0) {
                    this.snakeVelocity.y = 0;
                }
                break;
            case Left:
                if (snake.getX() <= 0) {
                    this.snakeVelocity.x = 0;
                }
                break;
        }

        // verifica se matou um inimigo
        for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            // se há interseção entre o retângulo da sprite e da snake,
            // o tiro acertou
            if (sprite.getBoundingRectangle().overlaps(
                    snake.getBoundingRectangle())) {
                // contabiliza um inimigo morto
                this.enemiesKilled++;
                // remove o inimigo do array
                this.enemies.removeValue(sprite, true);
                cariesDyingSound.play();
                // se tiver matado todos os inimigos, o desafio
                // está resolvido
                if (this.enemiesKilled >= this.totalEnemies) {
                    super.challengeSolved();
                }
                // pára de iterar, porque senão o tiro pode pegar em mais
                // de um inimigo
                break;
            }
        }
    }

    private void resizeSnake(Boolean toEmPe) {
        if (!toEmPe) {
            snake.setSize(snakeTexture.getWidth(), snakeTexture.getHeight());
        } else {
            snake.setSize(snakeTexture.getHeight(), snakeTexture.getWidth());
        }
    }

    private void mudaOrientacao(OrientacaoSnake orit) {
        switch (orit) {
            case Top:
                if (orientacao == OrientacaoSnake.Left) {
                    if (this.snake.getY() > this.screen.viewport.getWorldHeight() - this.snakeTexture.getWidth()) {
                        this.snake.setPosition(this.snake.getX(), this.screen.viewport.getWorldHeight() - this.snakeTexture.getWidth());
                    }
                }
                if (orientacao == OrientacaoSnake.Right) {
                    if (this.snake.getY() > this.screen.viewport.getWorldHeight() - this.snakeTexture.getWidth()) {
                        this.snake.setPosition(this.snake.getX() + this.snake.getWidth() / 2, this.screen.viewport.getWorldHeight() - this.snakeTexture.getWidth());
                    } else {
                        this.snake.setPosition(this.snake.getX() + this.snake.getWidth() / 2, this.snake.getY());
                    }
                }
                snakeVelocity.x = 0;
                snakeVelocity.y = lenSnakeVelocity;
                orientacao = OrientacaoSnake.Top;
                resizeSnake(true);
                break;
            case Right:
                if (orientacao == OrientacaoSnake.Down) {
                    if (this.snake.getX() > this.screen.viewport.getWorldWidth() - this.snakeTexture.getWidth()) {
                        this.snake.setPosition(this.screen.viewport.getWorldWidth() - this.snakeTexture.getWidth(), this.snake.getY());
                    }
                }
                if (orientacao == OrientacaoSnake.Top) {
                    if (this.snake.getX() > this.screen.viewport.getWorldWidth() - this.snakeTexture.getWidth()) {
                        this.snake.setPosition(this.screen.viewport.getWorldWidth() - this.snakeTexture.getWidth(), this.snake.getY() + (this.snake.getHeight() / 2));
                    } else {
                        this.snake.setPosition(this.snake.getX(), this.snake.getY() + (this.snake.getHeight() / 2));
                    }
                }
                snakeVelocity.x = lenSnakeVelocity;
                snakeVelocity.y = 0;
                orientacao = OrientacaoSnake.Right;
                resizeSnake(false);
                break;
            case Down:
                if (orientacao == OrientacaoSnake.Left) {
                    if (this.snake.getY() < 0) {
                        this.snake.setPosition(this.snake.getX(), 0);
                    }
                }
                if (orientacao == OrientacaoSnake.Right) {
                    if (this.snake.getY() < 0) {
                        this.snake.setPosition(this.snake.getX() + this.snake.getWidth() / 2, 0);
                    } else {
                        this.snake.setPosition(this.snake.getX() + this.snake.getWidth() / 2, this.snake.getY());
                    }
                }
                snakeVelocity.x = 0;
                snakeVelocity.y = -lenSnakeVelocity;
                orientacao = OrientacaoSnake.Down;
                resizeSnake(true);
                break;
            case Left:
                if (orientacao == OrientacaoSnake.Down) {
                    if (this.snake.getX() < 0) {
                        this.snake.setPosition(0, this.snake.getY());
                    }
                }
                if (orientacao == OrientacaoSnake.Top) {
                    if (this.snake.getX() < 0) {
                        this.snake.setPosition(0, this.snake.getY() + (this.snake.getHeight() / 2));
                    } else {
                        this.snake.setPosition(this.snake.getX(), this.snake.getY() + (this.snake.getHeight() / 2));
                    }
                }
                snakeVelocity.x = -lenSnakeVelocity;
                snakeVelocity.y = 0;
                orientacao = OrientacaoSnake.Left;
                resizeSnake(false);
                break;
        }
    }

    @Override
    public void onDrawGame() {
        fundo.draw(this.screen.batch);
        setaTop.draw(this.screen.batch);
        setaLeft.draw(this.screen.batch);
        setaRight.draw(this.screen.batch);
        setaDown.draw(this.screen.batch);
        for (int i = 0; i < enemies.size; i++) {
            Sprite sprite = enemies.get(i);
            sprite.draw(this.screen.batch);
        }
        snake.draw(this.screen.batch);
    }

    @Override
    public String getInstructions() {
        return "Destrua as caries movendo a escova!";
    }

    @Override
    public boolean shouldHideMousePointer() {
        return false;
    }

}
