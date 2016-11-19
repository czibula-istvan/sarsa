package ro.sarsa.som.clustering;

import ro.sarsa.clustering.ClusteringListener;
import ro.sarsa.clustering.Partition;
import ro.sarsa.som.SOM;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.traindata.SOMTrainData;

public class SOMClusteringListener implements ClusteringListener<SOMNeuron> {
	private SOM som;
	private SOMTrainData td;

	public SOMClusteringListener(SOM som, SOMTrainData td) {
		this.som = som;
		this.td = td;
	}

	@Override
	public void intermediatePartition(Partition<SOMNeuron> part) {
		System.out.println(SOMClusteringHierarhic.cluster(som, td, part));
	}

}
