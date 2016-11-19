package ro.sarsa.neuronal;


public class NetworkCreator {
	/**
	 * Creaza un network in care ponderile se pun aleator
	 * 
	 * @param inNeurons
	 * @param hiddenNeurons
	 * @param outNeurons
	 * @param weightLimit
	 * @return
	 */
	public static NeuronalNetwork createRandom(int inNeurons,
			int hiddenNeurons, int outNeurons, float weightLimit) {
		return createRandom(inNeurons, hiddenNeurons, outNeurons, -weightLimit,
				weightLimit);
	}

	/**
	 * Creaza un network in care ponderile se pun aleator
	 * 
	 * @param inNeurons
	 * @param hiddenNeurons
	 * @param outNeurons
	 * @param minWeight
	 * @param maxWeight
	 * @return
	 */
	public static NeuronalNetwork createRandom(int inNeurons,
			int hiddenNeurons, int outNeurons, float minWeight, float maxWeight) {
		NeuronalNetwork rez = new NeuronalNetwork(inNeurons, hiddenNeurons,
				outNeurons, new SigmoidFunction());
		// Random rnd = new Random(System.currentTimeMillis());
		// initializez ponderile pentru intre stratul in si stratul hidden
		for (int i = 0; i < inNeurons; i++) {
			for (int j = 0; j < hiddenNeurons; j++) {
				float weight = getRandomNumber(minWeight, maxWeight);
				rez.setWeigthInHidden(i, j, weight);
			}
		}

		// initializez ponderile pentru intre stratul hidden si stratul out
		for (int i = 0; i < hiddenNeurons; i++) {
			for (int j = 0; j < outNeurons; j++) {
				float weight = getRandomNumber(minWeight, maxWeight);
				rez.setWeigthHiddenOut(i, j, weight);
			}
		}

		// initialize biasul pe stratul de input
		for (int i = 0; i < hiddenNeurons; i++) {
			float weight = getRandomNumber(minWeight, maxWeight);
			rez.setBiasInInput(i, weight);
		}

		// initialize biasul pe stratul hidden
		for (int i = 0; i < outNeurons; i++) {
			float weight = getRandomNumber(minWeight, maxWeight);
			rez.setBiasInHidden(i, weight);
		}
		return rez;
	}

	/**
	 * GEnereaza un numar aleator
	 * 
	 * @param minWeight
	 * @param maxWeight
	 * @return
	 */
	private static float getRandomNumber(float minWeight, float maxWeight) {
		float weight = (float) Math.random() * (maxWeight - minWeight)
				+ minWeight;
//		weight = 0.5f * (float) Math.random();
//		 weight = 2.0f * ( (float) Math.random() - 0.5f ) * 0.6f ;
		return weight;
	}
}
