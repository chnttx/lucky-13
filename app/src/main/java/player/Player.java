import java.util.*;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import lucky.LuckyThirdteen;

public class Player {
    protected Hand currentHand;
    protected Card selected;
    protected Queue<String> autoMovements;
    private int score;
    private boolean canSumTo13 = false;
    public Player(Deck deck) {
        this.currentHand = new Hand(deck);
//        this.discardPile = DiscardPile.getInstance();
        this.score = 0;
    }

    public static final int seed = 30008;
    static final Random random = new Random(seed);

    // Common method to draw a card from the deck
    private void drawACardToHand(Hand deck) {
        if (deck.isEmpty()) return;
        Card dealt = randomCard(deck.getCardList());
        dealt.removeFromHand(true);
//        addCard(dealt, true);
        currentHand.insert(dealt, true);
    }

    public Card discard() { return null; }
    public void addCard(Card card, boolean doDraw) {
        currentHand.insert(card, doDraw);
    }
//    public ArrayList<Card> getCurrentHand() { return currentHand.getCardList(); }

    public Hand getCurrentHand() { return currentHand; }
    public int getCardCount() { return currentHand.getNumberOfCards(); }
    protected boolean isHandEmpty() { return currentHand.isEmpty(); }
    public int getScore() { return score; }
    public void setScore(int scoreThisRound) { score = scoreThisRound; }
    public void setCanSumTo13() {canSumTo13 = true; }
    public boolean getCanSumTo13() {
        return canSumTo13;
    }

    protected Card randomCard(ArrayList<Card> list) {
        int x = random.nextInt(list.size());
        return list.get(x);
    }

    public void sortHand(Hand.SortType sortType, boolean doDraw) {
        currentHand.sort(sortType, doDraw);
    }

    public void addToQueue(String[] autoMovement) {
        autoMovements = new LinkedList<>(Arrays.asList(autoMovement));
    }
    public void playTurn(boolean isAuto, Hand pack, int thinkingTime, int delayTime) {
//        System.out.println(pack.getNumberOfCards());
        Card toDiscard;
        if (isAuto && !autoMovements.isEmpty()) {
            toDiscard = applyAutoMovement(pack, autoMovements.poll());
            GameGrid.delay(delayTime);
            if (toDiscard == null) {
                GameGrid.delay(thinkingTime);
                toDiscard = discard();
//                GameGrid.delay(600);
            }

            toDiscard.removeFromHand(true);
            DiscardPile.addCardToCardsPlayed(toDiscard);
//            GameGrid.delay()
            toDiscard.setVerso(false);
            GameGrid.delay(delayTime);
        } else {
            // Draw one card
            drawACardToHand(pack);
            toDiscard = discard();
//            GameGrid.delay(delayTime);
            toDiscard.removeFromHand(true);
            DiscardPile.addCardToCardsPlayed(toDiscard);
            toDiscard.setVerso(false);
            GameGrid.delay(delayTime);
        }
    }

//    protected void
    private Card applyAutoMovement(Hand pack, String nextMovement) {
        String[] cardStrings = nextMovement.split("-");
        String cardDealtString = cardStrings[0];
        Card dealt = getCardFromList(pack.getCardList(), cardDealtString);
        if (dealt != null) {
            dealt.removeFromHand(false);
            currentHand.insert(dealt, true);
        } else System.out.println("cannot draw card: " + cardDealtString + " - hand: " + currentHand);

        // There is a card to discard
        return (cardStrings.length > 1) ? getCardFromList(currentHand.getCardList(), cardStrings[1]): null;
    }
    protected Rank getRankFromString(String cardName) {
        String rankString = cardName.substring(0, cardName.length() - 1);
        Integer rankValue = Integer.parseInt(rankString);

        for (Rank rank : Rank.values()) {
            if (rank.getRankCardValue() == rankValue) {
                return rank;
            }
        }

        return Rank.ACE;
    }

    protected Suit getSuitFromString(String cardName) {
        String rankString = cardName.substring(0, cardName.length() - 1);
        String suitString = cardName.substring(cardName.length() - 1, cardName.length());
        Integer rankValue = Integer.parseInt(rankString);

        for (Suit suit : Suit.values()) {
            if (suit.getSuitShortHand().equals(suitString)) {
                return suit;
            }
        }
        return Suit.CLUBS;
    }


    protected Card getCardFromList(List<Card> cards, String cardName) {
        Rank cardRank = getRankFromString(cardName);
        Suit cardSuit = getSuitFromString(cardName);
        for (Card card: cards) {
            if (card.getSuit() == cardSuit && card.getRank() == cardRank) {
                return card;
            }
        }

        return null;
    }

    public String getStatusString(int playerIdx) {
        return "Player " + playerIdx + " thinking...";
    }
}
