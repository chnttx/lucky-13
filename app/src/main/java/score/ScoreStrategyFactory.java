package score;

import score.Multi13Strategy;

public class ScoreStrategyFactory {
  public ScoreStrategy getScoreRule(int numberOfPlayersWithSum13) {
    ScoreStrategy scoreRule;
    switch (numberOfPlayersWithSum13) {
      case 0 -> scoreRule = new No13Strategy();
      case 1 -> scoreRule = new Single13Strategy();
      default -> scoreRule = new Multi13Strategy();
    }

    return scoreRule;
  }
}
