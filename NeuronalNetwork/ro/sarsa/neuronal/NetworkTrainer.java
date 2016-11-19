package ro.sarsa.neuronal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Se ocupa cu antrenarea unei retele
 * 
 * @author User
 * 
 */
public class NetworkTrainer {
	private NeuronalNetwork netw;

	private float learningRate;

	/** momentum */
	private float alfa;

	/** momentum pentru ponderile din Input - Hidden * */
	private float[][] momentumInHi;

	/** momentum pentru bias din Input - Hidden * */
	private float[] momentumBiasInHi;

	/** momentum pentru ponderile din Hidden -Output * */
	private float[][] momentumHiOut;

	/** momentum pentru bias din Hidden - Output * */
	private float[] momentumBiasHiOut;

	public NetworkTrainer(NeuronalNetwork netw) {
		this(netw, 0.3f, 0.1f);
	}

	public NetworkTrainer(NeuronalNetwork netw, float learningRate) {
		this(netw, learningRate, 0.1f);
	}

	public NetworkTrainer(NeuronalNetwork netw, float learningRate, float alfa) {
		this.netw = netw;
		this.learningRate = learningRate;
		this.alfa = alfa;
		reCreateMomentums();
	}

	/**
	 * Reinitializeaza toate valoarile pentru momentum cu 0
	 */
	public void reCreateMomentums() {
		momentumInHi = new float[netw.getNumberOfInputNeurons()][];
		for (int i = 0; i < momentumInHi.length; i++) {
			momentumInHi[i] = new float[netw.getNumberOfHiddenNeurons()];
		}
		momentumBiasInHi = new float[netw.getNumberOfHiddenNeurons()];

		momentumHiOut = new float[netw.getNumberOfHiddenNeurons()][];
		for (int i = 0; i < momentumHiOut.length; i++) {
			momentumHiOut[i] = new float[netw.getNumberOfOutputNeurons()];
		}

		momentumBiasHiOut = new float[netw.getNumberOfOutputNeurons()];
	}

	public float train(float[] input, float[] expectedOut) {
		PropagationResult result = netw.forwardPropagation(input);
		backPropagation(input, result, expectedOut);
		return computeError(result.getOutputs(), expectedOut);
	}

	private float computeError(float[] networkOut, float[] expectedOut) {
		float error = 0;
		for (int i = 0; i < netw.getNumberOfOutputNeurons(); i++) {
			error += (networkOut[i] - expectedOut[i]) * (networkOut[i] - expectedOut[i]);
		}
		return error;
	}

	/**
	 * Face backPropagation pentru un exemplu
	 * 
	 * @param input
	 * @param result
	 * @param expectedOut
	 */
	private void backPropagation(float[] input, PropagationResult result, float[] expectedOut) {
		float[] networkOut = result.getOutputs();

		ActivationFunction activationF = netw.getActivationF();
		// calculam eroarea pe stratul de iesire
		float[] outputDelta = new float[networkOut.length];
		for (int i = 0; i < networkOut.length; i++) {
			outputDelta[i] = (expectedOut[i] - networkOut[i]) * activationF.fDeriv(networkOut[i]);
		}
		// calculam eroarea pe stratul ascuns
		float[] networkHiddenOut = result.getHiddenResults();

		float[] hiddenDelta = new float[networkHiddenOut.length];
		for (int i = 0; i < netw.getNumberOfHiddenNeurons(); i++) {
			float sum = 0;
			for (int j = 0; j < networkOut.length; j++) {
				sum += netw.getWeigthHiddenOut(i, j) * outputDelta[j];
			}
			hiddenDelta[i] = activationF.fDeriv(networkHiddenOut[i]) * sum;
		}

		// actualizam ponderile hidenOutput
		for (int i = 0; i < networkOut.length; i++) {
			for (int j = 0; j < netw.getNumberOfHiddenNeurons(); j++) {
				momentumHiOut[j][i] = learningRate * outputDelta[i] * networkHiddenOut[j] + alfa * momentumHiOut[j][i];
				float aux = netw.getWeigthHiddenOut(j, i) + momentumHiOut[j][i];
				netw.setWeigthHiddenOut(j, i, aux);
			}
			// actualizez si pentru bias
			momentumBiasHiOut[i] = learningRate * outputDelta[i] + alfa * momentumBiasHiOut[i];
			float aux = netw.getBiasInHidden(i) + momentumBiasHiOut[i];
			netw.setBiasInHidden(i, aux);
		}

		// actualizam ponderile inputHidden
		for (int i = 0; i < netw.getNumberOfHiddenNeurons(); i++) {
			for (int j = 0; j < input.length; j++) {
				momentumInHi[j][i] = learningRate * hiddenDelta[i] * input[j] + alfa * momentumInHi[j][i];
				float aux = netw.getWeigthInHidden(j, i) + momentumInHi[j][i];
				netw.setWeigthInHidden(j, i, aux);
			}
			// actualizez si pentru bias
			momentumBiasInHi[i] = learningRate * hiddenDelta[i] + alfa * momentumBiasInHi[i];
			float aux = netw.getBiasInInput(i) + momentumBiasInHi[i];
			netw.setBiasInInput(i, aux);
		}
	}

