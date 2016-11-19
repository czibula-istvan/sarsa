package ro.sarsa.assocrules;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Entity {

	private List<Attribute> attr;

	public Entity() {
		attr = new ArrayList<Attribute>();
	}

	// cauta atribut dupa nume
	public Attribute searchAttr(Attribute a) {
		return attr.get(a.getPozInEntity());
	}

	public int getNoAttr() {
		return attr.size();
	}

	public List<Attribute> getAttr() {
		return attr;
	}

	public void addAttr(Attribute a) {
		attr.add(a);
	}

	public Attribute getAttr(int pos) {
		// pos incepe de la 0
		return ((Attribute) attr.get(pos));
	}

	public String toString() {
		String s = new String();
		List<Attribute> attr = getAttr();
		Iterator<Attribute> i = attr.iterator();
		while (i.hasNext()) {
			Attribute a = (Attribute) i.next();
			s = s + a.getValue() + " ";
		}
		return s;
	}

	public void writeEntity(PrintStream ps) {
		ps.println(toString());
	}

	public void writeAttributes(PrintStream ps) {
		Iterator<Attribute> i = attr.iterator();
		while (i.hasNext()) {
			Attribute a = (Attribute) i.next();
			ps.print(a.toString() + " ");
		}
		ps.println();
	}
}