package ro.sarsa.som.testapps;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import ro.sarsa.som.SOM;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.topology.RectangularTopology;
import ro.sarsa.som.topology.SOMTopology;
import ro.sarsa.som.traindata.SOMTrainData;

public class DrawColorSOMNetw implements INetworkDraw {
	private SOMTrainData trData;

	public DrawColorSOMNetw(SOMTrainData trData) {
		this.trData = trData;
	}

	@Override
	public void drawNetwork(Graphics g, int w, int h, SOM som, TrainIterationSnapshoot trSnap) {
		SOMTopology topo1 = som.getTopo();
		RectangularTopology topo = (RectangularTopology) topo1;
		int nW = w / topo.getWidth();
		int nH = h / topo.getHeight();

		for (int i = 0; i < topo.getHeight(); i++) {
			for (int j = 0; j < topo.getWidth(); j++) {
				SOMNeuron neu = topo.getNeuron(i, j);
				double[] rgb = neu.getWeights();
				float red = (float) rgb[0];
				float green = (float) rgb[1];
				float blue = (float) rgb[2];
				try {
					Color nc = new Color(red, green, blue);
					g.setColor(nc);
				} catch (IllegalArgumentException ex) {
					System.out.println(red + " " + green + " " + blue);
					red = Math.max(0f, Math.min(1f, red));
					green = Math.max(0f, Math.min(1f, green));
					blue = Math.max(0f, Math.min(1f, blue));
					Color nc = new Color(red, green, blue);
					g.setColor(nc);
					System.out.println(red + " " + green + " " + blue);
					// ex.printStackTrace();
				}
				// g.fillRect(i * nW, j * nH, nW, nH);
				((Graphics2D) g).fill(topo1.getNeuronShape(w, h, neu));
			}
		}
		g.setColor(Color.black);
		g.drawString(trSnap.getIteration() + " iter", 10, 10);
		if (trData != null) {
			SomDrawDataBMUs.drawData(g, w, h, som, trData);
		}
	}
}
