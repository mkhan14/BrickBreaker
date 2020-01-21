package main;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		//create jframe
		JFrame obj = new JFrame();
		
		//create gameplay object
		Gameplay gp = new Gameplay();
		
		//set the jframe's properties
		obj.setBounds(10, 10, 700, 600);
		obj.setTitle("Brick Breaker");
		obj.setResizable(false);
		obj.setVisible(true);
		obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//add gameplay object inside object of jframe
		//make Gameplay class extend JPanel so there's no error when adding gameplay object
		//into the jframe object
		obj.add(gp);

	}

}
