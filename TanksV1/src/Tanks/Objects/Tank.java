package Tanks.Objects;

import Tanks.Listeners.PlayerListener;
import Tanks.ObjectComponents.*;
import Tanks.Sounds.GameSound;
import Tanks.Window.Window;
import org.jsfml.system.Clock;

import java.awt.geom.Line2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

public class Tank
{
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

	protected Map map;
	private LevelContainer levelContainer;
	protected Window window;
	
	protected TankHull hull;
	protected TankTurret turret;

	protected float shellSpeed;
	protected int shellRicochetNumber;
	private PlayerListener listener;
	private boolean isPlayerControlled = false;
	private String shellTexturePath;

	private int health = 100;
	private int startingHealth = 100;
	private int damagePerShell;
	private int rammingDamage;
	private boolean armour = false;

	private float sizeMult_w, sizeMult_h;
	private ArrayList<TankShell> shellList = new ArrayList<>();

	private int money = 0;
	private String name = "Thomas";

	private Clock turretDelayClock = new Clock();
	private Clock fireDelayClock = new Clock();
	private Clock movementDelayClock = new Clock();
	private Clock rotationDelayClock = new Clock();
	private Clock tankMovingSoundHandlerClock = new Clock();
	public static float timePerFrame = 8; //Approx 120 fps
	private int delayBetweenShell;
	private int previousMove; //1 = forward, 2 = backward
	private int previousTurn; //1 = turn left, 2 = turn right
	private int previousMoveAlt;
	private int previousTurnAlt;
	private boolean collisionLastMove = false;
	private boolean loadNextLevel = false;

	private long lastShellFired = System.nanoTime();
	private String tankID;

	private boolean enemyCollision = false;
	private TankConfigs tankConfigs = new TankConfigs();

	private GameSound tankFiring;
	private GameSound tankMoving;

	private float tankFiringVolume;
	private float tankMovingVolume;
	public ArrayList<String> installedUpgrades = new ArrayList<>();

	/**
	 * Class constructor. Creates a new instance of Tank.
	 */
	public Tank()
	{
		this.hull = new TankHull();
		this.turret = new TankTurret();
		turret.setConnectedTankHull(hull);
		tankID = UUID.randomUUID().toString();
	}

	/**
	 * Mutator method for setting the texture of the Hull of this instance of Tank
	 * @param texturePath path to hull texture
	 */
	public void setHullTexture(String texturePath)
	{
		hull.setObjectTexture(texturePath);
	}

	/**
	 * Mutator method for setting the health of the Tank
	 * @param health health value
	 */
	public void setHealth(int health)
	{
		this.health = health;
		this.startingHealth = health;
	}

	/**
	 * Mutator method for setting the texture of the Turret of this instance of Tank
	 * @param texturePath path to turret texture
	 */
	public void setTurretTexture(String texturePath)
	{
		turret.setObjectTexture(texturePath);
	}

	/**
	 * Mutator method for setting the texture of shells fired from this instance of Tank
	 * @param texturePath path to shell texture
	 */
	public void setShellTexture(String texturePath)
	{
		shellTexturePath = texturePath;
	}

	/**
	 * Mutator method for setting the Window for this instance of Tank
	 * @param window Window
	 */
	public void setWindow(Window window)
	{
		hull.setWindow(window);
		turret.setWindow(window);
		this.window = window;
	}


	/**
	 * Mutator method for setting the LevelContainer for this instance of Tank
	 * @param levelContainer LevelContainer
	 */
	public void setLevelContainer(LevelContainer levelContainer)
	{
		this.levelContainer = levelContainer;
		this.map = levelContainer.getMap();
	}

	/**
	 * Mutator method for setting Tank location
	 * @param xPos x position
	 * @param yPos y position
	 */
	public void setTankLocation(float xPos, float yPos)
	{
		hull.setCenterLocation(xPos, yPos);
		turret.setTurretLocation();
	}

	/**
	 * Mutator method for setting the damage caused by shells fired from this Tank
	 * @param damagePerShell damage caused by shells
	 */
	public void setDamagePerShell(int damagePerShell) {this.damagePerShell = damagePerShell;}

