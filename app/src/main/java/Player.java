import java.util.*;

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
public class Player {
    protected Hand currentHand;
    protected Card selected;
    protected Queue<String> autoMovements;
//    protected DiscardPile discardPile;
//    protected DiscardPile discardPile;
    private int score;
    public Player(Deck deck) {
        this.currentHand = new Hand(deck);
//        this.discardPile = DiscardPile.getInstance();
        this.score = 0;
    }

    // Common method to draw a card from the deck
    public void drawACardToHand(Hand deck) {
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
    public void setScore(int scoreThisRound) { score += scoreThisRound; }
    protected Card randomCard(ArrayList<Card> list) {
        int x = LuckyThirdteen.random.nextInt(list.size());
        return list.get(x);
    }

    public void sortHand(Hand.SortType sortType, boolean doDraw) {
        currentHand.sort(sortType, doDraw);
    }

    public void addToQueue(String[] autoMovement) {
        autoMovements = new LinkedList<>(Arrays.asList(autoMovement));
    }
    public void playTurn(boolean isAuto, Hand pack) {
        Card toDiscard = null;
        if (isAuto && !autoMovements.isEmpty()) {
            toDiscard = applyAutoMovement(pack, autoMovements.poll());
            if (toDiscard != null) {
                toDiscard.removeFromHand(true);
            } else {
                // getCard() would vary depending on type of player
                toDiscard = discard();
                toDiscard.removeFromHand(true);
                DiscardPile.addCardToCardsPlayed(toDiscard);
            }
//            if (pack.isEmpty( )) ;
//            String[] currentMove = autoMovements.poll().split("-");
//            String cardDealtString = currentMove[0];
//            Card dealt = getCardFromList(pack.getCardList(), cardDealtString);
//            if (dealt != null) {
//
//            }
        } else {
            // Draw one card
            drawACardToHand(pack);
            toDiscard = discard();
            toDiscard.removeFromHand(true);
            DiscardPile.addCardToCardsPlayed(toDiscard);
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
            if (card.getSuit() == cardSuit
                && card.getRank() == cardRank) {
                return card;
            }
        }

        return null;
    }

//    public void setStatus() {}
//    public void humanPlayers
}
