package ro.sarsa.rl.simulation;

import java.io.PrintStream;

import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.learningagent.History;
import ro.sarsa.rl.learningagent.RLTrainListener;
import ro.sarsa.rl.learningagent.ReinforcementLearning;

public class Simulation {

	protected ReinforcementLearning a;
	protected Enviroment e;

	/**
	 * Constructs a new simulation. Initializes the environment and the agent.
	 */
	public Simulation(ReinforcementLearning a, Enviroment e) {

		this.e = e;
		this.a = a;
	}

	/**
	 * Runs the RL simulation
	 */

	public History run(int nrEpochs, double initValue, RLTrainListener l) {

		a.train(nrEpochs, initValue, l);
		return a.getLearnedPolicy();
	}

	public History runFile(int nrEpochs, double initValue, RLTrainListener l, PrintStream ps, boolean writeToConsole) {

		a.trainFile(nrEpochs, initValue, l, ps, writeToConsole);
		return a.getLearnedPolicy();
	}
}
