package ro.sarsa.som.traindata;

import java.util.Arrays;

public class NormalizedTrainingData implements SOMTrainData {
	private SOMTrainData td;
	private double mins[];
	private double maxs[];

	public NormalizedTrainingData(SOMTrainData td) {
		this.td = td;
		computeMinMax();
	}

	private void computeMinMax() {
		double[] firstData = td.get(0);
		mins = Arrays.copyOf(firstData, firstData.length);
		maxs = Arrays.copyOf(firstData, firstData.length);
		for (int i = 1; i < td.size(); i++) {
			double[] data = td.get(i);
			for (int j = 0; j < data.length; j++) {
				if (mins[j] > data[j]) {
					mins[j] = data[j];
				}
				if (maxs[j] < data[j]) {
					maxs[j] = data[j];
				}
			}
		}
	}

	@Override
	public int size() {
		return td.size();
	}

	@Override
	public double[] get(int inputIndex) {
		double[] data = td.get(inputIndex);
		data = Arrays.copyOf(data, data.length);
		for (int i = 0; i < data.length; i++) {
			data[i] = (data[i]-mins[i]) / (maxs[i] - mins[i]);
		}
		return data;
	}

	@Override
	public int getDataDimension() {
		return td.getDataDimension();
	}

	@Override
	public Object getLabel(int inputIndex) {
		return td.getLabel(inputIndex);
	}

}
