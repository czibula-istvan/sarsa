package ro.sarsa.rl.utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.enviroment.State;

/**
 * Use hasmap to store the values
 * 
 * @author istvan
 * 
 */
public class QValuesMap extends AbstractQValues {
	private Map<State, Map<Action, Double>> values;
	private double defVal;

	public QValuesMap() {
		initQValues();
	}

	public void initQValues() {
		values = new HashMap<State, Map<Action, Double>>();
	}

	public void resetTo(double val) {
		initQValues();
		defVal = val;
	}

	public double get(State state, Action act) {
		Map<Action, Double> act2vals = values.get(state);
		if (act2vals == null) {
			return defVal;
		}
		Double rez = act2vals.get(act);
		if (rez == null) {
			return defVal;
		}
		return rez;
	}

	public void set(State state, Action act, double val) {
		Map<Action, Double> act2vals = values.get(state);
		if (act2vals == null) {
			act2vals = new HashMap<Action, Double>();
			values.put(state, act2vals);
		}
		act2vals.put(act, val);
	}

	public Iterator<State> visitedStates() {
		return values.keySet().iterator();
	}

	public int getNrVisitedStates() {
		return values.keySet().size();
	}

	public Iterator<Action> visitedActions(State state) {
		Map<Action, Double> act2vals = values.get(state);
		if (act2vals == null) {
			return null;
		}
		return act2vals.keySet().iterator();
	}
}
