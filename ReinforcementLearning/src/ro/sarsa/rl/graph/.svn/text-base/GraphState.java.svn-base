package ro.sarsa.rl.graph;

import ro.sarsa.rl.enviroment.State;

public class GraphState extends State {
	private int s; // numarul de ordine al starii
	private int i, j; // pozitia in grid (linie, coloana)
	private int dim; // dimensiunea gridului

	public GraphState(int s, int dim) {
		this.s = s;
		this.dim = dim;
		this.i = s / dim;
		this.j = s % dim;
	}

	public GraphState moveDirection(Direction direction) {
		int ii = i - 1;
		int jj = j;
		if (direction == Direction.N)
			return new GraphState(ii * dim + jj, dim);
		ii = i + 1;
		jj = j;
		if (direction == Direction.S)
			return new GraphState(ii * dim + jj, dim);
		ii = i;
		jj = j - 1;
		if (direction == Direction.V)
			return new GraphState(ii * dim + jj, dim);
		ii = i;
		jj = j + 1;
		if (direction == Direction.E)
			return new GraphState(ii * dim + jj, dim);
		ii = i - 1;
		jj = j - 1;
		if (direction == Direction.NV)
			return new GraphState(ii * dim + jj, dim);
		ii = i + 1;
		jj = j - 1;
		if (direction == Direction.SV)
			return new GraphState(ii * dim + jj, dim);
		ii = i + 1;
		jj = j + 1;
		if (direction == Direction.SE)
			return new GraphState(ii * dim + jj, dim);
		ii = i - 1;
		jj = j + 1;
		if (direction == Direction.NE)
			return new GraphState(ii * dim + jj, dim);
		return null;
	}

	public String toString() {
		return new Integer(s).toString();
	}

	@Override
	public int hashCode() {
		return s;
	}

	public boolean equals(Object s) {
		return ((GraphState) s).s == this.s;
	}

	public int rank() {
		return s;
	}
}
