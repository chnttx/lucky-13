package sum;

import ch.aplu.jcardgame.Card;
import enums.Rank;
import player.Player;
import singletons.PlayingArea;

import java.util.*;

/* Rule 3: 2 public cards + 2 private cards*/
public class SumRule3 extends BaseSumRule implements SumRule {
  @Override
  public boolean checkSumEquals13(Player player) {

    List<Card> publicCards = PlayingArea.getPublicCards();
    List<Card> privateCards = player.getCurrentHand().getCardList();
    Card card1 = publicCards.get(0), card2 = publicCards.get(1), card3 = privateCards.get(0),
        card4 = privateCards.get(1);
    Rank rank1 = (Rank) card1.getRank(), rank2 = (Rank) card2.getRank(),
        rank3 = (Rank) card3.getRank(), rank4 = (Rank) card4.getRank();

    boolean canSumTo13 = isThirteenFromPossibleValues(rank1.getPossibleSumValues(), rank2.getPossibleSumValues(),
        rank3.getPossibleSumValues(), rank4.getPossibleSumValues());

    if (canSumTo13) {
      player.addCombination(Arrays.asList(card1, card2, card3, card4));
    }

    return canSumTo13;
  }

  // Better time complexity O(n^2) instead of O(n^4) with 4 nested loops
  private boolean isThirteenFromPossibleValues(int[] possibleValues1, int[] possibleValues2,
                                              int[] possibleValues3, int[] possibleValues4) {
    Set<Integer> sumSet1And2 = new HashSet<>();
    for (int v1 : possibleValues1) {
      for (int v2 : possibleValues2) {
        sumSet1And2.add(v1 + v2);
      }
    }

    for (int v3 : possibleValues3) {
      for (int v4 : possibleValues4) {
        if (sumSet1And2.contains(THIRTEEN_GOAL - (v3 + v4))) {
          return true;
        }
      }
    }

    return false;
  }
}
