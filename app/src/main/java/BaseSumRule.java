import ch.aplu.jcardgame.*;

public abstract class BaseSumRule implements SumRule {
  protected int getScorePrivateCard(Card card) {
    Rank rank = (Rank) card.getRank();
    Suit suit = (Suit) card.getSuit();

    return rank.getScoreCardValue() * suit.getMultiplicationFactor();
  }
  protected int getScorePublicCard(Card card) {
    Rank rank = (Rank) card.getRank();
    return rank.getScoreCardValue() * Suit.PUBLIC_CARD_MULTIPLICATION_FACTOR;
  }
}
