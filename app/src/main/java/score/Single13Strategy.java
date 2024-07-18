package score;

import player.Player;
import score.ScoreStrategy;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* Only 1 player has sum 13*/
public class Single13Strategy implements ScoreStrategy {
  public void applyScore(Player[] players) {
    for (Player player: players) {
      if (player.getCanSumTo13()) player.setScore(100);
    }
  }

  public List<Integer> findWinners(Player[] players) {
    List<Integer> ans = new ArrayList<>();
    int maxScore = 0;
    for (Player player: players) maxScore = Math.max(player.getScore(), maxScore);
    for (int i = 0; i < players.length; i++) {
      if (players[i].getScore() == maxScore) ans.add(i);
    }
    return ans;
  }
}
