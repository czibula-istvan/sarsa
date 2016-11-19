package ro.sarsa.clustering.hierarhic;

import ro.sarsa.clustering.Cluster;
import ro.sarsa.clustering.IDistance;

public class MaxLinkageMetric<T> implements LinkageMetric<T> {
	private IDistance<T> distance;

	public MaxLinkageMetric(IDistance<T> distance) {
		this.distance = distance;
	}

	@Override
	public double metric(Cluster<T> a, Cluster<T> b) {
		double dmax = Double.MIN_VALUE;
		for (int i = 0; i < a.getNRObjs(); i++) {
			for (int j = 0; j < b.getNRObjs(); j++) {
				double aux = distance.distance(a.get(i), b.get(j));
				if (dmax < aux) {
					dmax = aux;
				}
				if (dmax == distance.getIninity()) {
					return dmax;
				}
			}
		}
		return dmax;
	}

}
