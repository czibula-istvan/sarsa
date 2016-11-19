package ro.sarsa.rl.utilities;

import java.util.Iterator;
import java.util.List;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.enviroment.State;

public abstract class AbstractQValues {

	public interface UpdateFunction {
		double update(double qvalue);
	}

	public void updateValue(State state, Action act, UpdateFunction updateF) {
		set(state, act, updateF.update(get(state, act)));
	}

	public abstract void resetTo(double val);

	public abstract double get(State state, Action act);

	public abstract void set(State state, Action act, double val);

	public abstract Iterator<State> visitedStates();

	public abstract int getNrVisitedStates();

	public Action getMax(State state, List<Action> possibleActions) {
		double max = get(state, possibleActions.get(0));
		Action pozMax = possibleActions.get(0);
		for (int i = 1; i < possibleActions.size(); i++) {
			double val = get(state, possibleActions.get(i));
			if (max < val) {
				max = val;
				pozMax = possibleActions.get(i);
			}
		}

		return pozMax;
	}

	public double getMaxValue(State state, List<Action> possibleActions) {
		double max = get(state, possibleActions.get(0));
		for (int i = 1; i < possibleActions.size(); i++) {
			double val = get(state, possibleActions.get(i));
			if (max < val) {
				max = val;
			}
		}
		return max;
	}

	public double getMinValue(State state, List<Action> possibleActions) {
		double min = get(state, possibleActions.get(0));
		for (int i = 1; i < possibleActions.size(); i++) {
			double val = get(state, possibleActions.get(i));
			if (min > val) {
				min = val;
			}
		}
		return min;
	}

	public void add(State state, Action act, double val) {
		double prevVal = get(state, act);
		set(state, act, prevVal + val);
	}

	public void mul(State state, Action act, double val) {
		double prevVal = get(state, act);
		set(state, act, prevVal * val);
	}
}