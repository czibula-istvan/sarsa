package ro.sarsa.som.trainsamplechooser;

/**
 * Da fiecare input pe rand
 * 
 * @author istvan
 *
 */
public class EachSampleSOMTrainChooser implements SOMTrainInputChooser {
	private int currentSample = -1;
	private int nrInputSamples;

	public EachSampleSOMTrainChooser(int nrInputSamples) {
		this.nrInputSamples = nrInputSamples;
	}

	@Override
	public int[] getNextInputIndex() {
		currentSample++;
		currentSample = currentSample % nrInputSamples;
		return new int[] { currentSample };
	}
}