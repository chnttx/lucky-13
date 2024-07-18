package score;

import player.Player;

import java.util.List;

/* Base interface for implementing scoring strategy */
public interface ScoreStrategy {
  void applyScore(Player[] players);

  List<Integer> findWinners(Player[] Players);
}
