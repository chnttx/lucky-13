import ch.aplu.jcardgame.*;
//import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.*;

/* Class for drawing all objects */
public class DrawManager {

  private final CardGame  cardGame;
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
  public DrawManager(CardGame cardGame) {
    this.cardGame = cardGame;
  }

  public void drawHands(int nbPlayers, Player[] allPlayers) {
//    RowLayout[] layouts = new RowLayout(handLocations[)
    RowLayout[] layouts = new RowLayout[nbPlayers];
    for (int i = 0; i < nbPlayers; i++) {
      layouts[i] = new RowLayout(handLocations[i], handWidth);
      layouts[i].setRotationAngle(((double) fullAngle / nbPlayers) * i);

      allPlayers[i].getCurrentHand().setView(cardGame, layouts[i]);
      allPlayers[i].getCurrentHand().setTargetArea(new TargetArea(trickLocation));
      allPlayers[i].getCurrentHand().draw();
    }
  }
}

