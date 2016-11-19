package ro.sarsa.neuronal;

/**
 * Retine rezultatul propagarii unui set de valori S-a creat pentru a permite
 * accesul si la valorile de pe straturile ascunse
 * 
 * @author User
 * 
 */
public class PropagationResult {
	/** rezultatele pe stratul hidden* */
	private float[] hiddenOuts;

	/** rezultatele pe stratul de iesire* */
	private float outputs[];

	public PropagationResult() {
	}

	public void setHiddenResults(float[] hiddenOuts) {
		this.hiddenOuts = hiddenOuts;
	}

	public void setOutputs(float outputs[]) {
		this.outputs = outputs;
	}

	public float[] getHiddenResults() {
		return hiddenOuts;
	}

	public float[] getOutputs() {
		return outputs;
	}
}
