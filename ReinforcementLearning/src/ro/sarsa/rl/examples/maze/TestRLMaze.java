package ro.sarsa.rl.examples.maze;

import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.graph.GraphEnviroment;
import ro.sarsa.rl.graph.GraphState;
import ro.sarsa.rl.learningagent.History;
import ro.sarsa.rl.learningagent.QLearning;
import ro.sarsa.rl.learningagent.RLTrainListener;
import ro.sarsa.rl.learningagent.ReinforcementLearning;
import ro.sarsa.rl.simulation.Simulation;

public class TestRLMaze extends Simulation {

	public TestRLMaze(ReinforcementLearning a, Enviroment e) {
		super(a, e);
	}

	public static void main(String[] args) {
		GraphEnviroment grEnv = new GraphEnviroment(true, 16, new GraphState(0, 4), new GraphState(15, 4));
		grEnv.initCompleteMazeSNEWStyle2();
		// final Sarsa lAlg = new Sarsa(grEnv, 1000, 0.4, 0.9);
		final QLearning lAlg = new QLearning(grEnv, 1000, 0.4, 0.9);
		// final SarsaLambda lAlg = new SarsaLambda(grEnv, 1000,
		// EligibilityTraceType.Acummulating, 0.4, 0.9, 0.95);
		TestRLMaze test = new TestRLMaze(lAlg, grEnv);

		History hist = test.run(15000, 0, new RLTrainListener() {
			@Override
			public int epoch(int ord, int total, History lastEpochHistory) {
				System.out.println(ord + "/" + total + " policy>" + lAlg.getLearnedPolicy());
				return 100;
			}
		});

		System.out.println("Final policy:" + hist);
		// s-a testat cu eps=0.01 si GammaToLambda=0.9095

	}
}
