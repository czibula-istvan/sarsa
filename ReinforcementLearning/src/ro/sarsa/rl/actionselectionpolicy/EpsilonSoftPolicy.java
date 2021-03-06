package ro.sarsa.rl.actionselectionpolicy;

import java.util.List;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.utilities.AbstractQValues;

/**
 * very similar to -greedy. The best action is selected with probability 1 - and
 * the rest of the time a random action is chosen uniformly.
 * 
 * @author istvan
 * 
 */
public class EpsilonSoftPolicy implements ActionSelectionPolicy {
	private double epsilon;

	public EpsilonSoftPolicy(double epsilon) {
		this.epsilon = epsilon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ro.sarsa.rl.actionselectionpolicy.ActionSelectionPolicy#choose(ro.sarsa.
	 * rl.utilities.QValues, ro.sarsa.rl.enviroment.GraphEnviroment)
	 */
	@Override
	public Action chooseAction(AbstractQValues qVals, Enviroment env, List<Action> possibleActions) {
		return null;
	}
}
