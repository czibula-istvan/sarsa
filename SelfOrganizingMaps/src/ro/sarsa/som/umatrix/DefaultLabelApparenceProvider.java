package ro.sarsa.som.umatrix;

import java.awt.Color;

public class DefaultLabelApparenceProvider implements LabelApparenceProvider {

	@Override
	public String getText(Object label) {
		return label.toString();
	}

	@Override
	public Color getColor(Object label) {
		return Color.red;
	}

}
