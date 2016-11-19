package ro.sarsa.som.clustering;

import ro.sarsa.clustering.Partition;
import ro.sarsa.clustering.hierarhic.ClusteringStopCondition;
import ro.sarsa.clustering.metrics.DavisBoudlinMetric;
import ro.sarsa.clustering.metrics.DunnMetric;
import ro.sarsa.clustering.metrics.GabiMetric;
import ro.sarsa.clustering.metrics.SilhouetteMetric;
import ro.sarsa.clustering.objects.VectorModelProvider;

/**
 * This index (Davies and Bouldin, 1979) is a function of the ratio of the sum
 * of within-cluster scatter to between-cluster separation
 *
 * @author istvan
 *
 */
public class DavisBouldinStopCondition<T> implements ClusteringStopCondition<T> {
	private int nrDesiredClusters;
	private VectorModelProvider<T> vectModel;
	private DunnMetric<T> dunnMetric;
	private DavisBoudlinMetric<T> davBouMetric;
	private SilhouetteMetric<T> silMetric;
	private GabiMetric<T> gaMetric;

	public DavisBouldinStopCondition(int nrDesiredClusters, VectorModelProvider<T> vectModel) {
		this.nrDesiredClusters = nrDesiredClusters;
		this.vectModel = vectModel;
		dunnMetric = new DunnMetric<T>(vectModel);
		davBouMetric = new DavisBoudlinMetric<T>(vectModel);
		silMetric = new SilhouetteMetric<T>(vectModel);
		gaMetric = new GabiMetric<T>(vectModel);
	}

	@Override
	public boolean isStopConditionReached(Partition<T> currentPart) {

		// if (currentPart.getSmalestCluster().getNRObjs() == 1) {
		// System.out.println(currentPart.getNRClusters()
		// + " avem cluster cu 1 element");
		// return nrPArtitionReached(currentPart);
		// }

		double db = davBouMetric.compute(currentPart);
		db = ((int) (db * 10)) / 10d;
		System.out.println();
		System.out.println(currentPart.getNRClusters() + " davis " + db);

		double dunn = dunnMetric.compute(currentPart);
		dunn = ((int) (dunn * 10)) / 10d;
		System.out.println(currentPart.getNRClusters() + " dunn " + dunn);

		double sil = silMetric.compute(currentPart);
		sil = ((int) (sil * 10)) / 10d;
		System.out.println(currentPart.getNRClusters() + " sil " + sil);

		double ga = gaMetric.compute(currentPart);
		ga = ((int) (ga * 10)) / 10d;
		System.out.println(currentPart.getNRClusters() + " ga " + ga);

		return nrPArtitionReached(currentPart);
	}

	private boolean nrPArtitionReached(Partition<T> currentPart) {
		return currentPart.getNRClusters() == 1 || currentPart.getNRClusters() == nrDesiredClusters;
	}

}
