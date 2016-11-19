package ro.sarsa.clustering.hierarhic;

import ro.sarsa.clustering.Partition;

public interface ClusteringStopCondition<T> {
	boolean isStopConditionReached(Partition<T> currentPart);
}
