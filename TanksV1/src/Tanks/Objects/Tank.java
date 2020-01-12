package Tanks.Objects;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
	private int health = 1;
	
	private String shellTexturePath;
	private float shellSpeed;
	private ArrayList<TankShell> shellList = new ArrayList<>();
	private int shellRicochetNumber;
	
	private Map map;
	
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

	public void setHealth(int health) {this.health = health; }

	public void getHit() {
		System.out.println("tank: " + health);
		this.health--; }

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
		hull.setSize(width, height);
		turret.setSize(width, height);
	}
	
	public void setShellSpeed(float shellSpeed) 
	{
		this.shellSpeed = shellSpeed;
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
		shellList.add(createShell());
	}
	
	/**
	 * Move forward by one movementSpeed
	 */
	public void moveForward() 
	{
		hull.moveForward();
		turret.setTurretLocation();
	}
	
	/**
	 * Move backward by one movementSpeed
	 */
	public void moveBackward()
	{
		hull.moveBackward();
		turret.setTurretLocation();
	}
	
	/**
	 * Turn left by one turningDistance
	 */
	public void turnLeft()
	{
		hull.turnLeft();
		turret.setTurretLocation();
	}
	
	/**
	 * Turn right by one turningDistance
	 */
	public void turnRight()
	{
		hull.turnRight();
		turret.setTurretLocation();
	}
	
	/**
	 * Turn left by one TurretTurningDistance
	 */
	public void rotateTurretLeft()
	{
		turret.rotateLeft();
	}
	
	/**
	 * Turn right by one TurretTurningDistance
	 */
	public void rotateTurretRight()
	{
		turret.rotateRight();
	}
	
	private boolean tankCollisionCheck() 
	{
		for(int i = 0; i < map.getObjectsInMap().size(); i++) {
			if(hull.getGlobalBounds().intersection(map.getObjectsInMap().get(i).getGlobalBounds()) != null) {
				return true;
			}
		}
		return false;
	}
	
	//Call this in game loop
	public void update()
	{
		shellList = new ArrayList<TankShell>
				(shellList.stream()
				.filter(s -> !s.checkOutOfBounds() && s.isActive() && s.getRicochetNum() != shellRicochetNumber)
				.collect(Collectors.toList()));
		for (TankShell s : shellList) { s.update(); }

		if(tankCollisionCheck()) {
			System.out.println("Collision");
		}
		
		hull.update();
		turret.update();
		
		if(isPlayerControlled) {
			listener.handleInput();
			turret.setPlayerTurretDirection();
		}
	}

	public boolean isOpponent() { return !isPlayerControlled; }

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

	public float getLeftBounds() { return hull.getLeftBounds(); }

	public float getRightBounds() { return hull.getRightBounds(); }

	public float getTopBounds() { return hull.getTopBounds(); }

	public float getBottomBounds() { return hull.getBottomBounds(); }

	public boolean isAlive() { return (health <= 0 ? false : true); }
}
