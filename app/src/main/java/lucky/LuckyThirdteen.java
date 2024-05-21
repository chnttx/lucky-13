// LuckyThirteen.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class LuckyThirdteen extends CardGame {

//    public enum Suit {
//        SPADES ("S", 4), HEARTS ("H", 3),
//        DIAMONDS ("D", 2), CLUBS ("C", 1);
//        private String suitShortHand = "";
//        private int multiplicationFactor = 1;
//        public static final int PUBLIC_CARD_MULTIPLICATION_FACTOR = 2;
//        Suit(String shortHand, int multiplicationFactor) {
//            this.suitShortHand = shortHand;
//            this.multiplicationFactor = multiplicationFactor;
//        }
//
//        public String getSuitShortHand() {
//            return suitShortHand;
//        }
//
//        public int getMultiplicationFactor() {
//            return multiplicationFactor;
//        }
//    }
//
//    public enum Rank {
//        // Reverse order of rank importance (see rankGreater() below)
//        ACE (1, 1, 0, 1),
//        KING (13, 13, 10, 11, 12, 13),
//        QUEEN (12, 12, 10, 11, 12, 13),
//        JACK (11, 11, 10, 11, 12, 13),
//        TEN (10, 10, 10), NINE (9, 9, 9),
//        EIGHT (8, 8, 8), SEVEN (7, 7, 7),
//        SIX (6, 6, 6), FIVE (5, 5, 5),
//        FOUR (4, 4, 4), THREE (3, 3, 3),
//        TWO (2, 2, 2);
//
//        private int rankCardValue = 1;
//        private int scoreValue = 0;
//        private int []possibleSumValues = null;
//        Rank(int rankCardValue, int scoreValue, int... possibleSumValues) {
//            this.rankCardValue = rankCardValue;
//            this.scoreValue = scoreValue;
//            this.possibleSumValues = possibleSumValues;
//        }
//
//        public int getRankCardValue() {
//            return rankCardValue;
//        }
//
//        public int getScoreCardValue() { return scoreValue; }
//
//        public int[] getPossibleSumValues() {
//            return possibleSumValues;
//        }
//
//        public String getRankCardLog() {
//            return String.format("%d", rankCardValue);
//        }
//    }

    final String trumpImage[] = {"bigspade.gif", "bigheart.gif", "bigdiamond.gif", "bigclub.gif"};

    static public final int seed = 30008;
//    static public final long seed = System.currentTimeMillis() / 1000L;
    static final Random random = new Random(seed);
    private Properties properties;
    private StringBuilder logResult = new StringBuilder();
    private List<List<String>> playerAutoMovements = new ArrayList<>();
    private SumRuleComposite sumRuleComposite = null;
    private ScoreRuleFactory scoreRuleFactory = null;

    public boolean rankGreater(Card card1, Card card2) {
        return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
    }

    private final String version = "1.0";
    public final int nbPlayers = 4;
    public final int nbStartCards = 2;
    public final int nbFaceUpCards = 2;
    private final int handWidth = 400;
    private final int trickWidth = 40;
    public static final int THIRTEEN_GOAL = 13;
    private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
//    private final List<Player> players = new ArrayList<>();
    private final Player[] players = new Player[nbPlayers];
//    private final Location[] handLocations = {
//            new Location(350, 625),
//            new Location(75, 350),
//            new Location(350, 75),
//            new Location(625, 350)
//    };
//    private final Location[] scoreLocations = {
//            new Location(575, 675),
//            new Location(25, 575),
//            new Location(575, 25),
//            // new Location(650, 575)
//            new Location(575, 575)
//    };
//    private Actor[] scoreActors = {null, null, null, null};
    private final Location trickLocation = new Location(350, 350);
    private final Location textLocation = new Location(350, 450);
    private int thinkingTime = 2000;
    private int delayTime = 600;
    private Hand[] hands;
    public void setStatus(String string) {
        setStatusText(string);
    }

    private int[] scores = new int[nbPlayers];

    private int[] autoIndexHands = new int [nbPlayers];
    private boolean isAuto = false;
    private Hand playingArea;
    private Hand pack;

    private PublicCards publicCards;

    private final PlayerFactory playerFactory = new PlayerFactory();
