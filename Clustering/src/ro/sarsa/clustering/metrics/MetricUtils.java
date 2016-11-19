package ro.sarsa.clustering.metrics;

import java.util.List;

import ro.sarsa.clustering.Cluster;
import ro.sarsa.clustering.IDistance;
import ro.sarsa.clustering.distances.EuclideanDistance;
import ro.sarsa.clustering.objects.VectorModelProvider;

public class MetricUtils {
	private static IDistance<double[]> dist = new EuclideanDistance();

	public static <T> double computeAverageObjectDistance(
			double[] clusterCentre, Cluster<T> clus,
			VectorModelProvider<T> vectModel) {
		return computeAverageObjectDistance(clusterCentre, clus.getAll(),
				vectModel, dist);
	}

	public static <T> double computeAverageObjectDistance(
			double[] clusterCentre, List<T> clus,
			VectorModelProvider<T> vectModel, IDistance<double[]> dist) {
		double avgDist = 0;
		for (int i = 0; i < clus.size(); i++) {
			double[] attrs = vectModel.getVectorialModel(clus.get(i));
			avgDist += dist.distance(attrs, clusterCentre);
		}
		return avgDist / clus.size();
	}

	public static <T> double[] computeClusterCenter(Cluster<T> clus,
			VectorModelProvider<T> vectModel) {
		return computeClusterCenter(clus.getAll(), vectModel, dist);
	}

	public static <T> double computeAvgDistanceToCenter(List<T> objs,
			VectorModelProvider<T> vectModel) {
		double[] center = computeClusterCenter(objs, vectModel, dist);
		return computeAverageObjectDistance(center, objs, vectModel, dist);
	}

	public static <T> double[] computeClusterCenter(List<T> objs,
			VectorModelProvider<T> vectModel) {
		return computeClusterCenter(objs, vectModel, dist);
	}

	public static <T> double[] computeClusterCenter(List<T> objs,
			VectorModelProvider<T> vectModel, IDistance<double[]> dist) {
		int nrAttrs = vectModel.getVectorialModel(objs.get(0)).length;
		double[] median = new double[nrAttrs];
		for (int i = 0; i < objs.size(); i++) {
			double[] attrs = vectModel.getVectorialModel(objs.get(i));
			for (int j = 0; j < nrAttrs; j++) {
				median[j] += attrs[j];
			}
		}
		for (int j = 0; j < nrAttrs; j++) {
			median[j] = median[j] / objs.size();
		}
		return median;
	}

}
