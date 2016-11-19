package ro.sarsa.assocrules.generators;

import ro.sarsa.assocrules.AssociationRule;
import ro.sarsa.assocrules.AssociationRuleSet;
import ro.sarsa.assocrules.Attribute;
import ro.sarsa.assocrules.Entity;
import ro.sarsa.assocrules.EntitySet;
import ro.sarsa.assocrules.RelationSet;
import ro.sarsa.assocrules.relations.Relation;

public class ByJoinAdaptivVers2Generator {
	private AssociationRuleSet neextins;
	private EntitySet set;
	private int nrAtributeNeextins;
	private int nrTotalAtribute;

	public ByJoinAdaptivVers2Generator(AssociationRuleSet neextins, EntitySet set, int nrAtributeNeextins,
			int nrTotalAtribute) {
		this.neextins = neextins;
		this.set = set;
		this.nrAtributeNeextins = nrAtributeNeextins;
		this.nrTotalAtribute = nrTotalAtribute;
	}

	/**
	 * Generereaza reguli binare in care cel putin un atribut e din lista de
	 * atribute cu care se extinde
	 * 
	 * @param minConf
	 * @param minSup
	 */
	public AssociationRuleSet generateBinaryRules(double minConf, double minSup) {
		AssociationRuleSet rez = new AssociationRuleSet();
		// reguli binare cu un atribut din lista neextinsa si una din lista
		// extinsa
		Entity primObiect = set.get(0);
		for (int i = 0; i < nrAtributeNeextins; i++) {
			for (int j = nrAtributeNeextins; j < nrTotalAtribute; j++) {
				Attribute attr1 = primObiect.getAttr(i);
				Attribute attr2 = primObiect.getAttr(j);
				addAllValidRelation(minConf, minSup, rez, attr1, attr2);
			}
		}
		// reguli binare ce contin doar atribute extinse
		for (int i = nrAtributeNeextins; i < nrTotalAtribute; i++) {
			for (int j = i + 1; j < nrTotalAtribute; j++) {
				Attribute attr1 = primObiect.getAttr(i);
				Attribute attr2 = primObiect.getAttr(j);
				addAllValidRelation(minConf, minSup, rez, attr1, attr2);
			}
		}
		return rez;
	}

	private void addAllValidRelation(double minConf, double minSup, AssociationRuleSet rez, Attribute attr1,
			Attribute attr2) {
		for (int k = 0; k < RelationSet.numberOfRelations(); k++) {
			Relation rnew = RelationSet.getRelation(k);
			AssociationRule anew = new AssociationRule(attr1, rnew, attr2);
			if (anew.valid()) {// relatia este aplicabila
				if (anew.computeIfInteresting(minSup, minConf, set)) {
					rez.addRuleLast(anew);
				}
			}
		}
	}

	public AssociationRuleSet adaptivVers2(double minConf, double minSup) {
		AssociationRuleSet.nrJoins = 0;
		AssociationRuleSet r = neextins;
		AssociationRuleSet ars = generateBinaryRules(minConf, minSup);
		AssociationRuleSet rPrim = genAssociationRulesByJoin(minSup, minConf, ars);
		long timegenByJoin = 0;
		long timeAsocByJoin = 0;
		boolean terminat = false;
		do {
			AssociationRuleSet rSecund = new AssociationRuleSet();
			long t = System.currentTimeMillis();
			rSecund.genByJoin(r, rPrim, set, minSup, minConf);
			timegenByJoin += System.currentTimeMillis() - t;

			terminat = rSecund.size() == 0;
			// System.out.println(" incep cu:" + rSecund.size() + " "
			// + AssociationRuleSet.nrJoins);
			t = System.currentTimeMillis();
			rSecund = genAssociationRulesByJoin(minSup, minConf, rSecund);
			timeAsocByJoin += System.currentTimeMillis() - t;
			// System.out.println("termin cu:" + rSecund.size() + " "
			// + AssociationRuleSet.nrJoins);
			// cu siguranta nu avem duplicate in r si rPrim
			r.addAllLast(rPrim);
			rPrim = rSecund;
		} while (!terminat);
		// System.out.println(" -->genByJoin:" + timegenByJoin
		// + " ms");
		// System.out.println("-->genAssociationRulesByJoin:" + timeAsocByJoin
		// + " ms");
		System.out.println("NrJoins:" + AssociationRuleSet.nrJoins);
		return r;
	}

	/**
	 * Porneste de lista de reguli binare face lista tuturor regulilor cu suport
	 * si confidenta cerut
	 * 
	 * @param minSup
	 * @param minConf
	 * @param ars
	 * @return
	 * @throws Exception
	 */
	public AssociationRuleSet genAssociationRulesByJoin(double minSup, double minConf, AssociationRuleSet ars) {
		AssociationRuleSet rez = new AssociationRuleSet(ars);
		AssociationRuleSet prevGenerated = ars;
		boolean gata = false;
		while (!gata) {
			AssociationRuleSet joined = new AssociationRuleSet();
			joined.genByJoin(prevGenerated, set, minSup, minConf);

			gata = joined.size() == 0;

			rez.addAllLast(joined);
			prevGenerated = joined;
		}
		return rez;
	}

}
