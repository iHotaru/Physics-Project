package main;

import math.Vector2D;

public class Collision {
	
	public static boolean collisionDetectionBB(Ball a, Ball b){
		
		Vector2D center1 = a.position.add(new Vector2D(a.radius, a.radius));
		Vector2D center2 = b.position.add(new Vector2D(b.radius, b.radius));
		
		//detects whether the balls are colliding by comparing both radii to the distance between balls
		//if the distance between balls is less than the radius of both balls then there is a collision 
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
		//gets unit vector of normal line and scales it by half of the penetration depth
		Vector2D penRes = dist.unitVec().scalar(penDepth/2);
		//moves position of ball 'a' by half of penetration depth
		a.position = a.position.add(penRes);
		//moves position of ball 'b' by half of penetration depth in opposite direction
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
		//adding together the normal and tangential lines to get new velocity
		a.velVec = normal.scalar(normalMagA).add(tangent.scalar(tanMagA));
		b.velVec = normal.scalar(normalMagB).add(tangent.scalar(tanMagB));
		
	}
	
	public static Vector2D closestPointBW(Ball b1, Wall w1) {
		
		Vector2D center = b1.position.add(new Vector2D(b1.radius, b1.radius));
		
		//if the dot product of the ball is negative compared to the walls starting point then we know that is the closest point
		Vector2D wallStartToBall = center.subtract(w1.startPoint);
		if(Vector2D.dot(w1.wallUnitVec(), wallStartToBall) < 0) {
			return w1.startPoint;
		}
		//same thing as above except with the ending point
		Vector2D wallEndToBall= center.subtract(w1.endPoint);
		if(Vector2D.dot(w1.wallUnitVec(), wallEndToBall) > 0) {
			return w1.endPoint;
		}
		//for the closest distance we get the dot product of the ball To Walls starting point with the unit vector of the wall itself
		double closestDist = Vector2D.dot(w1.wallUnitVec(), wallStartToBall);
		//we then use the dot product to scale the walls unit vector up 
		Vector2D closestVec = w1.wallUnitVec().scalar(closestDist);
		//we then add from the starting point vector to get a vector pointing to the closest point the ball is from the wall
		return w1.startPoint.add(closestVec);
	}
	
	public static boolean collisionDetectionBW(Ball b1, Wall w1) {
		
		Vector2D center = b1.position.add(new Vector2D(b1.radius, b1.radius));
		
		//we subtract the position vector by the closest point to ge the distance between the position and the closest point
		//then we compare the radius to the magnitude of the vector to see if there is a collision
		if(b1.radius >= center.subtract(closestPointBW(b1, w1)).magnitude()) {
			return true;
		}
		else {
			return false;
		}
		
	}
	
	public static void penResBW(Ball b1, Wall w1) {
		Vector2D center = b1.position.add(new Vector2D(b1.radius, b1.radius));
		//this is just the distance vector of the position of the ball with the closest point of the ball to the wall
		Vector2D penVec = center.subtract(closestPointBW(b1, w1));
		//adds back to the position vector the amount the ball went through the wall
		//(radius) - (distance between center and wall) = (amount ball penetrated the wall)
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
