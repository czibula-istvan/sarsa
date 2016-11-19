package ro.sarsa.som;

import ro.sarsa.clustering.IDistance;
import ro.sarsa.som.traindata.SOMTrainData;

public class SOMErrorUtil {

	/**
	 * Calculeaza eroarea... e vorba de media dintre |x-mx| pentru fiecare
	 * input... unde x e un imput iar mx e bmu pentru acel input
	 * 
	 * @param td
	 * @return
	 */
	public static double computeAverageQuantizationError(SOMTrainData td, SOM som, IDistance<double[]> dist) {
		double rez = 0;
		for (int i = 0; i < td.size(); i++) {
			double[] input = td.get(i);
			BMU bmu = BMU.computeBestMatchingUnit(input, som.getTopo(), dist);
			rez += dist.distance(input, bmu.getNeuron().getWeights());
		}
		return rez / td.size();
	}

	/**
	 * Eroare ce ia in calcul si veciinii suma(Hci*|x-mi]^2);
	 * 
	 * @param td
	 * @return
	 */
	public static double computeAverageDistorsionError(SOMTrainData td) {
		return -1;
	}

}
