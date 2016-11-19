package ro.sarsa.assocrules.relations;

import ro.sarsa.assocrules.Attribute;
import ro.sarsa.assocrules.attributetypes.AttributeType;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: S.C SARSA S.R.L.
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */

public abstract class Relation {

	public abstract AttributeType getType1();

	public abstract AttributeType getType2();

	public abstract String getRelation();

	public boolean areInRelation(Attribute at1, Attribute at2) {
		if (at1.isNull() || at2.isNull())
			return false;
		return areInRelationaNonNull(at1, at2);
	}

	public abstract boolean areInRelationaNonNull(Attribute at1, Attribute at2);

	public abstract boolean areInRelationDoubleNonNull(Attribute at1, Attribute at2);

	public abstract Relation inv();

	public boolean inv(Relation r) {
		return this.equals(r.inv());
	}

	public abstract String toString();

	public boolean isApplicable(Attribute a, Attribute b) {
		return getType1().equals(a.getType()) && getType2().equals(b.getType());
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		return getRelation().equals(((Relation) o).getRelation());
	}
}