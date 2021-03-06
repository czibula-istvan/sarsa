package ro.sarsa.rl.learningagent;

import java.util.List;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.enviroment.State;
import ro.sarsa.rl.trace.EligibilityTrace;
import ro.sarsa.rl.trace.EligibilityTraceType;

public class SarsaLambda extends ReinforcementLearning {

	private double lambda;

	private EligibilityTrace elTrace;

	public SarsaLambda(Enviroment env, int maxSteps, EligibilityTraceType traceType, double learningRate,
			double discountFactor, double lambda) {
		super(env, maxSteps, learningRate, discountFactor);
		this.lambda = lambda;
		elTrace = new EligibilityTrace(traceType, createQValue(env));
	}

	public void initTraining(double initValue) {
		q.resetTo(initValue);
		elTrace.resetToZero();
	}

	public History epoch() {

		History hist = new History();

		env.resetToInitialState();

		Action currentAction = policy.chooseAction(q, env, null);

		int noSteps = 0;

		do {
			noSteps++;
			State cState = env.getCurrentState();
			hist.add(env.getCurrentState(), currentAction);
			double reward = env.takeActionGiveReward(hist, currentAction);
			State newState = env.getCurrentState();
			Action newAction = policy.chooseAction(q, env, null);
			updateQValues(currentAction, cState, reward, newState, newAction, hist);
			currentAction = newAction;
		} while (!env.isFinalState(hist) && noSteps <= maxSteps);
		hist.add(env.getCurrentState());
		return hist;
	}

	private void updateForState(State s, double tdError) {
		List<Action> acts = env.getPosibleActions(s, null);
		for (int j = 0; j < acts.size(); j++) {
			Action a = acts.get(j);
			q.add(s, a, alfa * tdError * elTrace.get(s, a));
			elTrace.mul(s, a, gama * lambda);// 0.9095 );
		}
	}

	// private void updateQValues(AnAction currentAction, State cState,
	// double reward, State newState, AnAction newAction) {
	// double tdError = reward + gama * q.get(newState, newAction)
	// - q.get(cState, currentAction);
	//
	// if (traceType.compareTo("acummulating") == 0)
	// elTrace.add(cState, currentAction, 1);
	// else
	// // replacing
	// elTrace.set(cState, currentAction, 1);
	//
	// List<State> updatedStates = new ArrayList();
	//
	// Iterator<State> allVisitedStates = q.visitedStates();
	// while (allVisitedStates.hasNext()) {
	// State s = allVisitedStates.next();
	// if (!updatedStates.contains(s)) {
	// updatedStates.add(s);
	// updateForState(s, tdError);
	// }
	// }
	//
	// if (!updatedStates.contains(cState))
	// updateForState(cState, tdError);
	// if (!updatedStates.contains(newState))
	// updateForState(newState, tdError);
	// }

	// for all the states from the environment - only if the environment is
	// known

	private void updateQValues(Action currentAction, State cState, double reward, State newState, Action newAction,
			History hist) {

		double tdError = reward + gama * q.get(newState, newAction) - q.get(cState, currentAction);

		elTrace.update(cState, currentAction, 1);

		for (int i = 0; i < hist.getSize(); i++) {
			State s = hist.getState(i);
			updateForState(s, tdError);
		}
	}

}
