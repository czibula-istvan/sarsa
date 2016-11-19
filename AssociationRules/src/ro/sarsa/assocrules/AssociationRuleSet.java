package ro.sarsa.assocrules;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ro.sarsa.assocrules.relations.Relation;

public class AssociationRuleSet {

	private List<AssociationRule> rule;

	public AssociationRuleSet() {
		rule = new ArrayList<AssociationRule>();
	}

	public AssociationRuleSet(AssociationRuleSet ot) {
		rule = new ArrayList<AssociationRule>(ot.rule.size());
		for (AssociationRule r : ot.rule) {
			rule.add(new AssociationRule(r));
		}
	}

	public boolean in(AssociationRuleSet ars) throws Exception {
		for (int i = 0; i < rule.size(); i++)
			if (!ars.contains((AssociationRule) rule.get(i))
					&& !ars.contains(((AssociationRule) rule.get(i)).invRule()))
				return false;
		return true;
	}

	public boolean equalsTo(AssociationRuleSet ars) throws Exception {
		return this.in(ars) && ars.in(this);
	}

	public void addAll(AssociationRuleSet ars) {
		for (int i = 0; i < ars.size(); i++)
			addRule(ars.get(i));
	}

	public void addAllLast(AssociationRuleSet ars) {
		rule.addAll(ars.rule);
		// for (int i = 0; i < ars.size(); i++)
		// addRuleLast(ars.get(i));
	}

	// verifica daca o regula apare in set
	public boolean contains(AssociationRule ar) throws Exception {
		for (int i = 0; i < rule.size(); i++)
			if (ar.identical((AssociationRule) rule.get(i)))
				return true;
		return false;
	}

	// sterge din set o regula sau inversa ei
	public void remove(AssociationRule ar) throws Exception {
		for (int i = 0; i < rule.size(); i++)
			if (ar.identical(rule.get(i)) || ar.identicalWithInverse(rule.get(i))) {
				rule.remove(i);
				break;
			}
	}

	// sterge din set o regula sau inversa ei
	public boolean sterge(AssociationRule ar) throws Exception {
		boolean sters = false;
		for (int i = 0; i < rule.size(); i++)
			if (ar.identical(rule.get(i)) || ar.identicalWithInverse(rule.get(i))) {
				sters = true;
				rule.remove(i);
				break;
			}
		return sters;
	}

	public void clear() {
		rule.clear();
	}

	// se adauga regula doar daca nu apare
	public void addRule(AssociationRule r) {
		if (!containsRorInv(r)) {
			rule.add(r);
		}
		// else
		// System.out.println("**");

	}

	public boolean containsRorInv(AssociationRule ar) {
		for (AssociationRule r : rule)
			if (ar.identical(r) || ar.identicalWithInverse(r)) {
				return true;
			}
		return false;
	}

	public void addRuleLast(AssociationRule r) {
		rule.add(r);
	}

	public void union(AssociationRuleSet ars) throws Exception {
		for (int i = 0; i < ars.size(); i++) {
			AssociationRule ar = (AssociationRule) ars.get(i);
			addRule(ar);
		}
	}

	public Iterator<AssociationRule> iterator() {
		return rule.iterator();
	}

	public List<AssociationRule> getRules() {
		return rule;
	}

	public AssociationRule get(int pos) {
		return (AssociationRule) rule.get(pos);
	}

	public int size() {
		return rule.size();
	}

	public void genByJoinAdaptiv(AssociationRuleSet ars, int naMic) throws Exception {
		long time = System.currentTimeMillis();

		for (int i = 0; i < ars.size() - 1; i++) {
			for (int j = i + 1; j < ars.size(); j++) {
				AssociationRule ari = ars.get(i);
				AssociationRule arj = ars.get(j);
				boolean cond = true;
				// if (naMic!=0)
				cond = ari.getMaxim() > naMic || arj.getMaxim() > naMic;
				if (cond) {
					addJoin2rules(ari, arj);
				}
			}
		}
		// System.out.println("getByJoin (" + ars.size() + "):"
		// + (System.currentTimeMillis() - time) + "ms");
	}

