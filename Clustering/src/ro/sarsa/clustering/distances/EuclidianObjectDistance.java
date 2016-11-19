package ro.sarsa.clustering.distances;

import ro.sarsa.clustering.IDistance;
import ro.sarsa.clustering.objects.VectorModelProvider;

public class EuclidianObjectDistance<T> implements IDistance<T> {
	private EuclideanDistance dist = new EuclideanDistance();
	private VectorModelProvider<T> vectM;

	public EuclidianObjectDistance(VectorModelProvider<T> vectM) {
		this.vectM = vectM;
	}

	@Override
	public double distance(T a, T b) {
		return dist.distance(vectM.getVectorialModel(a), vectM.getVectorialModel(b));
	}

	@Override
	public double getIninity() {
		return dist.getIninity();
	}

}
