package ro.sarsa.neuronal;

/**
 * Reprezinta un set de date de antrenare
 * 
 * @author User
 * 
 */
public interface TrainingData {
	/**
	 * 
	 * @return numarul de exemple de antrenare
	 */
	public int getNumberInputExamples();

	/**
	 * returneaza exemplul de antrenare i codificat pentru nrInputNeurons valori
	 * de intrare
	 * 
	 * @param i
	 * @param nrInputNeurons
	 * @return
	 */
	public float[] getInput(int i, int nrInputNeurons);

	/**
	 * returneaza rezultatul asteptat pwntru exemplul de antrenare i codificat
	 * pentru nrInputNeurons valori de intrare
	 * 
	 * @param i
	 * @param nrInputNeurons
	 * @return
	 */
	public float[] getExpectedResult(int i, int nrInputNeurons);
}