	public void genByJoin(AssociationRuleSet ars, EntitySet es, double minSup, double minConf) {
		// long time = System.currentTimeMillis();
		for (int i = 0; i < ars.size() - 1; i++) {
			for (int j = i + 1; j < ars.size(); j++) {
				AssociationRule ari = ars.get(i);
				AssociationRule arj = ars.get(j);
				addJoin2rules(ari, arj, es, minSup, minConf);
			}
		}
		// System.out.println("getByJoin (" + ars.size() + "):"
		// + (System.currentTimeMillis() - time) + "ms");
	}

	public void genByJoin(AssociationRuleSet ars1, AssociationRuleSet ars2, EntitySet es, double minSup,
			double minConf) {
		for (int i = 0; i < ars1.size(); i++) {
			for (int j = 0; j < ars2.size(); j++) {
				AssociationRule ari = ars1.get(i);
				AssociationRule arj = ars2.get(j);
				addJoin2rules(ari, arj, es, minSup, minConf);
			}
		}
	}

	public static long nrJoins = 0;

	private void addJoin2rules(AssociationRule ari, AssociationRule arj, EntitySet es, double minSup, double minConf) {

		if (ari.length() != arj.length()) {
			// facem join doar cu reguli de acelasi lungime
			return;
		}
		nrJoins++;
		if (ari.identicalWithInverseIgnoreLasts(arj)) {
			// regula 4
			Attribute atr = arj.getLastAtr();
			if (!ari.contains(atr)) {
				Relation rel = arj.getLastRel().inv();
				AssociationRule arnew = new AssociationRule(ari);
				arnew.addFirst(rel, atr);
				if (arnew.valid()) {
					if (arnew.computeIfInteresting(minSup, minConf, es)) {
						addRuleLast(arnew);
					}
				}
			}
		} else if (ari.identicalWithInverseIgnoreFirsts(arj)) {
			// regula 3
			Attribute atr = arj.getAttribute(0);
			if (!ari.contains(atr)) {
				Relation rel = arj.getRelation(0).inv();
				AssociationRule arnew = new AssociationRule(ari);
				arnew.add(rel, atr);
				if (arnew.valid()) {
					if (arnew.computeIfInteresting(minSup, minConf, es)) {
						addRuleLast(arnew);
					}
				}
			}
		} else if (ari.identicalIgnoreFirstIgnoreLast(arj)) {
			// regula 2
			Attribute atr = arj.getLastAtr();
			if (!ari.contains(atr)) {
				Relation rel = arj.getLastRel();
				AssociationRule arnew = new AssociationRule(ari);
				arnew.add(rel, atr);
				if (arnew.valid()) {
					if (arnew.computeIfInteresting(minSup, minConf, es)) {
						addRuleLast(arnew);
					}
				}
			}
		} else if (ari.identicalIgnoreLastIgnoreFirst(arj)) {
			// regula 1
			Attribute atr = arj.getAttribute(0);
			if (!ari.contains(atr)) {
				Relation rel = arj.getRelation(0);
				AssociationRule arnew = new AssociationRule(ari);
				arnew.addFirst(rel, atr);
				if (arnew.valid()) {
					if (arnew.computeIfInteresting(minSup, minConf, es)) {
						addRuleLast(arnew);
					}
				}
			}
		}
	}

