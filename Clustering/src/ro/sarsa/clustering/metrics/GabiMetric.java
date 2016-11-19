package ro.sarsa.clustering.metrics;

import ro.sarsa.clustering.Cluster;
import ro.sarsa.clustering.IDistance;
import ro.sarsa.clustering.Partition;
import ro.sarsa.clustering.distances.EuclideanDistance;
import ro.sarsa.clustering.objects.VectorModelProvider;

public class GabiMetric<T> implements PartitionMetric<T> {
	private static IDistance<double[]> dist = new EuclideanDistance();
	private VectorModelProvider<T> vectModel;

	public GabiMetric(VectorModelProvider<T> vectModel) {
		this.vectModel = vectModel;
	}

	public double compute(Partition<T> part) {
		double max = 0;
		double sum = 0;
		for (int i = 0; i < part.getNRClusters(); i++) {
			Cluster<T> cl1 = part.get(i);
			double[] cl1Centre = MetricUtils.computeClusterCenter(cl1, vectModel);
			double min = Double.MAX_VALUE;
			for (int j = 0; j < part.getNRClusters(); j++) {
				if (i == j) {
					continue;
				}
				Cluster<T> cl2 = part.get(j);
				double[] cl2Centre = MetricUtils.computeClusterCenter(cl2, vectModel);
				double distance = dist.distance(cl1Centre, cl2Centre);
				if (min > distance) {
					min = distance;
				}
			}
			double aux = MetricUtils.computeAverageObjectDistance(cl1Centre, cl1, vectModel) / min;
			sum = sum + aux;
			if (max < aux) {
				max = aux;
			}
		}
		return max;
		// return sum/part.getNRClusters();
	}
}
