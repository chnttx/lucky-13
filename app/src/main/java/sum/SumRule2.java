import ch.aplu.jcardgame.Card;
import player.Player;

import java.util.List;

/* Rule 2: 1 private card + 1 public card */
public class SumRule2 extends BaseSumRule implements SumRule {
  @Override
  public boolean checkSumEquals13(Player player) {
//    PublicCards instance = PublicCards.getInstance();
    List<Card> publicCards = PublicCards.getPublicCards();
    List<Card> privateCards = player.getCurrentHand().getCardList();
    for (Card card1 : publicCards) {
      for (Card card2: privateCards) {
        if (isThirteenCards(card1, card2)) return true;
      }
    }
    return false;
  }
}
