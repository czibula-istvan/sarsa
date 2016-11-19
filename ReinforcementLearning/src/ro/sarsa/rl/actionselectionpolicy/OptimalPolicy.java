package ro.sarsa.rl.actionselectionpolicy;

import java.util.List;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.utilities.AbstractQValues;

public class OptimalPolicy implements ActionSelectionPolicy {

	@Override
	public Action chooseAction(AbstractQValues qVals, Enviroment env, List<Action> possibleActions) {
		List<Action> posActs = env.getPosibleActions(env.getCurrentState(), null);
		return qVals.getMax(env.getCurrentState(), posActs);
	}

}
