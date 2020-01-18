package Tanks.Objects;

import java.util.ArrayList;
import java.util.stream.Collectors;

import Tanks.ObjectComponents.MapObject;
import Tanks.ObjectComponents.Textures;
import Tanks.Window.Window;

public class Map{

	private Window window;
	private ArrayList<MapObject> objectList = new ArrayList<>();
	private ArrayList<Tank> tankList = new ArrayList<>();
	
	public Map(Window window) 
	{
		this.window = window;
		createMap();
	}
	
	private void createMap() {
		objectList.add(new MapObject(window, 500, 500, 100, 100, Textures.BRICKBLOCK));
		objectList.add(new MapObject(window, 680, 600, 100, 100, Textures.BRICKBLOCK));
	}
	
	public void update() {
		for(int i = 0; i < objectList.size(); i++) 
		{
			objectList.get(i).update();
		}
		updateTanks();
	}

	public void updateTanks() {
		tankList = new ArrayList<Tank>(
				tankList.stream()
						.filter(t -> t.isAlive())
						.collect(Collectors.toList()));

		for (Tank tank : tankList)
		{
			tank.update();
		}
	}
	public void addTank(Tank t) { tankList.add(t); }

	public ArrayList<Tank> getTanks() { return tankList; }

	public ArrayList<MapObject> getObjectsInMap()
	{
		return objectList;
	}
	
}
