package ro.sarsa.rl.utilities;

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
public class QValuesIndexedSSO extends AbstractQValues {

	private Map<State, double[]> values;
	private Map<State, double[]> smalSizeOpt;
	private double defVal;
	private int nrTotalActions;
	private State sampleState;

	public QValuesIndexedSSO(int nrTotalActions, State sampleState) {
		this.nrTotalActions = nrTotalActions;
		this.sampleState = sampleState;
		initQValues(sampleState);
	}

	public void initQValues(State sampleState) {
		if (sampleState instanceof Comparable) {
			values = new TreeMap<State, double[]>();
			smalSizeOpt = new TreeMap<State, double[]>();
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
		// begin small size optimization
		double[] index_val = smalSizeOpt.get(state);
		if (index_val != null) {
			short actIndex = ((IndexedAction) act).getIndex();
			if ((short) index_val[0] == actIndex) {
				return index_val[1];
			}
			return defVal;
		}
		// end small size optimization

		double[] act2vals = values.get(state);
		if (act2vals == null) {
			return defVal;
		}
		return act2vals[((IndexedAction) act).getIndex()];
	}

	public synchronized void set(State state, Action act, double val) {
		double[] act2vals = values.get(state);
		if (act2vals == null) {
			// begin small size optimization
			double[] index_val = smalSizeOpt.get(state);
			short actIndex = ((IndexedAction) act).getIndex();
			if (index_val == null) {
				smalSizeOpt.put(state, new double[] { actIndex, val });
				return;
			} else {
				if ((short) index_val[0] == actIndex) {
					index_val[1] = val;
					return;
				} else {
					smalSizeOpt.remove(state);
				}
			}
			// end small size optimization
			act2vals = initact2Vals();
			if (index_val != null) {
				act2vals[(short) index_val[0]] = index_val[1];
			}
			values.put(state, act2vals);
		}
		act2vals[((IndexedAction) act).getIndex()] = val;
	}

	public synchronized void add(State state, Action act, double val) {
		throw new IllegalArgumentException();
		// double[] act2vals = values.get(state);
		// if (act2vals == null) {
		// act2vals = initact2Vals();
		// values.put(state, act2vals);
		// }
		// act2vals[((IndexedAction) act).getIndex()] += val;
	}

	private double[] initact2Vals() {
		double[] act2vals;
		act2vals = new double[nrTotalActions];
		for (int i = 0; i < nrTotalActions; i++) {
			act2vals[i] = defVal;
		}
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
		// begin small size optimization
		double[] index_val = smalSizeOpt.get(state);
		if (index_val != null) {
			if (getforSmallOpt(possibleActions, index_val) != null) {
				return index_val[1];
			}
			return defVal;
		}
		// end small size optimization
		double[] act2vals = values.get(state);
		if (act2vals == null) {
			return defVal;
		}

		double max = act2vals[((IndexedAction) possibleActions.get(0)).getIndex()];
		for (int i = 1; i < possibleActions.size(); i++) {
			double val = act2vals[((IndexedAction) possibleActions.get(i)).getIndex()];
			if (max < val) {
				max = val;
			}
		}
		return max;
	}

	public synchronized Action getMax(State state, List<Action> possibleActions) {
		// begin small size optimization
		double[] index_val = smalSizeOpt.get(state);
		if (index_val != null) {
			Action rez = getforSmallOpt(possibleActions, index_val);
			if (rez == null) {
				return possibleActions.get(0);
			}
			return rez;
		}
		// end small size optimization

		double[] act2vals = values.get(state);
		if (act2vals == null) {
			return possibleActions.get(0);
		}

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

	public Iterator<State> visitedStates() {
		return values.keySet().iterator();
	}

	public int getNrVisitedStates() {
		return values.size() + smalSizeOpt.size();
	}

	public void printStatistics() {

	}

	public void printStatistics2() {
		int nrDefVals = 0;
		int histDefVals[] = new int[nrTotalActions];

		int nrTotalVals = 0;
		Iterator<double[]> it = values.values().iterator();
		while (it.hasNext()) {
			double[] qvals = it.next();
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
		for (int i = 0; i < nrTotalActions; i++) {
			System.out
					.println(" " + i + " " + histDefVals[i] + "	 procent:" + (histDefVals[i] * 100.0) / (nrTotalVals));
		}
	}
}
