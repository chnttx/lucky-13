import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import java.util.*;

public class DiscardPile {
    private Hand currentDiscardPile;
    private final List<DiscardPileObserver> players = new ArrayList<>();

    public void addCardToDiscardPile() {
        notifyObservers();
    }

    private void notifyObservers() {
        for (DiscardPileObserver player: players) {
            player.update();
        }
    }
}
