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
	
	Thread WindowThread;
	
	//Ball (position, circumference, velocity, acceleration, mass, elasticity)
	//(elasticity < 1 == ball loses speed on impact) |||| (elasticity > 1 == ball gains speed on impact)
	Ball ball1 = new Ball(new Vector2D(110,250), 50, 1, 10, 1);
	Ball ball2 = new Ball(new Vector2D(300,250), 50, 1, 10, 1);
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
	
	//Rectangle (x position, y position, width, height)
	Rectangle rect1 = new Rectangle(100, 530, 48, 48, new Vector2D(10,10));
	
	//a new Keyhandler is here to add onto the window panel
	KeyHandler keyH = new KeyHandler();
	
	//FPS
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
				repaint();
				delta--;
			}
		}
	}
	
	public void update() {
		
        ball1.moveBallInput(keyH);
        
        for(int i = 0;  i<balls.size(); i++) {
        	for(int j = i+1; j<balls.size(); j++) {
		        if(Collision.collisionDetectionBB(balls.get(i), balls.get(j))) {
		        	System.out.println("COLLISION");
		        	Collision.penResBB(balls.get(i), balls.get(j));
		        	Collision.collResBB(balls.get(i), balls.get(j));
		        }
        	}
        	balls.get(i).reposition();
        }
        
        //System.out.println(Collision.closestPointBW(ball1, wall1).magnitude());
        
        for(int i = 0;  i<walls.size(); i++) {
        	for(int j = 0; j<balls.size(); j++) {
        		if(Collision.collisionDetectionBW(balls.get(j), walls.get(i))) {
		        	System.out.println("COLLISION");
		        	Collision.penResBW(balls.get(j), walls.get(i));
		        	Collision.collresBW(balls.get(j), walls.get(i));
		        }
        	}
        }
        
		//System.out.println(ball1.position.subtract(ball2.position).magnitude());
		
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D)g;
		
		for(Ball i : balls) {
			i.drawBall(g2, Color.pink);
			i.drawBallVel(g2);
			i.drawBallAcc(g2);
		}
		
		
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
		
		//rect1.drawRect(g2, Color.pink);
		
		g2.dispose();
		
	}
}
