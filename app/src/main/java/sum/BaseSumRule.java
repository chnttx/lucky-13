package sum;

import ch.aplu.jcardgame.*;
import enums.Rank;
public abstract class BaseSumRule implements SumRule {
  protected final int THIRTEEN_GOAL = 13;

  /* Check if 2 cards can sum to 13 */
  protected boolean isThirteenCards(Card card1, Card card2) {
    Rank rank1 = (Rank) card1.getRank();
    Rank rank2 = (Rank) card2.getRank();
    return isThirteenFromPossibleValues(rank1.getPossibleSumValues(), rank2.getPossibleSumValues());
  }

  private boolean isThirteenFromPossibleValues(int[] possibleValues1, int[] possibleValues2) {
    for (int value1 : possibleValues1) {
      for (int value2 : possibleValues2) {
        if (value1 + value2 == THIRTEEN_GOAL) {
          return true;
        }
      }
    }
    return false;
  }
}
