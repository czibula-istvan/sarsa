package ro.sarsa.som.testapps;

import java.awt.Graphics;

import ro.sarsa.som.SOM;

public interface INetworkDraw {
	public void drawNetwork(Graphics g, int w, int h, SOM som, TrainIterationSnapshoot trSnap);
}
