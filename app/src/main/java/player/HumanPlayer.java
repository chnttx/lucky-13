package player;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.GameGrid;
import player.Player;
//import ch.aplu.jcardgame.CardListener;

public class HumanPlayer extends Player {

//  private Card selected;
  public HumanPlayer(Deck deck) {
    super(deck);
    CardListener cardListener = new CardAdapter() {
      public void leftDoubleClicked(Card card) {
        selected = card;
        currentHand.setTouchEnabled(false);
      }
    };
    currentHand.addCardListener(cardListener);
  }



//  public void discard() {
//    currentHand.setTouchEnabled(true);
//    Card selected = null;
//    while (null == selected) GameGrid.delay(600);
//    selected.removeFromHand(true);
//  }
  public Card discard() {
    currentHand.setTouchEnabled(true);
    selected = null;
    while (null == selected) GameGrid.delay(2000);
    return selected;
  }

  public String getStatusString (int playerIdx) {
    return "player.Player " + playerIdx + " is playing. Please double click on a card to discard";
  }

//  public void playTurn(boolean isAuto)
}
