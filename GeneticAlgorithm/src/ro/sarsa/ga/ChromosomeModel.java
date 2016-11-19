package ro.sarsa.ga;

public interface ChromosomeModel {

	Chromosome createRandomCromosome();

	double fitness(Chromosome chromosome);

	Chromosome crossOver(Chromosome parent1, Chromosome parent2);

	Chromosome mutate(Chromosome parent1, double mutation);

}