//    private ScoreRule scoreRule = null;
    private final RenderManager renderManager = new RenderManager(this, nbPlayers);
//    Font bigFont = new Font("Arial", Font.BOLD, 36);

//    private void initScore() {
//        for (int i = 0; i < nbPlayers; i++) {
//            // scores[i] = 0;
//            String text = "[" + String.valueOf(scores[i]) + "]";
//            scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
//            addActor(scoreActors[i], scoreLocations[i]);
//        }
//    }

    private int getScorePrivateCard(Card card) {
        Rank rank = (Rank) card.getRank();
        Suit suit = (Suit) card.getSuit();

        return rank.getScoreCardValue() * suit.getMultiplicationFactor();
    }

    private int getScorePublicCard(Card card) {
        Rank rank = (Rank) card.getRank();
        return rank.getScoreCardValue() * Suit.PUBLIC_CARD_MULTIPLICATION_FACTOR;
    }

    private int calculateMaxScoreForThirteenPlayer(int playerIndex) {
        List<Card> privateCards = hands[playerIndex].getCardList();
        List<Card> publicCards = playingArea.getCardList();
        Card privateCard1 = privateCards.get(0);
        Card privateCard2 = privateCards.get(1);
        Card publicCard1 = publicCards.get(0);
        Card publicCard2 = publicCards.get(1);

        int maxScore = 0;
        if (isThirteenCards(privateCard1, privateCard2)) {
            int score = getScorePrivateCard(privateCard1) + getScorePrivateCard(privateCard2);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if (isThirteenCards(privateCard1, publicCard1)) {
            int score = getScorePrivateCard(privateCard1) + getScorePublicCard(publicCard1);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if (isThirteenCards(privateCard1, publicCard2)) {
            int score = getScorePrivateCard(privateCard1) + getScorePublicCard(publicCard2);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if (isThirteenCards(privateCard2, publicCard1)) {
            int score = getScorePrivateCard(privateCard2) + getScorePublicCard(publicCard1);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        if(isThirteenCards(privateCard2, publicCard2)) {
            int score = getScorePrivateCard(privateCard2) + getScorePublicCard(publicCard2);
            if (maxScore < score) {
                maxScore = score;
            }
        }

        return maxScore;
    }

    private void calculateScoreEndOfRound() {
        List<Boolean> isThirteenChecks = Arrays.asList(false, false, false, false);
        for (int i = 0; i < hands.length; i++) {
            isThirteenChecks.set(i, isThirteen(i));
        }
        List<Integer> indexesWithThirteen = new ArrayList<>();
        for (int i = 0; i < isThirteenChecks.size(); i++) {
            if (isThirteenChecks.get(i)) {
                indexesWithThirteen.add(i);
            }
        }
        long countTrue = indexesWithThirteen.size();
        Arrays.fill(scores, 0);
        if (countTrue == 1) {
            int winnerIndex = indexesWithThirteen.get(0);
            scores[winnerIndex] = 100;
        } else if (countTrue > 1) {
            for (Integer thirteenIndex : indexesWithThirteen) {
                scores[thirteenIndex] = calculateMaxScoreForThirteenPlayer(thirteenIndex);
            }

        } else {
            for (int i = 0; i < scores.length; i++) {
                scores[i] = getScorePrivateCard(hands[i].getCardList().get(0)) +
                        getScorePrivateCard(hands[i].getCardList().get(1));
            }
        }
    }

//    private void updateScore(int player) {
//        removeActor(scoreActors[player]);
//        int displayScore = Math.max(scores[player], 0);
//        String text = "P" + player + "[" + String.valueOf(displayScore) + "]";
//        scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
//        addActor(scoreActors[player], scoreLocations[player]);
//    }

    private void initScores() {
        Arrays.fill(scores, 0);
    }

    private Card selected;

    private void initGame() {
        dealingOut(players, nbFaceUpCards, nbStartCards);
        renderManager.drawPlayingArea();
        for (int i = 0; i < nbPlayers; i++) {
            players[i].sortHand(Hand.SortType.SUITPRIORITY, false);
        }
        // Set up human player for interaction
//        CardListener cardListener = new CardAdapter()  // Human Player plays card
//        {
//            public void leftDoubleClicked(Card card) {
//                selected = card;
//                hands[0].setTouchEnabled(false);
//            }
//        };
//        hands[0].addCardListener(cardListener);
        // graphics
//        RowLayout[] layouts = new RowLayout[nbPlayers];
//        for (int i = 0; i < nbPlayers; i++) {
//            layouts[i] = new RowLayout(handLocations[i], handWidth);
//            layouts[i].setRotationAngle(90 * i);
//            // layouts[i].setStepDelay(10);
//            hands[i].setView(this, layouts[i]);
//            hands[i].setTargetArea(new TargetArea(trickLocation));
//            hands[i].draw();
//        }
        renderManager.drawHands(players);
    }


    // return random Enum value
    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    // return random Card from ArrayList
    public static Card randomCard(ArrayList<Card> list) {
        int x = random.nextInt(list.size());
        return list.get(x);
    }

    public Card getRandomCard(Hand hand) {
        dealACardToHand(hand);

        delay(thinkingTime);

        int x = random.nextInt(hand.getCardList().size());
        return hand.getCardList().get(x);
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

    private Card applyAutoMovement(Hand hand, String nextMovement) {
        if (pack.isEmpty()) return null;
        String[] cardStrings = nextMovement.split("-");
        String cardDealtString = cardStrings[0];
        Card dealt = getCardFromList(pack.getCardList(), cardDealtString);
        if (dealt != null) {
            dealt.removeFromHand(false);
            hand.insert(dealt, true);
        } else {
            System.out.println("cannot draw card: " + cardDealtString + " - hand: " + hand);
        }

        if (cardStrings.length > 1) {
            String cardDiscardString = cardStrings[1];
            return getCardFromList(hand.getCardList(), cardDiscardString);
        } else {
            return null;
        }
    }


    private boolean isThirteenFromPossibleValues(int[] possibleValues1, int[] possibleValues2) {
        for (int value1 : possibleValues1) {
            for (int value2 : possibleValues2) {
                if (value1 + value2 == THIRTEEN_GOAL) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isThirteenCards(Card card1, Card card2) {
        Rank rank1 = (Rank) card1.getRank();
        Rank rank2 = (Rank) card2.getRank();
        return isThirteenFromPossibleValues(rank1.getPossibleSumValues(), rank2.getPossibleSumValues());
    }

    private boolean isThirteenMixedCards(List<Card> privateCards, List<Card> publicCards) {
        for (Card privateCard : privateCards) {
            for (Card publicCard : publicCards) {
                if (isThirteenCards(privateCard, publicCard)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isThirteen(int playerIndex) {
        List<Card> privateCards = hands[playerIndex].getCardList();
        List<Card> publicCards = playingArea.getCardList();
        boolean isThirteenPrivate = isThirteenCards(privateCards.get(0), privateCards.get(1));
        boolean isThirteenMixed = isThirteenMixedCards(privateCards, publicCards);
        return isThirteenMixed || isThirteenPrivate;
    }


    // Create players with their initial hand
    private void createPlayers() {
        for (int i = 0; i < nbPlayers; i++) {
            String playerMode = "players." + i;
//            String playerAutoMovement = "players." + i + "cardsPlayed";

            players[i] = playerFactory.createPlayer(properties.getProperty(playerMode), deck);
//            String
//            String playerInitial = "players. "/
        }
    }

    private void dealingOut(Player[] players, int nbSharedCards, int nbCardsPerPlayer) {
        pack = deck.toHand(false);
//        System.out.println(pack.getCardList().size());
        String initialShareKey = "shared.initialcards";
        String initialShareValue = properties.getProperty(initialShareKey);
//        publicCards = PublicCards.getInstance();
        if (initialShareValue != null) {
            String[] initialCards = initialShareValue.split(",");
            for (String initialCard : initialCards) {
                if (initialCard.length() <= 1) {
                    continue;
                }
                Card card = getCardFromList(pack.getCardList(), initialCard);
                if (card != null) {
                    card.removeFromHand(true);
//                    playingArea.insert(card, true);
                    PublicCards.addCardToHand(card);
                }
            }
        }
        int cardsToShare = nbSharedCards - PublicCards.getPublicCards().size();

        // If shared cards is not in properties, randomly draw 2 cards from the pile to use as our public cards
        for (int j = 0; j < cardsToShare; j++) {
            if (pack.isEmpty()) return;
            Card dealt = randomCard(pack.getCardList());
            dealt.removeFromHand(true);
            PublicCards.addCardToHand(dealt);
//            playingArea.insert(dealt, true);
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
//                    Enum<Suit> suit = card.getSuit();
//                    System.out.println("Card name: " + card.toString() + ", " +card.getSuitId());
                }
            }


            int cardsDealtRandom = nbCardsPerPlayer - players[i].getCardCount();
            for (int j = 0; j < cardsDealtRandom; j++) {
                if (pack.isEmpty()) return;
                Card dealt = randomCard(pack.getCardList());
                dealt.removeFromHand(false);
                players[i].addCard(dealt, false);
            }

        }

//        for (int i = 0; i < pack.getNumberOfCards(); i++) {
//            System.out.println(pack.getCardList().get(i).toString());
//        }
    }

    private void dealACardToHand(Hand hand) {
        if (pack.isEmpty()) return;
        Card dealt = randomCard(pack.getCardList());
        dealt.removeFromHand(false);
        hand.insert(dealt, true);
    }

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
        }
        logResult.append("\n");
        logResult.append("Winners:" + String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList())));
    }
    private void play() {
        renderManager.updateScore(players);
        for (int i = 1; i <= 4; i++) {
            for (int j = 0; j < nbPlayers; j++) {
                Player currentPlayer = players[j];
                setStatusText(currentPlayer.getStatusString(j));
                currentPlayer.playTurn(isAuto, pack, thinkingTime, delayTime);
            }
//            addEndOfRoundToLog();
//            addRoundInfoToLog(i);
            delay(delayTime);
        }
        updateScore(players);
    }

    public void updateScore(Player[] players) {
         int numberOfPlayersWith13 = sumRuleComposite.getAllPlayersWithSum13(players);

//         System.out.println(playerIdxWithSum13.size());
         ScoreStrategy scoreRule = scoreRuleFactory.getScoreRule(numberOfPlayersWith13);
         scoreRule.applyScore(players);
         for (int i = 0; i < nbPlayers; i++) {
             System.out.println(players[i].getScore());
         }
    }
    private void playGame() {
        // End trump suit
        int winner = 0;
        int roundNumber = 1;
//        for (int i = 0; i < nbPlayers; i++) updateScore(i);
        renderManager.updateScore(players);

        List<Card>cardsPlayed = new ArrayList<>();
        addRoundInfoToLog(roundNumber);

        int nextPlayer = 0;
        while(roundNumber <= 4) {
            selected = null;
            boolean finishedAuto = false;

            if (isAuto) {
                int nextPlayerAutoIndex = autoIndexHands[nextPlayer];
                List<String> nextPlayerMovement = playerAutoMovements.get(nextPlayer);
                String nextMovement = "";

                if (nextPlayerMovement.size() > nextPlayerAutoIndex) {
                    nextMovement = nextPlayerMovement.get(nextPlayerAutoIndex);
                    nextPlayerAutoIndex++;

                    autoIndexHands[nextPlayer] = nextPlayerAutoIndex;
                    Hand nextHand = hands[nextPlayer];

                    // Apply movement for player
                    selected = applyAutoMovement(nextHand, nextMovement);
                    delay(delayTime);
                    if (selected != null) {
                        selected.removeFromHand(true);
                    } else {
                        selected = getRandomCard(hands[nextPlayer]);
                        selected.removeFromHand(true);
                    }
                } else {
                    finishedAuto = true;
                }
            }

            if (!isAuto || finishedAuto) {
                if (0 == nextPlayer) {
                    hands[0].setTouchEnabled(true);
                    setStatusText("Player 0 is playing. Please double click on a card to discard");

                    selected = null;
                    dealACardToHand(hands[0]);
                    while (null == selected) delay(delayTime);
                    selected.removeFromHand(true);
                } else {
                    setStatusText("Player " + nextPlayer + " thinking...");
                    selected = getRandomCard(hands[nextPlayer]);
                    selected.removeFromHand(true);
                }
            }

            addCardPlayedToLog(nextPlayer, hands[nextPlayer].getCardList());
            if (selected != null) {
                cardsPlayed.add(selected);
                selected.setVerso(false);  // In case it is upside down
                delay(delayTime);
                // End Follow
            }

            nextPlayer = (nextPlayer + 1) % nbPlayers;

            if (nextPlayer == 0) {
                roundNumber ++;
                addEndOfRoundToLog();

                if (roundNumber <= 4) {
                    addRoundInfoToLog(roundNumber);
                }
            }

            if (roundNumber > 4) {
                calculateScoreEndOfRound();
            }
            delay(delayTime);
        }
    }

    private void setupAutoMovements(int playerIdx) {
        String playerAutoMovement = properties.getProperty("players." + playerIdx + ".cardsPlayed");
        if (playerAutoMovement != null) {
            String[] autoMovements = playerAutoMovement.split(",");
            players[playerIdx].addToQueue(autoMovements);
        }
    }
//    private void setupPlayerAutoMovements() {
//        String player0AutoMovement = properties.getProperty("players.0.cardsPlayed");
//        String player1AutoMovement = properties.getProperty("players.1.cardsPlayed");
//        String player2AutoMovement = properties.getProperty("players.2.cardsPlayed");
//        String player3AutoMovement = properties.getProperty("players.3.cardsPlayed");
//
//        String[] playerMovements = new String[] {"", "", "", ""};
//        if (player0AutoMovement != null) {
//            playerMovements[0] = player0AutoMovement;
//        }
//
//        if (player1AutoMovement != null) {
//            playerMovements[1] = player1AutoMovement;
//        }
//
//        if (player2AutoMovement != null) {
//            playerMovements[2] = player2AutoMovement;
//        }
//
//        if (player3AutoMovement != null) {
//            playerMovements[3] = player3AutoMovement;
//        }
//
//        for (int i = 0; i < playerMovements.length; i++) {
//            String movementString = playerMovements[i];
//            if (movementString.equals("")) {
//                playerAutoMovements.add(new ArrayList<>());
//                continue;
//            }
//            List<String> movements = Arrays.asList(movementString.split(","));
//            playerAutoMovements.add(movements);
//        }
//    }

    public String runApp() {
        setTitle("LuckyThirteen (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        createPlayers();
        for (int i = 0; i < nbPlayers; i++) renderManager.drawInitialScore(i, players);
//        drawManager.drawInitialScore(players);
//        initScore();
        for (int i = 0; i < nbPlayers; i++) setupAutoMovements(i);
//        setupPlayerAutoMovements();
//        createPlayers();

//        System.out.println(pack.getNumberOfCards());
        initGame();
//        playGame();
        play();

//        for (int i = 0; i < nbPlayers; i++) updateScore(i);
//        drawManager.updateScore(players);
//        int maxScore = 0;
//        for (int i = 0; i < nbPlayers; i++) if (scores[i] > maxScore) maxScore = scores[i];
//        List<Integer> winners = new ArrayList<Integer>();
//        for (int i = 0; i < nbPlayers; i++) if (scores[i] == maxScore) winners.add(i);
//        String winText;
//        if (winners.size() == 1) {
//            winText = "Game over. Winner is player: " +
//                    winners.iterator().next();
//        } else {
//            winText = "Game Over. Drawn winners are players: " +
//                    String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toList()));
//        }
        addActor(new Actor("sprites/gameover.gif"), textLocation);
//        setStatusText(winText);
//        refresh();
//        addEndOfGameToLog(winners);
//
//        return logResult.toString();

        return "";
    }

    public LuckyThirdteen(Properties properties) {
        super(700, 700, 30);
        this.properties = properties;
        isAuto = Boolean.parseBoolean(properties.getProperty("isAuto"));
        thinkingTime = Integer.parseInt(properties.getProperty("thinkingTime", "200"));
        delayTime = Integer.parseInt(properties.getProperty("delayTime", "50"));
        publicCards = PublicCards.getInstance(deck);
        sumRuleComposite = new SumRuleComposite();
        sumRuleComposite.addSumRule(new SumRule1());
        sumRuleComposite.addSumRule(new SumRule2());
        sumRuleComposite.addSumRule(new SumRule3());
        scoreRuleFactory = new ScoreRuleFactory();
    }

}