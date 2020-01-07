package Tanks.ObjectComponents;

import Tanks.Objects.Map;
import Tanks.Window.Window;

public class TankShell extends RotatingObject{
	
	private float shellSpeed;
	private int ricochetNum = 0;
	protected Window window;
	private Map map;
	
	public TankShell(TankTurret connectedTankTurret, String texturePath, Window window, float shellSpeed, Map map) 
	{
		this.window = window;
		this.shellSpeed = shellSpeed;
		this.map = map;
		setObjectTexture(texturePath);
		setCenterLocation(connectedTankTurret.getxPos(), connectedTankTurret.getyPos());
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
		for(int i = 0; i < map.getObjectsInMap().size(); i++) {
			MapObject thisObject = map.getObjectsInMap().get(i);
			
			//Get shell tip position
			float tip_xPos = getxPos() + getWidth()/2;
			float tip_yPos = getyPos() - getHeight()/2;
			
			float margin = getHeight();
			
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
}
