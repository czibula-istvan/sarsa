package ro.sarsa.clustering;

public interface ClusteringListener<T> {
	public void intermediatePartition(Partition<T> part);
}
