package ro.sarsa.som;

import java.util.ArrayList;
import java.util.List;

import ro.sarsa.clustering.IDistance;
import ro.sarsa.som.topology.SOMTopology;
import ro.sarsa.som.traindata.SOMTrainData;

public class BMU {
	private double distToInput = Double.MAX_VALUE;// distanta minima
	private double[] input;
	private SOMNeuron bmuN = null;// neuronul castigator
	private Object label;

	public BMU(SOMNeuron n, double dist, double[] input) {
		this.bmuN = n;
		this.distToInput = dist;
		this.input = input;
	}

	public BMU(SOMNeuron n, double dist, double[] input, String label) {
		this.bmuN = n;
		// this.dist = dist;
		this.input = input;
		this.label = label;
	}

	public Object getLabel() {
		return this.label;
	}

	public void setLabel(Object lbl) {
		this.label = lbl;
	}

	public int getNeuronIndex() {
		return bmuN.getIndex();
	}

	public SOMNeuron getNeuron() {
		return bmuN;
	}

	public boolean equals(Object o) {
		return bmuN == ((BMU) o).bmuN;
	}

	public double getDistToInput() {
		return distToInput;
	}

	public static BMU computeBestMatchingUnit(double[] input,
			SOMTopology comptationL, IDistance<double[]> dist, Object label) {
		BMU rez = computeBestMatchingUnit(input, comptationL, dist);
		rez.setLabel(label);
		return rez;
	}

	public static BMU computeBestMatchingUnit(double[] input,
			SOMTopology comptationL, IDistance<double[]> dist) {
		double minDist = Double.MAX_VALUE;// distanta minima
		SOMNeuron bmuN = null;// neuronul castigator
		int nrNeurons = comptationL.getNrNeurons();
		for (int i = 0; i < nrNeurons; i++) {
			SOMNeuron neuron = comptationL.getNeuron(i);
			double d = dist.distance(input, neuron.getWeights());
			if (d < minDist) {
				minDist = d;
				bmuN = neuron;
			}
		}
		return new BMU(bmuN, minDist, input);
	}

	/**
	 * Best Matching unit for all the input sample
	 * 
	 * @param trD
	 * @param som
	 * @param dist
	 * @return
	 */
	public static List<BMU> getBMUS(SOMTrainData trD, SOM som,
			IDistance<double[]> dist) {
		List<BMU> bmus = new ArrayList<BMU>(trD.size());
		for (int i = 0; i < trD.size(); i++) {
			bmus.add(BMU.computeBestMatchingUnit(trD.get(i), som.getTopo(),
					dist, trD.getLabel(i)));
			// computeBestMatchingUnit(trD.get(i), trD.getLabel(i))
			// bmus.add(BMU.computeBestMatchingUnit(trD.get(i), som.getTopo(),
			// dist));
		}
		return bmus;
	}

}
