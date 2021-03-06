package ro.sarsa.rl.learningagent;

import java.util.List;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.actionselectionpolicy.ActionSelectionPolicy;
import ro.sarsa.rl.actionselectionpolicy.EpsilonGreedyPolicy;
import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.enviroment.State;
import ro.sarsa.rl.trace.EligibilityTrace;
import ro.sarsa.rl.trace.EligibilityTraceType;

// Q(lambda) - Watkins approach

public class QLearningEligibilityTraces extends ReinforcementLearning {
	private History hist = new History();
	private EligibilityTrace elTrace;
	private double lambda;
	private boolean naiveQLambda; // if this is false => reset eligibility
									// traces for exploratory actions to 0

	public QLearningEligibilityTraces(Enviroment env, int maxSteps, double learningRate, double discountFactor) {
		this(env, maxSteps, learningRate, discountFactor, new EpsilonGreedyPolicy(0.1),
				EligibilityTraceType.Acummulating, 1, false);
	}

	public QLearningEligibilityTraces(Enviroment env, int maxSteps, double learningRate, double discountFactor,
			ActionSelectionPolicy policy, EligibilityTraceType traceType, double lambda, boolean naiveQLambda) {
		super(env, maxSteps, learningRate, discountFactor, policy);
		this.lambda = lambda;
		this.naiveQLambda = naiveQLambda;
		if (traceType == null) {
			this.elTrace = null;
			return;
		}
		this.elTrace = new EligibilityTrace(traceType, createQValue(env));
	}

	public void initTraining(double initValue) {
		q.resetTo(initValue);
	}

	public History epoch() {
		hist.clear();
		env.resetToInitialState();
		int noSteps = 0;
		do {
			noSteps++;
			State cState = env.getCurrentState();
			Action currentAction = policy.chooseAction(q, env, null);

			hist.add(env.getCurrentState(), currentAction);

			double reward = env.takeActionGiveReward(hist, currentAction);

			State newS = env.getCurrentState();
			Action newAction = policy.chooseAction(q, env, null);
			Action maxAction = q.getMax(newS, env.getPosibleActions(newS, null));

			this.updateQValues(currentAction, cState, reward, newS, newAction, maxAction, hist);
		} while (!env.isFinalState(hist) && noSteps <= maxSteps);

		hist.add(env.getCurrentState());
		return hist;
	}

	private void updateQValues(Action currentAction, State cState, double reward, State newState, Action newAction,
			Action maxAction, History hist) {
		double maxQVal = q.getMaxValue(newState, env.getPosibleActions(newState, null));
		double tdError = reward + gama * maxQVal - q.get(cState, currentAction);

		elTrace.update(cState, currentAction, 1);

		for (int i = 0; i < hist.getSize(); i++) {
			State s = hist.getState(i);
			updateForState(s, tdError, newAction, maxAction);
		}
	}

	private void updateForState(State s, double tdError, Action newAction, Action maxAction) {
		List<Action> acts = env.getPosibleActions(s, null);
		for (int j = 0; j < acts.size(); j++) {
			Action a = acts.get(j);
			this.q.add(s, a, this.alfa * tdError * this.elTrace.get(s, a));
			if (this.naiveQLambda) {
				elTrace.mul(s, a, this.gama * this.lambda);
				continue;
			}
			// if not naiveQLambda - set eligibility to 0 for exploratory
			// actions
			if (newAction.equals(maxAction))
				elTrace.mul(s, a, this.gama * this.lambda);
			else
				elTrace.mul(s, a, 0);
		}
	}
}
