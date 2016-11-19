package ro.sarsa.fuzzysom;

import ro.sarsa.som.BMU;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.topology.SOMTopology;

public class SOMFuzzyBMUWeightUpdate extends SOMFuzzyWeightUpdate {

	private static final double fuzinessDegree = 2;

	public SOMFuzzyBMUWeightUpdate(int inputSpaceDimension, SOMTopology topo) {
		super(inputSpaceDimension, topo);
	}

	protected BMU computeBestMatchingUnit(double[] input) {
		SOMTopology topo = getTopo();
		SOMNeuron bmu = null;
		double maxMiu = Double.MIN_VALUE;
		for (int i = 0; i < topo.getNrNeurons(); i++) {
			SOMNeuron neuron = topo.getNeuron(i);
			double miu = MiuComputer.computeMiu(input, neuron, this,
					fuzinessDegree);
			if (miu > maxMiu) {
				bmu = neuron;
				maxMiu = miu;
			}
		}
		return new BMU(bmu, maxMiu, input);
	}
}
