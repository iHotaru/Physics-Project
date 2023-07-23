package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import math.Vector2D;

public class WindowPanel extends JPanel implements Runnable{
	
	//This is needed to create the program loop using the overridden run method
	Thread WindowThread;
	
	//Ball (position, circumference, velocity, acceleration, mass, elasticity)
	//(elasticity < 1 == ball loses speed on impact) |||| (elasticity > 1 == ball gains speed on impact)
	Ball ball1 = new Ball(new Vector2D(110,250), 50, 0.5, 10, 1);
	Ball ball2 = new Ball(new Vector2D(300,250), 50, 1, 10, 1);
	Ball ball3 = new Ball(new Vector2D(500,250), 50, 1, 10, 1);
	
	//this is static so i can just add the new balls to the list in the Ball constructor
	static ArrayList<Ball> balls = new ArrayList<Ball>();
	
	//Wall (x1, y1, x2, y2)
	Wall wall1 = new Wall(100, 500, 650, 500);
	Wall wall2 = new Wall(650, 500, 650, 100);
	Wall wall3 = new Wall(650, 100, 100, 100);
	Wall wall4 = new Wall(100, 100, 100, 500);
	Wall wall5 = new Wall(400, 250, 500, 350);
	
	//this is static so i can just add the new walls to the list in the Wall constructor
	static ArrayList<Wall> walls = new ArrayList<Wall>();
	
	//Rectangle (x position, y position, width, height) yet to be implemented
	//Rectangle rect1 = new Rectangle(100, 530, 48, 48, new Vector2D(10,10));
	
	//a Keyhandler is here to add onto the window panel when the constructor is called
	KeyHandler keyH = new KeyHandler();
	
	//FPS of program
	final int FPS = 60;
	
	public WindowPanel() {
		this.setPreferredSize(new Dimension(768, 576));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyH);
		this.setFocusable(true);
	}
	
	public void startWindowThread() {
		WindowThread = new Thread(this);
		WindowThread.start();
	}
	
	@Override
	public void run() {
		
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0/FPS;
		double delta = 0;
		
		while(WindowThread != null) {
			
			long currentTime = System.nanoTime();
			delta += (currentTime-lastTime) / ns;
			lastTime = currentTime;
			
			while(delta >= 1) {
				//1. updates information such as position
				update();
				//2. draws to the screen with updated information
				//repaint calls paintComponent method
				repaint();
				delta--;
			}
		}
	}
	
	public void update() {
		
		//makes ball1 controllable by user
        ball1.moveBallInput(keyH);
        
        //loops through each ball and detects whether that ball is colliding with the other balls using collision detection method
        //if the ball is colliding with another it calls penResBB & collResBB to resolve it
        for(int i = 0;  i<balls.size(); i++) {
        	for(int j = i+1; j<balls.size(); j++) {
		        if(Collision.collisionDetectionBB(balls.get(i), balls.get(j))) {
		        	System.out.println("COLLISION BB");
		        	Collision.penResBB(balls.get(i), balls.get(j));
		        	Collision.collResBB(balls.get(i), balls.get(j));
		        }
        	}
        	//repositions ball (**causes errors if called more than once**)
        	balls.get(i).reposition();
        }
        
        //loops through each wall and detects whether a ball is colliding with the wall using collision detection method
        //if the ball is colliding with the wall penResBW & collResBW to resolve it
        for(int i = 0;  i<walls.size(); i++) {
        	for(int j = 0; j<balls.size(); j++) {
        		if(Collision.collisionDetectionBW(balls.get(j), walls.get(i))) {
		        	System.out.println("COLLISION BW");
		        	Collision.penResBW(balls.get(j), walls.get(i));
		        	Collision.collresBW(balls.get(j), walls.get(i));
		        }
        	}
        }
        
	}
	
	//built in method for JPanel
	public void paintComponent(Graphics g) {
		
		//this repaints the background for you, so you aren't left with the previous paint artifacts.
		//if not called it ends up looking like the windows xp solitaire win animation
		super.paintComponent(g);
		
		//this extends the graphics class and gives us more sophisticated methods to work with
		Graphics2D g2 = (Graphics2D)g;
		
		//goes through balls arraylist and draws each one
		for(Ball i : balls) {
			i.drawBall(g2, Color.pink);
			i.drawBallVel(g2);
			i.drawBallAcc(g2);
		}
		
		//goes through walls arraylist and draws each one
		for(Wall i : walls) {
			i.drawWall(g2);
		}
		
		g2.setColor(Color.green);
		
		ball1.drawBBVec(ball2, g2);
		
		g2.setColor(Color.cyan);
		
		/*for(int i = 0; i<walls.size(); i++) {
			ball1.drawBWVec(walls.get(i), g2);
		}*/
		
		ball1.drawBWVec(wall5, g2);
		ball2.drawBWVec(wall5, g2);
		
		g2.dispose();
		
	}
}
