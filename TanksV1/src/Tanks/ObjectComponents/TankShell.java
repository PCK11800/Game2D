package Tanks.ObjectComponents;

import java.awt.geom.Line2D;

import org.jsfml.system.Clock;

import Tanks.Objects.Map;
import Tanks.Objects.Tank;
import Tanks.Window.Window;

public class TankShell extends RotatingObject{
	
	private float shellSpeed;
	private int ricochetNum = 0;
	protected Window window;
	private Map map;
	private Clock movementClock = new Clock();
	private Clock ricochetClock = new Clock();
	private boolean active = true;
	private boolean ghost = false;
	private boolean hitPlayer = false;
	
	public TankShell(TankTurret connectedTankTurret, String texturePath, Window window, float shellSpeed, Map map) 
	{
		boolean hasFired = false;
		this.window = window;
		this.shellSpeed = shellSpeed;
		this.map = map;
		setObjectTexture(texturePath);
		setCenterLocation(connectedTankTurret.getxPos() + (float)(connectedTankTurret.getWidth() * Math.sin(Math.toRadians(connectedTankTurret.objectDirection))), connectedTankTurret.getyPos() - (float)(connectedTankTurret.getWidth() * Math.cos(Math.toRadians(connectedTankTurret.objectDirection))));
		rotateObject(connectedTankTurret.objectDirection);
	}

	public void setGhostMode()
	{
		ghost = true;
	}


	public void launchedForward()
	{
		if(movementClock.getElapsedTime().asMilliseconds() >= Tank.timePerFrame) {
			xPos = xPos + (float)(shellSpeed * Math.sin(Math.toRadians(this.objectDirection)));
			yPos = yPos - (float)(shellSpeed * Math.cos(Math.toRadians(this.objectDirection)));
			setCenterLocation(xPos, yPos);
			movementClock.restart();
		}
	}
	
	/**
	 * If out of window, return true
	 */
	public boolean checkOutOfBounds() 
	{
		if(getxPos() > window.getWidth() || getxPos() < 0) {
			return true;
		}
		else if (getyPos() > window.getHeight() || getyPos() < 0) {
			return true;
		}
		return false;
	}

	public void collisionHandling() 
	{
		for(int i = 0; i < map.getObjectsInMap().size(); i++) 
		{
			
			float x1, y1, x2, y2, x3, y3, x4, y4;
			x1 = this.getCornerCoordinates("topleft", "x");
			y1 = this.getCornerCoordinates("topleft", "y") * -1;
			x2 = this.getCornerCoordinates("topright", "x");
			y2 = this.getCornerCoordinates("topright", "y") * -1;
			x3 = this.getCornerCoordinates("bottomleft", "x");
			y3 = this.getCornerCoordinates("bottomleft", "y") * -1;
			x4 = this.getCornerCoordinates("bottomright", "x");
			y4 = this.getCornerCoordinates("bottomright", "y") * -1;
			
			Line2D top = new Line2D.Float(x1, y1, x2, y2); 
			Line2D left = new Line2D.Float(x1, y1, x3, y3);
			Line2D right = new Line2D.Float(x2, y2, x4, y4);			
			Line2D bottom = new Line2D.Float(x3, y3, x4, y4);
			
			float i1, j1, i2, j2, i3, j3, i4, j4;
			i1 = map.getObjectsInMap().get(i).getCornerCoordinates("topleft", "x");
			j1 = map.getObjectsInMap().get(i).getCornerCoordinates("topleft", "y") * -1;
			i2 = map.getObjectsInMap().get(i).getCornerCoordinates("topright", "x");
			j2 = map.getObjectsInMap().get(i).getCornerCoordinates("topright", "y") * -1;
			i3 = map.getObjectsInMap().get(i).getCornerCoordinates("bottomleft", "x");
			j3 = map.getObjectsInMap().get(i).getCornerCoordinates("bottomleft", "y") * -1;
			i4 = map.getObjectsInMap().get(i).getCornerCoordinates("bottomright", "x");
			j4 = map.getObjectsInMap().get(i).getCornerCoordinates("bottomright", "y") * -1;
			
			Line2D map_top = new Line2D.Float(i1, j1, i2, j2);
			Line2D map_bottom = new Line2D.Float(i3, j3, i4, j4);
			Line2D map_left = new Line2D.Float(i1, j1, i3, j3);
			Line2D map_right = new Line2D.Float(i2, j2, i4, j4);
			
			int collisionState = 0; //0 = no collision, 1 = top, 10 = bottom, 33 = left, 71 = right
			
			if(ricochetClock.getElapsedTime().asMilliseconds() >= Tank.timePerFrame) {
				if(top.intersectsLine(map_top) || right.intersectsLine(map_top) || left.intersectsLine(map_top)) {
					collisionState = collisionState + 1;
				}
				if(top.intersectsLine(map_bottom) || right.intersectsLine(map_bottom) || left.intersectsLine(map_bottom)) {
					collisionState = collisionState + 10;
				}
				if(top.intersectsLine(map_left) || right.intersectsLine(map_left) || left.intersectsLine(map_left)) {
					collisionState = collisionState + 33;
				}
				if(top.intersectsLine(map_right) || right.intersectsLine(map_right) || left.intersectsLine(map_right)) {
					collisionState = collisionState + 71;
				}
				if(bottom.intersectsLine(map_top) || bottom.intersectsLine(map_bottom) || bottom.intersectsLine(map_right) || bottom.intersectsLine(map_left)) {
					collisionState = 0;
				}
				
				if(collisionState > 0) {
					
					if(collisionState == 1 || collisionState == 10) {
						rotateObject(180 - objectDirection);
					}
					else if (collisionState == 33 || collisionState == 71) {
						rotateObject(0 - objectDirection);
					}
					else {
						//Top left & top right corner
						if(collisionState == 34 || collisionState == 72) {
							if(objectDirection < 270 && objectDirection > 90) {
								rotateObject(180 - objectDirection);
							}
							else {
								rotateObject(0 - objectDirection);
							}
						}
						//Bottom left & bottom right corner
						if(collisionState == 43 || collisionState == 81) {
							if(objectDirection >= 270 || objectDirection <= 90) {
								rotateObject(180 - objectDirection);
							}
							else {
								rotateObject(0 - objectDirection);
							}
						}
					}
					
					ricochetNum++;
					ricochetClock.restart();
				}
			}
		}
	}

	public void hitPlayer()
	{

	}


	public int getRicochetNum() {
		return ricochetNum;
	}
	
	public void update()
	{
		collisionHandling();
		launchedForward();
		draw(window);
	}

	public boolean isActive() { return active; }
}
