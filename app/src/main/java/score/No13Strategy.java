package score;

import ch.aplu.jcardgame.Card;
import enums.Rank;
import enums.Suit;
import player.Player;
import score.ScoreStrategy;

import java.util.List;

public class No13Strategy implements ScoreStrategy {
  public void applyScore(Player[] players) {
    for (Player player: players) {
      player.setScore(calculateScoreForPlayer(player));
    }
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
//  private int getScorePublicCard(Card card) {
//    enums.Rank rank = (enums.Rank) card.getRank();
//    return rank.getScoreCardValue() * enums.Suit.PUBLIC_CARD_MULTIPLICATION_FACTOR;
//  }
}
