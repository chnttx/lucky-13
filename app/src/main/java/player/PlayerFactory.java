package player;

import ch.aplu.jcardgame.*;
import player.*;
//import ch.aplu.jcardgame.Hand;


public class PlayerFactory {
  private static PlayerFactory instance;
  private PlayerFactory() {}

  public static PlayerFactory getInstance() {
    if (instance == null) {
      instance = new PlayerFactory();
    }
    return instance;
  }

  // Method to create players
  public Player createPlayer(String type, Deck deck) {
    Player player;

    switch (type) {
      case "random" -> player = new RandomPlayer(deck);
      case "basic" -> player = new BasicPlayer(deck);
      case "clever" -> player = new CleverPlayer(deck);
      case "human" -> player = new HumanPlayer(deck);
      default -> {
        player = new RandomPlayer(deck);
      }
    }
    return player;
  }
}

