package ro.sarsa.clustering.kmedoids;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ro.sarsa.clustering.Cluster;
import ro.sarsa.clustering.ClusteringListener;
import ro.sarsa.clustering.IDistance;
import ro.sarsa.clustering.Partition;

public class KMedoids<T> {
	private IDistance<T> dist;

	public KMedoids(IDistance<T> d) {
		this.dist = d;
	}

	/**
	 * Add each object to the closest seed (medoid)
	 * 
	 * @param seeds
	 * @return
	 */
	public static <T> Partition<T> computePartition(List<? extends T> seeds,
			List<? extends T> objects, IDistance<T> dist) {
		// start with partition with nrSeed empty clusters
		Partition<T> rez = new Partition<T>(seeds.size());
		// we will assign each element to the cluster corresponding to the
		// closest seed
		for (T ob : objects) {
			int seedNr = 0;
			double minDist = dist.distance(seeds.get(0), ob);
			for (int i = 1; i < seeds.size(); i++) {
				double d = dist.distance(seeds.get(i), ob);
				if (d < minDist) {
					minDist = d;
					seedNr = i;
				}
			}
			// assign object
			rez.addToCluster(seedNr, ob);
		}
		return rez;
	}

	public Partition<T> partition(List<T> objects, int nrClusters,
			ClusteringListener<T> list) {
		// select initial medoids randomly
		List<T> medoids = KMedoids.<T> pickRandomSeeds(nrClusters, objects);

		// assign each object to the closest medoid
		Partition<T> part = computePartition(medoids, objects, dist);
		double cost = computeCost(part, medoids);
		boolean changed = false;

		do {
			changed = false;
			List<T> medoidCopy = new ArrayList<T>(medoids);
			for (int i = 0; i < medoids.size(); i++) {
				Cluster<T> cl = part.get(i);
				for (int j = 0; j < cl.getNRObjs(); j++) {
					// change medoid
					medoidCopy.set(i, cl.get(j));
					// compute a new partition for the new medoid list
					Partition<T> partAux = computePartition(medoids, objects,
							dist);
					double costAux = computeCost(partAux, medoidCopy);
					if (costAux < cost) {
						// if the new partition is better
						changed = true;
						part = partAux;
						cost = costAux;
						medoids = new ArrayList<T>(medoidCopy);
						notifyNewPartition(list, part);
					}
				}
			}
		} while (changed);
		return part;
	}

	private void notifyNewPartition(ClusteringListener<T> list,
			Partition<T> part) {
		if (list != null) {
			list.intermediatePartition(part);
		}
	}

	/**
	 * Compute the cost associated with the partition Cost is defined as the sum
	 * of distances to medoids
	 * 
	 * @param part
	 * @param medoids
	 * @return
	 */
	private double computeCost(Partition<T> part, List<T> medoids) {
		double rez = 0;
		for (int i = 0; i < medoids.size(); i++) {
			Cluster<T> cl = part.get(i);
			T medoid = medoids.get(i);
			double sum = 0;
			for (int j = 0; j < cl.getNRObjs(); j++) {
				sum += dist.distance(medoid, cl.get(j));
			}
			rez += sum;
		}
		return rez;
	}

	/**
	 * Alege aleator nrSeeds obiecte diferite
	 * 
	 * @param nrSeeds
	 * @return
	 */
	public static <T> List<T> pickRandomSeeds(int nrSeeds, List<T> objects) {
		Random r = new Random();
		List<T> seeds = new ArrayList<T>(nrSeeds);
		List<T> cpy = new ArrayList<T>(objects);
		for (int i = 0; i < nrSeeds; i++) {
			int poz = r.nextInt(cpy.size());
			seeds.add(cpy.remove(poz));
		}
		return seeds;
	}
}
