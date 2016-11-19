package ro.sarsa.som.umatrix;

import java.util.ArrayList;
import java.util.List;

import ro.sarsa.som.NeighborSOMNeuron;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.topology.SOMTopology;

/**
 * Unified distance matrix
 *
 * @author istvan
 *
 */
public class SOMUMatrix {
	private SOMTopology topo;
	private List<UMatrixValue> normalizedUMatrix;
	private List<UMatrixValue> nonnormalizedUMatrix;

	public SOMUMatrix(SOMTopology topo) {
		this.topo = topo;
	}

	public double getUHeight(int neuronIndex) {
		return computeUHeight(topo.getNeuron(neuronIndex));
	}

	public SOMNeuron getNeuron(int neuronIndex) {
		return topo.getNeuron(neuronIndex);
	}

	public int getNeuronCount() {
		return topo.getNrNeurons();
	}

	public List<UMatrixValue> getNormalizedUMatrix() {
		List<UMatrixValue> umatrix = new ArrayList<UMatrixValue>(topo.getNrNeurons());
		double max = computeUMatrix(umatrix);
		return getNormalizedUMatrix(umatrix, max);
	}

	private List<UMatrixValue> getNormalizedUMatrix(List<UMatrixValue> umatrix, double max) {
		for (int i = 0; i < umatrix.size(); i++) {
			UMatrixValue umv = umatrix.get(i);
			umv.normalizeTo(max);
		}
		return umatrix;
	}

	private double computeUMatrix(List<UMatrixValue> rez) {
		double max = -1;
		for (int i = 0; i < topo.getNrNeurons(); i++) {
			double uh = computeUHeight(topo.getNeuron(i));
			if (max < uh) {
				max = uh;
			}
			rez.add(new UMatrixValue(topo.getNeuron(i), uh));
		}
		return max;
	}

	private double computeUHeight(SOMNeuron n) {
		// iau toti veciinii imediati
		List<NeighborSOMNeuron> nghs = topo.getImediateNeighbors(n);
		// List<NeighborSOMNeuron> nghs = topo.getNeighbors(n, 2);
		double uHeight = 0;
		for (int i = 0; i < nghs.size(); i++) {
			NeighborSOMNeuron nghN = nghs.get(i);
			// if (nghN.getFrom() == n) {
			// uHeight += nghN.getDistance();
			// } else {
			uHeight += topo.weightDistance(n, nghN.getNeuron());
			// }
		}
		return uHeight / nghs.size();
	}

	public void peekUMAtrix() {
		nonnormalizedUMatrix = new ArrayList<UMatrixValue>(topo.getNrNeurons());
		double max = computeUMatrix(nonnormalizedUMatrix);
		normalizedUMatrix = getNormalizedUMatrix(nonnormalizedUMatrix, max);

	}

	public double getNormalizedValue(SOMNeuron nneuron) {
		if (normalizedUMatrix == null) {
			peekUMAtrix();
		}

		for (int i = 0; i < normalizedUMatrix.size(); i++) {
			if (normalizedUMatrix.get(i).getNeuron() == nneuron) {
				return normalizedUMatrix.get(i).getValue();
			}
		}
		throw new IllegalArgumentException("Invalid neuron:" + nneuron);
	}

	public double getValue(SOMNeuron nneuron) {
		if (nonnormalizedUMatrix == null) {
			peekUMAtrix();
		}

		for (int i = 0; i < nonnormalizedUMatrix.size(); i++) {
			if (nonnormalizedUMatrix.get(i).getNeuron() == nneuron) {
				return nonnormalizedUMatrix.get(i).getValue();
			}
		}
		throw new IllegalArgumentException("Invalid neuron:" + nneuron);
	}
}
