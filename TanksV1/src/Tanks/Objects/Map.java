package Tanks.Objects;

import java.util.ArrayList;

import Tanks.ObjectComponents.MapObject;
import Tanks.ObjectComponents.Textures;
import Tanks.Window.Window;

public class Map{

	private Window window;
	private ArrayList<MapObject> objectList = new ArrayList<>();
	
	public Map(Window window) 
	{
		this.window = window;
		createMap();
	}
	
	private void createMap() 
	{
		objectList.add(new MapObject(window, 500, 500, 100, 100, Textures.BRICKBLOCK));
		objectList.add(new MapObject(window, 500, 600, 100, 100, Textures.BRICKBLOCK));
		objectList.add(new MapObject(window, 600, 500, 100, 100, Textures.BRICKBLOCK));
		objectList.add(new MapObject(window, 600, 600, 100, 100, Textures.BRICKBLOCK));
	}
	
	public void update() {
		for(int i = 0; i < objectList.size(); i++) 
		{
			objectList.get(i).update();
		}
	}
	
	public ArrayList<MapObject> getObjectsInMap()
	{
		return objectList;
	}
	
}
