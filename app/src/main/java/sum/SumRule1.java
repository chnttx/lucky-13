import java.util.*;
import ch.aplu.jcardgame.Card;
import player.Player;

/* Rule 1: Two private cards in their hand*/
public class SumRule1 extends BaseSumRule implements SumRule {
  @Override
  public boolean checkSumEquals13(Player player) {
    List<Card> cardsInHand = player.getCurrentHand().getCardList();
    Card card1 = cardsInHand.get(0), card2 = cardsInHand.get(1);
    return isThirteenCards(card1, card2);
  }

}
