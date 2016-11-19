package ro.sarsa.som.topology;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import ro.sarsa.clustering.IDistance;
import ro.sarsa.clustering.distances.EuclideanDistance;
import ro.sarsa.som.NeighborSOMNeuron;
import ro.sarsa.som.SOMNeuron;

public class Latice2DSOMTopology extends RectangularTopology {

	public Latice2DSOMTopology(int row, int column, int size) {
		super(row, column, size);
	}

	public void initRandom(double a, double b) {
		neurons = new SOMNeuron[row][column];
		for (int i = 0; i < row; i++) {
			neurons[i] = new SOMNeuron[column];
			for (int j = 0; j < column; j++) {
				neurons[i][j] = new SOMNeuron(i * row + j,
						new double[] { i, j }, size, a, b);
			}
		}
		initminDistance();
	}

	public void initConstant(double[] w) {
		neurons = new SOMNeuron[row][column];
		for (int i = 0; i < row; i++) {
			neurons[i] = new SOMNeuron[column];
			for (int j = 0; j < column; j++) {
				neurons[i][j] = new SOMNeuron(i * row + j,
						new double[] { i, j }, size);
				neurons[i][j].init(w);
			}
		}
		initminDistance();
	}

	public double gridDistance(SOMNeuron a, SOMNeuron b) {
		return dist.distance(a.getNeuronPosition(), b.getNeuronPosition());
	}

	// @Override
	// public List<NeighborSOMNeuron> getNeighbors(SOMNeuron bmu, double radius)
	// {
	// if (radius < minDist) {
	// return null; // nu am vecini
	// }
	// int bmuPoz = bmu.getIndex();
	// int bmuRow = bmuPoz / column;
	// int bmuCol = bmuPoz % column;
	// double[] bmuXY = new double[] { bmuRow, bmuCol };
	//
	// // caut distanta care corespunde acestui radius
	// int radiusInNRNeurins = getRadiusInNRNeurons(radius);
	// if (radiusInNRNeurins == 0) {
	// return null;
	// }
	// List<NeighborSOMNeuron> rez = new ArrayList<NeighborSOMNeuron>();
	// int startRow = Math.max(0, bmuRow - radiusInNRNeurins);
	// int endRow = Math.min(row, bmuRow + radiusInNRNeurins + 1);
	// int startCol = Math.max(0, bmuCol - radiusInNRNeurins);
	// int endCol = Math.min(column, bmuCol + radiusInNRNeurins + 1);
	// for (int i = startRow; i < endRow; i++) {
	// for (int j = startCol; j < endCol; j++) {
	// if (i == bmuRow && j == bmuCol) {
	// continue;
	// }
	// double dist = this.dist.distance(bmuXY, new double[] { i, j });
	// if (dist <= radius) {
	// rez.add(new NeighborSOMNeuron(bmu, neurons[i][j], dist));
	// }
	// }
	// }
	// return rez;
	// }

	// private int getRadiusInNRNeurons(double radius) {
	// int radiusInNRNeurins = 0;
	// double cdist = 0;
	// while (cdist < radius) {
	// radiusInNRNeurins++;
	// cdist = this.dist.distance(new double[] { 0, 0 }, new double[] { 0,
	// radiusInNRNeurins });
	// }
	// return radiusInNRNeurins--;
	// }

	public Shape getNeuronShape(int w, int h, SOMNeuron n) {
		double[] poz = getNeuronCenter(w, h, n);
		double x = poz[0];
		double y = poz[1];
		double nW = (double) w / getHeight();
		double nH = (double) h / getWidth();
		Rectangle2D r = new Rectangle2D.Double(x - nW / 2, y - nH / 2, nW, nH);
		return r;
	}

	public Shape getNeuronShape(int w, int h) {
		return getShapeAtPoz(w, h, 0, 0);
	}

	private Shape getShapeAtPoz(int w, int h, double x, double y) {
		double nW = (double) w / getHeight();
		double nH = (double) h / getWidth();
		Rectangle2D r = new Rectangle2D.Double(x - nW / 2, y - nH / 2, nW, nH);
		return r;
	}
}
