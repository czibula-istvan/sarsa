package ro.sarsa.rl.trace;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.enviroment.State;
import ro.sarsa.rl.utilities.AbstractQValues;

public class EligibilityTrace {
	private EligibilityTraceType type;
	/**
	 * We keep the values in a similar structure as the qvalues
	 */
	private AbstractQValues qVals;

	public EligibilityTrace(EligibilityTraceType type, AbstractQValues emptyQVals) {
		this.type = type;
		qVals = emptyQVals;
	}

	public EligibilityTraceType getType() {
		return this.type;
	}

	public void resetToZero() {
		qVals.resetTo(0);
	}

	public void set(State state, Action act, double val) {
		qVals.set(state, act, val);
	}

	public double get(State state, Action act) {
		return qVals.get(state, act);
	}

	public void update(State state, Action act, double val) {
		if (type.equals(EligibilityTraceType.Acummulating)) {
			qVals.add(state, act, val);
		} else {
			qVals.set(state, act, val);
		}
	}

	public void mul(State state, Action act, double val) {
		qVals.mul(state, act, val);
	}

}
