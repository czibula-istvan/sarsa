/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.sarsa.clustering.distances;

import ro.sarsa.clustering.IDistance;

/**
 *
 * @author istvan
 */
public class EuclidianDistanceNoSqrt implements IDistance<double[]> {

	@Override
	public double distance(double[] a, double[] b) {
		double dist = 0;
		for (int i = 0; i < a.length; i++) {
			double aux = a[i] - b[i];
			dist += aux * aux;
		}
		return dist;
	}

	@Override
	public double getIninity() {
		return Double.MAX_VALUE;
	}
}
