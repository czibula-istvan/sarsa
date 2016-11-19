package ro.sarsa.assocrules.attributetypes;

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

public abstract class AttributeType {

	public AttributeType() {
	}

	public abstract String getName();

	public abstract Comparable createValue(String s);

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		return ((AttributeType) o).getName().equals(getName());
	}
}