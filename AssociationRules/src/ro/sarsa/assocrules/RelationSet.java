package ro.sarsa.assocrules;

import java.util.ArrayList;
import java.util.List;

import ro.sarsa.assocrules.relations.Relation;

public class RelationSet {
	private static List<Relation> allRelations = new ArrayList<Relation>();

	// intre atributele a si b se exista relatia r
	public static boolean isRel(Attribute a, Attribute b, Relation r) {
		// din ceva motiv varianta asta merge foarte incet in comparatie cu for
		// return r.getType1() == a.getType() && r.getType2() == b.getType();
		for (int i = 0; i < allRelations.size(); i++) {
			Relation ri = (Relation) allRelations.get(i);
			if (ri.equals(r) && ri.getType1().equals(a.getType()) && ri.getType2().equals(b.getType()))
				return true;
		}
		return false;
	}

	public void clear() {
		allRelations = new ArrayList<Relation>();
	}

	public static int numberOfRelations() {
		return allRelations.size();
	}

	public static Relation getRelation(int i) {
		return (Relation) allRelations.get(i);
	}

	// relatiile posibile intre doua atribute de anumite tipuri
	public static List<Relation> findRelationsFor2Attributes(Attribute a, Attribute b) {
		List<Relation> v = new ArrayList<Relation>();
		for (int i = 0; i < allRelations.size(); i++) {
			Relation r = (Relation) allRelations.get(i);
			if ((r.getType1().equals(a.getType()) && r.getType2().equals(b.getType()))) {
				v.add(r);
			}
		}
		return v;
	}

	// daca intre 2 atribute exista o relatie
	public static boolean areInRelation(Attribute a, Attribute b) {
		for (int i = 0; i < allRelations.size(); i++) {
			Relation r = (Relation) allRelations.get(i);
			if ((r.getType1().equals(a.getType()) && r.getType2().equals(b.getType()))) {
				return true;
			}
		}
		return false;
	}

	// relatiile posibile un atribut de un anumit tip
	public static List<Relation> findRelationsFor1Attribute(Attribute a) {
		List<Relation> v = new ArrayList<Relation>();
		for (int i = 0; i < allRelations.size(); i++) {
			Relation r = (Relation) allRelations.get(i);
			if (r.getType1().equals(a.getType())) {
				v.add(r);
			}
		}
		return v;
	}

	public static void addRelation(Relation r) {
		if (!allRelations.contains(r)) {
			allRelations.add(r);
		}
	}
}