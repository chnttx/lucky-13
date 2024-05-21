package player;

import ch.aplu.jcardgame.*;
import enums.Rank;
import enums.Suit;
import player.Player;

import java.util.*;
public class BasicPlayer extends Player {

  public BasicPlayer(Deck deck) {
    super(deck);
  }

  public Card discard() {
    return getMinimumCard();
  }

  private Card getMinimumCard() {
    List<Card> cardsInHand = currentHand.getCardList();
    int minScore = Integer.MAX_VALUE;
    Card minScoreCard = null;
    for (Card card: cardsInHand) {
      Suit suit = (Suit) card.getSuit();
      Rank rank = (Rank) card.getRank();
      int currentScore = suit.getMultiplicationFactor() * rank.getScoreCardValue();
      if (currentScore < minScore) {
        minScoreCard = card;
        minScore = currentScore;
      }
    }

    return minScoreCard;
  }
}
