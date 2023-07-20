package main;

import java.awt.Color;
import java.awt.Graphics2D;

import math.Vector2D;

public class Rectangle {
	
	public Vector2D position = new Vector2D();
	public Vector2D velocity = new Vector2D();
	public int height = 0;
	public int width = 0;
	
	public Rectangle(int x, int y, int width, int height) {
		this.position.x = x;
		this.position.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Rectangle(int x, int y, int width, int height, Vector2D velocity) {
		this.position.x = x;
		this.position.y = y;
		this.width = width;
		this.height = height;
		this.velocity.x = velocity.x;
		this.velocity.y = velocity.y;
	}
	
	public void drawRect(Graphics2D g, Color color){
		g.setColor(color);
		g.fillRect((int)this.position.x, (int)this.position.y, width, height);
	}
	
	public void moveRectInput(KeyHandler keyh){
		if(keyh.leftPressed == true && this.position.x > 0) {
			this.position.x -= this.velocity.x;
		}
		if (keyh.rightPressed == true && this.position.x < 768-this.width) {
        	this.position.x += this.velocity.x;
        }
		if(keyh.upPressed == true && this.position.y > 0) {
			this.position.y -= this.velocity.y;
		}
		if (keyh.downPressed == true && this.position.y < 567-this.height) {
        	this.position.y += this.velocity.y;
        }
	}
	
	public void moveRectOnlyXInput(KeyHandler keyh){
		if(keyh.leftPressed == true && this.position.x > 0) {
			this.position.x -= this.velocity.x;
		}
		if (keyh.rightPressed == true && this.position.x < 768-this.width) {
        	this.position.x += this.velocity.x;
        }
	}
	
	public void moveRect(Vector2D velocity) {
		this.position.x += velocity.x;
		this.position.y += velocity.y;
	}

}
