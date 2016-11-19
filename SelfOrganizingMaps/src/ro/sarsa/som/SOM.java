package ro.sarsa.som;

import java.util.List;

import ro.sarsa.clustering.IDistance;
import ro.sarsa.clustering.distances.EuclideanDistance;
import ro.sarsa.som.topology.SOMTopology;
import ro.sarsa.som.traindata.SOMTrainData;
import ro.sarsa.som.trainsamplechooser.SOMTrainInputChooser;

public class SOM {

	/**
	 * Discriminant function Folosit pentru a determina cel mai apropiat neuron
	 * pentru 0 intrare
	 */
	protected IDistance<double[]> dist;
	// private IDistance<double[]> dist = new ManhattanDistance();
	protected SOMTopology comptationL;

	public SOM(int inputSpaceDimension, SOMTopology topo) {
		this(inputSpaceDimension, topo, new EuclideanDistance());
	}

	public SOM(int inputSpaceDimension, SOMTopology topo, IDistance<double[]> dist) {
		comptationL = topo;
		this.dist = dist;
	}

	public SOMTopology getTopo() {
		return comptationL;
	}

	public IDistance<double[]> getDistance() {
		return dist;
	}

	public void train(int nrIteration, SOMTrainData trData, double startLearnigRate, SOMTrainingListener l) {
		double maxNeighborRadius = comptationL.getMaxRadius() / 2d;
		train(nrIteration, trData, startLearnigRate, maxNeighborRadius, l);
	}

	public void train(int nrIteration, SOMTrainData trData, double startLearnigRate, double startNeighborRadius,
			SOMTrainingListener l) {
		train(nrIteration, trData, startLearnigRate, startNeighborRadius, l,
				new RandomSOMReainInputChooser(trData.size()));
	}

	public void train(int nrIteration, SOMTrainData trData, double startLearnigRate, double startNeighborRadius,
			SOMTrainingListener l, SOMTrainInputChooser inpuChooser) {
		double lambdaTimeConstant = nrIteration / Math.log(startNeighborRadius);

		for (int iteration = 0; iteration < nrIteration; iteration++) {
			// calculez learningRate
			double learningRate = startLearnigRate * Math.exp(-(double) iteration / nrIteration);
			double neighborRadius = startNeighborRadius * Math.exp(-(double) iteration / lambdaTimeConstant);

			// aleg input
			int[] inputIndexes = inpuChooser.getNextInputIndex();
			for (int i = 0; i < inputIndexes.length; i++) {
				int inputIndex = inputIndexes[i];
				trainingStep(iteration, trData.get(inputIndex), learningRate, neighborRadius, l);
			}
		}
	}

	/**
	 * O iteratie din training
	 */
	public void trainingStep(int curIter, double input[], double learningRate, double neighborRadius,
			SOMTrainingListener l) {
		// caut Best maching unit
		BMU bmu = computeBestMatchingUnit(input);
		// actualizez weights de la bmu
		updateNeuronWeights(bmu.getNeuron(), input, learningRate, 1);

		// obtin lista vecinilor ce pica in radiusul cerut
		List<NeighborSOMNeuron> neighbors = comptationL.getNeighbors(bmu.getNeuron(), neighborRadius);
		// actualizez valorile in functie de cat de apropiat e de BMU
		if (neighbors != null && neighbors.size() > 0) {
			updateNeighbors(input, learningRate, neighborRadius, neighbors);
		}
		if (l != null) {
			l.trainStepPerformed(curIter, input, bmu, neighbors);
		}
	}

	protected BMU computeBestMatchingUnit(double[] input) {
		BMU bmu = BMU.computeBestMatchingUnit(input, comptationL, dist);
		return bmu;
	}

	protected void updateNeighbors(double[] input, double learningRate, double neighborRadius,
			List<NeighborSOMNeuron> neighbors) {
		final double aux = 2 * neighborRadius * neighborRadius;
		for (int i = 0; i < neighbors.size(); i++) {
			NeighborSOMNeuron neighborSOMNeuron = neighbors.get(i);
			double influence = Math.exp(-(neighborSOMNeuron.getDistance() * neighborSOMNeuron.getDistance()) / aux);
			updateNeuronWeights(neighborSOMNeuron.getNeuron(), input, learningRate, influence);
		}
	}

	protected void updateNeuronWeights(SOMNeuron neuron, double[] input, double learningRate, double influence) {
		neuron.adjustWeights(input, learningRate, influence);
	}

}