	/**
	 * Mutator method for setting the turning distance of the Tank Hull
	 * @param turningDistance turning distance (in pixels per frame)
	 */
	public void setHullTurningDistance(float turningDistance)
	{
		hull.setTurningDistance(turningDistance);
	}

	/**
	 * Mutator method for setting the turning distance of the Tank Turret
	 * @param turningDistance turning distance (in pixels per frame)
	 */
	public void setTurretTurningDistance(float turningDistance)
	{
		turret.setTurningDistance(turningDistance);
	}

	/**
	 * Mutator method for setting the movement speed of the Tank
	 * @param movementSpeed movement speed (in pixels per frame)
	 */
	public void setMovementSpeed(float movementSpeed)
	{
		hull.setMovementSpeed(movementSpeed);
	}

	/**
	 * Mutator method for setting the initial rotation of the Tank
	 * @param objectDirection initial rotation (in degrees)
	 */
	public void setInitialDirection(float objectDirection)
	{
		hull.rotateObject(objectDirection);
		turret.rotateObject(objectDirection);
	}

	/**
	 * Mutator method for setting the size of the Tank
	 * @param width relative width
	 * @param height relative height
	 */
	public void setSize(float width, float height)
	{
		hull.setSize(width * 53, height * 75);
		turret.setSize(width * 53, height * 75);
		sizeMult_w = width;
		sizeMult_h = height;
	}

	/**
	 * Scales the Tank according to screen size
	 */
	public void setScale()
	{
		float[] scale = ObjectSizeHandler.scaleConstant();
		hull.setSize(hull.getWidth() * scale[0], hull.getHeight() * scale[1]);
		turret.setSize(turret.getWidth() * scale[0], turret.getHeight() * scale[1]);
	}

	/**
	 * Mutator method for setting the firing sound for this Tank.
	 * @param firingSound path to firing sound
	 * @param volume volume of sound
	 */
	public void setFiringSound(String firingSound, float volume)
	{
		tankFiring = new GameSound(firingSound);
		tankFiring.setVolume(volume);
		tankFiringVolume = volume;
	}

	/**
	 * Mutator method for setting the movement sound for this Tank
	 * @param moveSound path to movement sound
	 * @param volume volume of sound
	 */
	public void setMovingSound(String moveSound, float volume)
	{
		tankMoving = new GameSound(moveSound);
		tankMoving.setVolume(volume);
		tankMovingVolume = volume;
	}

	/**
	 * Mutator method for setting the speed at which shells fired by this Tank travel
	 * @param shellSpeed shell speed (pixels per frame)
	 */
	public void setShellSpeed(float shellSpeed)
	{
		this.shellSpeed = shellSpeed;
	}

	/**
	 * Mutator method to set the delay time between shells being fired from this tank
	 * @param rateOfFire time delay (in milliseconds) between shells being fired
	 */
	public void setFireDelay(int rateOfFire)
	{
		this.delayBetweenShell = rateOfFire;
	}


	/**
	 * Create an instance of TankShell with this tank's turret as the origin
	 * @return new TankShell
	 */
	private TankShell createShell()
	{
		TankShell shell = new TankShell(turret, shellTexturePath, window, shellSpeed, levelContainer, shellRicochetNumber, damagePerShell);
		shell.setSize((float) (turret.getWidth()/10), turret.getHeight()/5);
		lastShellFired = System.nanoTime();
		return shell;

	}

	/**
	 * Mutator method to set the amount of times shells fired by this tank can ricochet before they are removed from the game.
	 * @param ricochetNumber number of times shells can ricochet
	 */
	public void setShellRicochetNumber(int ricochetNumber) {
		shellRicochetNumber = ricochetNumber;
	}

	/**
	 * Make this instance of tank player controlled.
	 */
	public void enablePlayerControl()
	{
		listener = new PlayerListener(this);
		isPlayerControlled = true;
	}

