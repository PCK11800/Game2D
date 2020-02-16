package Tanks.ObjectComponents;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import Tanks.Objects.LevelContainer;
import Tanks.Objects.Opponent;
import org.jsfml.system.Clock;

import Tanks.Objects.Map;
import Tanks.Objects.Tank;
import Tanks.Window.Window;

public class TankShell extends RotatingObject{
	
	private float shellSpeed;
	private int damage;
	private int ricochetNum = 0;
	private int maxRicochetNum;
	protected Window window;
	private LevelContainer levelContainer;
	private Map map;
	private Clock movementClock = new Clock();
	private Clock ricochetClock = new Clock();
	private Clock shotClock;
	private boolean active = true;
	private String id;
	
	public TankShell(TankTurret connectedTankTurret, String texturePath, Window window, float shellSpeed, LevelContainer levelContainer, int maxRicochetNum, int damage)
	{
		boolean hasFired = false;
		this.window = window;
		this.shellSpeed = shellSpeed;
		this.levelContainer = levelContainer;
		this.map = levelContainer.getMap();
		this.maxRicochetNum = maxRicochetNum;
		this.damage = damage;
		setObjectTexture(texturePath);
		setCenterLocation(connectedTankTurret.getxPos() + (float)(connectedTankTurret.getWidth() * Math.sin(Math.toRadians(connectedTankTurret.objectDirection))), connectedTankTurret.getyPos() - (float)(connectedTankTurret.getWidth() * Math.cos(Math.toRadians(connectedTankTurret.objectDirection))));
		rotateObject(connectedTankTurret.objectDirection);
		shotClock = new Clock();
		id = UUID.randomUUID().toString();
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
			Line2D shellBounds[] = getShellBounds();
			Line2D top = shellBounds[0];
			Line2D bottom = shellBounds[1];
			Line2D left = shellBounds[2];
			Line2D right = shellBounds[3];

			Line2D objectBounds[] = map.getObjectsInMap().get(i).getObjectBounds();

			Line2D map_top = objectBounds[0];
			Line2D map_bottom = objectBounds[1];
			Line2D map_left = objectBounds[2];
			Line2D map_right = objectBounds[3];

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
		ArrayList<Opponent> enemyList = levelContainer.getEnemyList();
		ArrayList<Tank> playerList = levelContainer.getPlayerList();
		if(playerList.size() != 0)
		{
			Line2D playerBounds[] = playerList.get(0).getTankBounds(); //Single player game, so only one tank
			Line2D player_top = playerBounds[0];
			Line2D player_bottom = playerBounds[1];
			Line2D player_left = playerBounds[2];
			Line2D player_right = playerBounds[3];

			Line2D shellBounds[] = getShellBounds();
			Line2D top = shellBounds[0];
			Line2D bottom = shellBounds[1];
			Line2D left = shellBounds[2];
			Line2D right = shellBounds[3];

			for(int i = 0; i < enemyList.size(); i++)
			{
				Line2D enemyBounds[] = enemyList.get(i).getTankBounds();
				Line2D enemy_top = enemyBounds[0];
				Line2D enemy_bottom = enemyBounds[1];
				Line2D enemy_left = enemyBounds[2];
				Line2D enemy_right = enemyBounds[3];

				if (top.intersectsLine(enemy_top) || right.intersectsLine(enemy_top) || left.intersectsLine(enemy_top) || bottom.intersectsLine(enemy_top) ||
						top.intersectsLine(enemy_right) || right.intersectsLine(enemy_right) || left.intersectsLine(enemy_right) || bottom.intersectsLine(enemy_right) ||
						top.intersectsLine(enemy_left) || right.intersectsLine(enemy_left) || left.intersectsLine(enemy_left) || bottom.intersectsLine(enemy_left) ||
						top.intersectsLine(enemy_bottom) || right.intersectsLine(enemy_bottom) || left.intersectsLine(enemy_bottom) || bottom.intersectsLine(enemy_bottom))
				{
				    enemyList.get(i).tankIsHit(damage);
				    active = false;
				}
			}

			if (top.intersectsLine(player_top) || right.intersectsLine(player_top) || left.intersectsLine(player_top) || bottom.intersectsLine(player_top) ||
					top.intersectsLine(player_right) || right.intersectsLine(player_right) || left.intersectsLine(player_right) || bottom.intersectsLine(player_right) ||
					top.intersectsLine(player_left) || right.intersectsLine(player_left) || left.intersectsLine(player_left) || bottom.intersectsLine(player_left) ||
					top.intersectsLine(player_bottom) || right.intersectsLine(player_bottom) || left.intersectsLine(player_bottom) || bottom.intersectsLine(player_bottom))
			{
			    playerList.get(0).tankIsHit(damage);
			    active = false;
			}
		}
	}

	private void shellToShellCollisionHandling()
	{
		for(int i = 0; i < levelContainer.getShellList().size(); i++)
		{
			TankShell incomingShell = levelContainer.getShellList().get(i);
			if(!incomingShell.getID().equals(getID()))
			{
				Line2D shellBounds[] = getShellBounds();
				Line2D top = shellBounds[0];
				Line2D bottom = shellBounds[1];
				Line2D left = shellBounds[2];
				Line2D right = shellBounds[3];

				Line2D incomingShellBounds[] = incomingShell.getShellBounds();
				Line2D enemy_top = incomingShellBounds[0];
				Line2D enemy_bottom = incomingShellBounds[1];
				Line2D enemy_left = incomingShellBounds[2];
				Line2D enemy_right = incomingShellBounds[3];

				if (top.intersectsLine(enemy_top) || right.intersectsLine(enemy_top) || left.intersectsLine(enemy_top) || bottom.intersectsLine(enemy_top) ||
						top.intersectsLine(enemy_right) || right.intersectsLine(enemy_right) || left.intersectsLine(enemy_right) || bottom.intersectsLine(enemy_right) ||
						top.intersectsLine(enemy_left) || right.intersectsLine(enemy_left) || left.intersectsLine(enemy_left) || bottom.intersectsLine(enemy_left) ||
						top.intersectsLine(enemy_bottom) || right.intersectsLine(enemy_bottom) || left.intersectsLine(enemy_bottom) || bottom.intersectsLine(enemy_bottom))
				{
					active = false;
					incomingShell.active = false;
				}
			}
		}
	}

	public Line2D[] getShellBounds()
	{
		return getObjectBounds();
	}

	private void ricochetHandling()
	{
		if(ricochetNum >= maxRicochetNum)
		{
			active = false;
		}
	}
	
	public int getRicochetNum() {
		return ricochetNum;
	}
	
	public void update()
	{
		draw(window);
		shellToShellCollisionHandling();
		ricochetHandling();
		if(shotClock.getElapsedTime().asMilliseconds() > Tank.timePerFrame * 3) { hitPlayer(); }
		collisionHandling();
		checkOutOfBounds();
		launchedForward();
	}

	public boolean isActive() { return active; }

	public String getID() { return id; }
}
