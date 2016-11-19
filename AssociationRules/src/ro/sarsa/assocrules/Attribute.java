package ro.sarsa.assocrules;

import ro.sarsa.assocrules.attributetypes.AttributeType;

public class Attribute {
	private String name;
	private AttributeType type;
	private boolean isNull;
	private Comparable value;
	private double dValue;
	private int positionInEntity;

	public Attribute(String name, AttributeType type, Comparable value, int positionInEntity) {
		this.name = name;
		this.type = type;
		this.positionInEntity = positionInEntity;
		setValue(value);
	}

	public String getName() {
		return name;
	}

	public int getPozInEntity() {
		return positionInEntity;
	}

	public boolean equals(Object o) {
		Attribute ot = (Attribute) o;
		return name.equals(ot.name) && type.equals(ot.type);
	}

	public AttributeType getType() {
		return type;
	}

	public Comparable getValue() {
		return value;
	}

	public double getDoubleValue() {
		return dValue;
	}

	public boolean isNull() {
		return isNull;
	}

	public void setValue(Comparable o) {
		if (o == null) {
			isNull = true;
		} else {
			isNull = false;
			setValue(((Double) o).doubleValue());
		}
		value = o;
	}

	public void setValue(double v) {
		isNull = false;
		dValue = v;
	}

	public String toString() {
		return getName() + " " + getType() + " " + getValue();
	}

	public Attribute makeClone() {
		return new Attribute(name, type, value, positionInEntity);
	}
}
