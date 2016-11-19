package ro.sarsa.clustering.metrics;

import ro.sarsa.clustering.Cluster;
import ro.sarsa.clustering.IDistance;
import ro.sarsa.clustering.Partition;
import ro.sarsa.clustering.distances.EuclideanDistance;
import ro.sarsa.clustering.objects.VectorModelProvider;

/**
 * Calculeaza masura DavisBoudlin pentru o partitie
 *
 * @author istvan
 *
 */
public class DavisBoudlinMetric<T> implements PartitionMetric<T> {
	private static IDistance<double[]> dist = new EuclideanDistance();
	private VectorModelProvider<T> vectModel;

	public DavisBoudlinMetric(VectorModelProvider<T> vectModel) {
		this.vectModel = vectModel;
	}

	public double compute(Partition<T> currentPart) {
		double davisBoudlin = 0;
		for (int i = 0; i < currentPart.getNRClusters(); i++) {
			double max = 0;
			// double sum = 0;
			for (int j = i + 1; j < currentPart.getNRClusters(); j++) {
				Cluster<T> cl1 = currentPart.get(i);
				Cluster<T> cl2 = currentPart.get(j);
				double[] cl1Centre = MetricUtils.computeClusterCenter(cl1, vectModel);
				double[] cl2Centre = MetricUtils.computeClusterCenter(cl2, vectModel);
				double val = (MetricUtils.computeAverageObjectDistance(cl1Centre, cl1, vectModel)
						+ MetricUtils.computeAverageObjectDistance(cl2Centre, cl2, vectModel))
						/ dist.distance(cl1Centre, cl2Centre);
				if (val > max) {
					max = val;
				}
				// sum += val;
			}
			davisBoudlin += max;
			// davisBoudlin += sum;
		}
		davisBoudlin = davisBoudlin / currentPart.getNRClusters();
		return davisBoudlin;
	}
}