	/**
	 * Mutator method for setting the ramming damage done by this instance of tank
	 * @param rammingDamage ramming damage
	 */
	public void setRammingDamage(int rammingDamage)
	{
		this.rammingDamage = rammingDamage;
	}


	/**
	 * Shoot a shell from the tank turret.
	 */
	public void shoot()
	{
		if(fireDelayClock.getElapsedTime().asMilliseconds() >= delayBetweenShell)
		{
			levelContainer.getShellList().add(createShell());
			fireDelayClock.restart();

			tankFiring.play();
		}
	}

	/**
	 * Move forward by one movementSpeed
	 */
	public void moveForward()
	{
		if(movementDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame)
		{
			previousMove = 1;
			previousMoveAlt = 1;
			collisionHandling();
			movementDelayClock.restart();
		}
	}


	/**
	 * Move backward by one movementSpeed
	 */
	public void moveBackward()
	{
		if(movementDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame)
		{
			previousMove = 2;
			previousMoveAlt = 2;
			collisionHandling();
			movementDelayClock.restart();
		}
	}


	/**
	 * Turn left by one turningDistance
	 */
	public void turnLeft()
	{
		if(rotationDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame)
		{
			previousTurn = 1;
			previousTurnAlt = 1;
			collisionHandling();
			movementDelayClock.restart();
		}
	}


	/**
	 * Turn right by one turningDistance
	 */
	public void turnRight()
	{
		if(rotationDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame)
		{
			previousTurn = 2;
			previousTurnAlt = 2;
			collisionHandling();
			movementDelayClock.restart();
		}
	}


	/**
	 * Turn left by one TurretTurningDistance
	 */
	public void rotateTurretLeft()
	{
		if(turretDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame)
		{
			turret.rotateLeft();
			turretDelayClock.restart();
		}
	}


	/**
	 * Turn right by one TurretTurningDistance
	 */
	public void rotateTurretRight()
	{
		if(turretDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame)
		{
			turret.rotateLeft();
			turretDelayClock.restart();
		}
	}

	/**
	 * Handles the sound for tank movement
	 */
	private void tankMovingSoundHandler()
	{
		if(tankMovingSoundHandlerClock.getElapsedTime().asMilliseconds() > 180)
		{
			tankMovingSoundHandlerClock.restart();
			tankMoving.play();
		}
	}

	/**
	 * Returns an array holding the four lines of a tank.
	 * @return Line2D[top, bottom, left, right]
	 */
	public Line2D[] getTankBounds()
	{
		return hull.getObjectBounds();
	}

