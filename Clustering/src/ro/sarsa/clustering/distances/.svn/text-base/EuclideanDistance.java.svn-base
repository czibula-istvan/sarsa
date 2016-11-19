package ro.sarsa.clustering.distances;

import ro.sarsa.clustering.IDistance;


public class EuclideanDistance implements IDistance<double[]> {

	@Override
	public double distance(double[] a, double[] b) {
		double dist = 0;
		for (int i = 0; i < a.length; i++) {
			double aux = a[i] - b[i];
			dist += aux * aux;
		}
		return Math.sqrt(dist);
	}

	@Override
	public double getIninity() {
		return Double.MAX_VALUE;
	}

}
