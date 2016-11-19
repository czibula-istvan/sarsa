package ro.sarsa.som.traindata;

import java.util.Random;

public class RandomTrainingData implements SOMTrainData {
	private int dataSize;
	private double start;
	private double end;
	private Random rnd = new Random();

	public RandomTrainingData(int dataSize, double start, double end) {
		this.dataSize = dataSize;
		this.start = start;
		this.end = end;
	}

	@Override
	public double[] get(int inputIndex) {
		double[] rez = new double[dataSize];
		for (int i = 0; i < dataSize; i++) {
			rez[i] = rnd.nextDouble() * (end - start) + start;
		}
		return rez;
	}

	@Override
	public int size() {
		return 1;
	}

	public Object getLabel(int inputIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getDataDimension() {
		return dataSize;
	}

}
