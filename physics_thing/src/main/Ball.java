package main;

import java.awt.Color;
import java.awt.Graphics2D;

import math.Vector2D;

public class Ball {
	
	public Vector2D position = new Vector2D();
	public Vector2D velVec = new Vector2D();
	public Vector2D accVec = new Vector2D();
	private double acceleration = 0;
	public int diameter = 0;
	public double radius = 0;
	public double elasticity = 0;
	//for friction 0<x<1 |||| best result 0<x<0.1
	double friction = 0.00;
	double mass = 0;
	
	public Ball(Vector2D position, int diameter, double mass, double elasticity) {
		this.diameter = diameter;
		this.radius = diameter/2;
		this.position = position;
		this.mass = mass;
		this.elasticity = elasticity;
		//adds new ball to balls arraylist in WindowPanel
		WindowPanel.balls.add(this);
	}
	
	public Ball(Vector2D position, int diameter, double acceleration, double mass, double elasticity) {
		this.diameter = diameter;
		this.radius = diameter/2;
		this.position = position;
		this.acceleration = acceleration;
		this.mass = mass;
		this.elasticity = elasticity;
		//adds new ball to balls arraylist in WindowPanel
		WindowPanel.balls.add(this);
	}
	
	public void drawBall(Graphics2D g, Color color){
		g.setColor(color);
		g.fillOval((int)this.position.x, (int)this.position.y, diameter, diameter);
	}
	
	public void drawBallVel(Graphics2D g){
		this.velVec.drawVec((int)(this.position.x+radius), (int)(this.position.y+radius), Color.red, g, 5);
	}
	
	public void drawBallAcc(Graphics2D g){
		this.accVec.drawVec((int)(this.position.x+radius), (int)(this.position.y+radius), Color.green, g, 50);
	}
	
	public void drawBBVec(Ball b2, Graphics2D g) {
		g.drawLine((int)(this.position.x + this.radius), (int)(this.position.y + this.radius), (int)(b2.position.x + b2.radius), (int)(b2.position.y + b2.radius));
	}
	
	public void drawBWVec(Wall w2, Graphics2D g) {
		g.drawLine((int)(this.position.x + this.radius), (int)(this.position.y + this.radius), (int)Collision.closestPointBW(this, w2).x, (int)Collision.closestPointBW(this, w2).y);
	}

	
	public void moveBallInput(KeyHandler keyh){
		
		if(keyh.leftPressed == true) {
			this.accVec.x = -1*acceleration;
		}
		if (keyh.rightPressed == true) {
			this.accVec.x = acceleration;
        }
		if(keyh.upPressed == true) {
			this.accVec.y = -1*acceleration;
		}
		if (keyh.downPressed == true) {
			this.accVec.y = acceleration;
        }
		if((keyh.leftPressed == false && keyh.rightPressed == false)) {
			this.accVec.x = 0;
		}
		if((keyh.upPressed == false && keyh.downPressed == false)) {
			this.accVec.y = 0;
		}
		
	}
	
	public void reposition() {
		this.accVec = accVec.unitVec().scalar(acceleration);
		this.velVec = this.velVec.add(this.accVec);
		this.velVec = this.velVec.scalar(1-friction);
		this.position = this.position.add(velVec);
	}
	
	public void moveBall() {
		this.position.x += velVec.x;
		this.position.y += velVec.y;
	}
	
}
