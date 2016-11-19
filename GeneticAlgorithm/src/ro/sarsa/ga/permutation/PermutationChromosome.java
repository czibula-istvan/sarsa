package ro.sarsa.ga.permutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ro.sarsa.ga.Chromosome;

public class PermutationChromosome implements Chromosome {
	private short[] perm;

	public PermutationChromosome(short[] perm) {
		this.perm = perm;
	}

	public short get(int i) {
		return perm[i];
	}

	public List<Short> toList() {
		List<Short> rez = new ArrayList<>(perm.length);
		for (short p : perm) {
			rez.add(p);
		}
		return rez;
	}

	@Override
	public PermutationChromosome makeCopy() {
		return new PermutationChromosome(Arrays.copyOf(perm, perm.length));
	}

	public void switchGenes(int genePoz1, int genePoz2) {
		short aux = perm[genePoz1];
		perm[genePoz1] = perm[genePoz2];
		perm[genePoz2] = aux;
	}

	public short[] getArray() {
		return perm;
	}
}
