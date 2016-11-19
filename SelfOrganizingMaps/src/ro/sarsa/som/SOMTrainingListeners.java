package ro.sarsa.som;

import java.util.ArrayList;
import java.util.List;

public class SOMTrainingListeners implements SOMTrainingListener {
	private List<SOMTrainingListener> lss = new ArrayList<SOMTrainingListener>(2);

	public SOMTrainingListeners(SOMTrainingListener l) {
		addListener(l);
	}

	public void addListener(SOMTrainingListener l) {
		if (l != null && !lss.contains(l)) {
			lss.add(l);
		}
	}

	@Override
	public void trainStepPerformed(int iteration, double[] input, BMU bmu, List<NeighborSOMNeuron> neighbors) {
		for (int i = 0; i < lss.size(); i++) {
			lss.get(i).trainStepPerformed(iteration, input, bmu, neighbors);
		}

	}

}