	/**
	 * Handles collision with map objects
	 */
	private void collisionHandling()
	{
		//Previous move: 1 = forward, 2 = backward
		//Previous turn: 1 = left, 2 = right

		int fineTuneMove = 5;
		int fineTuneTurn = 8;
		float fineTuneWidthPadding = (float) 0.5;
		float fineTuneHeightPadding = (float) 1;
		TankHull ghostHull = new TankHull();
		ghostHull.setObjectTexture(Textures.TANKHULL_DEAD);
		ghostHull.setSize(getSizeMult_w() * (53 + fineTuneWidthPadding), getSizeMult_h() * (75 + fineTuneHeightPadding));
		ghostHull.setLocation(getXPos(), getYPos());
		ghostHull.rotateObject(hull.getObjectDirection());
		ghostHull.setMovementSpeed(getMovementSpeed());
		float[] scale = ObjectSizeHandler.scaleConstant();
		ghostHull.setSize(ghostHull.getWidth() * scale[0], ghostHull.getHeight() * scale[1]);

		if(previousMove == 1 && previousTurn >= 0){
			float xPos = (float) (getXPos() + (fineTuneMove * Math.sin(Math.toRadians(ghostHull.getObjectDirection()))));
			float yPos = (float) (getYPos() - (fineTuneMove * Math.cos(Math.toRadians(ghostHull.getObjectDirection()))));
			ghostHull.setCenterLocation(xPos, yPos);
		}
		else if(previousMove == 2 && previousTurn >= 0){
			float xPos = (float) (getXPos() - (fineTuneMove * Math.sin(Math.toRadians(ghostHull.getObjectDirection()))));
			float yPos = (float) (getYPos() + (fineTuneMove * Math.cos(Math.toRadians(ghostHull.getObjectDirection()))));
			ghostHull.setCenterLocation(xPos, yPos);
		}
		else if(previousMove == 0 && previousTurn == 1){
			ghostHull.rotateObject(ghostHull.getObjectDirection() - fineTuneTurn);
		}
		else if(previousMove == 0 && previousTurn == 2){
			ghostHull.rotateObject(ghostHull.getObjectDirection() + fineTuneTurn);
		}

		Line2D[] ghostHullBounds = ghostHull.getObjectBounds();
		Line2D top = ghostHullBounds[0];
		Line2D bottom = ghostHullBounds[1];
		Line2D left = ghostHullBounds[2];
		Line2D right = ghostHullBounds[3];

		//ghostHull.draw(window);
		boolean canMove = false;

		for(int i = 0; i < map.getObjectsInMap().size(); i++)
		{
			Line2D objectBounds[] = map.getObjectsInMap().get(i).getObjectBounds();

			Line2D map_top = objectBounds[0];
			Line2D map_bottom = objectBounds[1];
			Line2D map_left = objectBounds[2];
			Line2D map_right = objectBounds[3];

			if (top.intersectsLine(map_top) || right.intersectsLine(map_top) || left.intersectsLine(map_top) || bottom.intersectsLine(map_top) ||
					top.intersectsLine(map_right) || right.intersectsLine(map_right) || left.intersectsLine(map_right) || bottom.intersectsLine(map_right) ||
					top.intersectsLine(map_left) || right.intersectsLine(map_left) || left.intersectsLine(map_left) || bottom.intersectsLine(map_left) ||
					top.intersectsLine(map_bottom) || right.intersectsLine(map_bottom) || left.intersectsLine(map_bottom) || bottom.intersectsLine(map_bottom))
			{
				if (map.getObjectsInMap().get(i).isExit()) //If it is an exit
				{
					if (!map.getObjectsInMap().get(i).getLockedStatus()) //is unlocked
					{
						System.out.println("NEXT LEVEL!");
						this.loadNextLevel = true;
						this.increaseMoney(50);
					}
				}
				canMove = false;
				break;
			}
			else {
				canMove = true;
			}
		}

		if(canMove){
			if(previousMove == 1 && previousTurn >= 0){
				if(movementDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame)
				{
					hull.moveForward();
					turret.setTurretLocation();
					movementDelayClock.restart();
					tankMovingSoundHandler();
					previousMove = 0;

					if(previousTurn == 1){
						hull.turnLeft();
						turret.setTurretLocation();
						rotationDelayClock.restart();
						tankMovingSoundHandler();
						previousTurn = 0;
					}
					else if(previousTurn == 2){
						hull.turnRight();
						turret.setTurretLocation();
						rotationDelayClock.restart();
						tankMovingSoundHandler();
						previousTurn = 0;
					}
				}
			}
			else if(previousMove == 2 && previousTurn >= 0) {
				if (movementDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame) {
					hull.moveBackward();
					turret.setTurretLocation();
					movementDelayClock.restart();
					tankMovingSoundHandler();
					previousMove = 0;

					if(previousTurn == 1){
						hull.turnLeft();
						turret.setTurretLocation();
						rotationDelayClock.restart();
						tankMovingSoundHandler();
						previousTurn = 0;
					}
					else if(previousTurn == 2){
						hull.turnRight();
						turret.setTurretLocation();
						rotationDelayClock.restart();
						tankMovingSoundHandler();
						previousTurn = 0;
					}
				}
			}
			else if(previousMove == 0 && previousTurn == 1) {
				if(rotationDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame)
				{
					hull.turnLeft();
					turret.setTurretLocation();
					rotationDelayClock.restart();
					tankMovingSoundHandler();
					previousTurn = 0;
				}
			}
			else if(previousMove == 0 && previousTurn == 2) {
				if(rotationDelayClock.getElapsedTime().asMilliseconds() >= timePerFrame)
				{
					hull.turnRight();
					turret.setTurretLocation();
					rotationDelayClock.restart();
					tankMovingSoundHandler();
					previousTurn = 0;
				}
			}
		}
	}

