package ro.sarsa.assocrules.attributetypes;

public class AttributeTypeDouble extends AttributeType {
	public static AttributeTypeDouble Double = new AttributeTypeDouble();
	double epsilon = 0.0001;

	public AttributeTypeDouble() {
	}

	public Comparable createValue(String s) {
		try {
			Double value = new Double(s);
			return value;
		} catch (NumberFormatException ex) {
			return null;
		}
	}

	@Override
	public String getName() {
		return "Double";
	}

}
