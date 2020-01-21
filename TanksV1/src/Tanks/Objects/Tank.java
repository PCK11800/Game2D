package Tanks.Objects;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import org.jsfml.system.Clock;

import Tanks.Listeners.PlayerListener;
import Tanks.ObjectComponents.TankHull;
import Tanks.ObjectComponents.TankShell;
import Tanks.ObjectComponents.TankTurret;
import Tanks.Window.Window;

public class Tank {
	/**
	 * The tank class contains all the methods required to create a tank.
	 * However, it is important to note that the methods are required to be
	 * called in order, or JSFML throws a hissy fit and won't work.
	 * 
	 * The order is:
	 * 
	 * 1. setHullTexture()
	 * 2. setTurretTexture()
	 * 3. setWindow()
	 * 4. setMap()
	 * 5. Everything else
	 * 
	 * OPTIONAL:
	 * setPlayerControlled() : This enables the tank to be controlled by the player
	 */
	
	private TankHull hull;
	private TankTurret turret;
	private Window window;
	private PlayerListener listener;
	private boolean isPlayerControlled = false;
	
	private String shellTexturePath;
	private float shellSpeed;
	private ArrayList<TankShell> shellList = new ArrayList<>();
	private int shellRicochetNumber;
	
	private Map map;
	
	private Clock turretDelayClock = new Clock();
	private Clock fireDelayClock = new Clock();
	private Clock movementDelayClock = new Clock();
	private Clock rotationDelayClock = new Clock();
	public static float timePerFrame = 8; //Approx 120 fps
	private int delayBetweenShell;
	private int previousMove; //1 = forward, 2 = backward
	private int previousTurn; //1 = turn left, 2 = turn right
	
	public Tank() 
	{
		this.hull = new TankHull();
		this.turret = new TankTurret();
		turret.setConnectedTankHull(hull);
	}
	
	public void setHullTexture(String texturePath)
	{
		hull.setObjectTexture(texturePath);
	}
	
	public void setTurretTexture(String texturePath)
	{
		turret.setObjectTexture(texturePath);
	}
	
	public void setShellTexture(String texturePath)
	{
		shellTexturePath = texturePath;
	}
	
	public void setWindow(Window window) 
	{
		hull.setWindow(window);
		turret.setWindow(window);
		this.window = window;
	}
	
	public void setMap(Map map)
	{
		this.map = map;
	}
	
	public void setTankLocation(float xPos, float yPos) 
	{
		hull.setCenterLocation(xPos, yPos);
		turret.setTurretLocation();
	}
	
	public void setHullTurningDistance(float turningDistance)
	{
		hull.setTurningDistance(turningDistance);
	}
	
	public void setTurretTurningDistance(float turningDistance)
	{
		turret.setTurningDistance(turningDistance);
	}
	
	public void setMovementSpeed(float movementSpeed)
	{
		hull.setMovementSpeed(movementSpeed);
	}
	
	public void setInitialDirection(float objectDirection)
	{
		hull.rotateObject(objectDirection);
		turret.rotateObject(objectDirection);
	}
	
	public void setSize(float width, float height) 
	{
		hull.setSize(width * 53, height * 75);
		turret.setSize(width * 53, height * 75);
	}
	
	public void setShellSpeed(float shellSpeed) 
	{
		this.shellSpeed = shellSpeed;
	}
	
	public void setFireDelay(int rateOfFire)
	{
		this.delayBetweenShell = rateOfFire;
	}
	
	private TankShell createShell()
	{
		TankShell shell = new TankShell(turret, shellTexturePath, window, shellSpeed, map);
		shell.setSize((float) (turret.getWidth()/10), turret.getHeight()/5);
		return shell;
		
	}
	
	public ArrayList<TankShell> getShellList()
	{
		return shellList;
	}
	
	public void setShellRicochetNumber(int ricochetNumber) {
		shellRicochetNumber = ricochetNumber;
	}
	
	public void enablePlayerControl()
	{
		listener = new PlayerListener(this);
		isPlayerControlled = true;
	}
	
	/**
	 * Shoot a shell
	 */
	public void shoot() 
	{
		if(fireDelayClock.getElapsedTime().asMilliseconds() >= delayBetweenShell) {
			shellList.add(createShell());
			fireDelayClock.restart();
		}
	}
	
	/**
	 * Move forward by one movementSpeed
	 */
	public void moveForward() 
	{	
		if(movementDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame) {
			hull.moveForward();
			turret.setTurretLocation();
			movementDelayClock.restart();
			previousMove = 1;
		}
	}
	
