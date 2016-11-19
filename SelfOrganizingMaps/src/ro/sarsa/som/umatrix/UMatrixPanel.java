package ro.sarsa.som.umatrix;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ro.sarsa.som.BMU;
import ro.sarsa.som.NeighborSOMNeuron;
import ro.sarsa.som.SOM;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.SOMTrainingListener;
import ro.sarsa.som.testapps.SomDrawDataBMUs;
import ro.sarsa.som.topology.SOMTopology;
import ro.sarsa.som.traindata.SOMTrainData;

/**
 * Vizualizeaza UMatrix
 * 
 * @author istvan
 * 
 */
public class UMatrixPanel extends JPanel implements SOMTrainingListener {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	private SOMTopology topo;
	private SOM som;
	private SOMTrainData trD;
	private SOMUMatrix uMatr;
	private ColorGradient cg;
	private LabelApparenceProvider labelAP;

	public UMatrixPanel(SOM som, SOMTrainData trD) {
		this(som, trD, new DefaultLabelApparenceProvider());
	}

	public UMatrixPanel(SOM som, SOMTrainData trD,
			LabelApparenceProvider labelAP) {
		this.som = som;
		this.topo = som.getTopo();
		this.trD = trD;
		this.labelAP = labelAP;

		uMatr = new SOMUMatrix(topo);

		cg = new ColorGradient();
		cg.addPoint(Color.black);
		cg.addPoint(Color.white);
		cg.createGradient(100);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		int w = getWidth() - 10;
		int h = getHeight() - 10;
		long time = System.currentTimeMillis();
		List<UMatrixValue> umvs = uMatr.getNormalizedUMatrix();
		System.out
				.println("umatrx time:" + (System.currentTimeMillis() - time));
		for (int i = 0; i < umvs.size(); i++) {
			UMatrixValue umv = umvs.get(i);
			SOMNeuron n = umv.getNeuron();
			Color color = getColor(umv);
			g.setColor(color);
			Shape s = topo.getNeuronShape(w, h, n);
			g2d.fill(s);
			g2d.setColor(Color.black);
			g2d.draw(s);
		}

		SomDrawDataBMUs.drawData(g, w, h, som, trD, labelAP);
	}

	public void paintComponentNew(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		int w = getWidth() - 10;
		int h = getHeight() - 10;
		long time = System.currentTimeMillis();
		List<UMatrixValue> umvs = uMatr.getNormalizedUMatrix();
		// for (int i = 0; i < umvs.size(); i++) {
		// UMatrixValue umv = umvs.get(i);
		// SOMNeuron n = umv.getNeuron();
		// Color color = getColor(umv);
		// g.setColor(color);
		// Shape s = topo.getNeuronShape(w, h, n);
		// g2d.fill(s);
		// g2d.setColor(Color.black);
		// g2d.draw(s);
		// }
		Polygon s = (Polygon) topo.getNeuronShape(w, h);
		for (int i = 0; i < umvs.size(); i++) {
			UMatrixValue umv = umvs.get(i);
			SOMNeuron n = umv.getNeuron();
			Color color = getColor(umv);
			g.setColor(color);
			double[] center = topo.getNeuronCenter(w, h, n);
			g2d.setTransform(AffineTransform.getTranslateInstance(center[0],
					center[1]));
			g2d.fill(s);
			g2d.setColor(Color.black);
			g2d.draw(s);
		}
		g2d.setTransform(AffineTransform.getTranslateInstance(0, 0));
		System.out.println("drawUValstime:"
				+ (System.currentTimeMillis() - time));
		SomDrawDataBMUs.drawData(g, w, h, som, trD, labelAP);
		System.out.println("drawUValstime+drawData:"
				+ (System.currentTimeMillis() - time));
	}

	private Color getColor(UMatrixValue umv) {
		float c = (float) umv.getValue();
		// Color color = new Color(c, c, c);
		// return color;
		return cg.getColour((int) (c * 99));
	}

	@Override
	public void trainStepPerformed(int iteration, double[] input, BMU bmu,
			List<NeighborSOMNeuron> neighbors) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					// paintImmediately(0, 0, getWidth(), getHeight());
					repaint(0, 0, getWidth(), getHeight());
				}

			});
			// Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
