package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

//key listener detects arrow keys pressed to move slider and action listener detects
//moving ball

public class Gameplay extends JPanel implements KeyListener, ActionListener{
	//properties that we will work with
	
	//play is false because when game starts, game shouldn't start by itself
	private boolean play = false;
	private int score = 0;
	//number of bricks is 21 because we have 7x3 map
	private int numOfBricks = 21;
	
	//need a timer class for set how fast the ball should move 
	private Timer timer;
	//delay is speed given to timer
	private int delay = 4;
	//the starting x position of the slider (player)
	private int player_x = 310;
	private int ball_x_pos = 120;
	private int ball_y_pos = 350;
	//set direction of the ball
	private int ball_x_dir = -1;
	private int ball_y_dir = -2;
	
	private MapGenerator map;
	
	//add a constructor because i want to add the above properties when the gameplay object
	//is created in the main class
	public Gameplay() {
		map = new MapGenerator(3, 7);
		//add keylisterner first in order to work with keylistener
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		//context is this
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		//with help of the graphics object, g, im going to draw different shapes
		//like slider, ball, and the tiles (bricks)
		
		//first add background
		g.setColor(Color.black);
		//now we will create the rectangle for the background
		g.fillRect(1,1,692,592);
		
		//draw map
		map.draw((Graphics2D) g);
		
		//now lets create borders
		g.setColor(Color.yellow);
		g.fillRect(0,0,3,592);
		g.fillRect(0,0,692,3);
		g.fillRect(691,0,3,592);
		//no border at bottom because ball must touch bottom in order to end the game
		
		//scores
		g.setColor(Color.white);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString(""+score, 590, 30);
		
		//create the paddle
		g.setColor(Color.green);
		g.fillRect(player_x,550,100,8);
		
		//create the ball
		g.setColor(Color.yellow);
		g.fillOval(ball_x_pos,ball_y_pos,20,20);
		
		//if all the bricks are shattered, set play to false and prompt for game restart
		if(numOfBricks <= 0) {
			play = false;
			ball_x_dir = 0;
			ball_y_dir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("You won!", 260, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 230, 350);
		}
		
		
		if(ball_y_pos > 570) {
			play = false;
			ball_x_dir = 0;
			ball_y_dir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 30));
			//show game over message in center:
			g.drawString("Game Over", 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Press Enter to Restart", 230, 350);
		}
		
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//this will be automatically called
		timer.start();
		
		//to move ball:
		//if player moves left or right key, game has started, and must detect
		//whether ball is touching the top, left, or right borders
		if(play) {
			//to detect ball's collision with paddle:
			//create rectangle around the ball to detect intersection
			//of two different objects
			if(new Rectangle(ball_x_pos, ball_y_pos, 20, 20).intersects(
					new Rectangle(player_x, 550, 100, 8))) {
				ball_y_dir = -ball_y_dir;
			}
			
			//detect ball collision with brick
			A: for(int i = 0; i < map.mapOfBricks.length; i++) {
				for(int j = 0; j < map.mapOfBricks[0].length; j++) {
					if(map.mapOfBricks[i][j] > 0) {
						int brick_x = j * map.brickWidth + 80;
						int brick_y = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brick_x, brick_y, brickWidth, 
								brickHeight);
						Rectangle ballRect = new Rectangle(ball_x_pos, ball_y_pos, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);
							numOfBricks--;
							score += 5;
							
							if(ball_x_pos + 19 <= brickRect.x || ball_x_pos + 1 >=
									brickRect.x + brickRect.width) {
								ball_x_dir = -ball_x_dir;
							}else {
								ball_y_dir = -ball_y_dir;
							}
							break A;
						}
					}
				}
			}
			
			
			//create bricks, must detect ball intersection with each brick and
			//which brick the ball hit, so create a new class for this (MapGenerator)
			
			
			//ball's movement starts moving to the top-right
			ball_x_pos += ball_x_dir;
			ball_y_pos += ball_y_dir;
			//for left border
			if(ball_x_pos < 0) {
				//if ball hits left border, change x direction so that ball position
				//increments according to the new x direction
				ball_x_dir = -ball_x_dir;
			}
			//for top border
			if(ball_y_pos < 0) {
				ball_y_dir = -ball_y_dir;
			}
			//for right border
			if(ball_x_pos > 670) {
				ball_x_dir = -ball_x_dir;
			}
			//this is to refresh all the elements in the paint method so that
			//the ball's motion is displayed
			repaint();
		}
		
		//repaint will call the paint method and draw each and everything again
		//i need to call repaint because when i increment/decrement value of
		//player_x, there is no change shown
		//there is no change shown because the paddle doesn't get redrawn.
		//i need to redraw paddle and in order to redraw paddle, i need to
		//call paint method again
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//if right arrow is pressed, increment the paddle position,
		//but check that it doesn't go outside the panel
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(player_x >= 600) {
				//if player moves beyond panel, make player stay at border of panel
				player_x = 600;
			}else {
				moveRight();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(player_x < 10) {
				//if player moves beyond panel, make player stay at border of panel
				player_x = 10;
			}else {
				moveLeft();
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			//if ENTER is pressed and if game is over, start the game
			//and reset all the properties
			if(!play) {
				play = true;
				ball_x_pos = 120;
				ball_y_pos = 350;
				ball_x_dir = -1;
				ball_y_dir = -2;
				player_x = 310;
				score = 0;
				numOfBricks = 21;
				map = new MapGenerator(3, 7);
				
				repaint();
			}
		}
		
	}
	
	public void moveRight() {
		//set play to true because it was set to false
		play = true;
		//move 20 pixels to the right
		player_x += 20;
	}
	
	public void moveLeft() {
		//set play to true because it was set to false
		play = true;
		//move 20 pixels to the right
		player_x -= 20;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// not needed, but must keep it or else will show error
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// not needed, but must keep it or else will show error
		
	}

}
