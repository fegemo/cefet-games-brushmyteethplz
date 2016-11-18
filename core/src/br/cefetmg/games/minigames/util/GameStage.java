package br.cefetmg.games.minigames.util;

import br.cefetmg.games.minigames.factories.*;
import com.badlogic.gdx.math.Rectangle;
import java.util.Arrays;
import java.util.List;

/**
 * Uma fase do modo de campanha do jogo.
 * @author thais, fegemo
 */
public enum GameStage {
    /**
     * Estágio com MiniGames informativos sobre alimentos.
     */
    LEARNING_ABOUT_CANDY(new Rectangle(38, 126, 323, 204), 0, 0.2f,
            new CollectItensFactory(),
            new SaveTheTeethFactory(),
            new GallowsFactory(),
            new FleeFactory(),
            new ToothRunnerFactory()
    ),
    /**
     * Estágio com MiniGames de destrução de inimigos.
     */
    DESTRUCTION(new Rectangle(450, 546, 235, 155), 0.05f, 0.4f,
            new ShootTheCariesFactory(),
            new SmashItFactory(),
            new CarieSwordFactory(),
            new RamtoothFactory(),
            new DentalKombatFactory(),
            new SnakeCariesFactory()
    ),
    /**
     * Estágio com MiniGames sobre fuga dos perigos.
     */
    RUNNING_PROTECTING(new Rectangle(424, 0, 464, 150), 0.25f, 0.6f,
            new FleeTheTartarusFactory(),
            new GallowsFactory(),
            new FleeFromTartarusFactory(),
            new CarieEvasionFactory(),
            new ShooTheTartarusFactory(),
            new NinjaToothFactory(),
            new ToothRunnerFactory()
    ),
    /**
     * Estágio com MiniGames informativos sobre higiene.
     */
    LEARNING_ABOUT_HYGIENE(new Rectangle(928, 540, 220, 140), 0.4f, 0.7f,
            new DefenseOfFluorineFactory(),
            new EscoveOsDentesFactory(),
            new CleanTheToothFactory(),
            new CollectItensFactory(),
            new CarieSwordFactory(),
            new SnakeCariesFactory()
    ),
    /**
     * Estágio com MiniGames sobre cuidados com os dentes.
     */
    CARING_FOR_TEETH(new Rectangle(1058, 148, 144, 102), 0.6f, 1f,
            new PutTheBracesFactory(),
            new MouthLandingFactory(),
            new AngryToothsFactory(),
            new DentalKombatFactory(),
            new SmashItFactory(),
            new SideWalkingFactory()
    );

    public final Rectangle buttonBounds;
    public final float initialDifficulty, finalDifficulty;
    public final List<MiniGameFactory> miniGames;

    GameStage(Rectangle stageButtonBounds, float initialDifficulty, 
            float finalDifficulty, MiniGameFactory... miniGameNames) {
        this.buttonBounds = stageButtonBounds;
        this.initialDifficulty = initialDifficulty;
        this.finalDifficulty = finalDifficulty;
        this.miniGames = Arrays.asList(miniGameNames);
    }
}
