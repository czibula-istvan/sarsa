package ro.sarsa.som.clustering;

import ro.sarsa.clustering.Cluster;
import ro.sarsa.clustering.hierarhic.LinkageMetric;
import ro.sarsa.clustering.hierarhic.MaxLinkageMetric;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.topology.SOMTopology;
import ro.sarsa.som.umatrix.SOMUMatrix;

public class RefactoringSOMLinkageMetric implements LinkageMetric<SOMNeuron> {
	private LinkageMetric<SOMNeuron> avgLM;
	private SOMTopology topo;
	private NeuronDistance neuronDistance;

	public RefactoringSOMLinkageMetric(SOMUMatrix umatrix, SOMTopology topo) {
		neuronDistance = new NeuronDistance(umatrix, topo);
		// avgLM = new AvgLinkageMetric<SOMNeuron>(neuronDistance);
		avgLM = new MaxLinkageMetric<SOMNeuron>(neuronDistance);
		this.topo = topo;
	}

	public NeuronDistance getNeuronDistance() {
		return neuronDistance;
	}

	@Override
	public double metric(Cluster<SOMNeuron> a, Cluster<SOMNeuron> b) {
		if (isNeighbors(a, b)) {
			return avgLM.metric(a, b);
		}
		return Double.MAX_VALUE;
	}

	private boolean isNeighbors(Cluster<SOMNeuron> a, Cluster<SOMNeuron> b) {
		// if (topo == null) {
		return true;
		// }
		// List<SOMNeuron> neuronia = a.getAll();
		// List<SOMNeuron> neuronib = b.getAll();
		// for (int i = 0; i < neuronia.size(); i++) {
		// for (int j = 0; j < neuronib.size(); j++) {
		// if (topo.isNeighbors(neuronia.get(i), neuronib.get(j))) {
		// return true;
		// }
		// }
		// }
		// return false;
	}
}
