package ro.sarsa.som.clustering;

import java.util.ArrayList;
import java.util.List;

import ro.sarsa.clustering.Cluster;
import ro.sarsa.clustering.ClusteringListener;
import ro.sarsa.clustering.Partition;
import ro.sarsa.clustering.kmedoids.KMedoidsOld;
import ro.sarsa.som.BMU;
import ro.sarsa.som.NeighborSOMNeuron;
import ro.sarsa.som.SOM;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.topology.SOMTopology;
import ro.sarsa.som.traindata.SOMTrainData;
import ro.sarsa.som.umatrix.SOMUMatrix;

public class SOMClusteringKMedoids {
	private SOMTopology topo;
	private SOMUMatrix umatrix;
	private int nrClusteri;
	private ClusteringListener<SOMNeuron> list;

	public SOMClusteringKMedoids(SOMTopology topo, int nrClusteri,
			ClusteringListener<SOMNeuron> list) {
		this.topo = topo;
		umatrix = new SOMUMatrix(topo);
		this.nrClusteri = nrClusteri;
		this.list = list;
	}

	public Partition<SOMNeuron> neuronCluster() {
		KMedoidsOld<SOMNeuron> kmed = new KMedoidsOld<SOMNeuron>(new NeuronDistance(
				umatrix, topo));
		kmed.setClusteringListener(list);
		List<SOMNeuron> medoizi = kmed.calculMedoiziInitiali(topo
				.getAllNeurons(), 0, nrClusteri);

		Partition<SOMNeuron> part = kmed.clustering(topo.getAllNeurons(),
				medoizi, 10);
		return part;
	}
}
