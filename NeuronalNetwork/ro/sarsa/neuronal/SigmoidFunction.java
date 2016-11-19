package ro.sarsa.neuronal;

/**
 * Functie de activare standard.. sigmoidala
 * 
 * @author User
 * 
 */
public class SigmoidFunction implements ActivationFunction {

	public float f(float x) {
		return 1.0f / (1.0f + (float) Math.exp(-x));
	}

	public float fDeriv(float x) {
		return x * (1.0f - x);
	}

}
