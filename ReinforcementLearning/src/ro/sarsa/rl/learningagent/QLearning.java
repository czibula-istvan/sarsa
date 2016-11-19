package ro.sarsa.rl.learningagent;

import java.util.List;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.actionselectionpolicy.ActionSelectionPolicy;
import ro.sarsa.rl.actionselectionpolicy.EpsilonGreedyPolicy;
import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.enviroment.State;
import ro.sarsa.rl.utilities.AbstractQValues;

/**
 * Implements the QLearning algorithm
 * 
 * @author istvan
 * 
 */
public class QLearning extends ReinforcementLearning {
	private History hist = new History();

	public QLearning(Enviroment env, int maxSteps, double learningRate, double discountFactor) {
		this(env, maxSteps, learningRate, discountFactor, new EpsilonGreedyPolicy(0.1));
	}

	public QLearning(Enviroment env, int maxSteps, double learningRate, double discountFactor,
			ActionSelectionPolicy policy) {
		super(env, maxSteps, learningRate, discountFactor, policy);
	}

	public QLearning(Enviroment env, int maxSteps, double learningRate, double discountFactor,
			ActionSelectionPolicy policy, AbstractQValues qVals) {
		super(env, maxSteps, learningRate, discountFactor, policy, qVals);
	}

	public void initTraining(double initValue) {
		q.resetTo(initValue);
	}

	public History epoch() {
		hist.clear();
		env.resetToInitialState();
		List<Action> nextPossibleActions = env.getPosibleActions(env.getCurrentState(), null);

		int noSteps = 0;
		do {
			noSteps++;
			Action currentAction = policy.chooseAction(q, env, nextPossibleActions);

			State cState = env.getCurrentState();
			hist.add(cState, currentAction);

			double reward = env.takeActionGiveReward(hist, currentAction);

			State newS = env.getCurrentState();
			nextPossibleActions = env.getPosibleActions(newS, null);

			double maxNextQVal = q.getMaxValue(newS, nextPossibleActions);

			// asa evitam sa cautam de doua ori valoarea in qValues
			q.updateValue(cState, currentAction, (qVal) -> {
				double tdError = reward + gama * maxNextQVal - qVal;
				return qVal + alfa * tdError;
			});
			// double qVal = q.get(cState, currentAction);
			// double tdError = reward + gama * maxNextQVal - qVal;
			// q.set(cState, currentAction, qVal + alfa * tdError);

		} while (!env.isFinalState(hist) && noSteps <= maxSteps);

		hist.add(env.getCurrentState());
		return hist;
	}

}
