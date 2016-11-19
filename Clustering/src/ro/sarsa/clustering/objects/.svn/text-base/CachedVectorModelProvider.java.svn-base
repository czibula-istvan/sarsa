/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.sarsa.clustering.objects;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author istvan
 */
public class CachedVectorModelProvider<T> implements VectorModelProvider<T> {

	private VectorModelProvider<T> vmp;
	private Map<T, double[]> cache = new HashMap<T, double[]>();

	public CachedVectorModelProvider(VectorModelProvider<T> vmp) {
		this.vmp = vmp;
	}

	@Override
	public double[] getVectorialModel(T a) {
		double[] rez = cache.get(a);
		if (rez == null) {
			rez = vmp.getVectorialModel(a);
			cache.put(a, rez);
		}
		return rez;
	}
}
