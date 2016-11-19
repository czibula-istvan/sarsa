package ro.sarsa.som.testapps;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import ro.sarsa.som.NeighborSOMNeuron;
import ro.sarsa.som.SOM;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.topology.SOMTopology;

public class Draw2DNetworkNet implements INetworkDraw {

	public void drawNetwork(Graphics g, int w, int h, SOM som,
			TrainIterationSnapshoot trSnap) {
		SOMTopology topo = som.getTopo();
		g.setColor(Color.green);
		for (int i = 0; i < topo.getNrNeurons(); i++) {
			SOMNeuron neu = topo.getNeuron(i);
			drawNeuron(g, w, h, neu, topo);
		}
		if (trSnap.getBMU() == null) {
			return;
		}

		// desenez veciiinii
		g.setColor(Color.yellow);
		if (trSnap.getNeighbors() != null) {
			for (int i = 0; i < trSnap.getNeighbors().size(); i++) {
				drawNeuron(g, w, h, trSnap.getNeighbors().get(i).getNeuron(),
						topo);
			}
		}
		// desenez inputul
		g.setColor(Color.BLACK);
		drawNeuron(g, w, h, trSnap.getInput());
		// desenez BMU
		g.setColor(Color.red);
		drawNeuron(g, w, h, trSnap.getBMU().getNeuron(), topo);

		g.drawString(trSnap.getIteration() + " iter", 10, 10);
	}

	private void drawNeuron(Graphics g, int w, int h, SOMNeuron neu,
			SOMTopology topo) {

		// desenez si linii care le uneste cu vecini
		Color c = g.getColor();
		g.setColor(Color.black.brighter().brighter());
		List<NeighborSOMNeuron> neighbors = topo.getImediateNeighbors(neu);
		for (int i = 0; i < neighbors.size(); i++) {
			drawLine(g, w, h, neu.getWeights(), neighbors.get(i).getNeuron()
					.getWeights());
		}
		g.setColor(c);
		double xy[] = neu.getWeights();
		drawNeuron(g, w, h, xy);
	}

	private void drawLine(Graphics g, int w, int h, double[] xy1, double[] xy2) {
		double x1 = (xy1[0] + 1) * w / 2d;
		double y1 = (xy1[1] + 1) * h / 2d;
		double x2 = (xy2[0] + 1) * w / 2d;
		double y2 = (xy2[1] + 1) * h / 2d;
		g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);

	}

	private void drawNeuron(Graphics g, int w, int h, double[] xy) {
		double x = (xy[0] + 1) * w / 2d;
		double y = (xy[1] + 1) * h / 2d;
		g.fillArc((int) x - 3, (int) y - 3, 6, 6, 0, 360);
	}

}
