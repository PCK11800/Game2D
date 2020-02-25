package Tanks.Objects;

import Tanks.ObjectComponents.MapObject;
import Tanks.Window.Window;
import Tanks.ObjectComponents.Textures;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;


/**
 * This class is used to randomly generate a map given particular parameters
 * The method used to generate the map is a recursive backtracking maze algorithm
 */
public class MapGenerator
{
	//Instance variables
	private Window window;
	private Map map;
	
	private int[][] level;

	private int x;
	private int y;
	private float maxXPos;
	private float maxYPos;
	private float xScale;
	private float yScale;


	private int screenWidth; //= 1920;
	private int screenHeight; // = 1080;

	//Default values
	private float offsetX = 0;
	private float offsetTopY = 50;
	private float offsetBottomY = 0;

	private float wallShort = 10;
	private float wallLong = 100;
	private float tileSize = wallShort + wallLong; //This makes the actual tile size (space between walls), 1 wall wide

	private boolean uniformWallThickness = false;

	private long seed; //seed is usually system.CurrentTimeMillis(), but can be changed to a specific value for testing


	/**
	 * The constructor for the map generator
	 * @param w the window that map is to be drawn into
	 * @param map the map object that the mapObjects need to placed into
	 * @param x the size of the map in the x direction (measured in "tiles")
	 * @param y the size of the map in the y direction
	 * @param seed the seed - used for random generation - providing it with a seed will produce repeatable results
	 */
	public MapGenerator(Window w, Map map, int x, int y, long seed)
	{
		this.window = w;
		this.screenHeight = this.window.getHeight();
		this.screenWidth = this.window.getWidth();

		this.map = map;

		this.x = x;
		this.y = y;
		this.maxXPos = (this.tileSize * x);
		this.maxYPos = (this.tileSize * y);

		this.seed = seed;

		this.level = new int[x][y];

		setMapScale();
		generateLevel(0,0, seed);
		//createMap();
	}


	/**
	 * This method sets the mapObject sizes so that they scale based on the size of the map, but remain a constant screen size.
	 */
	private void setMapScale()
	{
		//Should be passing in the actual screen size values
		float maxPixelsX = this.screenWidth - (this.offsetX * 2);
		float maxPixelsY = this.screenHeight - (this.offsetTopY + this.offsetBottomY);

		float initPixelsX = (this.wallLong * x) + (this.wallShort * (x + 1));
		float initPixelsY = (this.wallLong * y) + (this.wallShort * (y + 1));

		this.xScale = maxPixelsX / initPixelsX;
		this.yScale = maxPixelsY / initPixelsY;
	}


