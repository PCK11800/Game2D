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

	private float sizeMult_w, sizeMult_h;
	private ArrayList<TankShell> shellList = new ArrayList<>();

	private int money = 0;

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

	GameSound tankFiring;
	GameSound tankMoving;

	public Tank()
	{
		this.hull = new TankHull();
		this.turret = new TankTurret();
		turret.setConnectedTankHull(hull);
		tankID = UUID.randomUUID().toString();
	}

	public void setHullTexture(String texturePath)
	{
		hull.setObjectTexture(texturePath);
	}

	public void setHealth(int health) {
		this.health = health;
		this.startingHealth = health;
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

	public void setLevelContainer(LevelContainer levelContainer)
	{
		this.levelContainer = levelContainer;
		this.map = levelContainer.getMap();
	}

	public void setTankLocation(float xPos, float yPos)
	{
		hull.setCenterLocation(xPos, yPos);
		turret.setTurretLocation();
	}

	public void setDamagePerShell(int damagePerShell) {this.damagePerShell = damagePerShell;}

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
		sizeMult_w = width;
		sizeMult_h = height;
	}

	public void setFiringSound(String firingSound, float volume)
	{
		tankFiring = new GameSound(firingSound);
		tankFiring.setVolume(volume);
	}

	public void setMovingSound(String moveSound, float volume)
	{
		tankMoving = new GameSound(moveSound);
		tankMoving.setVolume(volume);
	}

	public void setShellSpeed(float shellSpeed)
	{
		this.shellSpeed = shellSpeed;
	}

	public void setFireDelay(int rateOfFire)
	{
		this.delayBetweenShell = rateOfFire;
	}

	public void setMoney(int money) { this.money = money; }


	private TankShell createShell()
	{
		TankShell shell = new TankShell(turret, shellTexturePath, window, shellSpeed, levelContainer, shellRicochetNumber, damagePerShell);
		shell.setSize((float) (turret.getWidth()/10), turret.getHeight()/5);
		lastShellFired = System.nanoTime();
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

	public void enableEnemyCollision()
	{
		enemyCollision = true;
	}

	public void disableEnemyCollision()
	{
		enemyCollision = false;
	}

	public void setRammingDamage(int rammingDamage)
	{
		this.rammingDamage = rammingDamage;
	}


	/**
	 * Shoot a shell
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

	private void collisionHandling()
	{
		//Previous move: 1 = forward, 2 = backward
		//Previous turn: 1 = left, 2 = right

		int fineTuneMove = 2;
		int fineTuneTurn = 8;
		float fineTuneWidthPadding = (float) 0.5;
		float fineTuneHeightPadding = (float) 0.5;
		TankHull ghostHull = new TankHull();
		ghostHull.setObjectTexture(Textures.TANKHULL_DEAD);
		ghostHull.setSize(getSizeMult_w() * (53 + fineTuneWidthPadding), getSizeMult_h() * (75 + fineTuneHeightPadding));
		ghostHull.setLocation(getXPos(), getYPos());
		ghostHull.rotateObject(hull.getObjectDirection());
		ghostHull.setMovementSpeed(getMovementSpeed());

		if(previousMove == 1 && previousTurn >= 0){
			for(int i = 0; i < fineTuneMove; i++){
				ghostHull.moveForward();
			}
		}
		else if(previousMove == 2 && previousTurn >= 0){
			for(int i = 0; i < fineTuneMove; i++){
				ghostHull.moveBackward();
			}
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

	private void checkPreviousMove()
	{
		//Only forward
		if(previousMoveAlt == 1 && previousTurnAlt >= 0)
		{
			hull.moveBackward();
			previousMoveAlt = 1;
		}
		//Only backward
		else if(previousMoveAlt == 2 && previousTurnAlt >= 0)
		{
			hull.moveForward();
			previousMoveAlt = 2;
		}
		//Only turnLeft
		else if(previousMoveAlt == 0 && previousTurnAlt == 1)
		{
			hull.turnRight();
			previousTurn = 1;
		}
		//Only turnRight
		else if(previousMoveAlt == 0 && previousTurnAlt == 2)
		{
			hull.turnLeft();
			previousTurnAlt = 2;
		}
		else if(previousMoveAlt > 0 && previousTurnAlt == 1)
		{
			hull.turnRight();
			previousTurnAlt = 1;
			if(previousMoveAlt == 1){
				hull.moveBackward();
				previousMoveAlt = 1;
			}
			else if (previousMoveAlt == 2){
				hull.moveForward();
				previousMoveAlt = 2;
			}
		}
		else if(previousMoveAlt > 0 && previousTurnAlt == 2)
		{
			hull.turnLeft();
			previousTurnAlt = 2;
			if(previousMoveAlt == 1){
				hull.moveBackward();
				previousMoveAlt = 1;
			}
			else if (previousMoveAlt == 2){
				hull.moveForward();
				previousMoveAlt = 2;
			}
		}
	}

	//Call this in game loop
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


	public void resetLoadFlag() { this.loadNextLevel = false; }

	public void tankIsHit(int damage)
	{
		health = health - damage;
	}

	public void tankIsRammed(int damage)
	{
		health = health - damage;
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

	public void increaseMoney(int i)
	{
		this.money += i;
	}

	public void decreaseMoney(int i)
	{
		this.money -= i;
	}

	public int getMoney()
	{
		return this.money;
	}

	public void increaseHealth(int i)
	{
		this.health = this.health + i;
	}

	public void increaseMaxHealth(int i)
	{
		this.startingHealth = this.startingHealth + i;
	}

	public int getFireDelay()
	{
		return delayBetweenShell;
	}

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

	public int[] getCurrentData()
	{
		int[] data = new int[8];
		data[0] = health;

		return data;
	}

	public float getXPos() { return turret.getxPos(); }

	public float getYPos() { return turret.getyPos(); }

	public float getTurretDir() { return turret.getDirection(); }

	public float getLeftBounds() { return hull.getLeftBounds(); }

	public float getRightBounds() { return hull.getRightBounds(); }

	public float getTopBounds() { return hull.getTopBounds(); }

	public float getBottomBounds() { return hull.getBottomBounds(); }

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
}
