package main;

import math.Vector2D;

public class Collision {
	
	public static boolean collisionDetectionBB(Ball a, Ball b){
		
		Vector2D center1 = a.position.add(new Vector2D(a.radius, a.radius));
		Vector2D center2 = b.position.add(new Vector2D(b.radius, b.radius));
		
		if(a.radius + b.radius >= center1.subtract(center2).magnitude()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void penResBB(Ball a, Ball b) {
		
		Vector2D center1 = a.position.add(new Vector2D(a.radius, a.radius));
		Vector2D center2 = b.position.add(new Vector2D(b.radius, b.radius));
		
		//vector of distance between ball a & b
		Vector2D dist = center1.subtract(center2);
		//length of both radii - actuall distance between positions is equal to depth of penetration
		double penDepth = a.radius + b.radius - dist.magnitude();
		
		Vector2D penRes = dist.unitVec().scalar(penDepth/2);
		
		a.position = a.position.add(penRes);
		
		b.position = b.position.add(penRes.scalar(-1));
	}
	
	public static void collResBB(Ball a, Ball b){
		
		Vector2D center1 = a.position.add(new Vector2D(a.radius, a.radius));
		Vector2D center2 = b.position.add(new Vector2D(b.radius, b.radius));
		
		//assuming both masses are the same v1 = u2 & v2 = u1
		//if that is not the case we have to take into acount mass when finding the new velocities 
		//formula for collisions can be found here -> https://en.wikipedia.org/wiki/Elastic_collision
		
		//finding the unit normal (aka line where the balls collide)
		Vector2D normal = center2.subtract(center1).unitVec();
		//finding the unit tangent (line perpendicular to normal)
		Vector2D tangent = new Vector2D(-normal.y, normal.x);
		//finding magnitude of velocity along unit normal using dot product
		double normalMagA = (((a.mass-b.mass)/(a.mass+b.mass))*Vector2D.dot(a.velVec, normal)) + (((2*b.mass)/(a.mass+b.mass))*Vector2D.dot(b.velVec, normal));
		double normalMagB = (((b.mass-a.mass)/(a.mass+b.mass))*Vector2D.dot(b.velVec, normal)) + (((2*a.mass)/(a.mass+b.mass))*Vector2D.dot(a.velVec, normal));
		//finding magnitude of velocity along unit tangent using dot product
		double tanMagA = Vector2D.dot(a.velVec, tangent);
		double tanMagB = Vector2D.dot(b.velVec, tangent);
		//adding together the normal and tangential lines to get new velocity |||| Remember that the normal components swap but the tangential components stay the same
		a.velVec = normal.scalar(normalMagA).add(tangent.scalar(tanMagA));
		b.velVec = normal.scalar(normalMagB).add(tangent.scalar(tanMagB));
		
	}
	
	public static Vector2D closestPointBW(Ball b1, Wall w1) {
		
		Vector2D center = b1.position.add(new Vector2D(b1.radius, b1.radius));
		
		Vector2D ballToWallStart = w1.startPoint.subtract(center);
		if(Vector2D.dot(w1.wallUnitVec(), ballToWallStart) > 0) {
			return w1.startPoint;
		}
		
		Vector2D wallEndToBall= center.subtract(w1.endPoint);
		if(Vector2D.dot(w1.wallUnitVec(), wallEndToBall) > 0) {
			return w1.endPoint;
		}
		
		double closestDist = Vector2D.dot(w1.wallUnitVec(), ballToWallStart);
		Vector2D closestVec = w1.wallUnitVec().scalar(closestDist);
		return w1.startPoint.subtract(closestVec);
	}
	
	public static boolean collisionDetectionBW(Ball b1, Wall w1) {
		
		Vector2D center = b1.position.add(new Vector2D(b1.radius, b1.radius));
		
		if(b1.radius >= center.subtract(closestPointBW(b1, w1)).magnitude()) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public static void penResBW(Ball b1, Wall w1) {
		Vector2D center = b1.position.add(new Vector2D(b1.radius, b1.radius));
		
		Vector2D penVec = center.subtract(closestPointBW(b1, w1));
		
		b1.position = b1.position.add(penVec.unitVec().scalar(b1.radius-penVec.magnitude()));
	}
	
	public static void collresBW(Ball b1, Wall w1) {
		//this is the same as collresBB except i dont move the wall and just reverse the normal velocity component
		Vector2D center = b1.position.add(new Vector2D(b1.radius, b1.radius));
		
		//finding the unit normal (aka line where the ball collides)
		Vector2D normal = center.subtract(closestPointBW(b1, w1)).unitVec();
		//finding the unit tangent (line perpendicular to normal)
		Vector2D tangent = new Vector2D(-normal.y, normal.x);
		//finding magnitude of velocity along unit normal using dot product
		//elasticity is the amount of bounce the wall gives (elasticity < 1 == ball loses speed on impact) (elasticity > 1 == ball gains speed on impact)
		double normalMag = -1*Vector2D.dot(b1.velVec, normal)*b1.elasticity;
		//finding magnitude of velocity along unit tangent using dot product
		double tanMag = Vector2D.dot(b1.velVec, tangent);
		
		b1.velVec = normal.scalar(normalMag).add(tangent.scalar(tanMag));
	}
		
}