	private void addJoin2rules(AssociationRule ari, AssociationRule arj) {
		if (ari.length() != arj.length()) {
			// facem join doar cu reguli de acelasi lungime
			return;
		}
		AssociationRule ari1 = ari.extractSubRuleNoLast();
		AssociationRule ari2 = ari.extractSubRuleNoFirst();
		AssociationRule arj1 = arj.extractSubRuleNoLast();
		AssociationRule arj2 = arj.extractSubRuleNoFirst();

		if (ari1.identicalWithInverse(arj1)) {
			// regula 4
			Attribute atr = arj.getLastAtr();
			if (!ari.contains(atr)) {
				Relation rel = arj.getLastRel().inv();
				AssociationRule arnew = new AssociationRule(ari);
				arnew.addFirst(rel, atr);
				if (arnew.valid()) {
					addRuleLast(arnew);
				}
			}
		}

		if (ari2.identicalWithInverse(arj2)) {
			// regula 3
			Attribute atr = arj.getAttribute(0);
			if (!ari.contains(atr)) {
				Relation rel = arj.getRelation(0).inv();
				AssociationRule arnew = new AssociationRule(ari);
				arnew.add(rel, atr);
				if (arnew.valid()) {
					addRuleLast(arnew);
				}
			}
		}

		if (ari2.identical(arj1)) {
			// regula 2
			Attribute atr = arj.getLastAtr();
			if (!ari.contains(atr)) {
				Relation rel = arj.getLastRel();
				AssociationRule arnew = new AssociationRule(ari);
				arnew.add(rel, atr);
				if (arnew.valid()) {
					addRuleLast(arnew);
				}
			}
		}

		if (ari1.identical(arj2)) {
			// regula 1
			Attribute atr = arj.getAttribute(0);
			if (!ari.contains(atr)) {
				Relation rel = arj.getRelation(0);
				AssociationRule arnew = new AssociationRule(ari);
				arnew.addFirst(rel, atr);
				if (arnew.valid()) {
					addRuleLast(arnew);
				}
			}
		}
	}

	public AssociationRuleSet getRulesOfLength(int k) throws Exception {
		AssociationRuleSet ars = new AssociationRuleSet();
		if (rule.size() == 0)
			return ars;
		for (int i = 0; i < rule.size(); i++) {
			AssociationRule ar = (AssociationRule) rule.get(i);
			if (ar.length() == k)
				ars.addRuleLast(ar);
			if (ar.length() > k)
				break;
		}
		return ars;
	}

	public void calculateSupportConfidence(EntitySet es) {
		long time = System.currentTimeMillis();
		// calculeaza suportul in setul de entitati ale regulilor din setul cs
		for (int i = 0; i < size(); i++) {
			AssociationRule ar = get(i);
			ar.calculateSupportConfidenceRule(es);
		}
		// System.out.println("calculateSupportConfidence (" + cs.size() + "):"
		// + (System.currentTimeMillis() - time) + "ms");
	}

	public String toString() {
		String s = new String();
		Iterator<AssociationRule> ics = iterator();
		int la = 0; // lungimea regulii anterior tiparite
		while (ics.hasNext()) {
			AssociationRule ar = (AssociationRule) ics.next();
			if (ar.length() != la) {
				la = ar.length();
				s = s + "Regulile de lungime " + (la + 1) + "\n";
			}
			s = s + ar.toString() + "\n";
		}
		return s;
	}

	public String toString1() {
		String s = new String();
		int lgmax = get(size() - 1).length() + 1;
		for (int lg = 2; lg <= lgmax; lg++) {
			s = s + "Regulile de lungime " + lg + "\n";
			for (int i = 0; i < size(); i++) {
				AssociationRule ar = (AssociationRule) get(i);
				if (ar.length() == lg - 1)
					s = s + ar.toString() + "\n";
			}
		}
		return s;
	}

	public void writeAssociationRules(PrintStream ps) throws Exception {
		ps.println(toString());
	}

	// public void writeAssociationRules(PrintStream ps) throws Exception {
	// Iterator ics = iterator();
	// while (ics.hasNext()) {
	// AssociationRule ar = (AssociationRule) ics.next();
	// ps.println(((AssociationRule) ics.next()).toString());
	// }
	// }

	public int noRules(int lg) {
		int nr = 0;
		for (int i = 0; i < size(); i++) {
			AssociationRule ar = (AssociationRule) get(i);
			if (ar.length() == lg - 1)
				nr++;
		}
		return nr;
	}

	public void tiparire() {
		System.out.println("Numar total reguli - " + size());
		int lgmax = get(size() - 1).length() + 1;
		for (int lg = 2; lg <= lgmax; lg++)
			System.out.print(lg + " --" + noRules(lg) + ",");
		System.out.println();
	}

}