	/**
	 * Handles collision with another tank
	 */
	private void tankToTankCollisionHandling()
	{
		Line2D[] tankHullBounds = hull.getObjectBounds();
		Line2D top = tankHullBounds[0];
		Line2D bottom = tankHullBounds[1];
		Line2D left = tankHullBounds[2];
		Line2D right = tankHullBounds[3];

		if(isPlayerControlled)
		{
			for(int i = 0; i < levelContainer.getEnemyList().size(); i++)
			{
				Opponent thisEnemy = levelContainer.getEnemyList().get(i);
				Line2D enemyLines[] = thisEnemy.getTankBounds();
				//Lines of tank hull
				Line2D enemy_top = enemyLines[0];
				Line2D enemy_bottom = enemyLines[1];
				Line2D enemy_left = enemyLines[2];
				Line2D enemy_right = enemyLines[3];

				if (top.intersectsLine(enemy_top) || right.intersectsLine(enemy_top) || left.intersectsLine(enemy_top) || bottom.intersectsLine(enemy_top) ||
						top.intersectsLine(enemy_right) || right.intersectsLine(enemy_right) || left.intersectsLine(enemy_right) || bottom.intersectsLine(enemy_right) ||
						top.intersectsLine(enemy_left) || right.intersectsLine(enemy_left) || left.intersectsLine(enemy_left) || bottom.intersectsLine(enemy_left) ||
						top.intersectsLine(enemy_bottom) || right.intersectsLine(enemy_bottom) || left.intersectsLine(enemy_bottom) || bottom.intersectsLine(enemy_bottom)) {
					checkPreviousMove();
					tankIsRammed(levelContainer.getEnemyList().get(i).getRammingDamage());
				}
			}
		}
		else
		{
			if(enemyCollision) {
				for (int i = 0; i < levelContainer.getEnemyList().size(); i++) {
					Opponent thisEnemy = levelContainer.getEnemyList().get(i);

					if (!thisEnemy.getID().equals(tankID)) {

						Line2D enemyLines[] = thisEnemy.getTankBounds();
						//Lines of tank hull
						Line2D enemy_top = enemyLines[0];
						Line2D enemy_bottom = enemyLines[1];
						Line2D enemy_left = enemyLines[2];
						Line2D enemy_right = enemyLines[3];

						if (top.intersectsLine(enemy_top) || right.intersectsLine(enemy_top) || left.intersectsLine(enemy_top) || bottom.intersectsLine(enemy_top) ||
								top.intersectsLine(enemy_right) || right.intersectsLine(enemy_right) || left.intersectsLine(enemy_right) || bottom.intersectsLine(enemy_right) ||
								top.intersectsLine(enemy_left) || right.intersectsLine(enemy_left) || left.intersectsLine(enemy_left) || bottom.intersectsLine(enemy_left) ||
								top.intersectsLine(enemy_bottom) || right.intersectsLine(enemy_bottom) || left.intersectsLine(enemy_bottom) || bottom.intersectsLine(enemy_bottom)) {
							checkPreviousMove();
							tankIsRammed(levelContainer.getEnemyList().get(i).getRammingDamage());
						}
					}
				}
			}
			for (int i = 0; i < levelContainer.getPlayerList().size(); i++) {
				Line2D playerLines[] = levelContainer.getPlayerList().get(i).getTankBounds();

				Line2D player_top = playerLines[0];
				Line2D player_bottom = playerLines[1];
				Line2D player_left = playerLines[2];
				Line2D player_right = playerLines[3];

				if (top.intersectsLine(player_top) || right.intersectsLine(player_top) || left.intersectsLine(player_top) || bottom.intersectsLine(player_top) ||
						top.intersectsLine(player_right) || right.intersectsLine(player_right) || left.intersectsLine(player_right) || bottom.intersectsLine(player_right) ||
						top.intersectsLine(player_left) || right.intersectsLine(player_left) || left.intersectsLine(player_left) || bottom.intersectsLine(player_left) ||
						top.intersectsLine(player_bottom) || right.intersectsLine(player_bottom) || left.intersectsLine(player_bottom) || bottom.intersectsLine(player_bottom)) {
					checkPreviousMove();
					tankIsRammed(levelContainer.getPlayerList().get(i).getRammingDamage());
				}
			}
		}
	}

