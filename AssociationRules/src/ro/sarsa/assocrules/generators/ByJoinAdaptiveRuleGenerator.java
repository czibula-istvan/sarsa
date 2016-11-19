package ro.sarsa.assocrules.generators;

import ro.sarsa.assocrules.AssociationRule;
import ro.sarsa.assocrules.AssociationRuleSet;
import ro.sarsa.assocrules.EntitySet;
import ro.sarsa.assocrules.RelationSet;
import ro.sarsa.assocrules.relations.Relation;

public class ByJoinAdaptiveRuleGenerator {
	private EntitySet es;
	private int noSuppConfComputations = 0;
	private double time = 0;

	private AssociationRuleSet rse; // by extension

	private int binar;
	private boolean maximal;

	public double getTime() {
		return time;
	}

	public int getNrSuppConf() {
		return noSuppConfComputations;
	}

	public ByJoinAdaptiveRuleGenerator(EntitySet es, int binar, String maximal) {
		this.es = es;
		this.rse = new AssociationRuleSet();
		this.binar = binar;
		this.maximal = maximal.equalsIgnoreCase("maximal");
		noSuppConfComputations = 0;
		time = 0;
	}

	// prin join adaptiv
	public AssociationRuleSet genAssociationRulesByJoinAdaptiv(AssociationRuleSet arsMic, double minSup, double minConf,
			int naMic, int naMare) throws Exception {

		// reguli mici de lungime 2
		AssociationRuleSet adaptiveRulesOfLengthL = genBinRules(arsMic, naMic, naMare, minSup, minConf);

		AssociationRuleSet rsel = new AssociationRuleSet();

		rse = new AssociationRuleSet();

		for (int i = 0; i < adaptiveRulesOfLengthL.size(); i++) {
			rse.addRuleLast((AssociationRule) adaptiveRulesOfLengthL.get(i));
		}

		int l = 2;
		boolean gata = false;
		if (binar > 2)
			while (!gata) {
				rsel.clear();

				AssociationRuleSet ars = arsMic.getRulesOfLength(l);

				/*
				 * for (int i = 0; i < ars.size(); i++) {
				 * rsel.addRuleLast((AssociationRule) ars .get(i)); }
				 */

				// rsel.genByJoin(adaptiveRulesOfLengthL);
				rsel.genByJoinAdaptiv(adaptiveRulesOfLengthL, naMic);

				adaptiveRulesOfLengthL.clear();

				for (int i = 0; i < rsel.size(); i++) {
					AssociationRule r = rsel.get(i);
					double mi = System.currentTimeMillis();
					boolean isInteresting = r.computeIfInteresting(minSup, minConf, es);
					double mf = System.currentTimeMillis();

					time += mf - mi;

					if (isInteresting) {
						// if (r.length()==2) System.out.println(r.toString());

						adaptiveRulesOfLengthL.addRuleLast(r);
					}
					// if (r.getMaxim()>naMic)
					noSuppConfComputations++;
					/*
					 * if (maximal) { // eliminam subregulile regulii r ptr a
					 * pastra // in rezultat doar reguli maximale!!!
					 * AssociationRule arnew = new AssociationRule(r); // sterg
					 * ultima relatie si ultimul atribut arnew.removeLast();
					 * rse.remove(arnew);
					 * 
					 * // sterg primul atribut si prima relatie
					 * arnew.copyRule(r); arnew.removeFirst();
					 * rse.remove(arnew); }
					 */
				}

				for (int i = 0; i < ars.size(); i++) {
					adaptiveRulesOfLengthL.addRuleLast((AssociationRule) ars.get(i));
				}

				if (adaptiveRulesOfLengthL.size() == 0)
					gata = true;
				else {

					l++;

					for (int i = 0; i < adaptiveRulesOfLengthL.size(); i++) {
						rse.addRuleLast((AssociationRule) adaptiveRulesOfLengthL.get(i));
					}
				}
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

	public AssociationRuleSet genBinRules(AssociationRuleSet arsMic, int naMic, int na, double minSup, double minConf)
			throws Exception {
		AssociationRuleSet ars = new AssociationRuleSet();
		AssociationRuleSet arsm = arsMic.getRulesOfLength(1);
		for (int i = 0; i < arsm.size(); i++)
			ars.addRuleLast((AssociationRule) arsm.get(i));
		for (int i = 0; i < na - 1; i++)
			// for (int j = i + 1; j < na; j++) {
			for (int j = naMic; j < na; j++) {
				if (i < j)
					for (int k = 0; k < RelationSet.numberOfRelations(); k++) {
						Relation rnew = RelationSet.getRelation(k);
						AssociationRule anew = new AssociationRule();
						anew.add(es.get(0).getAttr(i));
						anew.addRel(rnew, 0);
						anew.add(es.get(0).getAttr(j));
						if (anew.valid()) {
							// anew.calculateSupportConfidenceRule(es);
							if (anew.getMaxim() > naMic) {
								noSuppConfComputations++;

								// anew.setMax(i);

								double mi = System.currentTimeMillis();
								boolean isInteresting = anew.computeIfInteresting(minSup, minConf, es);
								double mf = System.currentTimeMillis();

								time += mf - mi;

								if (isInteresting) {
									ars.addRuleLast(anew);
								}
							}
						}
					}
			}
		return ars;
	}
}
