package ro.sarsa.som.topology;

import java.awt.Polygon;
import java.awt.Shape;

import ro.sarsa.clustering.IDistance;
import ro.sarsa.som.SOMNeuron;

public class TorusSOMTopology extends RectangularTopology {

	public TorusSOMTopology(int row, int column, int size, IDistance<double[]> dist) {
		super(row, column, size, dist);
	}

	public TorusSOMTopology(int row, int column, int size) {
		super(row, column, size);
	}

	public void initRandom(double a, double b) {
		neurons = new SOMNeuron[row][column];
		for (int i = 0; i < row; i++) {
			neurons[i] = new SOMNeuron[column];
			for (int j = 0; j < column; j++) {
				neurons[i][j] = new SOMNeuron(i * row + j, new double[] { i, j }, size, a, b);
			}
		}
		initminDistance();
	}

	public void initConstant(double[] w) {
		neurons = new SOMNeuron[row][column];
		for (int i = 0; i < row; i++) {
			neurons[i] = new SOMNeuron[column];
			for (int j = 0; j < column; j++) {
				neurons[i][j] = new SOMNeuron(i * row + j, new double[] { i, j }, size);
				neurons[i][j].init(w);
			}
		}
		initminDistance();
	}

	@Override
	public double gridDistance(SOMNeuron a, SOMNeuron b) {
		return gridDistance(a.getNeuronPosition()[0], a.getNeuronPosition()[1], b.getNeuronPosition()[0],
				b.getNeuronPosition()[1]);
	}

	public double gridDistance(double row1, double col1, double row2, double col2) {
		double x1 = Math.min(col1, col2);
		double x2 = Math.max(col1, col2);
		double y1 = Math.min(row1, row2);
		double y2 = Math.max(row1, row2);
		double dist1 = dist.distance(new double[] { x1, y1 }, new double[] { x2, y2 });

		double dist2 = dist.distance(new double[] { x1 + column, y1 }, new double[] { x2, y2 });

		double dist3 = dist.distance(new double[] { x1 + column, y1 + row }, new double[] { x2, y2 });
		double dist4 = dist.distance(new double[] { x1, y1 + row }, new double[] { x2, y2 });
		return Math.min(Math.min(Math.min(dist1, dist2), dist3), dist4);
	}

	@Override
	public double[] getNeuronCenter(int w, int h, SOMNeuron n) {
		double[] poz = n.getNeuronPosition();

		double nW = (double) w / (row + 1);
		double nH = ((double) h / column) * 1.2;
		nH = nH - nH / 4;
		double x = poz[0] * nW + nW / 2;
		double y = poz[1] * nH + nH;

		if ((int) poz[1] % 2 == 0) {
			x = x + nW / 2;
		}
		return new double[] { x, y };
	}

	@Override
	public Shape getNeuronShape(int w, int h) {
		return getShapeCenteredTo(w, h, 0, 0);
	}

	@Override
	public Shape getNeuronShape(int w, int h, SOMNeuron n) {
		double[] poz = getNeuronCenter(w, h, n);
		return getShapeCenteredTo(w, h, poz[0], poz[1]);
	}

	private Shape getShapeCenteredTo(int w, int h, double x, double y) {
		double nW = w / (row + 1.0);
		double nH = h / column * 1.2;

		Polygon poly = new Polygon();
		poly.addPoint((int) (x), (int) (y - nH / 2));
		poly.addPoint((int) (x + nW / 2), (int) (y - nH / 4));
		poly.addPoint((int) (x + nW / 2), (int) (y + nH / 4));
		poly.addPoint((int) (x), (int) (y + nH / 2));
		poly.addPoint((int) (x - nW / 2), (int) (y + nH / 4));
		poly.addPoint((int) (x - nW / 2), (int) (y - nH / 4));
		return poly;
	}
	// @Override
	// public Shape getNeuronShape(int w, int h, SOMNeuron n) {
	// double[] poz = getNeuronCenter(w, h, n);
	// double x = poz[0];
	// double y = poz[1];
	// double nW = (double) w / (row + 1);
	// double nH = ((double) h / column) * 1.2;
	// // Rectangle2D r = new Rectangle2D.Double(x - nW / 2, y - nH / 2, nW,
	// // nH);
	// // return r;
	//
	// Polygon poly = new Polygon();
	// poly.addPoint((int) (x), (int) (y - nH / 2));
	// poly.addPoint((int) (x + nW / 2), (int) (y - nH / 4));
	// poly.addPoint((int) (x + nW / 2), (int) (y + nH / 4));
	// poly.addPoint((int) (x), (int) (y + nH / 2));
	// poly.addPoint((int) (x - nW / 2), (int) (y + nH / 4));
	// poly.addPoint((int) (x - nW / 2), (int) (y - nH / 4));
	// return poly;
	// }
}
