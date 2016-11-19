package ro.sarsa.rl.permutation;

import java.util.ArrayList;
import java.util.List;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.enviroment.State;
import ro.sarsa.rl.learningagent.History;

/**
 * This class need to be extended if we want to work with permutations In a
 * large set of problems we are seeking for a permutation that maximizes some
 * utility function. In this case we can use this environment and the SARSA
 * method to solve the problem
 * 
 * It seems that is better than the other model
 * 
 * @author istvan
 * 
 */
public abstract class PermutationEnvironmentW extends Enviroment {
	private int permDim;
	private PermutationState initialState;
	private static List<Action> hackPossibleActions;

	public PermutationEnvironmentW(int permDim) {
		this.permDim = permDim;
		initialState = new PermutationState(new short[0]);
		// hackPossibleActions = new ArrayList<Action>(1);
		// hackPossibleActions.add(PermutationAction.get(0, permDim));
		hackPossibleActions = PermutationAction.getAll(5);
	}

	@Override
	public List<Action> getPosibleActions(State state, History hist) {
		PermutationState pS = (PermutationState) state;
		int lg = pS.getPermutationLg();
		if (lg == permDim) {
			// hack for sarsa...
			// when we reach the final node the sarsa invoke the
			// getPosibleActions for one more time
			return hackPossibleActions;
		}

		// return any action
		short[] perm = pS.getPermutation();
		List<Action> rez = new ArrayList<Action>(PermutationAction.getAll(5));
		return rez;
	}

	@Override
	public void resetToInitialState() {
		currentState = initialState;
	}

	@Override
	public boolean isFinalState(History hist) {
		// we do not have a final state.. we want all the possible permutations
		return ((PermutationState) currentState).getPermutationLg() == permDim;
	}

	@Override
	public double takeActionGiveReward(History h, Action act) {
		currentState = act.execute(currentState);
		PermutationState pS = (PermutationState) currentState;
		return evalPermutation(pS.getPermutation(), pS.getPermutationLg());
	}

	protected abstract double evalPermutation(short[] perm, int lg);

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
