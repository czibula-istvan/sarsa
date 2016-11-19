package ro.sarsa.assocrules.generators;

import ro.sarsa.assocrules.AssociationRule;
import ro.sarsa.assocrules.AssociationRuleSet;
import ro.sarsa.assocrules.EntitySet;
import ro.sarsa.assocrules.RelationSet;
import ro.sarsa.assocrules.relations.Relation;

public class ByJoinRuleGenerator {

	private EntitySet es;

	private AssociationRuleSet rse; // by extension

	private int binar;
	private boolean maximal;

	private double time = 0;

	int noSuppConfComputations;

	public double getTime() {
		return time;
	}

	public int getNrSuppConf() {
		return noSuppConfComputations;
	}

	public ByJoinRuleGenerator(EntitySet es, int binar, String maximal) {
		this.es = es;
		this.rse = new AssociationRuleSet();
		this.binar = binar;
		this.maximal = maximal.equalsIgnoreCase("maximal");
		noSuppConfComputations = 0;
		time = 0;
	}

	public AssociationRuleSet genBinCandidates(double minSup, double minConf) throws Exception {
		AssociationRuleSet ars = new AssociationRuleSet();
		int n = es.get(0).getNoAttr();
		for (int i = 0; i < n - 1; i++)
			for (int j = i + 1; j < n; j++) {
				for (int k = 0; k < RelationSet.numberOfRelations(); k++) {
					Relation rnew = RelationSet.getRelation(k);
					AssociationRule anew = new AssociationRule();

					anew.add(es.get(0).getAttr(i));
					anew.addRel(rnew, 0);
					anew.add(es.get(0).getAttr(j));
					if (anew.valid()) {
						if (anew.computeIfInteresting(minSup, minConf, es)) {
							ars.addRuleLast(anew);
						}
					}
					// System.out.println("candidat " + anew.toString());
				}
			}
		return ars;
	}

	// prin join
	public AssociationRuleSet genAssociationRulesByJoin(double minSup, double minConf) throws Exception {
		// candidatii de pe nivelul curent
		AssociationRuleSet ars = genBinCandidates(minSup, minConf);

		// regulile de lungime l (initial 2)
		AssociationRuleSet rsel = new AssociationRuleSet();
		int l = 2;
		boolean gata = false;
		int dim = 2; // dimensiunea maxima a regulilor
		if (binar > 2)
			dim = es.get(0).getNoAttr();

		while (!gata && l <= dim) {
			if (l != 2) {
				ars.clear();
				ars.genByJoin(rsel, es, minSup, minConf);
			}

			rsel.clear();
			rse.addAllLast(ars);
			rsel.addAllLast(ars);

			if (rsel.size() == 0)
				gata = true;
			l++;
		}
		if (maximal)
			return filtrare(rse);
		return rse;
	}

	public AssociationRuleSet filtrare(AssociationRuleSet rse) throws Exception {
		AssociationRuleSet rez = new AssociationRuleSet();
		for (int i = 0; i < rse.size(); i++) {
			rez.addRuleLast((AssociationRule) rse.get(i));
		}
		boolean term = false;
		while (!term) {
			int i = 0;
			boolean sasters = false;
			while (i < rez.size()) {
				AssociationRule r = rez.get(i);
				// eliminam subregulile regulii r ptr a pastra
				// in rezultat doar reguli maximale!!!
				AssociationRule arnew = new AssociationRule(r);
				// sterg ultima relatie si ultimul atribut
				arnew.removeLast();
				boolean sters1 = rez.sterge(arnew);

				// sterg primul atribut si prima relatie
				arnew.copyRule(r);
				arnew.removeFirst();
				rez.remove(arnew);
				boolean sters2 = rez.sterge(arnew);

				if (sters1 || sters2) {
					sasters = true;
					i = rez.size();
				} else
					i = i + 1;
			}
			if (!sasters)
				term = true;
		}
		return rez;
	}

}
