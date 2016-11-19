package ro.sarsa.clustering.objects;

import java.util.List;


public class ScaledVectorModelProvider<T> implements VectorModelProvider<T> {
	private VectorModelProvider<T> vectModel;
	private List<T> allObjs;
	private double[] min;
	private double[] max;

	public ScaledVectorModelProvider(VectorModelProvider<T> vectModel,
			List<T> allObjs) {
		this.vectModel = vectModel;
		this.allObjs = allObjs;
		findMinMax(allObjs);
	}

	private void findMinMax(List<T> allObjs) {
		min = new double[allObjs.size()];
		max = new double[allObjs.size()];
		for (int i = 0; i < allObjs.size(); i++) {
			min[i] = Double.MAX_VALUE;
			max[i] = Double.MIN_VALUE;
		}
		for (int i = 0; i < allObjs.size(); i++) {
			double[] input = vectModel.getVectorialModel(allObjs.get(i));
			for (int j = 0; j < input.length; j++) {
				if (min[j] > input[j]) {
					min[j] = input[j];
				}
				if (max[j] < input[j]) {
					max[j] = input[j];
				}
			}
		}

	}

	@Override
	public double[] getVectorialModel(T a) {
		double[] input = vectModel.getVectorialModel(a);
		double[] normalizedInput = new double[input.length];
		for (int i = 0; i < input.length; i++) {
			normalizedInput[i] = (input[i] - min[i]) / (max[i] - min[i]);
		}
		return normalizedInput;
	}

}
