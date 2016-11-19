package ro.sarsa.som;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ro.sarsa.som.trainsamplechooser.SOMTrainInputChooser;

/**
 * Alege aleator un sample la fiecare pas de antrenare (epoca)
 *
 * @author istvan
 *
 */
public class RandomSOMReainInputChooser implements SOMTrainInputChooser {
	private Random rnd = new Random();
	private int nrInputSamples;
	private List<Integer> remainIndexes;

	public RandomSOMReainInputChooser(int nrInputSamples) {
		this.nrInputSamples = nrInputSamples;
		reinitRemainIndexes();
	}

	private void reinitRemainIndexes() {
		remainIndexes = new ArrayList<Integer>(nrInputSamples);
		for (int i = 0; i < nrInputSamples; i++) {
			remainIndexes.add(i);
		}
	}

	@Override
	public int[] getNextInputIndex() {
		if (remainIndexes.isEmpty()) {
			reinitRemainIndexes();
		}
		return new int[] { rnd.nextInt(remainIndexes.size()) };
	}
}
