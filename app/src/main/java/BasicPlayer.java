import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import java.util.*;
//import LuckyThirdteen.*;
public class BasicPlayer extends Player {

  public BasicPlayer(Deck deck) {
    super(deck);
  }

  public void discard() {
//    setStatusText("Player " + playerIdx + " thinking...");
    Card minScoreCard = getMinimumCard();
    minScoreCard.removeFromHand(true);
  }

  private Card getMinimumCard() {
    List<Card> cardsInHand = currentHand.getCardList();
    int minScore = Integer.MAX_VALUE;
    Card minScoreCard = null;
    for (Card card: cardsInHand) {
      LuckyThirdteen.Suit suit = (LuckyThirdteen.Suit) card.getSuit();
      LuckyThirdteen.Rank rank = (LuckyThirdteen.Rank) card.getRank();
      int currentScore = suit.getMultiplicationFactor() * rank.getScoreCardValue();
      if (currentScore < minScore) {
        minScoreCard = card;
        minScore = currentScore;
      }
    }

    return minScoreCard;
  }
}
