package ro.sarsa.rl.actionselectionpolicy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.enviroment.State;
import ro.sarsa.rl.utilities.AbstractQValues;

/**
 * one drawback of -greedy and -soft is that they select random actions
 * uniformly. The worst possible action is just as likely to be selected as the
 * second best. Softmax remedies this by assigning a rank or weight to each of
 * the actions, according to their action-value estimate. A random action is
 * selected with regards to the weight associated with each action, meaning the
 * worst actions are unlikely to be chosen. This is a good approach to take
 * where the worst actions are very unfavourable.
 * 
 * @author istvan
 * 
 */
public class SoftMaxPolicy implements ActionSelectionPolicy {

	private double temperature;
	private Random rnd = new Random();

	public SoftMaxPolicy() {
	}

	public SoftMaxPolicy(double temp) {
		this.temperature = temp;
	}

	public void setTemperature(double temp) {
		this.temperature = temp;
	}

	@Override
	public Action chooseAction(AbstractQValues qVals, Enviroment env, List<Action> possibleActions) {
		State currentState = env.getCurrentState();
		List<Action> posActs = env.getPosibleActions(currentState, null);
		double sum = this.sumQVals(currentState, posActs, qVals);

		// create a list of action probabilities
		ArrayList<Double> probs = new ArrayList<Double>();

		double maxQValue = qVals.getMaxValue(currentState, posActs);
		double minQValue = qVals.getMinValue(currentState, posActs);
		double denom = maxQValue - minQValue;
		for (int i = 0; i < posActs.size(); i++) {
			// compute probability for action from position i
			double qVal = qVals.get(currentState, posActs.get(i));
			// if the action values are somewhat large => numerical problems
			// ex: for Q(s,a) = 10 and temperature = 0.1, the
			// exp(Q(s,a)/temperature) > 10^43
			// therefore, we must scale them (scale in [0,1])
			if (denom != 0)
				qVal = (qVal - minQValue) / denom;
			else
				qVal = 0;
			double prob = Math.exp(qVal / this.temperature) / sum;
			probs.add(prob);
		}

		// modify the list: at each index, add the previous probability and
		// memorize the current sum
		// ex - initial list: 0.25, 0.15, 0.35, 0.05, 0.2
		// modified list: 0.25, 0.4, 0.75, 0.8, 1
		// last value should always be 1
		sum = 0;
		for (int i = 0; i < probs.size(); i++) {
			sum += probs.get(i);
			probs.set(i, sum);
		}

		// generate a random number and choose the index of the interval (from
		// the new probs list)
		// that contains the generated number
		// ex: random number = 0.65 => choose action with index 2 (between 0.4
		// and 0.75 - third interval)
		double val = rnd.nextDouble();
		int n = probs.size();
		if (n == 0)
			return null;
		// special case for first action
		if (val <= probs.get(0))
			posActs.get(0);
		int pos = 0;
		for (int i = 0; i < n - 1; i++) {
			if ((val > probs.get(i)) && (val <= probs.get(i + 1))) {
				pos = i + 1;
				break;
			}
		}
		return posActs.get(pos);
	}

	/*
	 * computes the sum for all possible actions from the given state
	 * sum_a(exp(Q(s,a)/temperature), where s - current state
	 */
	private double sumQVals(State state, List<Action> actions, AbstractQValues qVals) {
		double sum = 0;
		double maxQValue = qVals.getMaxValue(state, actions);
		double minQValue = qVals.getMinValue(state, actions);
		double denom = maxQValue - minQValue;

		for (int i = 0; i < actions.size(); i++) {
			Action action = actions.get(i);
			double qVal = qVals.get(state, action);
			// if the action values are somewhat large => numerical problems
			// ex: for Q(s,a) = 10 and temperature = 0.1, the
			// exp(Q(s,a)/temperature) > 10^43
			// therefore, we must scale them (scale in [0,1])
			if (denom != 0)
				qVal = (qVal - minQValue) / denom;
			else
				qVal = 0;
			sum += Math.exp(qVal / this.temperature);
		}
		return sum;
	}
}
