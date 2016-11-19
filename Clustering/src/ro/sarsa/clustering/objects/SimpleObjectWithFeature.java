package ro.sarsa.clustering.objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class SimpleObjectWithFeature implements ObjectWithFeature {
	private double[] features;

	private SimpleObjectWithFeature(double[] features) {
		this.features = features;
	}

	@Override
	public double[] getFeatures() {
		return features;
	}

	public static List<ObjectWithFeature> generateRandom(int nrFeatures, int nrObjects) {
		Random rnd = new Random(new Date().getTime());
		List<ObjectWithFeature> rez = new ArrayList<ObjectWithFeature>(nrObjects);
		for (int i = 0; i < nrObjects; i++) {
			double[] features = new double[nrFeatures];
			for (int j = 0; j < nrFeatures; j++) {
				features[j] = rnd.nextDouble();
			}
			rez.add(new SimpleObjectWithFeature(features));
		}
		return rez;
	}
}
