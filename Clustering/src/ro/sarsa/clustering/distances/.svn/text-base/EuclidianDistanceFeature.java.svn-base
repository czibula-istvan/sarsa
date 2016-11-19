package ro.sarsa.clustering.distances;

import ro.sarsa.clustering.IDistance;
import ro.sarsa.clustering.objects.ObjectWithFeature;

public class EuclidianDistanceFeature<ObjType extends ObjectWithFeature> implements IDistance<ObjType> {
	private EuclideanDistance dist = new EuclideanDistance();

	@Override
	public double distance(ObjectWithFeature a, ObjectWithFeature b) {
		return dist.distance(a.getFeatures(), b.getFeatures());
	}

	@Override
	public double getIninity() {
		return dist.getIninity();
	}

}
