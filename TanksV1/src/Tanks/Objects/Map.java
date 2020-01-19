package Tanks.Objects;

import java.util.ArrayList;

import Tanks.ObjectComponents.MapExit;
import Tanks.ObjectComponents.MapObject;
import Tanks.ObjectComponents.Textures;
import Tanks.Window.Window;

public class Map
{
	private Window window;
	private ArrayList<MapObject> objectList = new ArrayList<>();
	private ArrayList<MapExit> exitList = new ArrayList<>();

	private int numOfEnemies = 1;


	public Map(Window window)
	{
		this.window = window;
		createMap();

		//Purely for testing
		enemyKilled();
	}


	private void createMap()
	{
		exitList.add(new MapExit(window, 500, 500, 200, 25, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED));
	}

	public void enemyKilled()
	{
		numOfEnemies--;

		if (numOfEnemies <= 0) //If there are no more enemies unlock the exits
		{
			for (int i = 0; i < exitList.size(); i++)
			{
				exitList.get(i).setLockedStatus(false);
				exitList.get(i).displayUnlocked();
			}
		}
	}


	/**
	 * This method adds all of the mapObjects created within the MapGenerator class to objectList
	 * It must be done this way as setting objectList = mapGenerator.getObjectList() doesn't work - the shells do not ricochet
	 */
	private void setObjectList()
	{
		objectList.add(new MapObject(window, 500, 500, 100, 100, Textures.BRICKBLOCK));
		objectList.add(new MapObject(window, 300, 500, 100, 100, Textures.BRICKBLOCK));
	}


	public void update()
	{
		//Objects
		for(int i = 0; i < objectList.size(); i++)
		{
			objectList.get(i).update();
		}
		//Exits
		for(int i = 0; i < exitList.size(); i++)
		{
			exitList.get(i).update();
		}
	}


	public void addMapObject(float x, float y, float width, float height, String texture)
	{
		objectList.add(new MapObject(window, x, y, width, height, texture));
	}


	//This is called in the tank class to check if the tank is colliding with an object in the map
	public ArrayList<MapObject> getObjectsInMap() { return objectList; }

	public ArrayList<MapExit> getMapExits() { return exitList; }
}
