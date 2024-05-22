package score;

import ch.aplu.jcardgame.Card;
import enums.Rank;
import player.Player;
import enums.*;

import java.util.List;

public class Multi13Strategy implements ScoreStrategy {
  @Override
  public void applyScore(Player[] players) {
    for (Player player: players) {
      if (player.getCanSumTo13()) {
        player.setScore(calculateScore(player));
      }
    }
  }
  private int calculateScore(Player player) {
    int ans = 0;
    List<List<Card>> allCombinations = player.getAllCombinationWithSum13();
    for (List<Card> combination : allCombinations) {
      int currentScore = 0;
      for (Card card: combination) {
        currentScore += player.isCardInHand(card) ? getScorePrivateCard(card) : getScorePublicCard(card);
      }
      ans = Math.max(ans, currentScore);
    }
    return ans;
  }
  private int getScorePrivateCard(Card card) {
    Rank rank = (Rank) card.getRank();
    Suit suit = (Suit) card.getSuit();
    return rank.getScoreCardValue() * suit.getMultiplicationFactor();
  }
  private int getScorePublicCard(Card card) {
    Rank rank = (Rank) card.getRank();
    return rank.getScoreCardValue() * Suit.PUBLIC_CARD_MULTIPLICATION_FACTOR;
  }

}
