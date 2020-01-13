package Tanks.ObjectComponents;

import Tanks.Window.Window;
import java.util.ArrayList;


/**
 *This class is used to randomly generate a map given particular parameters
 */
public class MapGenerator
{
	private ArrayList<MapObject> objectList = new ArrayList<>();
	private Window window;
	
	
	public MapGenerator(Window w)
	{
		this.window = w;
		//generateMap();
	}
	
	
	/**
	 * This method adds a given object to the arrayList and therefore the map
	 * @param x the x position of the object to be added in pixels (could use a grid system and this would be the x grid num)
	 * @param y the y position of the object to be added in pixels
	 * @param width the width of the object to be added in pixels
	 * @param height the height of the object to be added in pixels
	 * @param texture the texture of the object (Textures.NAME)
	 */
	private void addObject(float x, float y, float width, float height, String texture)
	{
		objectList.add(new MapObject(this.window, x, y, width, height, texture)); //window, x, y, width, height, texture
	}
	
	
	/**
	 * This is the method that is called from the Map class to generate a new map - could be called in the constructor, but this can make it more flexible
	 */
	public void generateMap(int xSize, int ySize, float xOffset, float yOffset, float bSize)
	{

    }
	
	
	/**
	 * Returns objectList - an array list of MapObjects which make up a level
	 * @return objectList
	 */
	public ArrayList<MapObject> getObjectList() { return objectList; }
	
	/**
	 * Returns a given mapObject based on a given index
	 * @param index the index of the object you want in objectList
	 * @return objectList
	 */
	public MapObject getObject(int index) { return objectList.get(index); }
	
	/**
	 * Returns the size of objectList (i.e. the number of MapObjects in the objectList)
	 * @return the size of objectList
	 */
	public int getObjectListSize() { return objectList.size(); } //The object list size is for some reason twice the size it should be
}