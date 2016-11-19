package ro.sarsa.som.umatrix;

import ro.sarsa.som.SOMNeuron;

public class UMatrixValue {
	private SOMNeuron n;
	private double uh;

	public UMatrixValue(SOMNeuron n, double dist) {
		this.n = n;
		this.uh = dist;
	}

	public double getValue() {
		return uh;
	}

	public SOMNeuron getNeuron() {
		return n;
	}

	public void normalizeTo(double max) {
		uh = uh / max;
	}
}
