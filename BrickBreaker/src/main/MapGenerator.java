package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
	//the 2d array, map, will contain all the bricks
	public int mapOfBricks[][];
	public int brickWidth;
	public int brickHeight;
	//how many rows and columns should be generated for a particular number of bricks
	public MapGenerator(int row, int col) {
		//instantiate the map 2d array with the values of row and col
		mapOfBricks = new int[row][col];
		for(int i = 0; i < mapOfBricks.length; i++) {
			for(int j = 0; j < mapOfBricks[0].length; j++) {
				//set each brick to 1 to denote that the bricks haven't been
				//hit by the ball yet
				//change the value of the particular position to 0 if i don't want to draw any 
				//particular brick inside my panel
				mapOfBricks[i][j] = 1;
			}
			
			brickWidth = 540/col;
			brickHeight = 150/row;
		}
	}
	
	public void draw(Graphics2D g) {
	//when this function is called, the bricks will be drawn on the particular positions
	//where there is a value of 1
		for(int i = 0; i < mapOfBricks.length; i++) {
			for(int j = 0; j < mapOfBricks[0].length; j++) {
				if(mapOfBricks[i][j] > 0) {
					//this condition means that if the particular brick is supposed to exist,
					//create that particular brick inside that particular position
					g.setColor(Color.white);
					g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth,
							brickHeight);
					
					//differentiate each brick by surrounding it by borders
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.black);
					g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth,
							brickHeight);
				}
			}
		}
	}
	
	public void setBrickValue(int value, int row, int col) {
		mapOfBricks[row][col] = value;
	}
}
