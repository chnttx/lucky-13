import java.util.*;
import java.util.logging.Logger;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
public class Player {
    private Hand currentHand;
//    protected final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Player(){}
    public void draw() {}
    public void discard() {}
    public void addCard(Card card) {
        currentHand.insert(card, true);
    }

}
