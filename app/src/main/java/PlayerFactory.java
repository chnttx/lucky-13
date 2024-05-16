import ch.aplu.jcardgame.Hand;

import java.util.Properties;

public class PlayerFactory {

  public Player createPlayer(String type) {
    Player player;

    switch (type) {
      case "random" -> player = new RandomPlayer();
      case "basic" -> player = new BasicPlayer();
      case "clever" -> player = new CleverPlayer();
      case "human" -> player = new HumanPlayer();
      default -> {
        player = null;
      }
    }
    return player;
  }
}
