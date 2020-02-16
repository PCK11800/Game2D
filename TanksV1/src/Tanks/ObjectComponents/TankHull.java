package Tanks.ObjectComponents;

import Tanks.Window.Window;

public class TankHull extends RotatingObject implements Cloneable {

	private float currentMovementSpeed;
	private float movementSpeed;
	protected float turningDistance; //The amount a tank will turn every time a turnLeft/Right method is called
	protected float currentTurningDistance;
	protected Window window;
	
	/**
	 * Moves the tank hull forward in the direction it is currently facing
	 */
	public void moveForward() 
	{
		xPos = (float) (xPos + (currentMovementSpeed * Math.sin(Math.toRadians(objectDirection))));
		yPos = (float) (yPos - (currentMovementSpeed * Math.cos(Math.toRadians(objectDirection))));
		setCenterLocation(xPos, yPos);
	}
	
	/**
	 * Moves the tank hull backward in the direction it is currently facing
	 */
	public void moveBackward() 
	{
		xPos = (float) (xPos - (currentMovementSpeed * Math.sin(Math.toRadians(objectDirection))));
		yPos = (float) (yPos + (currentMovementSpeed * Math.cos(Math.toRadians(objectDirection))));
		setCenterLocation(xPos, yPos);
	}
	
	public void turnRight() 
	{
		rotateObject(objectDirection + currentTurningDistance);
		unbrake();
	}
	
	public void turnLeft()
	{
		rotateObject(objectDirection - currentTurningDistance);
		unbrake();
	}
	
	public void brake() 
	{
		currentMovementSpeed = 0;
		currentTurningDistance = 0;
	}
	
	public void unbrake()
	{
		currentMovementSpeed = movementSpeed;
		currentTurningDistance = turningDistance;
	}
	
	public float getTurningDistance()
	{
		return turningDistance;
	}
	
	public void setTurningDistance(float turningDistance) 
	{
		this.turningDistance = turningDistance;
		this.currentTurningDistance = turningDistance;
	}
	
	public float getMovementSpeed() 
	{
		return currentMovementSpeed;
	}
	
	public void setMovementSpeed(float movementSpeed) 
	{
		this.movementSpeed = movementSpeed;
		this.currentMovementSpeed = movementSpeed;
	}
	
	public void setWindow(Window window) 
	{
		this.window = window;
	}

	public void update()
	{
		draw(window);
	}

	public float getObjectDirection() { return this.objectDirection; }
}
