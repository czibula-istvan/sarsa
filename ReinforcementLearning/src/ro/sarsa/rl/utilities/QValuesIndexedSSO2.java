package ro.sarsa.rl.utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.action.IndexedAction;
import ro.sarsa.rl.enviroment.State;

/**
 * Optimized for actions that implements IndexedAction interface
 * 
 * @author istvan
 * 
 */
public class QValuesIndexedSSO2 extends AbstractQValues {

	private Map<State, double[]> values;
	private double defVal;
	private int nrTotalActions;
	private State sampleState;

	public QValuesIndexedSSO2(int nrTotalActions, State sampleState) {
		this.nrTotalActions = nrTotalActions;
		this.sampleState = sampleState;
		initQValues(sampleState);
	}

	public void initQValues(State sampleState) {
		if (sampleState instanceof Comparable) {
			values = new TreeMap<State, double[]>();
			// values =new ConcurrentSkipListMap<State, double[]>();
		} else {
			values = new HashMap<State, double[]>();
		}
	}

	public void resetTo(double val) {
		initQValues(sampleState);
		defVal = val;
	}

	public synchronized double get(State state, Action act) {
		double[] act2vals = values.get(state);
		if (act2vals == null) {
			return defVal;
		}
		short actIndex = ((IndexedAction) act).getIndex();
		// begin small size optimization
		if (act2vals.length == 2) {
			if ((short) act2vals[0] == actIndex) {
				return act2vals[1];
			}
			return defVal;
		}
		// end small size optimization

		return act2vals[actIndex];
	}


	public synchronized void updateValue(State state, Action act, UpdateFunction updateF) {
		double[] act2vals = values.get(state);
		short actIndex = ((IndexedAction) act).getIndex();
		if (act2vals == null) {
			// begin small size optimization
			values.put(state, new double[] { actIndex, updateF.update(defVal) });
			return;
			// end small size optimization
		}

		if (act2vals.length == nrTotalActions) {
			act2vals[actIndex] = updateF.update(act2vals[actIndex]);
			return;
		}
		// begin small size optimization
		if ((short) act2vals[0] == actIndex) {
			act2vals[1] = updateF.update(act2vals[1]);
			return;
		}
		double[] newact2vals = initact2Vals();
		newact2vals[(short) act2vals[0]] = act2vals[1];
		newact2vals[actIndex] = updateF.update(defVal);
		values.put(state, newact2vals);
		// end small size optimization
	}

	public synchronized void set(State state, Action act, double val) {
		double[] act2vals = values.get(state);
		short actIndex = ((IndexedAction) act).getIndex();
		if (act2vals == null) {
			// begin small size optimization
			values.put(state, new double[] { actIndex, val });
			return;
			// end small size optimization
		}

		if (act2vals.length == nrTotalActions) {
			act2vals[actIndex] = val;
			return;
		}
		// begin small size optimization
		if ((short) act2vals[0] == actIndex) {
			act2vals[1] = val;
			return;
		}
		double[] newact2vals = initact2Vals();
		newact2vals[(short) act2vals[0]] = act2vals[1];
		newact2vals[actIndex] = val;
		values.put(state, newact2vals);
		// end small size optimization
	}

	public synchronized void add(State state, Action act, double val) {
		double[] act2vals = values.get(state);
		short actIndex = ((IndexedAction) act).getIndex();
		if (act2vals == null) {
			// begin small size optimization
			values.put(state, new double[] { actIndex, defVal + val });
			return;
			// end small size optimization
		}

		if (act2vals.length == nrTotalActions) {
			act2vals[actIndex] += val;
			return;
		}
		// begin small size optimization
		if ((short) act2vals[0] == actIndex) {
			act2vals[1] += val;
			return;
		}
		double[] newact2vals = initact2Vals();
		newact2vals[(short) act2vals[0]] = act2vals[1];
		newact2vals[actIndex] = defVal + val;
		values.put(state, newact2vals);
		// end small size optimization
	}

	private double[] initact2Vals() {
		double[] act2vals = new double[nrTotalActions];
		Arrays.fill(act2vals, defVal);
		return act2vals;
	}

	private Action getforSmallOpt(List<Action> possibleActions, double[] index_val) {
		for (Action act : possibleActions) {
			short actIndex = ((IndexedAction) act).getIndex();
			if ((short) index_val[0] == actIndex) {
				return act;
			}
		}
		return null;
	}

	public synchronized double getMaxValue(State state, List<Action> possibleActions) {
		double[] act2vals = values.get(state);
		if (act2vals == null) {
			return defVal;
		}

		if (act2vals.length == nrTotalActions) {
			double max = act2vals[((IndexedAction) possibleActions.get(0)).getIndex()];
			for (int i = 1; i < possibleActions.size(); i++) {
				double val = act2vals[((IndexedAction) possibleActions.get(i)).getIndex()];
				if (max < val) {
					max = val;
				}
			}
			return max;
		}

		// begin small size optimization
		if (getforSmallOpt(possibleActions, act2vals) != null) {
			return act2vals[1];
		}
		return defVal;
		// end small size optimization

	}

	public synchronized Action getMax(State state, List<Action> possibleActions) {
		double[] act2vals = values.get(state);
		if (act2vals == null) {
			return possibleActions.get(0);
		}

		if (act2vals.length == nrTotalActions) {
			double max = act2vals[((IndexedAction) possibleActions.get(0)).getIndex()];
			Action pozMax = possibleActions.get(0);
			for (int i = 1; i < possibleActions.size(); i++) {
				double val = act2vals[((IndexedAction) possibleActions.get(i)).getIndex()];
				if (max < val) {
					max = val;
					pozMax = possibleActions.get(i);
				}
			}
			return pozMax;
		}

		// begin small size optimization
		Action rez = getforSmallOpt(possibleActions, act2vals);
		if (rez == null) {
			return possibleActions.get(0);
		}
		return rez;
		// end small size optimization
	}

	public Iterator<State> visitedStates() {
		return values.keySet().iterator();
	}

	public int getNrVisitedStates() {
		return values.size();
	}

	public void printStatistics2() {

	}

	public void printStatistics() {
		int nrDefVals = 0;
		int histDefVals[] = new int[nrTotalActions];
		int nrSSOVal = 0;
		int nrTotalVals = 0;
		Iterator<double[]> it = values.values().iterator();
		while (it.hasNext()) {
			double[] qvals = it.next();

			if (qvals.length == 2) {
				nrSSOVal++;
				continue;
			}
			nrTotalVals++;
			int cateDef = 0;
			for (int i = 0; i < qvals.length; i++) {
				if (qvals[i] == defVal) {
					cateDef++;
				}
			}
			nrDefVals += cateDef;
			histDefVals[cateDef]++;

		}
		System.out.println(" totalVals " + nrTotalVals);
		System.out.println(" defaultVals " + nrDefVals + " percentDefVals "
				+ (nrDefVals * 100.0) / (nrTotalVals * nrTotalActions));
		System.out.println(" ssoVals " + nrSSOVal + " percentSSOVals " + (nrSSOVal * 100.0) / (values.size()));

		for (int i = 0; i < nrTotalActions; i++) {
			System.out.println(i + " " + histDefVals[i] + " procent:" + (histDefVals[i] * 100.0) / (nrTotalVals));
		}
	}
}
