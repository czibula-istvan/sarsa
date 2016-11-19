package ro.sarsa.som.clustering;

import ro.sarsa.clustering.Partition;
import ro.sarsa.som.SOMNeuron;

public interface SOMClustering {

	public abstract Partition<SOMNeuron> neuronCluster();

}