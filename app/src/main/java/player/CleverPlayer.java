import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import player.Player;

public class CleverPlayer extends Player {
  public CleverPlayer(Deck deck) {
    super(deck);
  }
  @Override
  public Card discard() { return null;}

}
