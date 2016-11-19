package ro.sarsa.rl.action;

/**
 * If the action implements this interface the sarsa engine can do some
 * optimizations
 * 
 * @author istvan
 * 
 */
public interface IndexedAction {
	public short getIndex();

	public int getTotalNumberOfAction();
}
