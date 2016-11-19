package ro.sarsa.som.umatrix;

import javax.swing.JFrame;

import ro.sarsa.som.SOM;
import ro.sarsa.som.traindata.SOMTrainData;

public class UMatrixFrame extends JFrame {

	public UMatrixFrame(SOM som, SOMTrainData trD) {
		UMatrixPanel umPanel = new UMatrixPanel(som, trD);
		setContentPane(umPanel);
		setSize(400, 400);
		setVisible(true);
	}

}