	/**
	 * Function that handles tank physics when a tank collides with another tank.
	 * Called whenever a collision occurs. Works essentially as Newton's Third Law.
	 */
	private void checkPreviousMove()
	{
		//Only forward
		if(previousMoveAlt == 1 && previousTurnAlt >= 0)
		{
			moveBackward();
			previousMoveAlt = 1;
		}
		//Only backward
		else if(previousMoveAlt == 2 && previousTurnAlt >= 0)
		{
			moveForward();
			previousMoveAlt = 2;
		}
		//Only turnLeft
		else if(previousMoveAlt == 0 && previousTurnAlt == 1)
		{
			turnRight();
			previousTurn = 1;
		}
		//Only turnRight
		else if(previousMoveAlt == 0 && previousTurnAlt == 2)
		{
			turnLeft();
			previousTurnAlt = 2;
		}
		else if(previousMoveAlt > 0 && previousTurnAlt == 1)
		{
			turnRight();
			previousTurnAlt = 1;
			if(previousMoveAlt == 1){
				moveBackward();
				previousMoveAlt = 1;
			}
			else if (previousMoveAlt == 2){
				moveForward();
				previousMoveAlt = 2;
			}
		}
		else if(previousMoveAlt > 0 && previousTurnAlt == 2)
		{
			hull.turnLeft();
			previousTurnAlt = 2;
			if(previousMoveAlt == 1){
				moveBackward();
				previousMoveAlt = 1;
			}
			else if (previousMoveAlt == 2){
				moveForward();
				previousMoveAlt = 2;
			}
		}
	}

	/**
	 * Updates tank. Repeatedly called.
	 * @return loadNextLevel - boolean showing whether should the next level be called
	 */
	public boolean update()
	{
		tankToTankCollisionHandling();
		previousMoveAlt = 0;
		previousTurnAlt = 0;
		hull.update();
		if (isPlayer()) turret.update();
		
		if(isPlayerControlled) {
			listener.handleInput();
			turret.setPlayerTurretDirection();
		}

		return loadNextLevel;
	}

	/**
	 * Method for setting the configuration for this Tank
	 * @param config_name name of config
	 */
	public void config(String config_name)
	{
		try{
			Method m = TankConfigs.class.getDeclaredMethod(config_name, new Class[]{Tank.class});
			try{
				m.invoke(tankConfigs, new Object[]{this});
			}catch(IllegalAccessException ie)
				{
				System.out.println("Upgrade method invoke access denied");
			}
			catch(InvocationTargetException ite)
			{
				System.out.println("Upgrade method invoke target exception");
			}
		}catch(NoSuchMethodException ne) {
			System.out.println("No such method exists!");
		}
	}

	/**
	 * Mutator method for reseting the flag which indicates whether a new level needs to be loaded or not
	 */
	public void resetLoadFlag() { this.loadNextLevel = false; }

	/**
	 * Method which 'damages' the health of this instance of Tank when it is hit by a shell
	 * @param damage the damage amount dealt by shell
	 */
	public void tankIsHit(int damage)
	{
		health = health - damage;
	}

	/**
	 * Method for damaging the health of this instance of Tank when it is rammed by another Tank
	 * @param damage damage dealth by other Tank
	 */
	public void tankIsRammed(int damage)
	{
		if (armour)
			health = health - damage/2;
		else
			health = health - damage;
	}

	/**
	 * Accessor method to determine if this instance of Tank is an Opponent
	 * @return true if yes, false if no
	 */
	public boolean isOpponent() { return !isPlayerControlled; }

