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
public class QValuesIndexedSSO3 extends AbstractQValues {
    /**
     * Eiter <State,double[]> or <State,SSO> - for a single value
     */
	private Map<State, Object> values;
	private double defVal;
	private int nrTotalActions;
	private State sampleState;
	
	private static final class SSO{
	   final byte actIndex;
	   double val;
	   public SSO(short actIndex,double val){
		this.actIndex = (byte) actIndex;
		this.val = val;		   
	   }
	}
	
	public QValuesIndexedSSO3(int nrTotalActions, State sampleState) {
		if (nrTotalActions>255){
			throw new IllegalArgumentException("Only works for max 255 actionsf");
		}
		this.nrTotalActions = nrTotalActions;
		this.sampleState = sampleState;
		initQValues(sampleState);
	}

	public void initQValues(State sampleState) {
		if (sampleState instanceof Comparable) {
			values = new TreeMap<State, Object>();
			// values =new ConcurrentSkipListMap<State, double[]>();
		} else {
			values = new HashMap<State, Object>();
		}
	}

	public void resetTo(double val) {
		initQValues(sampleState);
		defVal = val;
	}

	public synchronized double get(State state, Action act) {
		Object act2vals = values.get(state);
		if (act2vals == null) {
			return defVal;
		}
		short actIndex = ((IndexedAction) act).getIndex();
		// begin small size optimization
		if (act2vals instanceof SSO) {
			if (((SSO)act2vals).actIndex == actIndex) {
				return ((SSO)act2vals).val;
			}
			return defVal;
		}
		// end small size optimization

		return ((double[])act2vals)[actIndex];
	}

	public synchronized void updateValue(State state, Action act, UpdateFunction updateF) {
		Object act2vals = values.get(state);
		short actIndex = ((IndexedAction) act).getIndex();
		if (act2vals == null) {
			// begin small size optimization
			values.put(state, new SSO(actIndex, updateF.update(defVal)));
			return;
			// end small size optimization
		}

		
		if (act2vals instanceof double[]) {
			double[] aux = (double[])act2vals; 
			aux[actIndex] = updateF.update(aux[actIndex]);
			return;
		}
		// begin small size optimization
		SSO sso = (SSO) act2vals;
		if (sso.actIndex == actIndex) {
			sso.val = updateF.update(sso.val);
			return;
		}
		double[] newact2vals = initact2Vals();
		newact2vals[sso.actIndex] = sso.val;
		newact2vals[actIndex] = updateF.update(defVal);
		values.put(state, newact2vals);
		// end small size optimization
	}

	public synchronized void set(State state, Action act, double val) {
		Object act2vals = values.get(state);
		short actIndex = ((IndexedAction) act).getIndex();
		if (act2vals == null) {
			// begin small size optimization
			values.put(state, new SSO ( actIndex, val ));
			return;
			// end small size optimization
		}

		if (act2vals instanceof double[]) {			
			((double[])act2vals)[actIndex] = val;
			return;
		}
		
		// begin small size optimization
		SSO sso = (SSO) act2vals;
		if (sso.actIndex == actIndex) {
			sso.val = val;
			return;
		}
		double[] newact2vals = initact2Vals();
		newact2vals[sso.actIndex] = sso.val;
		newact2vals[actIndex] = val;
		values.put(state, newact2vals);
		// end small size optimization
	}

	public synchronized void add(State state, Action act, double val) {
		Object act2vals = values.get(state);
		short actIndex = ((IndexedAction) act).getIndex();
		if (act2vals == null) {
			// begin small size optimization
			values.put(state, new SSO( actIndex, defVal + val ));
			return;
			// end small size optimization
		}

		if (act2vals instanceof double[]) {	
			((double[])act2vals)[actIndex] += val;
			return;
		}
		// begin small size optimization
		SSO sso = (SSO) act2vals;
		if (sso.actIndex == actIndex) {
			sso.val += val;
			return;
		}
		double[] newact2vals = initact2Vals();
		newact2vals[sso.actIndex] = sso.val;
		newact2vals[actIndex] = defVal + val;
		values.put(state, newact2vals);
		// end small size optimization
	}

	private double[] initact2Vals() {
		double[] act2vals = new double[nrTotalActions];
		Arrays.fill(act2vals, defVal);
		return act2vals;
	}

	private Action getforSmallOpt(List<Action> possibleActions, SSO sso) {
		for (Action act : possibleActions) {
			short actIndex = ((IndexedAction) act).getIndex();
			if (sso.actIndex == actIndex) {
				return act;
			}
		}
		return null;
	}

	public synchronized double getMaxValue(State state, List<Action> possibleActions) {
		Object act2vals = values.get(state);
		if (act2vals == null) {
			return defVal;
		}

		if (act2vals instanceof double[]) {
			double[] aux = (double[])act2vals;
			double max = aux[((IndexedAction) possibleActions.get(0)).getIndex()];
			for (int i = 1; i < possibleActions.size(); i++) {
				double val = aux[((IndexedAction) possibleActions.get(i)).getIndex()];
				if (max < val) {
					max = val;
				}
			}
			return max;
		}

		// begin small size optimization
		SSO sso = (SSO) act2vals;
		if (getforSmallOpt(possibleActions, sso) != null) {
			return sso.val;
		}
		return defVal;
		// end small size optimization

	}

	public synchronized Action getMax(State state, List<Action> possibleActions) {
		Object act2vals = values.get(state);
		if (act2vals == null) {
			return possibleActions.get(0);
		}

		if (act2vals instanceof double[]) {
			double[] aux = (double[])act2vals;
			double max = aux[((IndexedAction) possibleActions.get(0)).getIndex()];
			Action pozMax = possibleActions.get(0);
			for (int i = 1; i < possibleActions.size(); i++) {
				double val = aux[((IndexedAction) possibleActions.get(i)).getIndex()];
				if (max < val) {
					max = val;
					pozMax = possibleActions.get(i);
				}
			}
			return pozMax;
		}

		// begin small size optimization
		SSO sso = (SSO)act2vals;
		Action rez = getforSmallOpt(possibleActions, sso);
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
		Iterator it = values.values().iterator();
		while (it.hasNext()) {
			Object qvalsOrSSo = it.next();

			if (qvalsOrSSo instanceof SSO) {
				nrSSOVal++;
				continue;
			}
			double[] qvals = (double[])qvalsOrSSo;
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
		System.out.println(" totalVals " + nrTotalVals+" nrTotalActions:"+nrTotalActions);
		System.out.println(" defaultVals " + nrDefVals + " percentDefVals "
				+ (nrDefVals * 100.0) / (nrTotalVals * nrTotalActions));
		System.out.println(" ssoVals " + nrSSOVal + " percentSSOVals " + (nrSSOVal * 100.0) / (values.size()));

		for (int i = nrTotalActions-5; i < nrTotalActions-1; i++) {
			System.out.println(i + " " + histDefVals[i] + " procent:" + (histDefVals[i] * 100.0) / (nrTotalVals)+ " procent total:" + (histDefVals[i] * 100.0) / (values.size()));
		}
	}
}
