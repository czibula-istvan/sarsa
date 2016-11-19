package ro.sarsa.som.traindata;

import java.awt.Color;

public class RandomColorSetTrainingData implements SOMTrainData {
	private Color cs[] = new Color[] { Color.red, Color.green, Color.BLUE, Color.BLACK, Color.yellow, Color.white,
			Color.CYAN, Color.magenta, Color.DARK_GRAY };

	public RandomColorSetTrainingData() {
	}

	@Override
	public double[] get(int inputIndex) {
		float rgba[] = cs[inputIndex].getColorComponents(null);
		double rez[] = new double[3];
		for (int i = 0; i < 3; i++) {
			rez[i] = rgba[i];
		}
		return rez;
	}

	@Override
	public int size() {
		return cs.length;
	}

	@Override
	public int getDataDimension() {
		return 3;
	}

	@Override
	public Object getLabel(int inputIndex) {
		return cs[inputIndex];
	}

}
