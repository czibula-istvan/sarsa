package ro.sarsa.clustering.kmeans;

import java.util.ArrayList;
import java.util.List;

import ro.sarsa.clustering.ClusteringListener;
import ro.sarsa.clustering.IDistance;
import ro.sarsa.clustering.Partition;
import ro.sarsa.clustering.distances.EuclidianDistanceFeature;
import ro.sarsa.clustering.kmedoids.KMedoids;
import ro.sarsa.clustering.objects.ObjectWithFeature;

public class KMeans<ObjType extends ObjectWithFeature> {
	private IDistance<ObjectWithFeature> dist;

	public KMeans() {
		this(new EuclidianDistanceFeature<ObjectWithFeature>());
	}

	public KMeans(IDistance<ObjectWithFeature> dist) {
		this.dist = dist;
	}

	private List<Centroid> computeCentroizi(Partition<ObjType> part) {
		List<Centroid> rez = new ArrayList<Centroid>();
		for (int i = 0; i < part.getNRClusters(); i++) {
			rez.add(computeCentroid(part.get(i).getAllReadOnly()));
		}
		return rez;
	}

	/**
	 * Calculeaza centroidul
	 * 
	 * @param objects
	 * @return
	 */
	private Centroid computeCentroid(List<ObjType> objects) {
		double[] centroidFeatureValues = new double[objects.get(0)
				.getFeatures().length];
		for (int i = 0; i < centroidFeatureValues.length; i++) {
			centroidFeatureValues[i] = 0;
		}
		// calculam mediiile
		for (ObjType ob : objects) {
			double[] obFeatures = ob.getFeatures();
			for (int i = 0; i < centroidFeatureValues.length; i++) {
				centroidFeatureValues[i] += obFeatures[i];
			}
		}
		for (int i = 0; i < centroidFeatureValues.length; i++) {
			centroidFeatureValues[i] /= objects.size();
		}
		return new Centroid(centroidFeatureValues);
	}

	private double computeDistance(
			List<? extends ObjectWithFeature> centroiziO,
			List<? extends ObjectWithFeature> centroiziN) {
		double d = 0;
		for (int i = 0; i < centroiziO.size(); i++) {
			d += dist.distance(centroiziO.get(i), centroiziN.get(i));
		}
		return d;
	}

	public Partition<ObjType> partition(List<ObjType> objects, int nrClusters,
			ClusteringListener<ObjType> list) {
		double epsilon = 0.001;
		List<? extends ObjectWithFeature> centroiziO = KMedoids
				.pickRandomSeeds(nrClusters, objects);
		Partition<ObjType> part;
		double centroidDist;
		do {
			// part = computePartition(centroiziO, objects);
			part = (Partition<ObjType>) KMedoids.<ObjectWithFeature> computePartition(centroiziO, objects,
							dist);
			if (list != null) {
				list.intermediatePartition(part);
			}
			List<Centroid> centroiziN = computeCentroizi(part);
			centroidDist = computeDistance(centroiziO, centroiziN);
			centroiziO = centroiziN;
		} while (centroidDist > epsilon);
		return part;
	}

	public static void main(String[] args) {

	}
}
