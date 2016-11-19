package ro.sarsa.assocrules;

import java.util.ArrayList;
import java.util.List;

import ro.sarsa.assocrules.relations.Relation;

public class AssociationRule {

	private List<Attribute> attrs;
	private List<Relation> rels;
	private long support;
	private long confidence;

	private int max;

	// regula e a0 r0 a1 r1 ... a(n-1) r(n-1) a(n)

	public int getMaxim() {
		int max = -1;
		for (Attribute atr : attrs) {
			String s = atr.getName();
			int indice = Integer.parseInt(s.substring(1));
			if (indice > max) {
				max = indice;
			}
		}
		return max;
	}

	public void setMax(int i) {
		max = i;
	}

	public int getMax() {
		return max;
	}

	public AssociationRule() {
		attrs = new ArrayList<Attribute>();
		rels = new ArrayList<Relation>();
		support = 0;
		confidence = 0;
	}

	public AssociationRule(Attribute a1, Relation r, Attribute a2) {
		attrs = new ArrayList<Attribute>(2);
		rels = new ArrayList<Relation>(1);
		support = 0;
		confidence = 0;
		attrs.add(a1);
		attrs.add(a2);
		rels.add(r);
	}

	public AssociationRule(AssociationRule old) {
		attrs = new ArrayList<Attribute>(old.attrs);
		rels = new ArrayList<Relation>(old.rels);
		support = old.support;
		confidence = old.confidence;
	}

	public void removeLast() {
		attrs.remove(attrs.size() - 1);
		rels.remove(rels.size() - 1);
	}

	public void removeFirst() {
		attrs.remove(0);
		rels.remove(0);
	}

	public void copyRule(AssociationRule old) {
		attrs.clear();
		rels.clear();
		for (int i = 0; i < old.length(); i++)
			add(old.getRelation(i), old.getAttribute(i));
		add(old.getAttribute(old.length()));
	}

	// verify if a rule has minimum support and confidence
	public boolean isInteresting(double minSup, double minConf, EntitySet es) {
		return confidence >= minConf * es.size() && support >= minSup * es.size();
	}

	public boolean computeIfInterestingOld(double minSup, double minConf, EntitySet es) {
		calculateSupportConfidenceRule(es);
		return isInteresting(minSup, minConf, es);
	}

	/**
	 * 
	 * @param minSup
	 * @param minConf
	 * @param es
	 * @return
	 */
	public boolean computeIfInteresting(double minSup, double minConf, EntitySet es) {
		if (rels.isEmpty()) {
			return false;
		}
		confidence = 0;
		support = 0;
		int nrEntities = es.size();
		int requiredConf = (int) Math.round(minConf * nrEntities);
		int requiredSupport = (int) Math.round(minSup * nrEntities);
		int remainedEntites = nrEntities;
		// calculeaza suportul in setul de entitati ale regulilor din setul cs
		for (int j = 0; j < nrEntities; j++) {
			Entity ec = es.get(j);
			// verific regula pentru entitatea curenta
			updateSupportConfidence(ec);
			remainedEntites--;
			if (support + remainedEntites < requiredSupport) {
				return false;// can not reach the requested support
			}
			if (confidence + remainedEntites < requiredConf) {
				return false;// can not reach the requested confidence
			}
			if (confidence >= requiredConf && support >= requiredSupport) {
				return true;// we reached the required support and confidence
			}
		}

		return false;
	}

