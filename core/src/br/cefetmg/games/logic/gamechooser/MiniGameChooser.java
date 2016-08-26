package br.cefetmg.games.logic.gamechooser;

import br.cefetmg.games.minigames.MiniGame;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public interface MiniGameChooser {
    MiniGame choose(Array<Class> availableGameClasses);
}
