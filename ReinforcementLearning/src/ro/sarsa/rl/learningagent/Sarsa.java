package ro.sarsa.rl.learningagent;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.actionselectionpolicy.ActionSelectionPolicy;
import ro.sarsa.rl.actionselectionpolicy.EpsilonGreedyPolicy;
import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.enviroment.State;

public class Sarsa extends ReinforcementLearning {

	private History hist = new History();

	public Sarsa(Enviroment env, int maxSteps, double learningRate, double discountFactor) {
		this(env, maxSteps, learningRate, discountFactor, new EpsilonGreedyPolicy(0.1));
	}

	public Sarsa(Enviroment env, int maxSteps, double learningRate, double discountFactor,
			ActionSelectionPolicy policy) {
		super(env, maxSteps, learningRate, discountFactor, policy);
	}

	public void initTraining(double initValue) {
		q.resetTo(initValue);
	}

	public History epoch() {
		hist.clear();
		env.resetToInitialState();

		Action currentAction = policy.chooseAction(q, env, null);

		double prevQValue = q.get(env.getCurrentState(), currentAction);
		int noSteps = 0;

		do {

			noSteps++;

			State cState = env.getCurrentState();

			hist.add(env.getCurrentState(), currentAction);

			double reward = env.takeActionGiveReward(hist, currentAction);

			State newState = env.getCurrentState();

			Action newAction = policy.chooseAction(q, env, null);

			double newQValue = q.get(newState, newAction);

			double tdError = reward + gama * newQValue - prevQValue;
			q.add(cState, currentAction, alfa * tdError);
			prevQValue = newQValue;
			currentAction = newAction;
		} while (!env.isFinalState(hist) && noSteps <= maxSteps);
		hist.add(env.getCurrentState());
		return hist;
	}

}
