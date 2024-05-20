import java.util.*;

public class SumRuleComposite implements SumRule {
  private List<SumRule> sumRules;
  public SumRuleComposite() {
    sumRules = new ArrayList<>() {{
      add(new SumRule1());
      add(new SumRule2());
      add(new SumRule3());
    }};
  }
  @Override
  public boolean checkSum(){
    return false;
  }

  // false: player's hand does not sum to 13 in under any sum rule, true otherwise
  public boolean[] getAllPlayersWithSum13 (Player[] players) {
    boolean[] ans = new boolean[players.length];
    Arrays.fill(ans, false);
    return ans;
  }

}
