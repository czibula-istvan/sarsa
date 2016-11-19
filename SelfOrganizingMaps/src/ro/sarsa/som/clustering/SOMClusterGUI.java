package ro.sarsa.som.clustering;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ro.sarsa.clustering.ClusteringListener;
import ro.sarsa.clustering.Partition;
import ro.sarsa.som.BMU;
import ro.sarsa.som.NeighborSOMNeuron;
import ro.sarsa.som.SOM;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.SOMTrainingListener;
import ro.sarsa.som.testapps.SomDrawDataBMUs;
import ro.sarsa.som.topology.SOMTopology;
import ro.sarsa.som.traindata.SOMTrainData;
import ro.sarsa.som.umatrix.ColorGradient;

public class SOMClusterGUI extends JPanel implements SOMTrainingListener, ClusteringListener<SOMNeuron> {
	private SOM som;
	private SOMTrainData trD;
	private Partition<SOMNeuron> part;
	private Color colors[] = new Color[] { Color.red, Color.green, Color.BLUE, Color.BLACK, Color.yellow, Color.white,
			Color.CYAN, Color.magenta, Color.DARK_GRAY, Color.orange };

	public SOMClusterGUI(SOM som, SOMTrainData trD, Partition<SOMNeuron> part) {
		this.som = som;
		this.part = part;
		this.trD = trD;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if (part == null) {
			return;
		}
		ColorGradient gradient = null;
		if (part.getNRClusters() > colors.length) {
			gradient = new ColorGradient(colors);
			gradient.createGradient(part.getNRClusters());
		}

		int w = getWidth() - 10;
		int h = getHeight() - 10;
		SOMTopology topo = som.getTopo();
		List<SOMNeuron> neurons = topo.getAllNeurons();
		for (int i = 0; i < neurons.size(); i++) {
			SOMNeuron n = neurons.get(i);
			// double[] poz = n.getNeuronPosition();
			// double x = poz[0] * w;
			// double y = poz[1] * h;

			int clustIndex = part.getClusterIndexFor(n);
			if (clustIndex < 0) {
				continue;
			}
			Shape s = topo.getNeuronShape(w, h, n);
			if (part.getNRClusters() > colors.length) {
				g2d.setColor(gradient.getColour(clustIndex));
			} else {
				g2d.setColor(colors[clustIndex]);
			}
			g2d.fill(s);
			g2d.setColor(Color.black);
			g2d.draw(s);
			// if (colors.length >= part.getNRClusters()) {
			// g2d.setColor(colors[clustIndex]);
			// g2d.fill(s);
			// } else {
			// g2d.setColor(Color.black);
			// g2d.draw(s);
			// g2d.setColor(Color.red);
			// double xy[] = topo.getNeuronCenter(w, h, n);
			// g2d.drawString(clustIndex + "", (int) xy[0] - 4, (int) xy[1]);
			// }
			// g.setColor(Color.red);
			// g.fillArc((int) x - 4, (int) y - 4, 8, 8, 0, 360);
		}
		SomDrawDataBMUs.drawData(g, w, h, som, trD);
	}

	@Override
	public void trainStepPerformed(int iteration, double[] input, BMU bmu, List<NeighborSOMNeuron> neighbors) {
		redrawAll();
	}

	private void redrawAll() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					paintImmediately(0, 0, getWidth(), getHeight());
				}

			});
			// Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void intermediatePartition(Partition<SOMNeuron> part) {
		this.part = part;
		redrawAll();
	}
}
