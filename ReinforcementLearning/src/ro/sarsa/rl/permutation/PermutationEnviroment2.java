package ro.sarsa.rl.permutation;

import java.util.List;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.enviroment.State;
import ro.sarsa.rl.learningagent.History;

/**
 * This class need to be extended if we want to work with permutations In a
 * large set of problems we are seeking for a permutation that maximise some
 * utility function. In this case we can use this environment and the SARSA
 * method to solve the problem
 * 
 * A second version for computing permutation A state is a valid permutation An
 * action is switching 2 values in the current state
 * 
 * @author istvan
 * 
 * 
 * @author istvan
 * 
 */
public abstract class PermutationEnviroment2 extends Enviroment {
	private int permDim;
	private PermutationState initialState;
	// Iuliana
	public static final double MAX = 56;
	public static final double EPSILON = 0.5;

	public PermutationEnviroment2(int permDim) {
		this.permDim = permDim;
		short[] perm = new short[permDim];
		for (short i = 0; i < permDim; i++) {
			perm[i] = i;
		}
		initialState = new PermutationState(perm);
	}

	@Override
	public List<Action> getPosibleActions(State state, History hist) {
		// PermutationState pS = (PermutationState) state;
		return PermutationAction2.getAll(permDim);
	}

	@Override
	public void resetToInitialState() {
		currentState = initialState;
	}

	@Override
	public boolean isFinalState(History hist) {
		// in this case we do not have a final state.. we just endlessly
		// generate permutations
		// the sarsa epoch will stop after a nrMax iteration
		// return hist.getSize() == permDim;
		// return false;
		PermutationState ps = ((PermutationState) currentState);
		double pm = this.performanceMeasureForPermutation(ps.getPermutation(), ps.getPermutationLg());
		return (MAX - pm < EPSILON);
	}

	@Override
	public double takeActionGiveReward(History h, Action act) {
		PermutationState pSBefore = (PermutationState) currentState;
		currentState = act.execute(currentState);
		PermutationState pS = (PermutationState) currentState;
		PermutationState iS = this.initialState;
		double reward = evalPermutation(pSBefore.getPermutation(), pSBefore.getPermutationLg(), pS.getPermutation(),
				pS.getPermutationLg(), iS.getPermutation(), iS.getPermutationLg(), this.isFinalState(h));
		return reward;
	}

	protected abstract double evalPermutation(short[] perm, int lg, short[] permBefore, int lgBefore,
			short[] initialPerm, int lgInitialPerm, boolean isFinalState);

	// Iuliana
	protected abstract double performanceMeasureForPermutation(short[] perm, int lg);

	@Override
	public int getNrStates() {
		// this is needed only if we apply sarsa lambda
		throw new IllegalArgumentException("the number of states is unknown");
	}

	@Override
	public State getState(int i) {
		// this is needed only if we apply sarsa lambda
		throw new IllegalArgumentException("the number of states is unknown");
	}

}
