package ro.sarsa.som.traindata;

import ro.sarsa.som.traindata.SOMTrainData;

public class PointTrainDAta implements SOMTrainData {

	@Override
	public double[] get(int inputIndex) {
		if (inputIndex == 0) {
			return new double[] { -0.8, -0.8 };
		}
		if (inputIndex == 1) {
			return new double[] { 0.8, 0.5 };
		}
		if (inputIndex == 2) {
			return new double[] { -0.8, -0.5 };
		}
		return new double[] { 0.8, 0.8 };
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public int getDataDimension() {
		return 2;
	}

	@Override
	public Object getLabel(int inputIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
