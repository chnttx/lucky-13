package score;
public class ScoreStrategyFactory {
  private static ScoreStrategyFactory instance;

  private ScoreStrategyFactory() {}

  public static ScoreStrategyFactory getInstance() {
    if (instance == null) {
      instance = new ScoreStrategyFactory();
    }
    return instance;
  }

  public ScoreStrategy getScoreStrategy(int numberOfPlayersWithSum13) {
    ScoreStrategy scoreRule;

    switch (numberOfPlayersWithSum13) {
      case 0 -> scoreRule = new No13Strategy();
      case 1 -> scoreRule = new Single13Strategy();
      default -> scoreRule = new Multi13Strategy();
    }
    return scoreRule;
  }
}

