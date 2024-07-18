package player;

import java.util.*;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import enums.*;
import singletons.*;

/* Base class for player */
public class Player {
    protected Hand currentHand;
    private Queue<String> autoMovements;
    private int score;
    private boolean canSumTo13 = false;
    private final List<List<Card>> allCombinationWithSum13 = new ArrayList<>();
    public Player(Deck deck) {
        this.currentHand = new Hand(deck);
        this.score = 0;
    }

    public static final int seed = 30008;
    static final Random random = new Random(seed);

    // Common method to draw a card from the deck
    private void drawCard(Hand pack) {
        if (pack.isEmpty()) return;
        Card dealt = randomCard(pack.getCardList());
        dealt.removeFromHand(true);
        currentHand.insert(dealt, true);
    }

    /* Remove a card from current hand and into the discard pile.
    * Which card is discarded depends on player type */
    public Card discard() { return null; }

    /* Add card to current player's hand*/
    public void addCard(Card card, boolean doDraw) {
        currentHand.insert(card, doDraw);
    }
    public Hand getCurrentHand() { return currentHand; }
    public int getCardCount() { return currentHand.getNumberOfCards(); }
    public int getScore() { return score; }
    public void setScore(int scoreThisRound) { score = scoreThisRound; }
    public void setCanSumTo13() {canSumTo13 = true; }
    public boolean getCanSumTo13() {
        return canSumTo13;
    }
    /* Get random card from card list*/
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
    /* Main method for handling player behaviour at each turn */
    public void playTurn(boolean isAuto, Hand pack, int thinkingTime, int delayTime) {
        Card toDiscard;
        if (isAuto && !autoMovements.isEmpty()) {
            toDiscard = applyAutoMovement(pack, autoMovements.poll());
            GameGrid.delay(delayTime);
            if (toDiscard == null) {
                GameGrid.delay(thinkingTime);
                toDiscard = discard();
            }

            toDiscard.removeFromHand(true);
            DiscardPile.addCardToCardsPlayed(toDiscard);
            toDiscard.setVerso(false);
            GameGrid.delay(delayTime);
        } else {
            // Draw one card
            drawCard(pack);
            toDiscard = discard();
            toDiscard.removeFromHand(true);
            DiscardPile.addCardToCardsPlayed(toDiscard);
            toDiscard.setVerso(false);
            GameGrid.delay(delayTime * 2L);
        }
    }

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

    /* Set status for GUI text */
    public String getStatusString(int playerIdx) {
        return "Player " + playerIdx + " thinking...";
    }

    public void addCombination(List<Card> cards) {
        allCombinationWithSum13.add(cards);
    }

    public List<List<Card>> getAllCombinationWithSum13() { return allCombinationWithSum13; }

    /* Method to check if a card is a private card or public card for scoring*/
    public boolean isCardInHand(Card card) { return currentHand.contains(card);}
}
