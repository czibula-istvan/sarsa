package ro.sarsa.neuronal;

public interface ActivationFunction {
	/**
	 * Functia sigmoida folosita la propagarea stimulului
	 * 
	 * @param x
	 * @return
	 */
	public float f(float x);

	/**
	 * Derivata functiei folosita la backPropagation
	 * 
	 * @param x
	 * @return
	 */
	public float fDeriv(float x);
}
