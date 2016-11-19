package ro.sarsa.assocrules.relations;

import ro.sarsa.assocrules.Attribute;
import ro.sarsa.assocrules.attributetypes.AttributeType;
import ro.sarsa.assocrules.attributetypes.AttributeTypeDouble;

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

public class DoubleDoubleLess extends Relation {
	public static DoubleDoubleLess sing = new DoubleDoubleLess();

	private DoubleDoubleLess() {
	}

	public AttributeType getType1() {
		return AttributeTypeDouble.Double;
	}

	public AttributeType getType2() {
		return AttributeTypeDouble.Double;
	}

	public String getRelation() {
		return "Less";
	}

	public boolean areInRelationDoubleNonNull(Attribute at1, Attribute at2) {
		return at1.getDoubleValue() < at2.getDoubleValue();
	}

	public boolean areInRelationaNonNull(Attribute at1, Attribute at2) {
		return at1.getValue().compareTo(at2.getValue()) < 0;
	}

	public Relation inv() {
		return DoubleDoubleGreater.sing;
	}

	public String toString() {
		return "<";
	}
}