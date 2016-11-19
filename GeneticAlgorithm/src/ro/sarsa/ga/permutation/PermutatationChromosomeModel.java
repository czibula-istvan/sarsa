package ro.sarsa.ga.permutation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ro.sarsa.ga.Chromosome;
import ro.sarsa.ga.ChromosomeModel;

public abstract class PermutatationChromosomeModel implements ChromosomeModel {

	protected int dim;
	private List<Short> allNrs;
	private Random rnd = new Random();

	public PermutatationChromosomeModel(int dim) {
		this.dim = dim;
		allNrs = new ArrayList<>();
		for (short i = 0; i < dim; i++) {
			allNrs.add(i);
		}

	}

	/*
	 */
	@Override
	public Chromosome createRandomCromosome() {
		short[] perm = new short[dim];
		List<Short> cpy = new ArrayList<>(allNrs);
		for (int i = 0; i < dim; i++) {
			perm[i] = cpy.remove(rnd.nextInt(cpy.size()));
		}
		return new PermutationChromosome(perm);
	}

	/*
	 */
	@Override
	public Chromosome crossOver(Chromosome parent1, Chromosome parent2) {
		PermutationChromosome p1 = (PermutationChromosome) parent1;
		PermutationChromosome p2 = (PermutationChromosome) parent2;
		return orderCrossower1(p1, p2);
	}

	/**
	 * Order Crossover 1 first introduced by Dave Davis (1985) Known as "C1" -
	 * Reeves(1993)
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	private Chromosome orderCrossower1(PermutationChromosome p1, PermutationChromosome p2) {
		int cutPoint1 = rnd.nextInt(dim);
		int cutPoint2 = rnd.nextInt(dim);
		if (cutPoint1 > cutPoint2) {
			int aux = cutPoint1;
			cutPoint1 = cutPoint2;
			cutPoint2 = aux;
		}

		short[] offspringPerm = new short[dim];
		// copy from parent 1 the crossover section
		List<Short> filler = p2.toList();
		for (int i = cutPoint1; i < cutPoint2; i++) {
			offspringPerm[i] = p1.get(i);
			filler.remove(Short.valueOf(p1.get(i)));
		}
		// copy the remaining
		for (int i = dim - 1; i >= cutPoint2; i--) {
			offspringPerm[i] = filler.remove((int) (filler.size() - 1));
		}
		for (int i = cutPoint1-1; i >= 0; i--) {
			offspringPerm[i] = filler.remove((int) (filler.size() - 1));
		}
		return new PermutationChromosome(offspringPerm);
	}

	/*
	 */
	@Override
	public Chromosome mutate(Chromosome parent1, double mutationProb) {
		PermutationChromosome rez = ((PermutationChromosome) parent1).makeCopy();
		for (int i = 0; i < dim; i++) {
			if (rnd.nextDouble() < mutationProb) {
//				int genePoz1 = rnd.nextInt(dim);
//				int genePoz2 = rnd.nextInt(dim);
//				rez.switchGenes(genePoz1, genePoz2);
				int genePoz1 = rnd.nextInt(dim-1);
				rez.switchGenes(genePoz1, genePoz1+1);
			}
		}
		return rez;
	}

}
