package model;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BoidRunner {

	public static void main(String[] args) throws Exception {
		
		JFrame frame = new JFrame("Boids!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 600);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		frame.add(panel);
		frame.setVisible(true);

		Boid.initBoids(600, 600);

		while (true) {
			Boid.updateBoids();
			Boid.drawBoids(panel.getGraphics());
			Thread.sleep(100);
		}
	}
}
