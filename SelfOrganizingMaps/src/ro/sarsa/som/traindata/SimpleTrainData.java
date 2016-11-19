package ro.sarsa.som.traindata;

public class SimpleTrainData implements SOMTrainData {

	private double[][] data;
	private Object[] labels;

	public SimpleTrainData(SOMTrainData td) {
		this.data = new double[td.size()][td.getDataDimension()];
		for (int i = 0; i < data.length; i++) {
			data[i] = td.get(i);
		}
		this.labels = new Object[td.size()];
		for (int i = 0; i < labels.length; i++) {
			labels[i] = td.getLabel(i);
		}
	}

	public SimpleTrainData(double[][] data, Object[] labels) {
		this.data = data;
		this.labels = labels;
	}

	@Override
	public int size() {
		return data.length;
	}

	@Override
	public double[] get(int inputIndex) {
		return data[inputIndex];
	}

	@Override
	public int getDataDimension() {
		return data[0].length;
	}

	@Override
	public Object getLabel(int inputIndex) {
		int row = inputIndex % data.length;
		return labels[row];
	}
}
