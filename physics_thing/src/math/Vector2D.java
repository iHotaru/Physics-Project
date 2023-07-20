package math;

import java.awt.Color;
import java.awt.Graphics2D;

import main.Ball;

public class Vector2D {
	
	public double x;
	public double y;
	
	public Vector2D() {
		this.x = 0;
		this.y = 0;
	}
	
	public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
	
	public Vector2D(Vector2D vector) {
        this.x = vector.x;
        this.y = vector.y;
    }
	
	public Vector2D add(Vector2D v1) {
		return new Vector2D(this.x + v1.x, this.y + v1.y);
    }
	
	public Vector2D subtract(Vector2D v1) {
        return new Vector2D(this.x - v1.x, this.y - v1.y); 
    }
	
	public Vector2D scalar(double a) {
        return new Vector2D(this.x * a, this.y * a);
    }
	
	public double magnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }
	
	public Vector2D unitVec() {
		if(this.magnitude() == 0) {
			return new Vector2D();
		}
		else {
			return new Vector2D(this.x / this.magnitude(), this.y / this.magnitude());
		}
	}
	
	public Vector2D NormalVec() {
		return new Vector2D(-this.y, this.x).unitVec();
	}
	
	public static double dot(Vector2D a, Vector2D b) {
		return (a.x * b.x) + (a.y * b.y);
	}
	
	public void drawVec(int startX, int startY, Color color, Graphics2D g, int magnitude) {
		g.setColor(color);
		g.drawLine(startX, startY, (int)(startX + this.x * magnitude), (int)(startY + this.y * magnitude));
	}
	
}
