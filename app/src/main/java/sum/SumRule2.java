package sum;

import ch.aplu.jcardgame.Card;
import player.Player;
import singletons.PlayingArea;

import java.util.Arrays;
import java.util.List;

/* Rule 2: 1 private card + 1 public card */
public class SumRule2 extends BaseSumRule implements SumRule {
  @Override
  public boolean checkSumEquals13(Player player) {
//    singletons.PublicCards instance = singletons.PublicCards.getInstance();
    boolean ans = false;
    List<Card> publicCards = PlayingArea.getPublicCards();
    List<Card> privateCards = player.getCurrentHand().getCardList();
    for (Card card1 : publicCards) {
      for (Card card2: privateCards) {
        boolean current = isThirteenCards(card1, card2);
        if (current) {
          ans = true;
          player.addCombination(Arrays.asList(card1, card2));
        }
      }
    }
    return ans;
  }
}