	/**
	 * Antreneaza reteaua folosind un set de date
	 * 
	 * @param in
	 *            datele de intrare
	 * @param expectedOut
	 *            rezultate asteptate
	 * @param nrMaxIterations
	 *            numarul maxim de ture de antrenament
	 * @return true daca s-a obtinut errTrashold dorit, false daca s-a oprit
	 *         antrenarea la nrMaxIterations
	 */
	public boolean train(float[][] in, float[][] expectedOut, int nrMaxIterations, float errThreshold) {
		float err = 0;
		for (int i = 0; i < nrMaxIterations; i++) {
			err = trainInOrder(in, expectedOut, err);
			// err = trainInRandomOrder(in, expectedOut, err);
			err = err / in.length;
			// System.out.println("eroare la iteratia: "+i+" este:"+err);
			if (err < errThreshold) {
				System.out.println("eroare la iteratia: " + i + " este:" + err);
				// am optinut eroarea dorita
				return true;
			}
		}
		System.out.println("eroare dupoa toate iteratiile este:" + err);
		return false;
	}

	/**
	 * Alege Exsercitiile in ordine aleatoare
	 * 
	 * @param in
	 * @param expectedOut
	 * @param err
	 * @return
	 */
	private float trainInRandomOrder(float[][] in, float[][] expectedOut, float err) {
		List<Integer> pozitions = new ArrayList<Integer>(in.length);
		for (int j = 0; j < in.length; j++) {
			pozitions.add(j);
		}
		Random rnd = new Random();
		while (pozitions.size() > 0) {
			// aleg aleator un test
			int random = rnd.nextInt(pozitions.size());
			int poz = pozitions.remove(random);
			// calculez eroarea
			err += train(in[poz], expectedOut[poz]);
			// System.out.println("eroarea este:"+err);
		}
		return err;
	}

	/**
	 * Alege exercitiile in ordine
	 * 
	 * @param in
	 * @param expectedOut
	 * @param err
	 * @return
	 */
	private float trainInOrder(float[][] in, float[][] expectedOut, float err) {
		for (int j = 0; j < in.length; j++) {
			err += train(in[j], expectedOut[j]);
		}
		return err;
	}

	/**
	 * 
	 * @param trData
	 * @param nrMaxIterations
	 * @param errThreshold
	 * @return
	 */
	public boolean train(TrainingData trData, int nrMaxIterations, float errThreshold) {
		Random rnd = new Random();
		for (int i = 0; i < nrMaxIterations; i++) {
			float err = 0;
			List<Integer> pozitions = new ArrayList<Integer>(trData.getNumberInputExamples());
			for (int j = 0; j < trData.getNumberInputExamples(); j++) {
				pozitions.add(j);
			}

			while (pozitions.size() > 0) {
				// aleg aleator un test
				int random = rnd.nextInt(pozitions.size());
				int poz = pozitions.get(random);
				// calculez eroarea
				err += train(trData.getInput(poz, netw.getNumberOfInputNeurons()),
						trData.getExpectedResult(poz, netw.getNumberOfInputNeurons()));
			}
			err = err / trData.getNumberInputExamples();
			if (err < errThreshold) {
				// am optinut eroarea dorita
				return true;
			}
		}
		return false;
	}

}
