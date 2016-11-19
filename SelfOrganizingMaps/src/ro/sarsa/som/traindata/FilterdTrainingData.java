package ro.sarsa.som.traindata;

import java.util.ArrayList;
import java.util.List;

public class FilterdTrainingData implements SOMTrainData {
	private SOMTrainData td;
	private int ftrsToRemove[];
	private List<Integer> pozitions;

	public FilterdTrainingData(SOMTrainData td, int ftrsToRemove[]) {
		this.td = td;
		this.ftrsToRemove = ftrsToRemove;
		pozitions = new ArrayList<Integer>();
		for (int i = 0; i < td.getDataDimension(); i++) {
			pozitions.add(Integer.valueOf(i));
		}
		for (int i = 0; i < ftrsToRemove.length; i++) {
			pozitions.remove(Integer.valueOf(ftrsToRemove[i]));
		}
	}

	@Override
	public int size() {
		return td.size();
	}

	@Override
	public double[] get(int inputIndex) {
		double[] data = td.get(inputIndex);
		// remove features
		double rez[] = new double[pozitions.size()];
		for (int i = 0; i < pozitions.size(); i++) {
			rez[i] = data[pozitions.get(i)];
		}
		return rez;
	}

	@Override
	public int getDataDimension() {
		return td.getDataDimension() - ftrsToRemove.length;
	}

	@Override
	public Object getLabel(int inputIndex) {
		return td.getLabel(inputIndex);
	}

}
