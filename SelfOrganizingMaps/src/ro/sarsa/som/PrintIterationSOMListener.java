package ro.sarsa.som;

import java.util.List;

import ro.sarsa.som.traindata.SOMTrainData;

public class PrintIterationSOMListener implements SOMTrainingListener {
	private SOM som;
	private SOMTrainData td;
	private int prevIT = -1;

	public PrintIterationSOMListener(SOM som, SOMTrainData td) {
		this.td = td;
		this.som = som;
	}

	@Override
	public void trainStepPerformed(int iteration, double[] input, BMU bmu, List<NeighborSOMNeuron> neighbors) {
		if (prevIT != iteration) {
			// double avgQE = som.computeAverageQuantizationError(td);
			// System.out.format("%.5f%n", avgQE);
			System.out.println(iteration);
		}
		prevIT = iteration;

	}

}
