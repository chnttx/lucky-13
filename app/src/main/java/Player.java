import java.util.*;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
public class Player {
    protected Hand currentHand;
    protected Card selected;
    private int score = 0;
    public Player(Deck deck) {
        this.currentHand = new Hand(deck);
    }

    // Common method to draw a card from the deck
    public void draw(Hand deck) {
        if (deck.isEmpty()) return;
        Card dealt = randomCard(deck.getCardList());
        dealt.removeFromHand(true);
        addCard(dealt, true);
    }

    public void discard() {}
    public void addCard(Card card, boolean doDraw) {
        currentHand.insert(card, doDraw);
    }
//    public ArrayList<Card> getCurrentHand() { return currentHand.getCardList(); }

    public Hand getCurrentHand() { return currentHand; }
    public int getCardCount() { return currentHand.getNumberOfCards(); }
    protected boolean isHandEmpty() { return currentHand.isEmpty(); }
    public int getScore() { return score; }
    public void setScore(int scoreThisRound) { score += scoreThisRound; }
    protected Card randomCard(ArrayList<Card> list) {
        int x = LuckyThirdteen.random.nextInt(list.size());
        return list.get(x);
    }

    public void sortHand(Hand.SortType sortType, boolean doDraw) {
        currentHand.sort(sortType, doDraw);
    }

//    public void playTurn(int playerIdx) {
//        setStatusText("Player
//    }

}
