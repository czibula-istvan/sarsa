package ro.sarsa.som.testapps;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import ro.sarsa.som.BMU;
import ro.sarsa.som.SOM;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.topology.SOMTopology;
import ro.sarsa.som.traindata.SOMTrainData;
import ro.sarsa.som.umatrix.LabelApparenceProvider;

public class SomDrawDataBMUs {

	public static void drawData(Graphics g, int w, int h, SOM som,
			SOMTrainData trD, LabelApparenceProvider lblAp) {
		System.out.println("Mda");

		// obtin lista de BMU
		List<BMU> bmus = BMU.getBMUS(trD, som, som.getDistance());
		g.setColor(Color.black);
		SOMTopology topo = som.getTopo();
		double[] dim = topo.getNeuronDim(w, h, topo.getNeuron(0));
		// desenez pe fiecare
		while (bmus.size() > 0) {
			int r = (int) Math.min(dim[0], dim[1]) / 3;
			BMU bmu = bmus.remove(0);
			// drawCircle(g, w, h, topo, r, bmu);
			drawString(g, w, h, topo, r, bmu, lblAp);
			// daca mai e cazul desenez tot aici cercuri mai mici
			while (bmus.indexOf(bmu) >= 0) {
				// System.out.println("Aici");
				bmu = bmus.remove(bmus.indexOf(bmu));
				r = r - 2;
				drawCircle(g, w, h, topo, r, bmu, lblAp);
				drawString(g, w, h, topo, r, bmu, lblAp);
			}
		}
	}

	private static void drawString(Graphics g, int w, int h, SOMTopology topo,
			int r, BMU bmu, LabelApparenceProvider lblAp) {
		if (r < 0) {
			return;
		}
		SOMNeuron n = bmu.getNeuron();
		double[] center = topo.getNeuronCenter(w, h, n);
		// desenez stringul intr-un anumit loc
		int xc = (int) (center[0] - r);
		int yc = (int) (center[1] - r);
		g.setColor(lblAp.getColor(bmu.getLabel()));
		g.drawString(lblAp.getText(bmu.getLabel()), xc, yc);

	}

	public static void drawData(Graphics g, int w, int h, SOM som,
			SOMTrainData trD) {

		// optin lista de BMU
		List<BMU> bmus = BMU.getBMUS(trD, som, som.getDistance());
		g.setColor(Color.black);
		SOMTopology topo = som.getTopo();
		double[] dim = topo.getNeuronDim(w, h, topo.getNeuron(0));
		// desenez pe fiecare
		while (bmus.size() > 0) {
			int r = (int) Math.min(dim[0], dim[1]) / 3;
			BMU bmu = bmus.remove(0);
			drawCircle(g, w, h, topo, r, bmu, null);
			// daca mai e cazul desenez tot aici cercuri mai mici

			while (bmus.indexOf(bmu) >= 0) {
				bmu = bmus.remove(bmus.indexOf(bmu));
				r = r - 1;
				drawCircle(g, w, h, topo, r, bmu, null);
			}
		}
	}

	private static void drawCircle(Graphics g, int w, int h, SOMTopology topo,
			int r, BMU bmu, LabelApparenceProvider lblAp) {
		if (r < 0) {
			return;
		}
		SOMNeuron n = bmu.getNeuron();
		double[] center = topo.getNeuronCenter(w, h, n);
		// desenez cercuri
		int xc = (int) (center[0] - r);
		int yc = (int) (center[1] - r);
		if (lblAp != null) {
			g.setColor(lblAp.getColor(bmu.getLabel()));
		}
		g.drawOval(xc, yc, 2 * r, 2 * r);
	}
}
