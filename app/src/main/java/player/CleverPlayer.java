package player;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import enums.Rank;
import singletons.DiscardPile;
import singletons.PlayingArea;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class CleverPlayer extends Player {
  public CleverPlayer(Deck deck) {
    super(deck);
  }
  @Override
  public Card discard() {
    // Get all visible cards (public cards, cards in hand, and cards discarded)
    List<Card> cardsInHand = getCurrentHand().getCardList(),
        publicCards = PlayingArea.getPublicCards(), cardsPlayed = DiscardPile.getCardsPlayed();

    List<Card> allCardsVisible = Stream.concat(Stream.concat(cardsInHand.stream(), publicCards.stream()),
        cardsPlayed.stream()).toList();

    Map<Integer, Integer> hashMap = new HashMap<>();
    // Get all possible sum values of non-public cards
    for (Rank rank : Rank.values()) {
      for (int possibleValue : rank.getPossibleSumValues()) {
        hashMap.put(possibleValue, hashMap.getOrDefault(possibleValue, 0) + 4);
      }
    }
    for (Card card: allCardsVisible) {
      Rank rank = (Rank) card.getRank();
      for (int possibleValue : rank.getPossibleSumValues()) {
        hashMap.put(possibleValue, hashMap.getOrDefault(possibleValue, 0) - 1);
      }
    }

    // Find card with least likelihood of summing up to 13
    Card toDiscard = null;
    int minScore = Integer.MAX_VALUE;
    for (Card card: cardsInHand) {
      int currentScore = 0;
      Rank rank = (Rank) card.getRank();
      for (int possibleValue: rank.getPossibleSumValues()) {
        currentScore += hashMap.getOrDefault(13 - possibleValue, 0);
      }

      if (currentScore < minScore) {
        minScore = currentScore;
        toDiscard = card;
      }
    }
    return toDiscard;
  }

}
