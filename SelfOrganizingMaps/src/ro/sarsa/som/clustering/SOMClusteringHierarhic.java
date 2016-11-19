package ro.sarsa.som.clustering;

import java.util.ArrayList;
import java.util.List;

import ro.sarsa.clustering.ClusteringListener;
import ro.sarsa.clustering.Partition;
import ro.sarsa.clustering.hierarhic.Hierarhic;
import ro.sarsa.clustering.hierarhic.MaxLinkageMetric;
import ro.sarsa.som.BMU;
import ro.sarsa.som.SOM;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.topology.SOMTopology;
import ro.sarsa.som.traindata.SOMTrainData;
import ro.sarsa.som.umatrix.SOMUMatrix;

public class SOMClusteringHierarhic implements SOMClustering {
	private SOM som;
	private SOMTopology topo;
	private SOMUMatrix umatrix;
	private SOMTrainData td;
	private int nrClusteri;
	private ClusteringListener<SOMNeuron> l;

	public SOMClusteringHierarhic(SOM som, SOMTopology topo, SOMTrainData td,
			int nrClusteri, ClusteringListener<SOMNeuron> l) {
		this.som = som;
		this.topo = topo;
		this.td = td;
		umatrix = new SOMUMatrix(topo);
		this.nrClusteri = nrClusteri;
		this.l = l;
	}

	public Partition<SOMNeuron> neuronCluster3() {
		NeuronDistance neuronDistance = new NeuronDistance(umatrix, topo);
		MaxLinkageMetric<SOMNeuron> lm = new MaxLinkageMetric<SOMNeuron>(
				neuronDistance);
		List<SOMNeuron> allNeurons = getAllBMUNeurons();
		Hierarhic<SOMNeuron> hier = new Hierarhic<SOMNeuron>(lm, allNeurons, 2);
		Partition<SOMNeuron> part = hier
				.clusterizare(new SOMClusteringListener(som, td));
		return part;
	}

	public Partition<SOMNeuron> neuronCluster() {
		RefactoringSOMLinkageMetric somLM = new RefactoringSOMLinkageMetric(
				umatrix, topo);
		List<SOMNeuron> allNeurons = topo.getAllNeurons();

		Hierarhic<SOMNeuron> hier = new Hierarhic<SOMNeuron>(somLM, allNeurons,
				new DavisBouldinStopCondition<SOMNeuron>(nrClusteri,
						somLM.getNeuronDistance()));

		Partition<SOMNeuron> part = hier.clusterizare(l);
		return part;
	}

	private List<SOMNeuron> getAllBMUNeurons() {
		List<BMU> bmus = BMU.getBMUS(td, som, som.getDistance());
		List<SOMNeuron> rez = new ArrayList<SOMNeuron>(bmus.size());
		for (int i = 0; i < bmus.size(); i++) {

			SOMNeuron neuron = bmus.get(i).getNeuron();
			if (!rez.contains(neuron)) {
				rez.add(neuron);
			}
		}
		return rez;
	}

	public static Partition cluster(SOM som, SOMTrainData data,
			Partition<SOMNeuron> neuronPart) {
		Partition rez = Partition.createEmpty(neuronPart.getNRClusters());
		for (int i = 0; i < data.size(); i++) {
			BMU bmu = BMU.computeBestMatchingUnit(data.get(i), som.getTopo(),
					som.getDistance());
			int clusterIndex = neuronPart.getClusterIndexFor(bmu.getNeuron());
			rez.get(clusterIndex).add(data.getLabel(i));
		}
		rez.eliiminaGoale();
		return rez;
	}
}