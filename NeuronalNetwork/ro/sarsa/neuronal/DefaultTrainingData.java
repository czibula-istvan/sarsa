package ro.sarsa.neuronal;

public class DefaultTrainingData implements TrainingData {
	private float[][] in;

	private float[][] expectedOut;

	public DefaultTrainingData(float[][] in, float[][] expectedOut) {
		this.in = in;
		this.expectedOut = expectedOut;
	}

	public int getNumberInputExamples() {
		return in.length;
	}

	public float[] getInput(int i, int nrInputNeurons) {		
		return in[i];
	}

	public float[] getExpectedResult(int i, int nrInputNeurons) {
		return expectedOut[i];
	}

}
