package Tanks.Objects;

import java.util.ArrayList;

import Tanks.ObjectComponents.MapObject;
import Tanks.Window.Window;

public class Map
{
	private Window window;
	private ArrayList<MapObject> objectList = new ArrayList<>();

	public Map(Window window)
	{
		this.window = window;
	}


	public void unlockExits()
	{
		for (int i = 0; i < objectList.size(); i++)
		{
			if (objectList.get(i).isExit() == true)
			{
				objectList.get(i).unlock();
			}
		}
	}


	public void update()
	{
		for(int i = 0; i < objectList.size(); i++)
		{
			objectList.get(i).update();
		}
	}


	//This is called in the tank class to check if the tank is colliding with an object in the map
	public ArrayList<MapObject> getObjectsInMap() { return objectList; }
	public float getWidth() { return window.getWidth(); }

	public float getHeight() { return window.getHeight(); }

}
