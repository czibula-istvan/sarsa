package ro.sarsa.assocrules.attributetypes;

public class AttributeTypeFactory {
	public static AttributeType getForName(String typeName) {
		if (typeName.equalsIgnoreCase(AttributeTypeDouble.Double.getName())) {
			return AttributeTypeDouble.Double;
		}
		throw new IllegalArgumentException("Unknown attribute type:" + typeName);
	}
}
