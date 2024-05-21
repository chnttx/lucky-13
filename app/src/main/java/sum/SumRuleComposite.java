package sum;

import player.Player;
import sum.SumRule;

import java.util.*;

public class SumRuleComposite implements SumRule {
  private final List<SumRule> sumRules = new ArrayList<>();
  public SumRuleComposite() {
  }
  @Override
  public boolean checkSumEquals13(Player player){
    boolean ans = false;
    for (SumRule sumRule: sumRules) {
      ans = ans || sumRule.checkSumEquals13(player);
    }

    return ans;
  }

  // false: player's hand does not sum to 13 in under any sum rule, true otherwise
  public int getAllPlayersWithSum13 (Player[] players) {
    int ans = 0;
//    List<Integer> playerIdxWithSum13 = new ArrayList<>();
    for (int i = 0; i < players.length; i++) {
      if (checkSumEquals13(players[i])) {
        players[i].setCanSumTo13();
        ans++;
      }
    }
    return ans;
//    return playerIdxWithSum13;
  }

  public void addSumRule(SumRule sumRule) {
    sumRules.add(sumRule);
  }
}