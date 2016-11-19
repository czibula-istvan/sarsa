package ro.sarsa.fuzzysom;

import ro.sarsa.clustering.IDistance;
import ro.sarsa.som.SOM;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.topology.SOMTopology;

public class SOMFuzzyWeightUpdate extends SOM {

	private static final int fuzinessDegree = 2;

	public SOMFuzzyWeightUpdate(int inputSpaceDimension, SOMTopology topo) {
		super(inputSpaceDimension, topo);
	}

	public SOMFuzzyWeightUpdate(int inputSpaceDimension, SOMTopology topo,
			IDistance<double[]> dist) {
		super(inputSpaceDimension, topo, dist);
	}

	/**
	 * When we update the weights we take into account membership degrees (miu)
	 */
	protected void updateNeuronWeights(SOMNeuron neuron, double[] input,
			double learningRate, double influence) {
		double m = 0;
		m = MiuComputer.computeMiu(input, neuron, this, fuzinessDegree);
		m = Math.pow(m, fuzinessDegree);
//		System.out.println(m + " " + influence + " " + learningRate);
		influence = influence * m;
		neuron.adjustWeights(input, learningRate, influence);
	}
}
