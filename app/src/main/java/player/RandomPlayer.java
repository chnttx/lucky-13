import ch.aplu.jcardgame.*;
import player.Player;

public class RandomPlayer extends Player implements DiscardPileObserver{

  public RandomPlayer(Deck deck) {
    super(deck);
  }

  public void update() {
  }

  // Randomly removes a card from their hand and into the discard pile
  public Card discard() {
    return randomCard(currentHand.getCardList());
  }
}
