package ro.sarsa.rl.actionselectionpolicy;

import java.util.List;
import java.util.Random;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.utilities.AbstractQValues;

/**
 * most of the time the action with the highest estimated reward is chosen,
 * called the greedest action. Every once in a while, say with a small
 * probability , an action is selected at random. The action is selected
 * uniformly, independent of the action-value estimates. This method ensures
 * that if enough trials are done, each action will be tried an infinite number
 * of times, thus ensuring optimal actions are discovered.
 * 
 * @author istvan
 * 
 */
public class EpsilonGreedyPolicy implements ActionSelectionPolicy {
	private double epsilon;
	private Random rnd = new Random();

	public EpsilonGreedyPolicy() {
	}

	public EpsilonGreedyPolicy(double epsilon) {
		this.epsilon = epsilon;
	}

	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	@Override
	public Action chooseAction(AbstractQValues qVals, Enviroment env, List<Action> possibleActions) {
		List<Action> posActs = env.getPosibleActions(env.getCurrentState(), null);
		double val = rnd.nextDouble();
		if (val < 1 - epsilon) {
			return qVals.getMax(env.getCurrentState(), posActs);
		}

		int poz = rnd.nextInt(posActs.size());
		return posActs.get(poz);
	}

}
