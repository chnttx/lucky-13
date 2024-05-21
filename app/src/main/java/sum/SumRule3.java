package sum;

import ch.aplu.jcardgame.Card;
import enums.Rank;
import player.Player;
import singletons.PublicCards;
import sum.BaseSumRule;
import sum.SumRule;

import java.util.*;

/* Rule 3: 2 public cards + 2 private cards*/
public class SumRule3 extends BaseSumRule implements SumRule {
  @Override
  public boolean checkSumEquals13(Player player) {

    List<Card> publicCards = PublicCards.getPublicCards();
    List<Card> privateCards = player.getCurrentHand().getCardList();
    Rank rank1 = (Rank) publicCards.get(0).getRank(), rank2 = (Rank) publicCards.get(1).getRank(),
        rank3 = (Rank) privateCards.get(0).getRank(), rank4 = (Rank) privateCards.get(1).getRank();

    return isThirteenFromPossibleValues(rank1.getPossibleSumValues(), rank2.getPossibleSumValues(),
        rank3.getPossibleSumValues(), rank4.getPossibleSumValues());
  }

  // Better time complexity O(n^2) instead of O(n^4) with 4 nested loops
  public boolean isThirteenFromPossibleValues(int[] possibleValues1, int[] possibleValues2,
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
