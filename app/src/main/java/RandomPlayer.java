import ch.aplu.jcardgame.*;

import java.util.logging.*;

public class RandomPlayer extends Player implements DiscardPileObserver{

  public RandomPlayer(Deck deck) {
    super(deck);
  }

  public void update() {
  }

  // Randomly removes a card from their hand and into the discard pile
  public void discard() {
//    draw()
    Card selected = randomCard(currentHand.getCardList());
    selected.removeFromHand(false);
//    discardPile.insert(selected, false)
  }
}
