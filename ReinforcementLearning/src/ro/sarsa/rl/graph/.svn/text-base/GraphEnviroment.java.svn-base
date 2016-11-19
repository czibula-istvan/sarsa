package ro.sarsa.rl.graph;

import java.util.ArrayList;
import java.util.List;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.enviroment.Enviroment;
import ro.sarsa.rl.enviroment.State;
import ro.sarsa.rl.learningagent.History;

/**
 * Describe the enviroment using a directed or nondirected graph Can be used if
 * the problem to be solved can be modelled as path find problem
 * 
 * @author istvan
 * 
 */
public class GraphEnviroment extends Enviroment {
	private boolean directed;
	private int nrStates;
	private int dim; // dimensiunea gridului
	private Direction[][] adiacencyMatrx;
	private GraphState initialState;
	private GraphState finalState;
	private int maximumPathLength = -1;

	// private int currentState = -1;

	public GraphEnviroment() {
	}

	public GraphEnviroment(boolean directed, int nrStates,
			GraphState initialState, GraphState finalState) {
		this.directed = directed;
		this.nrStates = nrStates;
		this.initialState = initialState;
		this.finalState = finalState;
		dim = (int) Math.sqrt(nrStates);
		initMatrx();

	}

	private void initMatrx() {
		adiacencyMatrx = new Direction[nrStates][];
		for (int i = 0; i < nrStates; i++) {
			adiacencyMatrx[i] = new Direction[nrStates];
		}
	}

	public void initCompleteMazeSNEWStyle() {
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				GraphState node = new GraphState(i * dim + j, dim);
				int ii = i - 1;
				int jj = j;
				if (isValidLineCol(ii, jj, dim)) {
					setConected(node, Direction.N, new GraphState(
							ii * dim + jj, dim));
				}
				ii = i + 1;
				jj = j;
				if (isValidLineCol(ii, jj, dim)) {
					setConected(node, Direction.S, new GraphState(
							ii * dim + jj, dim));
				}
				ii = i;
				jj = j - 1;
				if (isValidLineCol(ii, jj, dim)) {
					setConected(node, Direction.V, new GraphState(
							ii * dim + jj, dim));
				}
				ii = i;
				jj = j + 1;
				if (isValidLineCol(ii, jj, dim)) {
					setConected(node, Direction.E, new GraphState(
							ii * dim + jj, dim));
				}
			}
		}
	}

	private boolean isValidLineCol(int ii, int jj, int dim) {
		if (ii >= dim || ii < 0) {
			return false;
		}
		if (jj >= dim || jj < 0) {
			return false;
		}
		return true;
	}

	public void initCompleteMazeSNEWStyle2() {
		initCompleteMazeSNEWStyle();
		int dim = (int) Math.sqrt(nrStates);
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				GraphState node = new GraphState(i * dim + j, dim);
				int ii = i - 1;
				int jj = j - 1;
				if (isValidLineCol(ii, jj, dim)) {
					setConected(node, Direction.NV, new GraphState(ii * dim
							+ jj, dim));
				}
				ii = i + 1;
				jj = j - 1;
				if (isValidLineCol(ii, jj, dim)) {
					setConected(node, Direction.SV, new GraphState(ii * dim
							+ jj, dim));
				}
				ii = i + 1;
				jj = j + 1;
				if (isValidLineCol(ii, jj, dim)) {
					setConected(node, Direction.SE, new GraphState(ii * dim
							+ jj, dim));
				}
				ii = i - 1;
				jj = j + 1;
				if (isValidLineCol(ii, jj, dim)) {
					setConected(node, Direction.NE, new GraphState(ii * dim
							+ jj, dim));
				}
			}
		}
	}

	public boolean isValid(State state) {
		return ((GraphState) state).rank() >= 0
				&& ((GraphState) state).rank() < nrStates;
	}

	public void setConected(State state1, Direction direction, State state2) {
		if (!isValid(state1) || !isValid(state2)) {
			return;
		}
		adiacencyMatrx[((GraphState) state1).rank()][((GraphState) state2)
				.rank()] = direction;
		if (!directed) {
			adiacencyMatrx[((GraphState) state2).rank()][((GraphState) state1)
					.rank()] = direction;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.sarsa.rl.enviroment.Enviroment#getPosibleActions(int)
	 */
	@Override
	public List<Action> getPosibleActions(State state, History hist) {
		List<Action> rez = new ArrayList<Action>(nrStates);
		for (int i = 0; i < nrStates; i++) {
			Direction direction = connected((GraphState) state, new GraphState(
					i, dim));
			if (direction != null) {
				rez.add(new GraphAction(direction));
			}
		}

		// for(int i=0;i<rez.size();i++)
		// System.out.println(rez.get(i)+" "+rez.get(i).getEndState());

		return rez;
	}

	public void resetToInitialState() {
		currentState = initialState;
	}

	private Direction connected(GraphState state1, GraphState state2) {
		// System.out.println(state1.rank()+" "+state2.rank());
		return adiacencyMatrx[state1.rank()][state2.rank()];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.sarsa.rl.enviroment.Enviroment#resetToInitialState()
	 */

	@Override
	public boolean isFinalState(History hist) {
		if (currentState.equals(finalState)) {
			return true;
		}
		if (maximumPathLength > 0) {
			return hist.getSize() > maximumPathLength;
		}
		return false;
	}

	@Override
	public double takeActionGiveReward(History h, Action act) {
		// h este istoricul in mediu: starea curenta este ultima din history
		State endS = act.execute(currentState);
		currentState = endS;
		if (endS.equals(finalState)) {
			return 0;
		}
		return -1;
	}

	public int getNrStates() {
		return nrStates;
	}

	public State getState(int i) {
		return new GraphState(i, dim);
	}

	public void setMaximumPathLength(int i) {
		maximumPathLength = i;
	}

}
