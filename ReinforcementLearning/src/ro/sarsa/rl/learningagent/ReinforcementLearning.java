package ro.sarsa.rl.learningagent;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.action.IndexedAction;
import ro.sarsa.rl.actionselectionpolicy.ActionSelectionPolicy;
import ro.sarsa.rl.actionselectionpolicy.EpsilonGreedyPolicy;
import ro.sarsa.rl.actionselectionpolicy.OptimalPolicy;
import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.enviroment.State;
import ro.sarsa.rl.utilities.AbstractQValues;
import ro.sarsa.rl.utilities.QValuesIndexed;
import ro.sarsa.rl.utilities.QValuesIndexedSSO2;
import ro.sarsa.rl.utilities.QValuesMap;

public abstract class ReinforcementLearning {

	/**
	 * the learning rate, set between 0 and 1. Setting it to 0 means that the
	 * Q-values are never updated, hence nothing is learned. Setting a high
	 * value such as 0.9 means that learning can occur quickly.
	 */
	protected double alfa;
	/**
	 * discount factor, also set between 0 and 1. This models the fact that
	 * future rewards are worth less than immediate rewards. Mathematically, the
	 * discount factor needs to be set less than 0 for the algorithm to
	 * converge.
	 */
	protected double gama;
	/**
	 * the maximum reward that is attainable in the state following the current
	 * one. i.e the reward for taking the optimal action thereafter.
	 */

	protected int maxSteps; // the maximum allowed number of steps in an
							// episode

	/**
	 * Enviroment
	 */
	protected Enviroment env;

	protected ActionSelectionPolicy policy;
	public AbstractQValues q;

	public ReinforcementLearning(Enviroment env, int maxSteps, double learningRate, double discountFactor) {
		this(env, maxSteps, learningRate, discountFactor, new EpsilonGreedyPolicy(0.8));// 10%
																						// explorare
																						// 90%
																						// exploatare
	}

	public ReinforcementLearning(Enviroment env, int maxSteps, double learningRate, double discountFactor,
			ActionSelectionPolicy policy) {
		this(env, maxSteps, learningRate, discountFactor, policy, createQValue(env));
	}

	public ReinforcementLearning(Enviroment env, int maxSteps, double learningRate, double discountFactor,
			ActionSelectionPolicy policy, AbstractQValues qvals) {
		this.env = env;
		this.maxSteps = maxSteps;
		this.alfa = learningRate;
		this.gama = discountFactor;
		this.policy = policy;
		q = qvals;
	}

	protected static AbstractQValues createQValue(Enviroment env) {
		env.resetToInitialState();
		List<Action> acts = env.getPosibleActions(env.getCurrentState(), null);
		if (acts.get(0) instanceof IndexedAction) {
			int totalNumberOfAction = ((IndexedAction) acts.get(0)).getTotalNumberOfAction();
			if (totalNumberOfAction > 10) {
				return new QValuesIndexedSSO2(totalNumberOfAction, env.getCurrentState());
				// return new QValuesIndexedSSO(totalNumberOfAction,
				// env.getCurrentState());
			}
			return new QValuesIndexed(totalNumberOfAction, env.getCurrentState());
		} else {
			return new QValuesMap();
		}
	}

	public abstract void initTraining(double initValue);

	public abstract History epoch();

	public void train(int nrEpochs, double initValue, RLTrainListener l) {
		initTraining(initValue);
		int samplingRate = 5000;
		for (int i = 0; i < nrEpochs; i++) {
			if (i % samplingRate == 0) {
				History hist = epoch();
				if (l != null) {
					samplingRate = l.epoch(i, nrEpochs, hist);
				}
			} else {
				epoch();
			}
		}
	}

	public void trainFile(int nrEpochs, double initValue, RLTrainListener l, PrintStream ps, boolean writeToConsole) {
		long tmAll = System.currentTimeMillis();
		long tmInterm = System.currentTimeMillis();
		initTraining(initValue);
		int samplingRate = 5000;
		for (int i = 0; i < nrEpochs; i++) {
			if (i % samplingRate == 0) {
				epoch();
				if (writeToConsole)
					System.out.println(i + " time:" + ((System.currentTimeMillis() - tmInterm) / 1000) + " s");
				ps.println(i + " time:" + ((System.currentTimeMillis() - tmInterm) / 1000) + " s");
				tmInterm = System.currentTimeMillis();
				if (l != null) {
					samplingRate = l.epoch(i, nrEpochs, null);
				}
			} else {
				epoch();
			}
		}
		if (writeToConsole)
			System.out.println("Total time:" + ((System.currentTimeMillis() - tmAll) / 1000) + " s");
		ps.println("Total time:" + ((System.currentTimeMillis() - tmAll) / 1000) + " s");
	}

	public History getLearnedPolicy() {
		// tiparesc si Q-valorile
		// printQValues();
		History hist = new History();
		env.resetToInitialState();

		OptimalPolicy policy = new OptimalPolicy();
		while (!env.isFinalState(hist) && hist.getSize() <= maxSteps) {
			Action currentAction = policy.chooseAction(q, env, null);
			State cState = env.getCurrentState();
			hist.add(cState, currentAction);
			env.takeActionGiveReward(hist, currentAction);

		}
		hist.add(env.getCurrentState());
		return hist;
	}

	public void printQValues() {
		Iterator<State> allVisitedStates = q.visitedStates();
		while (allVisitedStates.hasNext()) {
			State s = allVisitedStates.next();
			List<Action> acts = env.getPosibleActions(s, null);
			for (int j = 0; j < acts.size(); j++) {
				Action a = acts.get(j);
				System.out.println(s + "-" + a + ":" + q.get(s, a));
			}
		}
	}

	public void printNumberOfVisitedStates() {
		System.out.println("Number of visited states:" + q.visitedStates());
	}

	public AbstractQValues getQValues() {
		return q;
	}
}
