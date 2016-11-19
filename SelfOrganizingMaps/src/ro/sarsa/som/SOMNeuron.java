package ro.sarsa.som;

import java.util.Random;

public class SOMNeuron {

	private double[] weights;
	private int neuronIndex;
	/**
	 * Pozitia neuronului in spatiu topologic
	 */
	private double[] neuronPosition;

	public SOMNeuron(int index, double[] neuronPosition, int size) {
		weights = new double[size];
		neuronIndex = index;
		this.neuronPosition = neuronPosition;
	}

	public SOMNeuron(int index, double[] neuronPosition, int size, double a, double b) {
		this(index, neuronPosition, size);
		initRandom(a, b);
	}

	public void init(double[] sample) {
		for (int i = 0; i < weights.length; i++) {
			weights[i] = sample[i];
		}
	}

	public void initRandom(double a, double b) {
		Random rnd = new Random();
		for (int i = 0; i < weights.length; i++) {
			weights[i] = rnd.nextDouble() * (b - a) + a;
		}
	}

	public double[] getWeights() {
		return weights;
	}

	public double[] getNeuronPosition() {
		return neuronPosition;
	}

	public void adjustWeights(double[] input, double learningRate, double influence) {
		double li = learningRate * influence;
		for (int i = 0; i < weights.length; i++) {
			weights[i] += (input[i] - weights[i]) * li;
		}
	}

	public int getIndex() {
		return neuronIndex;
	}
	// public boolean equals(Object o) {
	// return neuronIndex == ((SOMNeuron) o).neuronIndex;
	// }
}
