package ro.sarsa.ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GAAlgorithm {
	private ChromosomeModel indCr;
	private Random rnd = new Random();
	private double elitismPerc;// between 0-1; Percent of best individuals
								// copied to
								// the new generation
	private double crossOverProb;// between 0-1; Probability to apply crossover
	private double mutationProb;// between 0-1; Probability to apply mutation

	public GAAlgorithm(ChromosomeModel indCr, double elitismPerc, double crossOverProb, double mutationProb) {
		this.indCr = indCr;
		this.elitismPerc = elitismPerc;
		this.crossOverProb = crossOverProb;
		this.mutationProb = mutationProb;

	}

	/**
	 * Generate initial population
	 * 
	 * @param n
	 * @return
	 */
	public List<Chromosome> generateRandomPolulation(int n) {
		List<Chromosome> rez = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			rez.add(indCr.createRandomCromosome());
		}
		return rez;
	}

	public List<Chromosome> evolve(List<Chromosome> population) {
		FitnessValues fitness = new FitnessValues(indCr, population);
		
		int popSize = population.size();
		List<Chromosome> newPopulation = new ArrayList<>(popSize);
		if (elitismPerc > 0) {
			// copy best individuals
			int topX = (int) Math.floor(popSize * elitismPerc);
			newPopulation.addAll(fitness.takeBest(topX));
		}

		// generate the rest of the population
		while (newPopulation.size() < popSize) {
			Chromosome parent1 = fitness.selectRulete(rnd);
			Chromosome parent2 = fitness.selectRulete(rnd);
			Chromosome offspring;
			if (rnd.nextDouble() < crossOverProb) {
				offspring = indCr.crossOver(parent1, parent2);
			} else {
				offspring = parent1;
			}
			newPopulation.add(indCr.mutate(offspring, mutationProb));
		}
		return newPopulation;
	}

	public Chromosome solve(int dimPopulatie, int nrIteratii) {
		int i = 0;
		List<Chromosome> pop = generateRandomPolulation(dimPopulatie);
		
		while (i < nrIteratii) {
			i++;
			List<Chromosome> newPop = evolve(pop);
			System.out.println(i);
			pop = newPop;
			
		}
		FitnessValues fv = new FitnessValues(indCr, pop);
		return fv.getBest();
	}

}
