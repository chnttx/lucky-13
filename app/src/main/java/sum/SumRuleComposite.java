package sum;

import player.Player;
import sum.SumRule;

import java.util.*;

public class SumRuleComposite implements SumRule {
  private final List<SumRule> sumRules = new ArrayList<>();
  public SumRuleComposite() {
    sumRules.add(new SumRule1());
    sumRules.add(new SumRule2());
    sumRules.add(new SumRule3());
  }

  /* Check if a player can sum to 13 under any summation rules */
  @Override
  public boolean checkSumEquals13(Player player){
    boolean ans = false;
    for (SumRule sumRule: sumRules) {
      boolean canSumTo13 = sumRule.checkSumEquals13(player);
      if (canSumTo13) player.setCanSumTo13();
      ans = ans || canSumTo13;
    }

    return ans;
  }
}
