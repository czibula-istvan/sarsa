package ro.sarsa.som.umatrix;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ro.sarsa.som.BMU;
import ro.sarsa.som.NeighborSOMNeuron;
import ro.sarsa.som.SOM;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.SOMTrainingListener;
import ro.sarsa.som.topology.SOMTopology;
import ro.sarsa.som.traindata.SOMTrainData;

public class TrainDataDistriburionPanel extends JPanel implements
		SOMTrainingListener {
	private SOMTopology topo;
	private SOMTrainData trData;
	private SOM som;

	public TrainDataDistriburionPanel(SOM som, SOMTopology topo,
			SOMTrainData trData) {
		this.som = som;
		this.topo = topo;
		this.trData = trData;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		int w = getWidth() - 10;
		int h = getHeight() - 10;
		for (int i = 0; i < trData.size(); i++) {
			Object label = trData.getLabel(i);
			double[] input = trData.get(i);
			BMU bmu = BMU.computeBestMatchingUnit(input, som.getTopo(),
					som.getDistance());
			SOMNeuron neuron = bmu.getNeuron();
			double xy[] = topo.getNeuronCenter(w, h, neuron);
			g.drawString(label.toString(), (int) xy[0], (int) xy[1] + 10);
		}
	}

	@Override
	public void trainStepPerformed(int iteration, double[] input, BMU bmu,
			List<NeighborSOMNeuron> neighbors) {
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