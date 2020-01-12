package Tanks.ObjectComponents;

import Tanks.Objects.Map;
import Tanks.Objects.Tank;
import Tanks.Window.Window;

public class TankShell extends RotatingObject{
	
	private float shellSpeed;
	private int ricochetNum = 0;
	protected Window window;
	private Map map;
	private boolean active = true;
	
	public TankShell(TankTurret connectedTankTurret, String texturePath, Window window, float shellSpeed, Map map) 
	{
		boolean hasFired = false;
		this.window = window;
		this.shellSpeed = shellSpeed;
		this.map = map;
		setObjectTexture(texturePath);
		//quick fix to stop tank damaging itself when shell is fired (will fix properly later):
		setCenterLocation(connectedTankTurret.getxPos() + (float)(80 * Math.sin(Math.toRadians(connectedTankTurret.objectDirection))), connectedTankTurret.getyPos() - (float)(80 * Math.cos(Math.toRadians(connectedTankTurret.objectDirection))));
		rotateObject(connectedTankTurret.objectDirection);
	}
	
	public void launchedForward()
	{
		xPos = xPos + (float)(shellSpeed * Math.sin(Math.toRadians(this.objectDirection)));
		yPos = yPos - (float)(shellSpeed * Math.cos(Math.toRadians(this.objectDirection)));
		setCenterLocation(xPos, yPos);
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
	
	/**
	 * Returns 1-4 depending on where the object is impacted against
	 * 1 = bottom
	 * 2 = top
	 * 3 = right
	 * 4 = left
	 * @return
	 */
	public void collisionHandling()
	{
		float tip_xPos = getxPos() + getWidth()/2;
		float tip_yPos = getyPos() - getHeight()/2;

		float margin = getHeight();
		for(int i = 0; i < map.getObjectsInMap().size(); i++) {
			MapObject thisObject = map.getObjectsInMap().get(i);

			//Get shell tip position


			if(tip_xPos >= thisObject.getLeftBounds() && tip_xPos <= thisObject.getRightBounds()) {
				if(tip_yPos >= thisObject.getBottomBounds() - margin && tip_yPos <= thisObject.getBottomBounds()) {
					rotateObject(180 - objectDirection);
					ricochetNum++;
				}
				else if (tip_yPos >= thisObject.getTopBounds() - margin && tip_yPos <= thisObject.getTopBounds() + margin) {
					rotateObject(180 - objectDirection);
					ricochetNum++;
				}
			}

			else if(tip_yPos >= thisObject.getTopBounds() && tip_yPos <= thisObject.getBottomBounds()) {
				if(tip_xPos >= thisObject.getRightBounds() - margin && tip_xPos <= thisObject.getRightBounds() + margin) {
					rotateObject(0 - objectDirection);
					ricochetNum++;
				}
				else if(tip_xPos >= thisObject.getLeftBounds() - margin/2 && tip_xPos <= thisObject.getLeftBounds() + margin) {
					rotateObject(0 - objectDirection);
					ricochetNum++;
				}
			}

		}
		for (Tank t : map.getTanks())
		{

			if(tip_xPos >= t.getLeftBounds() && tip_xPos <= t.getRightBounds()) {
				if(tip_yPos >= t.getBottomBounds() - margin && tip_yPos <= t.getBottomBounds()) {
					active = false;
					t.getHit();
				}
				else if (tip_yPos >= t.getTopBounds() - margin && tip_yPos <= t.getTopBounds() + margin) {
					active = false;
					t.getHit();
				}
			}

			else if(tip_yPos >= t.getTopBounds() && tip_yPos <= t.getBottomBounds()) {
				if(tip_xPos >= t.getRightBounds() - margin && tip_xPos <= t.getRightBounds() + margin) {
					active = false;
					t.getHit();
				}
				else if(tip_xPos >= t.getLeftBounds() - margin/2 && tip_xPos <= t.getLeftBounds() + margin) {
					active = false;
					t.getHit();
				}
			}
		}
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
