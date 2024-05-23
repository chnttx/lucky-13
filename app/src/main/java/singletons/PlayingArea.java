package singletons;

import ch.aplu.jcardgame.*;
import java.util.*;

public class PlayingArea {
  private static PlayingArea instance;

  private static Hand playingArea;
  private PlayingArea(Deck deck) {
    playingArea = new Hand(deck);
  }

  public static PlayingArea getInstance(Deck deck) {
    return (instance == null) ? new PlayingArea(deck) : instance;
  }

  public static Hand getPlayingArea() { return playingArea; }
  public static List<Card> getPublicCards() { return playingArea.getCardList(); }
  public static void addCardToPlayingArea(Card card) { playingArea.insert(card, true); }
//  public
}
