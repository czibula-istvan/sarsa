package ro.sarsa.rl.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.action.IndexedAction;
import ro.sarsa.rl.enviroment.State;

/**
 * An action when we generate some form of permutation will consist in a given
 * index.. 1,2...
 * 
 * @author istvan
 * 
 */
public class PermutationAction implements Action, IndexedAction {
	private short index;
	private int totalNrOfActions;

	public PermutationAction(short index, int totalNrOfActions) {
		this.index = index;
		this.totalNrOfActions = totalNrOfActions;
	}

	@Override
	public State execute(State s) {
		PermutationState p = (PermutationState) s;
		short[] newPerm = Arrays.copyOf(p.getPermutation(), p.getPermutationLg() + 1);
		newPerm[p.getPermutationLg()] = index;
		return new PermutationState(newPerm);
	}

	public short getIndex() {
		return index;
	}

	public boolean equals(Object o) {
		return ((PermutationAction) o).index == index;
	}

	public int hashCode() {
		return index;
	}

	public String toString() {
		return index + "";
	}

	@Override
	public int getTotalNumberOfAction() {
		return totalNrOfActions;
	}

	private static List<Action> allAct;

	public static PermutationAction get(int index, int nrTotalActions) {
		return (PermutationAction) getAll(nrTotalActions).get(index);
	}

	/**
	 * Generate for a given dimension all the possible actions Basically we
	 * generate all the possible positions that can be interchanged in the
	 * solution
	 * 
	 * @param dim
	 * @return
	 */
	public static List<Action> getAll(int nrTotalActions) {
		if (allAct == null) {
			allAct = new ArrayList<Action>(nrTotalActions);
			for (short i = 0; i < nrTotalActions; i++) {
				allAct.add(new PermutationAction(i, nrTotalActions));
			}
			/*
			 * allAct.add(new PermutationAction(6, nrTotalActions));
			 * allAct.add(new PermutationAction(9, nrTotalActions));
			 * allAct.add(new PermutationAction(1, nrTotalActions));
			 * allAct.add(new PermutationAction(2, nrTotalActions));
			 * allAct.add(new PermutationAction(3, nrTotalActions));
			 * allAct.add(new PermutationAction(5, nrTotalActions));
			 * allAct.add(new PermutationAction(8, nrTotalActions));
			 * allAct.add(new PermutationAction(7, nrTotalActions));
			 * allAct.add(new PermutationAction(4, nrTotalActions));
			 * allAct.add(new PermutationAction(0, nrTotalActions));
			 */
		}
		return allAct;
	}
}
