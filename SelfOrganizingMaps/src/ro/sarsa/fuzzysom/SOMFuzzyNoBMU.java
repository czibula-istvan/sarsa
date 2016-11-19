package ro.sarsa.fuzzysom;

import java.util.List;

import ro.sarsa.som.NeighborSOMNeuron;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.SOMTrainingListener;
import ro.sarsa.som.topology.SOMTopology;

public class SOMFuzzyNoBMU extends SOMFuzzyWeightUpdate {

	public SOMFuzzyNoBMU(int inputSpaceDimension, SOMTopology topo) {
		super(inputSpaceDimension, topo);
	}

	/**
	 * Every time update all the neurons Update will use the membership (miu -
	 * updateNeuronWeights defined in SOMFuzzyWeightUpdate)
	 */
	public void trainingStep(int curIter, double input[], double learningRate,
			double neighborRadius, SOMTrainingListener l) {
		for (int i = 0; i < getTopo().getNrNeurons(); i++) {
			SOMNeuron neuron = getTopo().getNeuron(i);
			// updateNeuronWeights(neuron, input, learningRate, 1);
			updateNeuronWeights(neuron, input, learningRate, 1);

			// obtin lista vecinilor ce pica in radiusul cerut
			List<NeighborSOMNeuron> neighbors = comptationL.getNeighbors(
					neuron, neighborRadius);
			// actualizez valorile in functie de cat de apropiat e de BMU
			if (neighbors != null && neighbors.size() > 0) {
				updateNeighbors(input, learningRate, neighborRadius, neighbors);
			}
		}
		if (l != null) {
			l.trainStepPerformed(curIter, input, null, null);
		}
	}

}
