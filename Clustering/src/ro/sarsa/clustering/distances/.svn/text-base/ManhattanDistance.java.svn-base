package ro.sarsa.clustering.distances;

import ro.sarsa.clustering.IDistance;

public class ManhattanDistance implements IDistance<double[]> {

	@Override
	public double distance(double[] a, double[] b) {
		double dist = 0;
		for (int i = 0; i < a.length; i++) {
			dist += Math.abs(a[i] - b[i]);
		}
		return dist;
	}

	@Override
	public double getIninity() {
		return Double.MAX_VALUE;
	}

}