	/**
	 * Move backward by one movementSpeed
	 */
	public void moveBackward()
	{	
		if(movementDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame) {
			hull.moveBackward();
			turret.setTurretLocation();
			movementDelayClock.restart();
			previousMove = 2;
		}
	}
	
	/**
	 * Turn left by one turningDistance
	 */
	public void turnLeft()
	{
		if(rotationDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame) {
			hull.turnLeft();
			turret.setTurretLocation();
			rotationDelayClock.restart();
			previousTurn = 1;
		}
	}
	
	/**
	 * Turn right by one turningDistance
	 */
	public void turnRight()
	{
		
		if(rotationDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame) {
			hull.turnRight();
			turret.setTurretLocation();
			rotationDelayClock.restart();
			previousTurn = 2;
		}
	}
	
	/**
	 * Turn left by one TurretTurningDistance
	 */
	public void rotateTurretLeft()
	{
		if(turretDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame) {
			turret.rotateLeft();
			turretDelayClock.restart();
		}
	}
	
	/**
	 * Turn right by one TurretTurningDistance
	 */
	public void rotateTurretRight()
	{
		if(turretDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame) {
			turret.rotateLeft();
			turretDelayClock.restart();
		}
	}
	
	private void tankCollisionHandling()
	{
		for(int i = 0; i < map.getObjectsInMap().size(); i++) {
			
			float x1, y1, x2, y2, x3, y3, x4, y4;
			x1 = hull.getCornerCoordinates("topleft", "x");
			y1 = hull.getCornerCoordinates("topleft", "y") * -1;
			x2 = hull.getCornerCoordinates("topright", "x");
			y2 = hull.getCornerCoordinates("topright", "y") * -1;
			x3 = hull.getCornerCoordinates("bottomleft", "x");
			y3 = hull.getCornerCoordinates("bottomleft", "y") * -1;
			x4 = hull.getCornerCoordinates("bottomright", "x");
			y4 = hull.getCornerCoordinates("bottomright", "y") * -1;
			
			//Lines of tank hull
			Line2D top = new Line2D.Float(x1, y1, x2, y2); 
			Line2D bottom = new Line2D.Float(x3, y3, x4, y4);
			Line2D left = new Line2D.Float(x1, y1, x3, y3);
			Line2D right = new Line2D.Float(x2, y2, x4, y4);
			
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
			
			if(top.intersectsLine(map_top) || right.intersectsLine(map_top) || left.intersectsLine(map_top) || bottom.intersectsLine(map_top) ||
					top.intersectsLine(map_right) || right.intersectsLine(map_right) || left.intersectsLine(map_right) || bottom.intersectsLine(map_right) ||
					top.intersectsLine(map_left) || right.intersectsLine(map_left) || left.intersectsLine(map_left) || bottom.intersectsLine(map_left) ||
					top.intersectsLine(map_bottom) || right.intersectsLine(map_bottom) || left.intersectsLine(map_bottom) || bottom.intersectsLine(map_bottom)) {
				
				//Only forward
				if(previousMove == 1 && previousTurn >= 0) {
					moveBackward();
				}
				//Only backward
				if(previousMove == 2 && previousTurn >= 0) {
					moveForward();
				}
				//Only turnLeft
				if(previousMove == 0 && previousTurn == 1) {
					turnRight();
				}
				//Only turnRight
				if(previousMove == 0 && previousTurn == 2) {
					turnLeft();
				}
			}
		}
	}
	
	//Call this in game loop
	public void update()
	{	
		for(int i = 0; i < shellList.size(); i++) {
			if(shellList.get(i).checkOutOfBounds()) {
				shellList.remove(i);
				shellList.trimToSize();	
			}
			else if(shellList.get(i).getRicochetNum() == shellRicochetNumber) {
				shellList.remove(i);
				shellList.trimToSize();	
			}
			else {
				shellList.get(i).update();
			}
		}
		
		//hull.unbrake();
		tankCollisionHandling();
		previousMove = 0;
		previousTurn = 0;
		
		hull.update();
		turret.update();
		
		if(isPlayerControlled) {
			listener.handleInput();
			turret.setPlayerTurretDirection();
		}
	}
	
	public float getMovementSpeed()
	{
		return hull.getMovementSpeed();
	}
	
	public float getTurningDistance()
	{
		return hull.getTurningDistance();
	}
	
	public float getTurretTurningDistance()
	{
		return turret.getTurningDistance();
	}
	
	public int getFireDelay()
	{
		return delayBetweenShell;
	}
	
}
