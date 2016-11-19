package ro.sarsa.neuronal;

public class NeuronalNetwork {
	/** numarul de neuroni din stratul de input */
	private int nrInputNeurons;

	/** numarul de neuroni din stratul de output */
	private int nrOutputNeurons;

	/** numarul de neuroni de pe stratul ascuns 1 * */
	private int nrHiddenNeuronsLayer1;

	/** ponderile sinapsurilor dintre intrarre si stratul ascuns * */
	float[][] weigthInHi;

	/** ponderile sinapsurilor dintre stratul ascuns si iesirilor* */
	float[][] weigthHiOut;	

	/** functia folosita la propagare si la bakPropagare* */
	private ActivationFunction activationF;

	public NeuronalNetwork(int nrInNeurons, int nrHidNeurons, int nrOutNeurons,
			ActivationFunction activationF) {
		this.nrInputNeurons = nrInNeurons;
		this.nrHiddenNeuronsLayer1 = nrHidNeurons;
		this.nrOutputNeurons = nrOutNeurons;
		this.activationF = activationF;
		// initializez cu zero toate ponderile intre in si hidden
		// nrInNeurons + 1 fiindca se retin biasurile
		weigthInHi = new float[nrInNeurons + 1][];
		for (int i = 0; i < nrInNeurons + 1; i++) {
			weigthInHi[i] = new float[nrHidNeurons];
		}
		// initializez cu zero toate ponderile intre hidden si out
		// nrHidNeurons + 1 fiindca se retin biasurile
		weigthHiOut = new float[nrHidNeurons + 1][];
		for (int i = 0; i < nrHidNeurons + 1; i++) {
			weigthHiOut[i] = new float[nrOutNeurons];
		}
	}

	public void setBiasInInput(int nrHidenNeuron, float w) {
		weigthInHi[nrInputNeurons][nrHidenNeuron] = w;
	}

	public void setBiasInHidden(int nrOutNeuron, float w) {
		weigthHiOut[nrHiddenNeuronsLayer1][nrOutNeuron] = w;
	}

	public float getBiasInInput(int nrHidenNeuron) {
		return weigthInHi[nrInputNeurons][nrHidenNeuron];
	}

	public float getBiasInHidden(int nrOutNeuron) {
		return weigthHiOut[nrHiddenNeuronsLayer1][nrOutNeuron];
	}

	public void setWeigthInHidden(int inNrInNeuron, int nrHidenNeuron, float w) {
		if (inNrInNeuron < 0 || inNrInNeuron >= nrInputNeurons) {
			throw new IllegalArgumentException("Invalid input neouron "
					+ nrInputNeurons);
		}
		if (nrHidenNeuron < 0 || nrHidenNeuron >= nrHiddenNeuronsLayer1) {
			throw new IllegalArgumentException("Invalid input neouron "
					+ nrInputNeurons);
		}
		weigthInHi[inNrInNeuron][nrHidenNeuron] = w;
	}

	public void setWeigthHiddenOut(int nrHiddenNeuron, int nrOutNeuron, float w) {
		if (nrOutNeuron < 0 || nrOutNeuron >= nrOutputNeurons) {
			throw new IllegalArgumentException("Invalid output neouron "
					+ nrInputNeurons);
		}
		if (nrHiddenNeuron < 0 || nrHiddenNeuron >= nrHiddenNeuronsLayer1) {
			throw new IllegalArgumentException("Invalid input neouron "
					+ nrInputNeurons);
		}
		weigthHiOut[nrHiddenNeuron][nrOutNeuron] = w;
	}

	/**
	 * Propaga informatia in retea si returneaza rezultatul
	 * 
	 * @param inValues
	 *            VAlorile pentru fiecare nod de intrare
	 * @return valorile la fiecare nod de iesire
	 */
	public PropagationResult forwardPropagation(float[] inValues) {
		if (inValues.length != nrInputNeurons) {
			throw new IllegalArgumentException(
					"The number of input values must be " + nrInputNeurons);
		}
		PropagationResult result = new PropagationResult();
		// calculam iesirile din stratul hidden
		float[] hiddenOuts = propagateForwardOnLayer(inValues, weigthInHi);
		result.setHiddenResults(hiddenOuts);

		// calculam iesirile din stratul out
		float outputs[] = propagateForwardOnLayer(hiddenOuts, weigthHiOut);
		result.setOutputs(outputs);
		return result;
	}

	private float[] propagateForwardOnLayer(float[] inValues, float[][] weigths) {
		float[] out = new float[weigths[0].length];
		for (int i = 0; i < out.length; i++) {
			// initializam cu bias0ul
			float sum = weigths[weigths.length - 1][i];
			for (int j = 0; j < weigths.length - 1; j++) {
				sum = sum + inValues[j] * weigths[j][i];
			}
			out[i] = activationF.f(sum);
		}
		return out;
	}

	public ActivationFunction getActivationF() {
		return activationF;
	}

	public float getWeigthInHidden(int inNrInNeuron, int nrHidenNeuron) {
		if (inNrInNeuron < 0 || inNrInNeuron >= nrInputNeurons) {
			throw new IllegalArgumentException("Invalid input neouron "
					+ nrInputNeurons);
		}
		if (nrHidenNeuron < 0 || nrHidenNeuron >= nrHiddenNeuronsLayer1) {
			throw new IllegalArgumentException("Invalid input neouron "
					+ nrInputNeurons);
		}
		return weigthInHi[inNrInNeuron][nrHidenNeuron];
	}

	public float getWeigthHiddenOut(int nrHiddenNeuron, int nrOutNeuron) {
		if (nrOutNeuron < 0 || nrOutNeuron >= nrOutputNeurons) {
			throw new IllegalArgumentException("Invalid output neouron "
					+ nrInputNeurons);
		}
		if (nrHiddenNeuron < 0 || nrHiddenNeuron >= nrHiddenNeuronsLayer1) {
			throw new IllegalArgumentException("Invalid input neouron "
					+ nrInputNeurons);
		}
		return weigthHiOut[nrHiddenNeuron][nrOutNeuron];
	}

	public int getNumberOfHiddenNeurons() {
		return nrHiddenNeuronsLayer1;
	}

	public int getNumberOfInputNeurons() {
		return nrInputNeurons;
	}

	public int getNumberOfOutputNeurons() {
		return nrOutputNeurons;
	}

	/**
	 * Face predictia
	 * 
	 * @param inValues
	 * @return returneaza rez= double[2] unde rez[0] = pozitia iesiri unde s-a
	 *         optinut iesirea maxima, rez[1] probabilitatea predictiei
	 */
	public float[] predict(float[] inValues) {
		PropagationResult rez = forwardPropagation(inValues);
		float[] outputs = rez.getOutputs();
		float sum = 0;
		int maxPoz = 0;
		for (int i = 0; i < outputs.length; i++) {
			sum += outputs[i];
			if (outputs[i] > outputs[maxPoz]) {
				maxPoz = i;
			}
		}
		// System.out.println(outputs[0]+" "+outputs[1]);
		return new float[] { maxPoz, outputs[maxPoz] / sum };
	}
}
