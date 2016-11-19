package ro.sarsa.som.testapps;

import java.awt.Graphics;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ro.sarsa.som.BMU;
import ro.sarsa.som.NeighborSOMNeuron;
import ro.sarsa.som.SOM;
import ro.sarsa.som.SOMTrainingListener;

public class GenericSOMNetworkView extends JPanel implements SOMTrainingListener {
	private BMU bmu;
	private double[] input;
	private List<NeighborSOMNeuron> neighbors;
	private int iteration;
	private SOM som;
	private INetworkDraw drawNet;

	public GenericSOMNetworkView(SOM som, INetworkDraw drawNet) {
		this.som = som;
		this.drawNet = drawNet;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// desenez fiecare neuron
		int w = getWidth() - 10;
		int h = getHeight() - 10;
		drawNet.drawNetwork(g, w, h, som, new TrainIterationSnapshoot(bmu, input, neighbors, iteration));
	}

	@Override
	public void trainStepPerformed(int iteration, double[] input, BMU bmu, List<NeighborSOMNeuron> neighbors) {
		this.bmu = bmu;
		this.neighbors = neighbors;
		this.input = input;
		this.iteration = iteration;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					paintImmediately(0, 0, getWidth(), getHeight());
				}

			});
			// Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
