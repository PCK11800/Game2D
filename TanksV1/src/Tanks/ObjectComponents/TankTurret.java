package Tanks.ObjectComponents;

import java.awt.MouseInfo;
import Tanks.Window.Window;

public class TankTurret extends RotatingObject {

	private TankHull connectedTankHull;
	protected float turningDistance;
	protected Window window;
	
	public void setTurretLocation() 
	{
		float xPos = connectedTankHull.getxPos();
		float yPos = connectedTankHull.getyPos();
		setCenterLocation(xPos, yPos);
	}
	
	public void rotateRight() 
	{
		rotateObject(objectDirection + turningDistance);
		if(objectDirection >= 360) {
			objectDirection = objectDirection - 360;
		}
	}
	
	public void rotateLeft() 
	{
		rotateObject(objectDirection - turningDistance);
		if(objectDirection <= 0) {
			objectDirection = objectDirection + 360;
		}
	}
	
	public void setPlayerTurretDirection() 
	{
		 float mouseXPos = MouseInfo.getPointerInfo().getLocation().x;
	     float mouseYPos = MouseInfo.getPointerInfo().getLocation().y;

	     double dx = connectedTankHull.getxPos() - mouseXPos;
	     double dy = connectedTankHull.getyPos() - mouseYPos;

	     double rotationAngle = Math.toDegrees(Math.atan2(dy, dx));
	     if(rotationAngle >= 360) {
	    	 rotationAngle = rotationAngle - 360;
	     }
	     else if(rotationAngle <= 0) {
	    	 rotationAngle = rotationAngle + 360;
	     }

	     rotateObject((float) rotationAngle - 90);
	}
	
	public void setConnectedTankHull(TankHull connectedTankHull) 
	{
		this.connectedTankHull = connectedTankHull;
	}
	
	public float getTurningDistance()
	{
		return turningDistance;
	}
	
	public void setTurningDistance(float turningDistance) 
	{
		this.turningDistance = turningDistance;
	}
	
	public void setWindow(Window window) 
	{
		this.window = window;
	}
	
	public void update()
	{
		draw(window);
	}
}