	/**
	 * This method can be called to make the map scale uniformly (i.e. the dimensions of all walls will remain constant)
	 * As as result the map will no longer fill the screen if this function is called
	 */
	public void setUniformScale()
	{
		if (xScale > yScale)
		{
			this.xScale = this.yScale;
		}
		else
		{
			this.yScale = this.xScale;
		}
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
		Collections.shuffle(Arrays.asList(dirs)); //Done twice so that it is less likely to produce the same map 

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
	 * @param upper the upper bound that is being tested against
	 * @return a boolean true if the value is between upper and 0, false if it isn't
	 */
	private boolean between(int i, int upper) { return (i >= 0) && (i < upper); }


	/**
	 * A function used to create and place the map objects within the map
	 * The function iterates through the level 2D array and places a wall of the correct size and direction onto the map.
	 */
	public void createMap()
	{
		Random rand = new Random(this.seed);

		boolean exitAdded = false;
		int exitWall = rand.nextInt(this.y);  //This determines where on the east wall the exit is placed

		for (int i = 0; i < this.y; i++)
		{
			float yPos = (i * this.tileSize);

			//Creates the north edge of the map
			for (int j = 0; j < this.x; j++)
			{
				float xPos = (j * this.tileSize);
				System.out.print((this.level[j][i] & 1) == 0 ? "+---" : "+   "); //ternary operator if: level[j][i] & 1 == 0 print "+---" else print "+   "

				if ((level[j][i] & 1) == 0)
				{
					addObject(xPos, yPos, this.wallShort, this.wallShort, Textures.BLACKWALL);
					addObject(xPos + this.wallShort, yPos, this.wallLong, this.wallShort, Textures.BLACKWALL);
				}

				else
				{
					addObject(xPos, yPos, this.wallShort, this.wallShort, Textures.BLACKWALL);
				}
			}

			//Creates part of the east edge of the map
			System.out.println("+");
			addObject(this.maxXPos, yPos, this.wallShort, this.wallShort, Textures.BLACKWALL);

			//Creates the west edge of the map
			for (int j = 0; j < x; j++)
			{
				float xPos = (j * this.tileSize);
				System.out.print((this.level[j][i] & 8) == 0 ? "|   " : "    ");

				if ((this.level[j][i] & 8) == 0)
				{
					addObject(xPos, yPos + this.wallShort, this.wallShort, this.wallLong, Textures.BLACKWALL);
				}
			}

			//Creates part of the east edge of the map
			//This will randomly add the mapExit to the east wall
			if (!exitAdded && i == exitWall)
			{
				System.out.println("E");
				addExit(this.maxXPos, yPos + this.wallShort, this.wallShort, this.wallLong, Textures.BLACKWALL, Textures.BLACK);
				addObject(this.maxXPos + wallShort, yPos + this.wallShort, this.wallShort, this.wallLong, Textures.BLACKWALL);


				exitAdded = true;
			}
			//Otherwise add a standard wall
			else
			{
				System.out.println("|");
				addObject(this.maxXPos, yPos + this.wallShort, this.wallShort, this.wallLong, Textures.BLACKWALL);
			}
		}

		//Creates the south edge of the map
		for (int j = 0; j < x; j++)
		{
			float xPos = (j * this.tileSize);

			System.out.print("+---");
			addObject(xPos, this.maxYPos, this.wallShort, this.wallShort, Textures.BLACKWALL);
			addObject(xPos + this.wallShort, this.maxYPos, this.wallLong, this.wallShort, Textures.BLACKWALL);
		}

		//Creates the other part of the east edge of the map
		System.out.println("+");
		addObject(this.maxXPos, this.maxYPos, this.wallShort, this.wallShort, Textures.BLACKWALL);
	}


	
	/**
	 * This method adds a given object to the maps Object arrayList and therefore the map
	 * @param xPos the x position of the object to be added in pixels
	 * @param yPos the y position of the object to be added in pixels
	 * @param width the width of the object to be added in pixels
	 * @param height the height of the object to be added in pixels
	 * @param texture the texture of the object (Textures.NAME)
	 */
	private void addObject(float xPos, float yPos, float width, float height, String texture)
	{
		//It is xPos + width / 2, is because mapObjects anchor point is its center, but to keep the map gen code cleaner, it is assumed the anchor point is the top left.
        xPos = ((xPos + (width / 2)) * this.xScale) + offsetX;
        yPos = ((yPos + (height / 2)) * this.yScale) + offsetTopY;

        width *= this.xScale;
        height *= this.yScale;

		try
		{
			map.getObjectsInMap().add(new MapObject(this.window, xPos, yPos, width, height, texture));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * This method almost identical to the addObject method, however this methods adds a given mapExit to the maps mapObject arrayList instead.
	 * @param xPos the x position of the object to be added in pixels
	 * @param yPos the y position of the object to be added in pixels
	 * @param width the width of the object to be added in pixels
	 * @param height the height of the object to be added in pixels
	 * @param lockedTexture the texture of the map exit when locked (Textures.NAME)
	 * @param unlockedTexture the texture of the map exit when unlocked (Textures.NAME)
	 */
	private void addExit(float xPos, float yPos, float width, float height, String lockedTexture, String unlockedTexture)
	{
		xPos = ((xPos + (width / 2)) * this.xScale) + offsetX;
		yPos = ((yPos + (height / 2)) * this.yScale) + offsetTopY;

		width *= this.xScale;
		height *= this.yScale;

		try
		{
			map.getObjectsInMap().add(new MapObject(this.window, xPos, yPos, width, height, lockedTexture));

			MapObject exit = map.getObjectsInMap().get(map.getObjectsInMap().size() -1);
			exit.makeExit(unlockedTexture);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}


	public int[][] getMap() { return level; }

	public float getMaxXPos() { return maxXPos; }

	public float getMaxYPos() { return maxYPos; }


	public int getXSize() { return this.x; }

    public int getYSize() { return this.y; }

    public float getXScale() { return this.xScale; }

    public float getYScale() { return this.yScale; }

    public float getWallLong() { return this.wallLong; }

    public float getWallShort() { return this.wallShort; }

    public float getTileSize() { return this.tileSize; }

    public float getOffsetTopY() { return this.offsetTopY; }
}