package ro.sarsa.som.testapps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ro.sarsa.som.BMU;
import ro.sarsa.som.NeighborSOMNeuron;
import ro.sarsa.som.SOM;
import ro.sarsa.som.SOMTrainingListener;
import ro.sarsa.som.Trainer2Phase;
import ro.sarsa.som.topology.Latice2DSOMTopology;
import ro.sarsa.som.traindata.RandomTrainingData;

public class Test1Som extends JPanel implements SOMTrainingListener {
	private SOM som;
	private BMU bmu;
	private double[] input;
	private List<NeighborSOMNeuron> neighbors;
	private int iteration;
	private Draw2DNetworkNet drawSOM = new Draw2DNetworkNet();

	public Test1Som(SOM som) {
		this.som = som;
		setBackground(Color.white);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// desenez fiecare neuron
		int w = getWidth() - 10;
		int h = getHeight() - 10;
		drawSOM.drawNetwork(g, w, h, som, new TrainIterationSnapshoot(bmu,
				input, neighbors, iteration));
	}

	public static void main(String[] args) {
		final Latice2DSOMTopology topo = new Latice2DSOMTopology(16, 16, 2);
		topo.initRandom(-0.5, 0.5);
		final SOM som = new SOM(2, topo);

		JFrame f = new JFrame();
		final Test1Som testSom = new Test1Som(som);
		f.getContentPane().add(testSom);
		f.setSize(500, 500);
		f.setVisible(true);
		JButton jbStart = new JButton("Train");
		f.getContentPane().add(jbStart, BorderLayout.SOUTH);
		jbStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						long time = System.currentTimeMillis();
						// som.train(3000, new RandomTrainingData(2, -1, 1),
						// 0.7,
						// testSom);

						Trainer2Phase.train2Phase(som, 3000,
								new RandomTrainingData(2, -1, 1), 0.7,
								topo.getMaxRadius() / 3, testSom);
						System.out.println("Time:"
								+ (System.currentTimeMillis() - time));
						// som.train(3000, new PointTrainDAta(), 0.7, testSom);
					}

				}).start();
			}
		});
	}

	@Override
	public void trainStepPerformed(int iteration, double[] input, BMU bmu,
			List<NeighborSOMNeuron> neighbors) {
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
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
