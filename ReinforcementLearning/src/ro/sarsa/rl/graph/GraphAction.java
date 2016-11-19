package ro.sarsa.rl.graph;

import ro.sarsa.rl.action.Action;
import ro.sarsa.rl.action.IndexedAction;
import ro.sarsa.rl.enviroment.State;

public class GraphAction implements Action, IndexedAction {

	private Direction direction;

	public State execute(State s) {
		GraphState gs = (GraphState) s;
		return gs.moveDirection(direction);
	}

	public String toString() {
		return direction.name();
	}

	public GraphAction(Direction direction) {
		this.direction = direction;
	}

	public int hashCode() {
		return direction.hashCode();
	}

	@Override
	public short getIndex() {
		return (short) direction.ordinal();
	}

	@Override
	public int getTotalNumberOfAction() {
		return Direction.values().length;
	}

	public boolean equals(Object o) {
		return direction == ((GraphAction) o).direction;
	}
}
