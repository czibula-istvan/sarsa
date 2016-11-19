package ro.sarsa.assocrules;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntitySet {

	private List<Entity> ent;

	public void empty() {
		ent.clear();
	}

	public EntitySet() {
		ent = new ArrayList<Entity>();
	}

	public void copie(EntitySet es) {
		ent = new ArrayList<Entity>(es.ent);
	}

	public void addEntity(Entity e) {
		ent.add(e);
	}

	public Iterator<Entity> iterator() {
		return ent.iterator();
	}

	public Entity get(int pos) {
		return ent.get(pos);
	}

	public int size() {
		return ent.size();
	}

	public EntitySet createNormalized() {
		EntitySet esn = new EntitySet();
		esn.copie(this);
		int na = esn.get(0).getNoAttr();
		for (int i = 0; i < na; i++) {
			// gasesc maximul si minimul pe coloana i
			double max = -Double.MAX_VALUE;
			double min = Double.MAX_VALUE;
			for (int j = 0; j < esn.size(); j++) {
				Comparable c = esn.get(j).getAttr(i).getValue();
				if (c != null) {
					double v = ((Double) c).doubleValue();
					if (v > max)
						max = v;
					if (v < min)
						min = v;
				}
			}
			// modific valorile
			for (int j = 0; j < esn.size(); j++) {
				Comparable c = esn.get(j).getAttr(i).getValue();
				if (c != null) {
					double v = ((Double) c).doubleValue();
					v = (v - min) / (max - min);
					esn.get(j).getAttr(i).setValue(new Double(v));
				}
			}
		}
		return esn;
	}

	public void writeData(PrintStream ps) {
		Iterator<Entity> i = iterator();
		boolean isFirst = true;
		while (i.hasNext()) {
			Entity e = (Entity) i.next();
			if (isFirst == true) {
				e.writeAttributes(ps);
				isFirst = false;
			}
			e.writeEntity(ps);
		}
	}

}
