package ro.sarsa.fuzzysom;

import ro.sarsa.clustering.IDistance;
import ro.sarsa.som.SOM;
import ro.sarsa.som.SOMNeuron;
import ro.sarsa.som.topology.SOMTopology;
import ro.sarsa.som.traindata.SOMTrainData;

public class MiuComputer {

	public static double computeMiu(double[] input, SOMNeuron neuron, SOM som,
			double fuzinessDegree) {
		IDistance<double[]> dist = som.getDistance();
		SOMTopology topo = som.getTopo();
		double[] wi = neuron.getWeights();
		double distXkWi = dist.distance(input, wi);
		double power = 2.0 / (fuzinessDegree - 1);
		double suma = 0;
		for (int j = 0; j < topo.getNrNeurons(); j++) {
			double[] wj = topo.getNeuron(j).getWeights();
			double distInputWj = dist.distance(input, wj);
			if (distInputWj > 0) {
				suma += Math.pow(distXkWi / distInputWj, power);
			}
		}
		suma = 1 + 1.0 / suma;
		return suma;
	}

	public static double[][] computeMiu(SOMTrainData trData, SOM som,
			double fuzinessDegree) {
		SOMTopology topo = som.getTopo();
		double[][] miu = new double[topo.getNrNeurons()][trData.size()];
		for (int k = 0; k < trData.size(); k++) {
			double[] xk = trData.get(k);
			for (int i = 0; i < topo.getNrNeurons(); i++) {
				SOMNeuron neuroni = topo.getNeuron(i);
				double miu_ki = computeMiu(xk, neuroni, som, fuzinessDegree);
				miu[i][k] = miu_ki;
			}
		}
		return miu;
	}

}
