package ro.sarsa.som.trainsamplechooser;


/**
 * La fiecare pas de epoca se folosesc toate sampelurile
 *
 * @author istvan
 *
 */
public class AllSampleSOMTrainInputChooser implements SOMTrainInputChooser {
	private int[] indexes;

	public AllSampleSOMTrainInputChooser(int nrInputSamples) {
		indexes = new int[nrInputSamples];
		for (int i = 0; i < indexes.length; i++) {
			indexes[i] = i;
		}
	}

	@Override
	public int[] getNextInputIndex() {
		return indexes;
	}

}
