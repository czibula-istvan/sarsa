package ro.sarsa.ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class FitnessValues {

	private List<Chromosome> population;
	private double[] fitness;
	private double sumFitness = 0;
	private ChromosomeModel indCr;
	private Integer[] sortedIndexes;

	public FitnessValues(ChromosomeModel indCr, List<Chromosome> population) {
		this.indCr = indCr;
		this.population = population;
		fitness = computeFitness(population);
	}

	private double[] computeFitness(List<Chromosome> population) {
		double[] rez = new double[population.size()];
		for (int i = 0; i < population.size(); i++) {
			rez[i] = indCr.fitness(population.get(i));
			sumFitness += rez[i];
		}
		return rez;
	}

	private void makesort() {
		if (sortedIndexes != null) {
			return;
		}
		sortedIndexes = new Integer[population.size()];
		for (int i = 0; i < sortedIndexes.length; i++) {
			sortedIndexes[i] = i;
		}
		Arrays.sort(sortedIndexes, new Comparator<Integer>() {

			@Override
			public int compare(Integer index1, Integer index2) {
				double fit1 = fitness[index1];
				double fit2 = fitness[index2];
				if (fit1 == fit2)
					return 0;
				if (fit1 < fit2)
					return 1;
				return -1;
			}
		});
	}

	public List<Chromosome> takeBest(int topX) {
		makesort();
		List<Chromosome> rez = new ArrayList<>(topX);
		for (int i = 0; i < topX; i++) {
			rez.add(population.get(sortedIndexes[i]));
		}
		return rez;
	}

	public Chromosome selectRulete(Random rnd) {
		double selectF = rnd.nextDouble() * sumFitness;
		double c = 0;
		for (int i = 0; i < fitness.length; i++) {
			c += fitness[i];
			if (c > selectF) {
				return population.get(i);
			}
		}
		return population.get(population.size() - 1);
	}

	public Chromosome getBest() {
		makesort();
		return population.get(sortedIndexes[0]);
	}
}
