package ro.sarsa.som;

import java.util.List;

import junit.framework.TestCase;
import ro.sarsa.som.topology.Latice2DSOMTopology;

public class TestLatice3DTopology extends TestCase {
	private Latice2DSOMTopology topo = new Latice2DSOMTopology(16, 16, 3);

	public void setUp() {
		topo.initRandom(-1, 1);
	}

	public void testNeighbors() {
		SOMNeuron neu = topo.getNeuron(topo.getNrNeurons() / 2 + 8);
		List<NeighborSOMNeuron> neighbors = topo.getNeighbors(neu, 1);
		assertEquals(4, neighbors.size());
	}
}
