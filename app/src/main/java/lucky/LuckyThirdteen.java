package lucky;// LuckyThirteen.java

import ch.aplu.jcardgame.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import player.*;
import enums.*;
import score.ScoreStrategy;
import score.ScoreStrategyFactory;
import singletons.*;
import sum.*;

@SuppressWarnings("serial")
public class LuckyThirdteen extends CardGame {
    final String trumpImage[] = {"bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif"};

    static public final int seed = 30008;
    static final Random random = new Random(seed);
    private Properties properties;
    private StringBuilder logResult = new StringBuilder();
    private SumRule sumRuleComposite = null;

    public boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
    }

    private final String version = "1.0";
    public final int nbPlayers = 4;
    public final int nbStartCards = 2;
    public final int nbFaceUpCards = 2;
    private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
    private final Player[] players = new Player[nbPlayers];
    private int thinkingTime = 2000;
    private int delayTime = 600;
    private boolean isAuto = false;
    private Hand pack;
    private PlayingArea publicCards;
    private final RenderManager renderManager = new RenderManager(this, nbPlayers);

    private void initGame() {
        dealingOut(players);
        renderManager.renderPlayingArea();
        for (int i = 0; i < nbPlayers; i++) {
            players[i].sortHand(Hand.SortType.SUITPRIORITY, false);
        }
        renderManager.renderHands(players);
    }

    // return random Card from ArrayList
    public static Card randomCard(ArrayList<Card> list) {
        int x = random.nextInt(list.size());
        return list.get(x);
    }

    private Rank getRankFromString(String cardName) {
        String rankString = cardName.substring(0, cardName.length() - 1);
        Integer rankValue = Integer.parseInt(rankString);

        for (Rank rank : Rank.values()) {
            if (rank.getRankCardValue() == rankValue) {
                return rank;
            }
        }

        return Rank.ACE;
    }

    private Suit getSuitFromString(String cardName) {
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


    private Card getCardFromList(List<Card> cards, String cardName) {
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

    /* Create players */
    private void createPlayers() {
        PlayerFactory playerFactory = PlayerFactory.getInstance();
        for (int i = 0; i < nbPlayers; i++) {
            String playerMode = "players." + i;
            players[i] = playerFactory.createPlayer(properties.getProperty(playerMode), deck);
        }
    }

    /* Dealing out initial cards to players and playingArea */
    private void dealingOut(Player[] players) {
        pack = deck.toHand(false);
        String initialShareKey = "shared.initialcards";
        String initialShareValue = properties.getProperty(initialShareKey);
        if (initialShareValue != null) {
            String[] initialCards = initialShareValue.split(",");
            for (String initialCard : initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }
                Card card = getCardFromList(pack.getCardList(), initialCard);
                if (card != null) {
                    card.removeFromHand(true);
                    PlayingArea.addCardToPlayingArea(card);
                }
            }
        }
        int cardsToShare = nbFaceUpCards - PlayingArea.getPublicCards().size();

        // If shared cards is not in properties, randomly draw 2 cards from the pile to use as our public cards
        for (int j = 0; j < cardsToShare; j++) {
            if (pack.isEmpty()) return;
            Card dealt = randomCard(pack.getCardList());
            dealt.removeFromHand(true);
            PlayingArea.addCardToPlayingArea(dealt);
        }

        for (int i = 0; i < nbPlayers; i++) {
            String initialCardsKey = "players." + i + ".initialcards";
            String initialCardsValue = properties.getProperty(initialCardsKey);
            if (initialCardsValue == null) {
                continue;
            }

            String[] initialCards = initialCardsValue.split(",");
            for (String initialCard: initialCards) {
                if (initialCard.length() <= 1) continue;
                Card card = getCardFromList(pack.getCardList(), initialCard);
                if (card != null) {
                    card.removeFromHand(false);
                    players[i].addCard(card, false);
                }
            }

            int cardsDealtRandom = nbStartCards - players[i].getCardCount();
            for (int j = 0; j < cardsDealtRandom; j++) {
                if (pack.isEmpty()) return;
                Card dealt = randomCard(pack.getCardList());
                dealt.removeFromHand(false);
                players[i].addCard(dealt, false);
            }
        }
    }

    /* Logging methods */
    private void addCardPlayedToLog(int player, List<Card> cards) {
        if (cards.size() < 2) {
            return;
        }
        logResult.append("P" + player + "-");

        for (int i = 0; i < cards.size(); i++) {
            Rank cardRank = (Rank) cards.get(i).getRank();
            Suit cardSuit = (Suit) cards.get(i).getSuit();
            logResult.append(cardRank.getRankCardLog() + cardSuit.getSuitShortHand());
            if (i < cards.size() - 1) {
                logResult.append("-");
            }
        }
        logResult.append(",");
    }

    private void addRoundInfoToLog(int roundNumber) {
        logResult.append("Round" + roundNumber + ":");
    }

    private void addEndOfRoundToLog() {
        logResult.append("Score:");
        for (int i = 0; i < nbPlayers; i++) {
            logResult.append(players[i].getScore() + ",");
        }
        logResult.append("\n");
    }

    private void addEndOfGameToLog(List<Integer> winners) {
        logResult.append("EndGame:");
        for (int i = 0; i < nbPlayers; i++) {
            logResult.append(players[i].getScore());
            if (i < nbPlayers - 1) {
                logResult.append(",");
            }
        }
        logResult.append("\n");
        logResult.append("Winners:" + String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList())));
    }

    /* Handle game flow */
    private List<Integer> play() {
        renderManager.renderScore(players);
        delay(delayTime);
        for (int i = 1; i <= 4; i++) {
            addRoundInfoToLog(i);
            for (int j = 0; j < nbPlayers; j++) {
                Player currentPlayer = players[j];
                setStatusText(currentPlayer.getStatusString(j));
                currentPlayer.playTurn(isAuto, pack, thinkingTime, delayTime);
                addCardPlayedToLog(j, currentPlayer.getCurrentHand().getCardList());
            }
            addEndOfRoundToLog();
            delay(delayTime);
        }
        List<Integer> winners = calculateScore(players);
        renderManager.renderScore(players);
        return winners;
    }

    /* Choose appropriate scoring strategy based on number of players with 13 sum
    * and apply scoring strategy to assign scores and find winners */
    public List<Integer> calculateScore(Player[] players) {
         int numberOfPlayersWithSum13 = 0;
         for (Player player: players) {
             numberOfPlayersWithSum13 += (sumRuleComposite.checkSumEquals13(player)) ? 1 : 0;
         }
         ScoreStrategyFactory scoreStrategyFactory = ScoreStrategyFactory.getInstance();
         ScoreStrategy scoreStrategy = scoreStrategyFactory.getScoreStrategy(numberOfPlayersWithSum13);
         scoreStrategy.applyScore(players);
         return scoreStrategy.findWinners(players);
    }

    /* Set up auto movements for each player */
    private void setupAutoMovements(int playerIdx) {
        String playerAutoMovement = properties.getProperty("players." + playerIdx + ".cardsPlayed");
        if (playerAutoMovement != null) {
            String[] autoMovements = playerAutoMovement.split(",");
            players[playerIdx].addToQueue(autoMovements);
        }
    }

    /* Main method for running our game */
    public String runApp() {
        setTitle("LuckyThirteen (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        createPlayers();
        for (int i = 0; i < nbPlayers; i++) renderManager.renderInitialScore(i, players);
        for (int i = 0; i < nbPlayers; i++) setupAutoMovements(i);
        initGame();
        List<Integer> winners = play();

        String winText;
        if (winners.size() == 1) {
            winText = "Game over. Winner is player: " +
                    winners.iterator().next();
        } else {
            winText = "Game Over. Drawn winners are players: " +
                    String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList()));
        }
        renderManager.renderEndGame();
        setStatusText(winText);
        addEndOfGameToLog(winners);
        refresh();
        return logResult.toString();

    }


    public LuckyThirdteen(Properties properties) {
        super(700, 700, 30);
        this.properties = properties;
        isAuto = Boolean.parseBoolean(properties.getProperty("isAuto"));
        thinkingTime = Integer.parseInt(properties.getProperty("thinkingTime", "200"));
        delayTime = Integer.parseInt(properties.getProperty("delayTime", "50"));
        publicCards = PlayingArea.getInstance(deck);
        sumRuleComposite = new SumRuleComposite();
    }

}
