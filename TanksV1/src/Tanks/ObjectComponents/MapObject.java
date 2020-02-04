package Tanks.ObjectComponents;

import Tanks.Window.Window;

public class MapObject extends RotatingObject
{
	protected Window window;

	private boolean isExit = false;

	private String unlockedTexture;
	private boolean isLocked = true; //This only matters if the object is an exit


	public MapObject(Window window, float x, float y, float width, float height, String objectTexture)
	{
		this.window = window;
		setObjectTexture(objectTexture);
		setCenterLocation(x, y);
		setSize(width, height);
	}


	public void update()
	{
		draw(window);
	}


	//All of the following functions can be ignored unless mapObject is to be used as an exit

	/**
	 * The following is used to give the mapObject all of the necessary data to be a mapExit
	 * @param unlockedTexture the texture to be displayed when the map is unlocked
	 */
	public void makeExit(String unlockedTexture)
	{
		this.unlockedTexture = unlockedTexture;
		this.isExit = true;
	}

	public boolean getLockedStatus() { return this.isLocked; }

	public boolean isExit() { return this.isExit; }

	public void unlock()
	{
		setObjectTexture(this.unlockedTexture);
		this.isLocked = false;
	}
}