	// verifica daca o regula este valida (relatiile intre atribute sunt valide)
	public boolean valid() {
		for (int i = 0; i < length(); i++) {
			Attribute ai = getAttribute(i);
			Attribute aiPlus1 = getAttribute(i + 1);
			Relation ri = getRelation(i);
			if (!RelationSet.isRel(ai, aiPlus1, ri)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Use this function if you need to know the value of support confidence
	 * 
	 * @param es
	 */
	public void calculateSupportConfidenceRule(EntitySet es) {
		confidence = 0;
		support = 0;
		// calculeaza suportul in setul de entitati ale regulilor din setul cs
		for (int j = 0; j < es.size(); j++) {
			Entity ec = es.get(j);
			// verific regula pentru entitatea curenta
			updateSupportConfidence(ec);
		}
	}

	/**
	 * Am modificat sa avem doar un singur apel la searchAttr (cautare lineara)
	 * 
	 * @param ar
	 * @param ec
	 */
	private void updateSupportConfidence(Entity ec) {
		boolean inRelation = true;
		Attribute ai = ec.searchAttr(getAttribute(0));
		if (ai.isNull()) {
			return; // not in support
		}
		for (int i = 0; i < length(); i++) {
			Attribute aiPlus1 = ec.searchAttr(getAttribute(i + 1));
			if (aiPlus1.isNull()) {
				return; // not in support
			}
			if (inRelation && !(getRelation(i).areInRelationaNonNull(ai, aiPlus1))) {
				// is clear that is not in confidence but we need to continue
				// to check if is in support
				inRelation = false;
			}
			ai = aiPlus1;
		}
		// is in support, all the values are different from null
		support++;
		if (inRelation) {
			// is a valid relation (all relations are satisfied
			confidence++;
		}
	}

	public int length() {
		// lungimea regulii = nr relatii
		return rels.size();
	}

	public Attribute getAttribute(int i) {
		// preconditie: i<=length
		return attrs.get(i);
	}

	public Relation getRelation(int i) {
		// preconditie: i<length
		return rels.get(i);
	}

	public void add(Attribute a) {
		attrs.add(a);
	}

	public void addAttr(Attribute a, int pos) {
		attrs.add(pos, a);
	}

	public void addRel(Relation r, int pos) {
		rels.add(pos, r);
	}

	public void add(Relation r, Attribute a) {
		attrs.add(a);
		rels.add(r);
	}

	public void add(Attribute a, Relation r, int pos) {
		// pos in intervalul [0, nr attr - 1]; inserare pe pos
		attrs.add(pos, a);
		rels.add(pos, r);
	}

	public void removeAttr(int pos) {
		attrs.remove(pos);
	}

	public void removeRel(int pos) {
		rels.remove(pos);
	}

	public String toString() {
		StringBuffer s = new StringBuffer();
		for (int i = 0; i < rels.size(); i++) {
			s.append(attrs.get(i).getName());
			s.append(rels.get(i).toString());
		}
		s.append(attrs.get(rels.size()).getName());

		// s += " suport = " + support + " confidenta " + confidence;

		return s.toString();
	}

	public boolean attrInRule(Attribute a) {
		// verifica daca un atribut este sau nu in regula
		for (int i = 0; i <= length(); i++)
			if (getAttribute(i).equals(a))
				return true;
		return false;
	}

	public boolean identicalIgnoreLastIgnoreFirst(AssociationRule ar) {
		int lg = length();
		if (lg != ar.length())
			return false;
		lg = lg - 1;
		for (int i = 0; i < lg; i++) {
			Attribute atr1 = ar.getAttribute(i + 1);
			Attribute atr2 = getAttribute(i);
			Relation rel1 = ar.getRelation(i + 1);
			Relation rel2 = getRelation(i);
			if ((!atr1.equals(atr2)) || (!rel1.equals(rel2)))
				return false;
		}
		if (!ar.getAttribute(lg + 1).equals(getAttribute(lg))) {
			return false;
		}
		return true;
	}

	public boolean identicalIgnoreFirstIgnoreLast(AssociationRule ar) {
		int lg = length();
		if (length() != ar.length())
			return false;
		lg = lg - 1;
		for (int i = 0; i < lg; i++) {
			Attribute atr1 = ar.getAttribute(i);
			Attribute atr2 = getAttribute(i + 1);
			Relation rel1 = ar.getRelation(i);
			Relation rel2 = getRelation(i + 1);
			if ((!atr1.equals(atr2)) || (!rel1.equals(rel2)))
				return false;
		}
		if (!ar.getAttribute(lg).equals(getAttribute(lg + 1))) {
			return false;
		}
		return true;
	}

	public boolean identical(AssociationRule ar) {
		if (length() != ar.length())
			return false;
		for (int i = 0; i < ar.length(); i++) {
			Attribute atr1 = ar.getAttribute(i);
			Attribute atr2 = getAttribute(i);
			Relation rel1 = ar.getRelation(i);
			Relation rel2 = getRelation(i);
			if ((!atr1.equals(atr2)) || (!rel1.equals(rel2)))
				return false;
		}
		if (!ar.getAttribute(ar.length()).equals(getAttribute(ar.length()))) {
			return false;
		}
		return true;
	}

	public AssociationRule invRule() {
		AssociationRule ar = new AssociationRule();
		for (int i = length() - 1; i >= 0; i--) {
			ar.add(getAttribute(i + 1));
			ar.addRel(getRelation(i).inv(), ar.length());
		}
		ar.add(getAttribute(0));
		return ar;
	}

	public boolean identicalWithInverse(AssociationRule ar) {
		if (length() != ar.length())
			return false;
		if (rels.size() > 0) {
			for (int i = 0; i < rels.size() / 2 + 1; i++) {
				Relation rel1 = ar.getRelation(rels.size() - 1 - i);
				Relation rel2 = rels.get(i);
				if (!rel1.equals(rel2))
					return false;
			}
		}
		for (int i = 0; i < attrs.size() / 2 + 1; i++) {
			Attribute atr1 = ar.getAttribute(attrs.size() - 1 - i);
			Attribute atr2 = getAttribute(i);
			if (!atr1.equals(atr2))
				return false;
		}
		return true;
	}

	public boolean identicalWithInverseIgnoreFirsts(AssociationRule ar) {
		if (length() != ar.length())
			return false;

		int relsLg = rels.size() - 1;
		if (relsLg > 0) {
			for (int i = 0; i < relsLg / 2 + 1; i++) {
				Relation rel1 = ar.getRelation(relsLg - 1 - i + 1);
				Relation rel2 = rels.get(i + 1);
				if (!rel1.equals(rel2))
					return false;
			}
		}
		int attrsLg = attrs.size() - 1;
		for (int i = 0; i < attrsLg / 2 + 1; i++) {
			Attribute atr1 = ar.getAttribute(attrsLg - 1 - i + 1);
			Attribute atr2 = getAttribute(i + 1);
			if (!atr1.equals(atr2))
				return false;
		}
		return true;
	}

	public boolean identicalWithInverseIgnoreLasts(AssociationRule ar) {
		if (length() != ar.length())
			return false;
		int relsLg = rels.size() - 1;
		if (relsLg > 0) {
			for (int i = 0; i < relsLg / 2 + 1; i++) {
				Relation rel1 = ar.getRelation(relsLg - 1 - i);
				Relation rel2 = rels.get(i);
				if (!rel1.equals(rel2))
					return false;
			}
		}
		int attrsLg = attrs.size() - 1;
		for (int i = 0; i < attrsLg / 2 + 1; i++) {
			Attribute atr1 = ar.getAttribute(attrsLg - 1 - i);
			Attribute atr2 = getAttribute(i);
			if (!atr1.equals(atr2))
				return false;
		}
		return true;
	}

	public AssociationRule extractSubRuleNoLast() {
		// returneaza regula fara ultimul atribut si ultima relatie
		AssociationRule ar = new AssociationRule(this);
		ar.removeLast();
		return ar;
	}

	public AssociationRule extractSubRuleNoFirst() {
		// returneaza regula fara ultimul atribut si ultima relatie
		AssociationRule ar = new AssociationRule(this);
		ar.removeFirst();
		return ar;
	}

	public boolean contains(Attribute attribute) {
		for (Attribute atr : attrs) {
			if (atr.getName().equalsIgnoreCase(attribute.getName())) {
				return true;
			}
		}
		return false;
	}

	public Attribute getLastAtr() {
		return attrs.get(attrs.size() - 1);
	}

	public Relation getLastRel() {
		return rels.get(rels.size() - 1);
	}

	public void addFirst(Relation rel, Attribute atr) {
		attrs.add(0, atr);
		rels.add(0, rel);
	}

	public Attribute getFirstAtr() {
		return attrs.get(0);
	}
}