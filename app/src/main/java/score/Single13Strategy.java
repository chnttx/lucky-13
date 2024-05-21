package score;

import player.Player;
import score.ScoreStrategy;

public class Single13Strategy implements ScoreStrategy {
  public void applyScore(Player[] players) {
    for (Player player: players) {
      if (player.getCanSumTo13()) player.setScore(100);
    }
  }
}
