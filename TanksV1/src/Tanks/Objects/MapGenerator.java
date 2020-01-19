package Tanks.Objects;

import Tanks.ObjectComponents.MapObject;
import Tanks.Window.Window;
import Tanks.ObjectComponents.Textures;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


/**
 * This class is used to randomly generate a map given particular parameters
 * The method used to generate the map is a modified recursive division maze algorithm.
 * The algorithms implementation is derived from the algorithm presented here: https://weblog.jamisbuck.org/2015/1/15/better-recursive-division-algorithm.html
 */
public class MapGenerator
{
	private enum Direction
	{
		//Creates instances of the enum - north, south, east and west, with the specified bits
		N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);

		private final int bit;
		private final int dx;
		private final int dy;
		private Direction opposite;

		// use the static initializer to resolve forward references - this section of code is called before the constructor
		static
		{
			N.opposite = S;
			S.opposite = N;
			E.opposite = W;
			W.opposite = E;
		}


		private Direction(int bit, int dx, int dy)
		{
			this.bit = bit;
			this.dx = dx;
			this.dy = dy;
		}
	};


	private ArrayList<MapObject> objectList = new ArrayList<>();
	private Window window;
	private Map map;

	private MapObject levelObjects[][];
	private int level[][];

	private int x;
	private int y;
	private int tileSize;
	private long seed; //seed is usually system.CurrentTimeMillis(), but can be changed to a specific value for testing

	
	public MapGenerator(Window w, Map map, int x, int y, int tileSize, long seed)
	{
		this.window = w;
		this.map = map;
		this.x = x;
		this.y = y;

		this.tileSize = tileSize;
		this.seed = seed;

		this.level = new int[x][y];
		this.levelObjects = new MapObject[x][y];
		generateLevel(0,0, seed);
		display();
	}

	/**
	 * This method is used to generate the level - it populates the level int 2D array which is used later to place all objects within the level
	 * @param cx the current x coordinate (level[x][y])
	 * @param cy the current y coordinate (level[x][y])
	 * @param seed the seed used to randomly generate the level - can be set to a given value which wil produce identical maps when run
	 */
	private void generateLevel(int cx, int cy, long seed)
	{
		Direction[] dirs = Direction.values();
		Collections.shuffle(Arrays.asList(dirs)); //Can add shuffle(Arrays.asList(dirs), new Random(seed)) which will allow for repeatable ,aps
		for (Direction dir : dirs)
		{
			int nx = cx + dir.dx;
			int ny = cy + dir.dy;
			if (between(nx, x) && between(ny, y) && (level[nx][ny] == 0))
			{
				level[cx][cy] |= dir.bit; //|=  is bitwise or equal (i.e. reads the same as +=)
				level[nx][ny] |= dir.opposite.bit;
				generateLevel(nx, ny, seed);
			}
		}
	}


	/**
	 * A helper function that determines if a value is between a given value and 0
	 * @param i the integer that is being tested
	 * @param upper the upper bound that is being tested againts
	 * @return a boolean true if the value is between upper and 0, false if it isn't
	 */
	private boolean between(int i, int upper)
	{
		return (i >= 0) && (i < upper);
	}


	/**
	 * A function used to create and place the map objects
	 */
	public void display()
	{
		for (int i = 0; i < y; i++)
		{
			float y = i * tileSize;

			// draw the north edge
			for (int j = 0; j < x; j++)
			{
				float x = j * tileSize;
				System.out.print((level[j][i] & 1) == 0 ? "+---" : "+   "); //ternary operator if: level[j][i] & 1 == 0 print "+---" else print "+   "

				if ((level[j][i] & 1) == 0) { addObject(x, y, 125, 25, Textures.BRICKBLOCK); } //Change this to be 1 100x25 and 1 25x25

				else { addObject(x, y, 25, 25, Textures.BRICKBLOCK); }
			}

			//Will need to add a 25x25 block here
			System.out.println("+");
			addObject(400, y, 25, 25, Textures.BRICKBLOCK);

			// draw the west edge
			for (int j = 0; j < x; j++)
			{
				float x = j * tileSize;
				System.out.print((level[j][i] & 8) == 0 ? "|   " : "    ");

				if ((level[j][i] & 8) == 0) { addObject(x, y, 25, 100, Textures.BRICKBLOCK); }
			}

			// draw the east side - right side of the maze
			System.out.println("|");
			addObject(400, y, 25, 100, Textures.BRICKBLOCK);
		}

		// draw the bottom line
		for (int j = 0; j < x; j++)
		{
			System.out.print("+---");
			addObject(j * tileSize, 400, 100, 25, Textures.BRICKBLOCK);
		}
		System.out.println("+");
		addObject(400, 400, 25, 25, Textures.BRICKBLOCK);
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
		//Include scaling here - i.e. take the scale factor and multiply it by the given x and y - x and y should be the position in the grid?
		map.getObjectsInMap().add(new MapObject(this.window, x, y, width, height, texture)); //window, x, y, width, height, texture
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