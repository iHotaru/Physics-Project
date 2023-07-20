package main;

import java.awt.Color;
import java.awt.Graphics2D;

import math.Vector2D;

public class Wall {
	
	public Vector2D startPoint;
	public Vector2D endPoint;
	
	
	public Wall() {
		this.startPoint = new Vector2D();
		this.endPoint = new Vector2D();
		WindowPanel.walls.add(this);
	}
	
	public Wall(int x1, int y1, int x2, int y2) {
		this.startPoint = new Vector2D(x1, y1);
		this.endPoint = new Vector2D(x2, y2);
		WindowPanel.walls.add(this);
	}
	
	public Vector2D wallUnitVec() {
		return this.endPoint.subtract(this.startPoint).unitVec();
	}
	
	public void drawWall(Graphics2D g) {
		g.setColor(Color.white);
		g.drawLine((int)startPoint.x, (int)startPoint.y, (int)endPoint.x, (int)endPoint.y);
	}

}
