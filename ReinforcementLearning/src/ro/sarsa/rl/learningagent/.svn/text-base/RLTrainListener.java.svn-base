package ro.sarsa.rl.learningagent;

public interface RLTrainListener {
	/**
	 * Callback method invoked by the learning algorithm. This allow inspecting
	 * the current learning process
	 * 
	 * @param ord
	 * @param total
	 * @param lastEpochHistory - history of the last epoch (!= from the learned policy)
	 * @return the sampling reate (how often this metod will be called)
	 */
	public int epoch(int ord, int total, History lastEpochHistory);

}
