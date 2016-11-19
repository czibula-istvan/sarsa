package ro.sarsa.rl.learningagent;

import java.util.ArrayList;
import java.util.List;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.enviroment.State;

public class History {

	private List<State> states;

	private List<Action> actions;

	public History() {
		states = new ArrayList<State>();
		actions = new ArrayList<Action>();
	}

	public void add(State s, Action a) {
		states.add(states.size(), s);
		actions.add(actions.size(), a);
	}

	public void add(State s) {
		states.add(states.size(), s);
	}

	public int getSize() {
		return actions.size();
	}

	public State getState(int i) {
		return states.get(i);
	}

	public Action getAction(int i) {
		return actions.get(i);
	}

	public State currentState() {
		return states.get(states.size() - 1);
	}

	public Action currentAction() {
		return actions.get(actions.size() - 1);
	}

	public String toString() {
		// StringBuffer s = new StringBuffer();
		// for (int i = 0; i < actions.size() - 1; i++)
		// s.append("-").append(actions.get(i).toString());
		// return s.toString();
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < states.size() - 1; i++)
			s.append(states.get(i)).append("-").append(actions.get(i)).append("-");
		s.append(states.get(states.size() - 1));
		return s.toString();
	}

	public void clear() {
		actions.clear();
		states.clear();
	}

	public State getFinalState() {
		return states.get(states.size() - 1);

	}
}
