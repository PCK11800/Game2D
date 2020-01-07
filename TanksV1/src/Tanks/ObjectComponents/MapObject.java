package Tanks.ObjectComponents;

import Tanks.Window.Window;

public class MapObject extends RotatingObject{
	
	protected Window window;
	
	public MapObject(Window window, float x, float y, float width, float height, String objectTexture) 
	{
		this.window = window;
		setObjectTexture(objectTexture);
		setLocation(x, y);
		setSize(width, height);
	}
	
	public void update()
	{
		draw(window);
	}
}
