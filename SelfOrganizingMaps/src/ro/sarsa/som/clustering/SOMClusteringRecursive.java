package ro.sarsa.som.clustering;

import java.util.ArrayList;
import java.util.List;

import ro.sarsa.clustering.Cluster;
import ro.sarsa.clustering.Partition;
import ro.sarsa.som.NeighborSOMNeuron;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.topology.SOMTopology;

public class SOMClusteringRecursive implements SOMClustering {
	private SOMTopology topo;
	private double trsh;

	public SOMClusteringRecursive(SOMTopology topo, double trsh) {
		this.topo = topo;
		this.trsh = trsh;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see ro.sarsa.som.clustering.SOMClustering#neuronCluster()
	 */
	public Partition<SOMNeuron> neuronCluster() {
		Partition<SOMNeuron> part = new Partition<SOMNeuron>();
		List<SOMNeuron> remaining = new ArrayList<SOMNeuron>(topo.getAllNeurons());
		while (remaining.size() > 0) {
			SOMNeuron first = remaining.remove(0);
			Cluster<SOMNeuron> clust = part.addSingletonCluster(first);
			visitNode(clust, first, remaining, trsh);
		}

		// uam custerii care sunt formati doar dintr-un neuraon si adaugam la
		// alt cluster
		removeSingletonClusters(part);
		return part;
	}

	private void removeSingletonClusters(Partition<SOMNeuron> part) {

	}

	private void visitNode(Cluster<SOMNeuron> cClust, SOMNeuron cNeuron, List<SOMNeuron> remaining, double trsh) {
		List<NeighborSOMNeuron> neighbors = topo.getImediateNeighbors(cNeuron);
		if (neighbors != null && neighbors.size() > 0) {
			// adaug vecinii care sunt mai apropiati decat trsh
			for (int i = 0; i < neighbors.size(); i++) {
				SOMNeuron nneuron = neighbors.get(i).getNeuron();
				if (remaining.contains(nneuron) && topo.weightDistance(cNeuron, nneuron) < trsh) {
					cClust.add(nneuron);
					remaining.remove(nneuron);
					visitNode(cClust, nneuron, remaining, trsh);
				}
			}
		}
	}
}
