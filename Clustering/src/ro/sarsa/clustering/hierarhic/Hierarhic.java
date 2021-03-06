package ro.sarsa.clustering.hierarhic;

import java.util.List;
import java.util.Map;

import ro.sarsa.clustering.Cluster;
import ro.sarsa.clustering.ClusteringListener;
import ro.sarsa.clustering.IDistance;
import ro.sarsa.clustering.Partition;
import ro.sarsa.clustering.hierarhic.HierarhicCache.MinCacheItem;

public class Hierarhic<T> {

	private List<T> objects;
	private LinkageMetric<T> linkMetric;

	private ClusteringStopCondition<T> stopC;
	private HierarhicCache<T> cache = new HierarhicCache<T>();

	public Hierarhic(LinkageMetric<T> lm, List<T> patCand, int nrDesiredClusters) {
		this(lm, patCand, new NumberOfClustersStopCondition<T>(nrDesiredClusters));
	}

	public Hierarhic(LinkageMetric<T> lm, List<T> objs, ClusteringStopCondition<T> stopC) {
		this.objects = objs;
		linkMetric = lm;
		this.stopC = stopC;
	}

	public Hierarhic(IDistance<T> d, List<T> patCand, int nrDesiredClusters) {
		this(new MaxLinkageMetric<T>(d), patCand, new NumberOfClustersStopCondition<T>(nrDesiredClusters));

		// linkMetric = new MaxLinkageMetric<T>(d);
		// linkMetric = new MinLinkageMetric<T>(d);
	}

	public Partition<T> clusterizare(ClusteringListener<T> list) {
		Partition<T> part = new Partition<T>(objects);

		boolean change = true;
		while (change) {
			hierarhicStep(part, list);
			if (stopC.isStopConditionReached(part)) {
				change = false;
			}
			if (list != null) {
				list.intermediatePartition(part);
			}
		}
		return part;
	}

	/**
	 * Pasul facut la fiecare iteratie Se cauta clusteri care sunt apropiati si
	 * se reunesc
	 * 
	 * @param part
	 */
	private void hierarhicStepNoCache(Partition<T> part) {
		int nrClusters = part.getNRClusters();
		Cluster<T> minCl1 = part.get(0);
		Cluster<T> minCl2 = part.get(1);
		double dmin = dist(minCl1, minCl2, null, Double.MAX_VALUE);

		// search for the pair of clusters with minimum distance
		for (int i = 0; i < nrClusters - 1; i++) {
			Cluster<T> cl1 = part.get(i);
			for (int j = i + 1; j < nrClusters; j++) {
				double auxDist = linkMetric.metric(cl1, part.get(j));
				if (auxDist < dmin) {
					dmin = auxDist;
					minCl1 = cl1;
					minCl2 = part.get(j);
				}
			}
		}

		Cluster<T> c = new Cluster<T>(minCl1, minCl2);
		part.delete(minCl1);
		part.delete(minCl2);
		part.add(c);
	}

	private void hierarhicStep(Partition<T> part, ClusteringListener<T> list) {
		long tm = System.currentTimeMillis();
		int nrClusters = part.getNRClusters();
		Cluster<T> minCl1 = part.get(0);
		Cluster<T> minCl2 = part.get(1);
		double dmin = dist(minCl1, minCl2, null, Double.MAX_VALUE);
		System.out.println("alta iteratie nrClusters" + nrClusters);
		// caut perechi de clusteri care au distanta minima intre ele
		for (int i = 0; i < nrClusters - 1; i++) {
			Cluster<T> cl1 = part.get(i);
			// try to avoid the inner 'for' altogether
			MinCacheItem min = cache.getMinFromCache(cl1, linkMetric, dmin);
			if (min != null) {
				if (min.dmin < dmin) {
					// linkMetric.metric(cl1, min.minCL)==min.dmin;
					dmin = min.dmin;
					minCl1 = cl1;
					minCl2 = min.minCL;
				}
				continue;
			}
			Cluster<T> minCl2Int = part.get(i + 1);
			double dminInt = dist(cl1, minCl2Int, null, dmin);

			// in order to avoid multiple cache lookup for cl1 in the inner
			// for
			Map<Cluster<T>, Double> cacheLMLine = cache.getLMCacheLine(cl1);
			for (int j = i + 1; j < nrClusters; j++) {
				double auxDist = dist(cl1, part.get(j), cacheLMLine, dmin);
				if (auxDist < dminInt) {
					dminInt = auxDist;
					minCl2Int = part.get(j);
				}

			}
			cache.addMinToCache(cl1, dminInt, minCl2Int);
			if (dminInt < dmin) {
				dmin = dminInt;
				minCl1 = cl1;
				minCl2 = minCl2Int;
			}
		}
		notifyToBeMerged(part, minCl1, minCl2, list);

		Cluster<T> c = new Cluster<T>(minCl1, minCl2);
		part.delete(minCl1);
		part.delete(minCl2);
		part.add(c);
		cache.partitionChanges(minCl1, minCl2, c);
		long timp = (System.currentTimeMillis() - tm);
		System.out.println("NR clusteri in partitia curenta:" + part.getNRClusters() + " timp:" + timp);

	}

	private void notifyToBeMerged(Partition<T> part, Cluster<T> minCl1, Cluster<T> minCl2, ClusteringListener<T> list) {
		if (list != null && list instanceof HierarhicClusteringListener) {
			((HierarhicClusteringListener<T>) list).clustersToBeMerged(part, minCl1, minCl2);
		}
	}

	private double dist(Cluster<T> a, Cluster<T> b, Map<Cluster<T>, Double> cacheLine, double currentMin) {
		Double rez = cache.getLMCache(cacheLine, b);
		if (rez != null) {
			return rez;
		}
		// System.out.println("metric computed " + nrClusters);

		double val = linkMetric.metric(a, b);
		cache.putLMCache(a, b, val);
		return val;
	}
}
