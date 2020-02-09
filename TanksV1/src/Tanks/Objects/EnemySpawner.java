package Tanks.Objects;

import Tanks.ObjectComponents.Textures;
import Tanks.Window.Window;
import java.util.ArrayList;
import java.util.Collections;


/**
 * This class is used to spawn enemies on the map
 */
public class EnemySpawner
{
    private Window window;
    private ArrayList<Opponent> enemyList;
    private ArrayList<Tank> playerList;
    private Map map;
    private MapGenerator mapGenerator;
    private int numEnemies;

    private ArrayList<Tile> availableTiles = new ArrayList<Tile>();


    //Should probably think about passing it a tank type / sprites
    /**
     * The constructor
     * @param window the window the enemies are to be drawn into
     * @param enemyList the enemy list contained within the LevelContainer class
     * @param playerList the player list contained with the levelContainer class - required as opponents need a reference to the player
     * @param map the map the enemies are to placed into
     * @param mapGenerator the mapGenerator contained within the levelContainer class - used to get the map's size, scale, etc.
     * @param numEnemies the number of enemies to be spawned into a level
     */
    public EnemySpawner(Window window, ArrayList<Opponent> enemyList, ArrayList<Tank> playerList, Map map, MapGenerator mapGenerator, int numEnemies)
    {
        this.window = window;
        this.enemyList = enemyList;
        this.playerList = playerList;
        this.map = map;
        this.mapGenerator = mapGenerator;
        this.numEnemies = numEnemies;

        getAvailableTiles();


        System.out.println(enemyList.size());
    }


    /**
     * This class is used to represent a tile within the map
     * Used in the getAvailableTiles and placeEnemies fumctions
     */
    private class Tile
    {
        //x and y pos are measured in tiles
        private int xPos;
        private int yPos;

        private Tile(int xPos, int yPos)
        {
            this.xPos = xPos;
            this.yPos = yPos;
        }
    }


    /**
     * This function obtains all of the tiles that are currently available for enemy tanks to be placed on
     * The tile is available unless it is the tile the player initially occupies or the tiles directly adjacent to it. THIS WILL NEED TO BE CHANGED - SHOULD HAVE A PIXEL VALUE - CAN SPAWN VERY CLOSE IF THE MAP SIZE IS LARGE
     * The player is assumed to be placed on the top left (0,0) tile
     */
    private void getAvailableTiles()
    {
        int xSize = this.mapGenerator.getXSize();
        int ySize = this.mapGenerator.getYSize();

        if (xSize > 0 && ySize > 0)
        {
            //3xX or Xx3 maps and above in size
            if ((xSize >= 3 || ySize >= 3))
            {
                for (int x = 0; x < xSize; x++)
                {
                    for (int y = 0; y < ySize; y++)
                    {
                        if (((x+1) * (y+1) != 1) && ((x+1) * (y+1) != 2)) //This prevents the players tile & adjacent tiles being available
                        {
                            availableTiles.add(new Tile(x, y));
                        }
                    }
                }
            }

            //2x2 maps and below in size - there is no restriction on enemy placement (other than not on the player)
            else if (xSize == 2 || ySize == 2)
            {
                for (int x = 0; x < this.mapGenerator.getXSize(); x++)
                {
                    for (int y = 0; y < this.mapGenerator.getYSize(); y++)
                    {
                        if ((x+1) * (y+1) != 1) //Cannot be placed on the players tile
                        {
                            availableTiles.add(new Tile(x, y));
                        }
                    }
                }
            }
        }

        placeEnemies();
    }



    /**
     * This function is used to place enemies randomly within the map.
     * The function uses the available tiles ArrayList and randomises its order, then places the enemies within the map.
     */
    private void placeEnemies()
    {
        System.out.println("Available Tiles: " + availableTiles.size());

        float tileCenterX = (this.mapGenerator.getTileSize() * this.mapGenerator.getXScale()) / 2;
        float tileCenterY = (this.mapGenerator.getTileSize() * this.mapGenerator.getYScale()) / 2;

        Collections.shuffle(availableTiles); //this shuffles the tiles randomly - this is what causes the enemies to be placed randomly
        Collections.shuffle(availableTiles);

        //Placing enemies
        if (this.availableTiles.size() >= this.numEnemies)
        {
            for (int i = 0; i < this.numEnemies; i++)
            {
                Tile currentTile = availableTiles.get(i);

                float enemyXPos = tileCenterX + ((tileCenterX * 2) * (currentTile.xPos));
                enemyXPos += (this.mapGenerator.getWallShort() * this.mapGenerator.getXScale());

                float enemyYPos = tileCenterY + ((tileCenterY * 2) * (currentTile.yPos));
                enemyYPos += this.mapGenerator.getOffsetTopY();

                initEnemy(enemyXPos, enemyYPos);
            }
        }


        else
        {
            for (Tile currentTile : this.availableTiles)
            {
                float enemyXPos = tileCenterX + ((tileCenterX * 2) * (currentTile.xPos));
                enemyXPos += this.mapGenerator.getWallShort() * this.mapGenerator.getXScale();

                float enemyYPos = tileCenterY + ((tileCenterY * 2) * (currentTile.yPos));
                enemyYPos += this.mapGenerator.getOffsetTopY();

                initEnemy(enemyXPos, enemyYPos);
            }
        }
    }


    /**
     * This method is used to initialize an enemy - create an enemy and place it in the world
     * @param xPos the initial x position of the tank
     * @param yPos the initial y position of the tank
     */
    private void initEnemy(float xPos, float yPos)
    {
        ConfusedOpponent enemy = new ConfusedOpponent(playerList.get(0), mapGenerator.getMap());
        enemy.setHullTexture(Textures.TANKHULL_GREEN);
        enemy.setTurretTexture(Textures.TANKTURRET_GREEN);
        enemy.setShellTexture(Textures.TANKSHELL_DEFAULT);
        enemy.setMap(this.map);
        enemy.setWindow(window);
        enemy.setSize((float) 1, (float) 1);
        enemy.setTankLocation(xPos, yPos);
        enemy.setHullTurningDistance(3);
        enemy.setTurretTurningDistance(3);
        enemy.setMovementSpeed(5);
        enemy.setInitialDirection(0);
        enemy.setShellSpeed(10);
        enemy.setShellRicochetNumber(2);
        enemy.setFireDelay(500);

        enemyList.add(enemy);
    }

}
