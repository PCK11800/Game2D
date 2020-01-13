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

	private int numOfEnemies = 0;

	//Testing
	private int xBlocks = 20;
	private int yBlocks = 10;
	private float xOffset = 50;
	private float yOffset = 0;
	private float blockSize = 75;
	
	public Map(Window window) 
	{
		this.window = window;
		createMap();

		//Purely for testing
		//enemyKilled();
	}
	
	
	private void createMap() 
	{
		exitList.add(new MapExit(window, 500, 500, 200, 100, Textures.BRICKBLOCK));
	}
	
	public void enemyKilled()
	{
		numOfEnemies--;

		if (numOfEnemies <= 0) //If there are no more enemies unlock the exits
		{
			for (int i = 0; i < exitList.size(); i++)
			{
				exitList.get(i).setLockedStatus(false);
			}
		}
	}
	
	public void update() 
	{
		//Objects
		for(int i = 0; i < objectList.size(); i++) 
		{
			objectList.get(i).update();
		}

		for(int i = 0; i < exitList.size(); i++)
		{
			exitList.get(i).update();

		}
	}


	//This is called in the tank class to check if the tank is colliding with an object in the map
	public ArrayList<MapObject> getObjectsInMap() { return objectList; }

	public ArrayList<MapExit> getMapExits() { return exitList; }
}
