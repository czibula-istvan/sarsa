package ro.sarsa.assocrules.generators;

import ro.sarsa.assocrules.AssociationRule;
import ro.sarsa.assocrules.AssociationRuleSet;
import ro.sarsa.assocrules.Attribute;
import ro.sarsa.assocrules.EntitySet;
import ro.sarsa.assocrules.RelationSet;
import ro.sarsa.assocrules.relations.Relation;

public class BacktrackingRuleGenerator {
	private int ai[];
	private int ri[];
	private EntitySet es;
	private AssociationRuleSet rs; // by back
	private AssociationRuleSet rse; // by extension

	public BacktrackingRuleGenerator(EntitySet es) {
		this.es = es;
		this.rs = new AssociationRuleSet();
	}

	public AssociationRuleSet genAssociationRulesByBack(double minSup, double minConf) throws Exception {
		for (int l = 2; l <= es.get(0).getNoAttr(); l++) {
			System.out.println("Back la lungimea " + l);
			AssociationRuleSet ars = genCandidateRules(l);
			ars.calculateSupportConfidence(es);
			int k = 0; // nr. reguli de lungime l
			for (int i = 0; i < ars.size(); i++) {
				AssociationRule r = ars.get(i);
				if (r.isInteresting(minSup, minConf, es)) {
					rs.addRule(r);
					k++;
				}
			}
			if (k == 0)
				break;
		}
		return rs;
		// rs contine toate regulile, de orice lungime, cu suportul >= minSupp
	}

	// prin insertie
	public AssociationRuleSet genAssociationRulesByExtension(double minSup, double minConf) throws Exception {
		AssociationRuleSet ars = genCandidateRules(2);
		AssociationRuleSet rsel = new AssociationRuleSet(); // regulile de
															// lungime l
															// (initial 2)

		int l = 2;
		boolean gata = false;
		while (!gata && l < es.get(0).getNoAttr()) {
			if (l != 2) {
				ars.clear();
				for (int i = 0; i < rsel.size(); i++) {
					AssociationRuleSet ars1Rule = extendRuleBy1Attr(rsel.get(i)); // extensiile
																					// regulii
																					// i
					ars.addAll(ars1Rule);
				}
			}
			System.out.println("Candidati de lungime " + l + " sunt " + ars.size());
			rsel.clear();
			ars.calculateSupportConfidence(es);
			for (int i = 0; i < ars.size(); i++) {
				AssociationRule r = ars.get(i);
				if (r.isInteresting(minSup, minConf, es)) {
					rse.addRule(r);
					rsel.addRule(r);
				}
			}
			System.out.println("Reguli de lungimea " + l + " sunt " + rsel.size());
			if (rsel.size() == 0)
				gata = true;
			l++;
		}
		return rse;
	}

	private AssociationRuleSet extendRuleBy1Attr(AssociationRule ar) throws Exception {
		AssociationRuleSet ars = new AssociationRuleSet();
		int n = ar.length(); // numar de relatii din regula
		for (int k = 0; k < es.get(0).getNoAttr(); k++) {
			Attribute anew = es.get(0).getAttr(k);
			for (int i = 0; i < ar.length(); i++)

				if (!ar.attrInRule(anew) && RelationSet.areInRelation(anew, ar.getAttribute(i))
						&& RelationSet.areInRelation(anew, ar.getAttribute(i + 1))) {
					for (int j = 0; j < RelationSet.numberOfRelations(); j++) {
						Relation rnew = RelationSet.getRelation(j);
						AssociationRule old1 = new AssociationRule();
						old1.copyRule(ar);
						AssociationRule old2 = new AssociationRule();
						old2.copyRule(ar);

						old1.addRel(rnew, i);
						old1.addAttr(anew, i + 1);
						if (old1.valid())
							ars.addRule(old1);

						old2.add(anew, rnew, i + 1);
						if (old2.valid())
							ars.addRule(old2);

						// extensie la inceput
						if (i == 0) {
							AssociationRule old3 = new AssociationRule();
							old3.copyRule(ar);
							old3.addAttr(anew, 0);
							old3.addRel(rnew, 0);
							if (old3.valid())
								ars.addRule(old3);
						}
						// extensie la sfarsit
						if (i == ar.length() - 1) {
							AssociationRule old4 = new AssociationRule();
							old4.copyRule(ar);
							old4.addRel(rnew, i + 1);
							old4.addAttr(anew, i + 2);
							if (old4.valid())
								ars.addRule(old4);
						}

					}
				}
		}
		return ars;
	}

	public AssociationRuleSet genCandidateRules(int l) throws Exception {
		// genereaza toate regulile posibile de lungime l atribute; 2 <= l <= nr
		// atr si cu orice relatii intre ele
		AssociationRuleSet cs = new AssociationRuleSet(); // setul de reguli
															// candidati
		int n = es.get(0).getNoAttr();
		ai = new int[n];
		// System.out.println("Regulile");
		back(0, n, l, cs);
		// System.out.println("Numar solutii: "+nrsol);
		return cs;
	}

	private boolean consistentRules(int k) {
		boolean verif = true;
		return verif;
	}

	private void backRules(int kr, int l, AssociationRuleSet cs) throws Exception {
		for (int i = 0; i < RelationSet.numberOfRelations(); i++) {
			ri[kr] = i;
			if (consistentRules(kr))
				if (kr == l) {
					// adauga in setul de reguli regulile cu atributele din
					// ai[0,l+1] si toate relatiile ri[0,l]
					AssociationRule ar = new AssociationRule();

					for (int j = 0; j <= l + 1; j++) {
						Attribute aj = es.get(0).getAttr(ai[j]); // atributul
																	// concret
																	// al
																	// entitatilor
																	// de pe
																	// pozitia j
						Attribute a = aj.makeClone();
						if (j != (l + 1)) {
							// String rj = Relation.relTypes[ri[j]]; // numele
							// relatiei pentru pozitia j in regula
							// cn = new String("Relation" + rj);
							Relation r = RelationSet.getRelation(ri[j]);
							ar.add(r, a);
						} else
							ar.add(a);
					}
					if (ar.valid()) {
						cs.addRule(ar);
					}
				} else
					backRules(kr + 1, l, cs);
		}
	}

	private void addRules(int k, AssociationRuleSet cs) throws Exception {
		ri = new int[k];
		backRules(0, k - 1, cs);
	}

	private boolean consistent(int k) {
		boolean verif = true;
		for (int i = 0; i < k; i++)
			if ((ai[i] == ai[k])) {
				verif = false;
				break;
			}
		if (verif)
			if ((k > 0) && !RelationSet.areInRelation(es.get(0).getAttr(ai[k]), es.get(0).getAttr(ai[k - 1])))
				verif = false;
		return verif;
	}

	private void back(int k, int n, int l, AssociationRuleSet cs) throws Exception {
		for (int i = 0; i < n; i++) {
			ai[k] = i;
			if (consistent(k))
				if (k == (l - 1))
					addRules(k, cs); // adauga in setul de reguli regulile cu
										// atributele din ai[0,l-1] si toate
										// relatiile posibile
				else
					back(k + 1, n, l, cs);
		}
	}

}
