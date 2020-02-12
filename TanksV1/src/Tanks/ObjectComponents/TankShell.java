package Tanks.ObjectComponents;

import java.awt.geom.Line2D;
import java.util.ArrayList;

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
	private boolean active = true;
	
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
		ArrayList<Opponent> enemyList = levelContainer.getEnemyList();
		ArrayList<Tank> playerList = levelContainer.getPlayerList();
		if(playerList.size() != 0)
		{
			Line2D playerBounds[] = playerList.get(0).getTankBounds(); //Single player game, so only one tank
			Line2D player_top = playerBounds[0];
			Line2D player_bottom = playerBounds[1];
			Line2D player_left = playerBounds[2];
			Line2D player_right = playerBounds[3];

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
		ricochetHandling();
		hitPlayer();
		collisionHandling();
		checkOutOfBounds();
		launchedForward();
	}

	public boolean isActive() { return active; }
}
