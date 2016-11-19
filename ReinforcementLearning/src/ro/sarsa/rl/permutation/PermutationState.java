package ro.sarsa.rl.permutation;

import ro.sarsa.rl.enviroment.State;

public class PermutationState extends State implements Comparable {
	private short[] sir;

	public PermutationState(short[] sir) {
		this.sir = sir;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		for (int i = 0; i < sir.length; i++) {
			sb.append(sir[i]).append(",");
		}
		sb.append("]");
		return sb.toString();
	}

	public short[] getPermutation() {
		return sir;
	}

	public int getPermutationLg() {
		return sir.length;
	}

	@Override
	public int compareTo(Object o) {
		PermutationState s = (PermutationState) o;
		if (s.sir.length != sir.length) {
			if (s.sir.length > sir.length) {
				return 1;
			} else {
				return -1;
			}
		}
		// au acelasi lungime
		int poz = 0;
		while (poz < sir.length && s.sir[poz] == sir[poz]) {
			poz++;
		}
		if (poz == sir.length) {
			return 0;
		}
		if (s.sir[poz] > sir[poz]) {
			return 1;
		} else {
			return -1;
		}

	}

	@Override
	public boolean equals(Object o) {
		PermutationState s = (PermutationState) o;
		if (s.sir.length != sir.length) {
			return false;
		}
		for (int i = 0; i < sir.length; i++) {
			if (s.sir[i] != sir[i]) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		return sir.length * 100 + (sir.length > 0 ? sir[0] : 0);
	}

}
