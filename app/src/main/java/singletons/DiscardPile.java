import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import java.util.*;

/* Our singleton object for handling discarded cards */
public class DiscardPile {
    private static DiscardPile instance;
    private static final List<Card> cardsPlayed = new ArrayList<>();
    private DiscardPile() {}

    public static DiscardPile getInstance() {
        if (instance == null) {
            instance = new DiscardPile();
        }
        return instance;
    }

    public static List<Card> getCardsPlayed() { return cardsPlayed; }
    public static void addCardToCardsPlayed(Card card) { cardsPlayed.add(card); }
}
