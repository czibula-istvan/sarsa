package ro.sarsa.rl.enviroment;

import java.util.List;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.learningagent.History;

public abstract class Enviroment {

	protected State currentState; // starea curenta a mediului

	public abstract List<Action> getPosibleActions(State state, History hist);

	public abstract void resetToInitialState();

	public State getCurrentState() {
		return currentState;
	}

	public abstract boolean isFinalState(History hist);

	public abstract double takeActionGiveReward(History h, Action act);

	// urmatoarele doua functii pentru mediu cunoscut

	public abstract int getNrStates();

	public abstract State getState(int i);

}