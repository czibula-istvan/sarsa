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
public class PermutationAction2 implements Action, IndexedAction {
	private int poz1, poz2;
	private int totalNrOfActions;
	private short index;

	public PermutationAction2(int poz1, int poz2, short index, int totalNrOfActions) {
		this.poz1 = poz1;
		this.poz2 = poz2;
		this.index = index;
		this.totalNrOfActions = totalNrOfActions;
	}

	@Override
	public State execute(State s) {
		PermutationState p = (PermutationState) s;
		short[] newPerm = Arrays.copyOf(p.getPermutation(), p.getPermutationLg());
		short aux = newPerm[poz2];
		newPerm[poz2] = newPerm[poz1];
		newPerm[poz1] = aux;
		return new PermutationState(newPerm);
	}

	public short getIndex() {
		return index;
	}

	public boolean equals(Object o) {
		return ((PermutationAction2) o).poz1 == poz1 && ((PermutationAction2) o).poz2 == poz2;
	}

	public int hashCode() {
		return poz1 * 100 + poz2;
	}

	public String toString() {
		return poz1 + "->" + poz2;
	}

	@Override
	public int getTotalNumberOfAction() {
		return totalNrOfActions;
	}

	private static List<Action> allAct;

	/**
	 * Generate for a given dimension all the possible actions Basically we
	 * generate all the possible positions that can be interchanged in the
	 * solution We also create an action that do not change the state (like
	 * PermutationAction(0,0)) Because we use a fixed set of actions per epoch
	 * (we do not have a true final state) so we need a dummy action to complete
	 * the epoch without braking a good result
	 * 
	 * @param dim
	 * @return
	 */
	public static List<Action> getAll(int permDim) {

		if (allAct == null) {
			int nrTotalActions = 1;
			for (int i = 0; i < permDim - 1; i++) {
				for (int j = i + 1; j < permDim; j++) {
					nrTotalActions++;
				}
			}

			allAct = new ArrayList<Action>(nrTotalActions);
			short ord = 0;
			allAct.add(new PermutationAction2(0, 0, ord++, nrTotalActions));
			for (int i = 0; i < permDim - 1; i++) {
				for (int j = i + 1; j < permDim; j++) {
					allAct.add(new PermutationAction2(i, j, ord++, nrTotalActions));
				}
			}
		}
		return allAct;
	}
}
