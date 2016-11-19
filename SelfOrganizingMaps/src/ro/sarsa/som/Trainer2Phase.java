package ro.sarsa.som;

import ro.sarsa.som.traindata.SOMTrainData;
import ro.sarsa.som.trainsamplechooser.SOMTrainInputChooser;

public class Trainer2Phase {

	public static void train2Phase(SOM som, int nrIteration,
			SOMTrainData trData, double startLearnigRate,
			double startNeighborRadius, SOMTrainingListener l,
			SOMTrainInputChooser inputChosser) {
		som.train(nrIteration, trData, startLearnigRate, startNeighborRadius,
				l, inputChosser);
		som.train(nrIteration / 2, trData, startLearnigRate / 10, 0, l,
				inputChosser);
	}

	public static void train2Phase(SOM som, int nrIteration,
			SOMTrainData trData, double startLearnigRate,
			double startNeighborRadius, SOMTrainingListener l) {
		train2Phase(som, nrIteration, trData, startLearnigRate,
				startNeighborRadius, l,
				new RandomSOMReainInputChooser(trData.size()));
	}

}
