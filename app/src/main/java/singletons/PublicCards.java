import ch.aplu.jcardgame.*;
import java.util.*;

public class PublicCards {
  private static PublicCards instance;

//  private static final List<Card> publicCards = new ArrayList<>();

  private static Hand playingArea;
  private PublicCards(Deck deck) {
    playingArea = new Hand(deck);
  }

  public static PublicCards getInstance(Deck deck) {
    return (instance == null) ? new PublicCards(deck) : instance;
  }

  public static Hand getPlayingArea() { return playingArea; }
  public static List<Card> getPublicCards() { return playingArea.getCardList(); }
  public static void addCardToHand(Card card) { playingArea.insert(card, true); }
//  public
}
