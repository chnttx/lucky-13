package player;

import ch.aplu.jcardgame.*;
import player.Player;

public class RandomPlayer extends Player {
  public RandomPlayer(Deck deck) {
    super(deck);
  }
  // Randomly removes a card from their hand
  public Card discard() {
    return randomCard(currentHand.getCardList());
  }
}
