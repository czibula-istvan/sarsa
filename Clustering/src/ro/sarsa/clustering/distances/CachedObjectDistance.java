/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.sarsa.clustering.distances;

import java.util.HashMap;
import java.util.Map;

import ro.sarsa.clustering.IDistance;

/**
 *
 * @author istvan
 */
public class CachedObjectDistance<T> implements IDistance<T> {

	private IDistance<T> dist;
	private Map<T, Map<T, Double>> cache = new HashMap<T, Map<T, Double>>();

	public CachedObjectDistance(IDistance<T> dist) {
		this.dist = dist;
	}

	@Override
	public double distance(T a, T b) {
		Map<T, Double> dists = cache.get(a);
		if (dists == null) {
			dists = new HashMap<T, Double>();
			cache.put(a, dists);
		}
		Double rez = dists.get(b);
		if (rez == null) {
			rez = dist.distance(a, b);
			dists.put(b, rez);
		}
		return rez;
	}

	@Override
	public double getIninity() {
		return dist.getIninity();
	}
}
