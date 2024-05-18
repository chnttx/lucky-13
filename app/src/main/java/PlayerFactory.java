import ch.aplu.jcardgame.*;
//import ch.aplu.jcardgame.Hand;

import java.util.Properties;

public class PlayerFactory {

  public Player createPlayer(String type, Deck deck) {
    Player player;

    switch (type) {
      case "random" -> player = new RandomPlayer(deck);
      case "basic" -> player = new BasicPlayer(deck);
      case "clever" -> player = new CleverPlayer(deck);
      case "human" -> player = new HumanPlayer(deck);
      default -> {
        player = null;
      }
    }
    return player;
  }
}
