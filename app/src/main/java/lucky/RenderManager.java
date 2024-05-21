import ch.aplu.jcardgame.*;
//import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.*;
import player.Player;

import java.awt.*;

/* Class for drawing all objects */
public class RenderManager {

  private final CardGame cardGame;
  private final int handWidth = 400, trickWidth = 40, fullAngle = 360;
  private final Location[] handLocations = {
      new Location(350, 625),
      new Location(75, 350),
      new Location(350, 75),
      new Location(625, 350)
  };
  private final Location[] scoreLocations = {
      new Location(575, 675),
      new Location(25, 575),
      new Location(575, 25),
      // new Location(650, 575)
      new Location(575, 575)
  };
  private final Location trickLocation = new Location(350, 350);
  private final Location textLocation = new Location(350, 450);
  private Actor[] scoreActors;
  private int nbPlayers;
  Font bigFont = new Font("Arial", Font.BOLD, 36);
  public RenderManager(CardGame cardGame, int nbPlayers) {
    this.cardGame = cardGame;
    this.nbPlayers = nbPlayers;
    this.scoreActors = new Actor[nbPlayers];
  }

  public void drawHands(Player[] allPlayers) {
    RowLayout[] layouts = new RowLayout[nbPlayers];
    for (int i = 0; i < nbPlayers; i++) {
      layouts[i] = new RowLayout(handLocations[i], handWidth);
      layouts[i].setRotationAngle(((double) fullAngle / nbPlayers) * i);

      allPlayers[i].getCurrentHand().setView(cardGame, layouts[i]);
      allPlayers[i].getCurrentHand().setTargetArea(new TargetArea(trickLocation));
      allPlayers[i].getCurrentHand().draw();
    }

  }

  public void drawInitialScore(int playerIdx, Player[] allPlayers) {
      String text = "[" + Math.max(allPlayers[playerIdx].getScore(), 0) + "]";
      scoreActors[playerIdx] = new TextActor(text, Color.WHITE, cardGame.bgColor, bigFont);
      cardGame.addActor(scoreActors[playerIdx], scoreLocations[playerIdx]);
  }

  public void updateScore(Player[] allPlayers) {
    for (int i = 0; i < nbPlayers; i++) {
      cardGame.removeActor(scoreActors[i]);
      String text = "P" + i + "[" + Math.max(allPlayers[i].getScore(), 0) + "]";
      scoreActors[i] = new TextActor(text, Color.WHITE, cardGame.bgColor, bigFont);
      cardGame.addActor(scoreActors[i], scoreLocations[i]);
    }
  }

  public void drawPlayingArea() {
    Hand publicArea = PublicCards.getPlayingArea();
    publicArea.setView(cardGame, new RowLayout(trickLocation, (publicArea.getNumberOfCards() + 2) * trickWidth));
    publicArea.draw();
  }

//  public void drawActor()

}

