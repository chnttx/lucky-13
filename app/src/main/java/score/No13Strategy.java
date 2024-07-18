package score;

import ch.aplu.jcardgame.Card;
import enums.Rank;
import enums.Suit;
import player.Player;
import score.ScoreStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* No player has a sum of 13 */
public class No13Strategy implements ScoreStrategy {
  public void applyScore(Player[] players) {
    for (Player player: players) {
      player.setScore(calculateScoreForPlayer(player));
    }
  }
  public List<Integer> findWinners(Player[] players) {
    List<Integer> ans = new ArrayList<>();
    int maxScore = 0;
    for (Player player: players) maxScore = Math.max(player.getScore(), maxScore);
    for (int i = 0; i < players.length; i++) {
      if (players[i].getScore() == maxScore) ans.add(i);
    }
    return ans;
  }
  private int calculateScoreForPlayer(Player player) {
    int ans = 0;
    List<Card> cards = player.getCurrentHand().getCardList();
    for (Card card: cards) {
      ans += getScorePrivateCard(card);
    }
    return ans;
  }
  private int getScorePrivateCard(Card card) {
    Rank rank = (Rank) card.getRank();
    Suit suit = (Suit) card.getSuit();

    return rank.getScoreCardValue() * suit.getMultiplicationFactor();
  }
}
