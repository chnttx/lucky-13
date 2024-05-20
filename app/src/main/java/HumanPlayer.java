import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.GameGrid;
//import ch.aplu.jcardgame.CardListener;

public class HumanPlayer extends Player implements DiscardPileObserver {

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
  @Override
  public void update() {

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
    while (null == selected) GameGrid.delay(600);
    return selected;
  }

//  public void playTurn(boolean isAuto)
}
