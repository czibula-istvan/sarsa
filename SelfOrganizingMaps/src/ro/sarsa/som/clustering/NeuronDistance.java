package ro.sarsa.som.clustering;

import ro.sarsa.clustering.IDistance;
import ro.sarsa.clustering.distances.EuclideanDistance;
import ro.sarsa.clustering.objects.VectorModelProvider;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.topology.SOMTopology;
import ro.sarsa.som.umatrix.SOMUMatrix;

public class NeuronDistance implements IDistance<SOMNeuron>, VectorModelProvider<SOMNeuron> {
	private SOMUMatrix umatrix;
	private SOMTopology topo;
	private IDistance<double[]> dist = new EuclideanDistance();

	public NeuronDistance(SOMUMatrix umatrix, SOMTopology topo) {
		this.umatrix = umatrix;
		this.topo = topo;
	}

	@Override
	public double distance(SOMNeuron a, SOMNeuron b) {
		return dist.distance(getVectorialModel(a), getVectorialModel(b));
		// if (topo.isNeighbors(a, b)) {
		// return dist.d(a.getWeights(), b.getWeights());
		// } else {
		// return dist.d(a.getNeuronPosition(), b.getNeuronPosition());
		// }
		// return dist
		// .d(
		// new double[] { a.getNeuronPosition()[0],
		// a.getNeuronPosition()[1],
		// umatrix.getNormalizedValue(a) }, new double[] {
		// b.getNeuronPosition()[0],
		// b.getNeuronPosition()[1],
		// umatrix.getNormalizedValue(b) });
		// return dist.d(new double[] { umatrix.getNormalizedValue(a) },
		// new double[] { umatrix.getNormalizedValue(b) });
	}

	public double[] getVectorialModel(SOMNeuron a) {
		return a.getWeights();
	}

	@Override
	public double getIninity() {
		return Double.MAX_VALUE;
	}

}
