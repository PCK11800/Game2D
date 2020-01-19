package Tanks.Objects;

import Tanks.ObjectComponents.MapObject;
import Tanks.Window.Window;
import Tanks.ObjectComponents.Textures;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


/**
 * This class is used to randomly generate a map given particular parameters
 * The method used to generate the map is a recursive backtracking maze algorithm
 * The algorithms implementation is derived from the algorithm presented here: https://weblog.jamisbuck.org/2015/1/15/better-recursive-division-algorithm.html
 */
public class MapGenerator
{
	//Instance variables
	private ArrayList<MapObject> objectList = new ArrayList<>();
	private Window window;
	private Map map;

	private MapObject levelObjects[][];
	private int level[][];

	private int x;
	private int y;
	private float maxX;
	private float maxY;

	private float tileSize;


	private float wallShort;
	private float wallLong;

	private long seed; //seed is usually system.CurrentTimeMillis(), but can be changed to a specific value for testing

	
	public MapGenerator(Window w, Map map, int x, int y, float wallShort, float tileSize, long seed)
	{
		this.window = w;
		this.map = map;

		this.x = x;
		this.y = y;
		this.maxX = tileSize * x;
		this.maxY = tileSize * y;

		this.wallShort = wallShort;
		this.wallLong = tileSize;
		this.tileSize = tileSize;

		this.seed = seed;

		this.level = new int[x][y];

		generateLevel(0,0, seed);
		createMap();
	}


	/**
	 * A private enum called direction which stores 4 instances of the enum - north, south, east and west
	 * A bit number - used for testing, direction on the x axis, as well direction on the y axis are stored, in addition to the the opposite of the current direction
	 */
	private enum Direction
	{
		//Creates instances of the enum - north, south, east and west, with the specified bits
		N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);

		private final int bit;
		private final int dx;
		private final int dy;
		private Direction opposite;

		//This is used to resolve forward references, as it is called before the constructor.
		static
		{
			N.opposite = S;
			S.opposite = N;
			E.opposite = W;
			W.opposite = E;
		}

		//Constructor
		private Direction(int bit, int dx, int dy)
		{
			this.bit = bit;
			this.dx = dx;
			this.dy = dy;
		}
	};

	

	/**
	 * This method is used to generate the level - it populates the level int 2D array which is used later to place all objects within the level
	 * @param cx the current x coordinate (level[x][y])
	 * @param cy the current y coordinate (level[x][y])
	 * @param seed the seed used to randomly generate the level - can be set to a given value which wil produce identical maps when run
	 */
	private void generateLevel(int cx, int cy, long seed)
	{
		Direction[] dirs = Direction.values();
		Collections.shuffle(Arrays.asList(dirs)); //Can add shuffle(Arrays.asList(dirs), new Random(seed)) which will allow for repeatable maps

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
	private boolean between(int i, int upper) { return (i >= 0) && (i < upper); }


	/**
	 * A function used to create and place the map objects within the map
	 * The function iterates through the level 2D array and places a wall of the correct size and direction onto the map.
	 */
	public void createMap()
	{
		for (int i = 0; i < y; i++)
		{
			float yPos = i * tileSize;

			//Creates the north edge of the map
			for (int j = 0; j < x; j++)
			{
				float xPos = j * tileSize;
				System.out.print((level[j][i] & 1) == 0 ? "+---" : "+   "); //ternary operator if: level[j][i] & 1 == 0 print "+---" else print "+   "

				if ((level[j][i] & 1) == 0)
				{
					addObject(xPos, yPos, wallShort, wallShort, Textures.EXIT_LOCKED);
					addObject(xPos + wallShort, yPos, wallLong, wallShort, Textures.BRICKBLOCK);
				}

				else
				{
					addObject(xPos, yPos, wallShort, wallShort, Textures.EXIT_LOCKED);
				}
			}

			//Creates part of the east wall
			System.out.println("+");
			addObject(maxX, yPos, wallShort, wallShort, Textures.EXIT_LOCKED);

			//Creates the west edge of the map
			for (int j = 0; j < x; j++)
			{
				float xPos = j * tileSize;
				System.out.print((level[j][i] & 8) == 0 ? "|   " : "    ");

				if ((level[j][i] & 8) == 0)
				{
					addObject(xPos, yPos + wallShort, wallShort, wallLong, Textures.BRICKBLOCK);
				}
			}

			//Creates part of the east edge of the map
			System.out.println("|");
			addObject(maxX, yPos + wallShort, wallShort, wallLong, Textures.BRICKBLOCK);
		}

		//Creates the south edge of the map
		for (int j = 0; j < x; j++)
		{
			System.out.print("+---");
			addObject(j * tileSize, maxY, wallShort, wallShort, Textures.EXIT_LOCKED);
			addObject(j * tileSize + wallShort, maxY, wallLong, wallShort, Textures.BRICKBLOCK);
		}

		//Creates the other part of the east edge of the map
		System.out.println("+");
		addObject(maxX, maxY, wallShort, wallShort, Textures.EXIT_LOCKED);
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
}