	/**
	 *  Accessor method to access movement speed of this instance of Tank
	 * @return movement speed
	 */
	public float getMovementSpeed()
	{
		return hull.getMovementSpeed();
	}

	/**
	 *  Accessor method to access the turning distance of this instance of Tank
	 * @return hull turning distance
	 */
	public float getTurningDistance()
	{
		return hull.getTurningDistance();
	}

	/**
	 *  Accessor method to access turret turning distance of this instance of Tank
	 * @return turret turning distance
	 */
	public float getTurretTurningDistance()
	{
		return turret.getTurningDistance();
	}

	/**
	 * Mutator method for increasing the money owned by this Tank
	 * @param i amount to increase money by
	 */
	public void increaseMoney(int i)
	{
		this.money += i;
	}

	/**
	 * Mutator method for decreasing the money owned by this Tank
	 * @param i amount to decrease money by
	 */
	public void decreaseMoney(int i)
	{
		this.money -= i;
	}

	/**
	 * Accessor method for getting the amount of money owned by this Tank
	 * @return amount of money owned by this Tank
	 */
	public int getMoney()
	{
		return this.money;
	}

	/**
	 * Method for calculating the score of this Tank
	 * @return score
	 */
	public int getScore()
	{
		int score = this.money;
		switch (window.getDifficulty())
		{
			case NORMAL:
				score = (int) Math.floor(score * 1.25);
				break;
			case HARD:
				score = (int) Math.floor(score * 1.5);
		}
		return score;
	}

	/**
	 * Mutator method for increasing the health of this Tank
	 * @param i amount to increase health by
	 */
	public void increaseHealth(int i)
	{
		this.health = this.health + i;
	}

	/**
	 * Mutator method for increasing the max health of this Tank
	 * @param i amount to increase max health by
	 */
	public void increaseMaxHealth(int i)
	{
		this.startingHealth = this.startingHealth + i;
	}

	/**
	 * Accessor method for getting shell firing delay for this Tank
	 * @return shell firing delay
	 */
	public int getFireDelay()
	{
		return delayBetweenShell;
	}

	/**
	 * Method to retrieve data about this Tank once it has been destroyed
	 * @return array of data about Tank in following format {hull x pos, hull y pos, turret x pos, turret y pos, width size multiplier, height size multiplier, hull direction, turret direction}
	 */
	public float[] getDeathData()
	{
		float[] data = new float[8];
		data[0] = hull.getxPos();
		data[1] = hull.getyPos();
		data[2] = turret.getxPos();
		data[3] = turret.getyPos();
		data[4] = getSizeMult_w();
		data[5] = getSizeMult_h();
		data[6] = hull.getObjectDirection();
		data[7] = turret.getDirection();

		return data;
	}


	public float getXPos() { return turret.getxPos(); }

	public float getYPos() { return turret.getyPos(); }

	public float getTurretDir() { return turret.getDirection(); }

	public boolean collision() { return collisionLastMove; }

	public void resetCollision() { collisionLastMove = false; }

	public boolean isAlive() { return (health <= 0 ? false : true); }

	public boolean isPlayer() { return isPlayerControlled; }

	public String getID() { return tankID; }

	public int getDamagePerShell() { return damagePerShell; }

	public int getHealth() { return health; }

	public int getRammingDamage() { return rammingDamage; }

	public float getSizeMult_w() { return sizeMult_w; }

	public float getSizeMult_h() { return sizeMult_h; }

	public TankHull getHull() { return hull; }

	public TankTurret getTurret() { return turret; }

	public int getStartingHealth() { return startingHealth; }

	public float getTankFiringVolume() { return tankFiringVolume; }

	public void setTankFiringVolume(float tankFiringVolume) { this.tankFiringVolume = tankFiringVolume; }

	public float getTankMovingVolume() { return tankMovingVolume; }

	public void setTankMovingVolume(float tankMovingVolume) { this.tankMovingVolume = tankMovingVolume; }

	public void setArmour(boolean b)
	{
		this.armour = b;
	}

	public String getName()
	{
		return name;
	}
}
