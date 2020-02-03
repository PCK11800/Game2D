package Tanks.Objects;

import java.util.ArrayList;
import java.util.Random;

import Tanks.ObjectComponents.Textures;
import Tanks.Window.Window;

/**
 * This class is used to store all of the data that is required in a level, i.e. a Map, Player(s), Enemies, etc.
 */
public class LevelContainer
{
    //Instance variables
    private Window window;
    private Map map;
    private int numEnemies;
    private long seed;
    private MapGenerator mapGenerator;

    private ArrayList<Tank> playerList = new ArrayList<Tank>();
    private ArrayList<Opponent> enemyList = new ArrayList<Opponent>(); //This should be changed once enemies are properly implemented
    private EnemySpawner enemySpawner;


    /**
     * The constructor
     * @param w the window that is to be drawn into
     * @param mapXSize the size of the map in the x axis (measured in "tiles")
     * @param mapYSize the size of the map in the y axis
     * @param  seed the seed for the random generation
     */
    public LevelContainer(Window w, int mapXSize, int mapYSize, int numEnemies, long seed) //Maybe should include a seed here? that is passed to the mapGen
    {
        this.window = w;
        this.map = new Map(window);
        this.mapGenerator = new MapGenerator(window, map, mapXSize, mapYSize, seed);
        this.numEnemies = numEnemies;
    }


    /**
     * This is used to create / initialise the map. It places all of the map objects within the level as well as the players & enemies
     */
    public void createLevel()
    {
        this.mapGenerator.createMap();
        float playerX = ((this.mapGenerator.getTileSize() + this.mapGenerator.getWallShort()) * this.mapGenerator.getXScale()) / 2;
        float playerY = ((this.mapGenerator.getTileSize() + this.mapGenerator.getWallShort()) * this.mapGenerator.getYScale()) / 2;
        playerY += this.mapGenerator.getOffsetTopY();

        initPlayer(Textures.TANKHULL_BLUE, Textures.TANKTURRET_BLUE, Textures.TANKSHELL_DEFAULT, playerX, playerY);

        this.enemySpawner = new EnemySpawner(this.window, this.enemyList, this.playerList, this.map, this.mapGenerator, this.numEnemies);
        //testing
        enemyKilled(0);
    }


    /**
     * This method is used to initialise the player - i.e. place them in the map, set their textures, etc.
     * @param hullTexture the texture for the tanks hull
     * @param turretTexture the texture for the tanks turret
     * @param shellTexture the texture for the tanks shell / projectile
     */
    private void initPlayer(String hullTexture, String turretTexture, String shellTexture, float xPos, float yPos)
    {
        Tank player = new Tank();
        player.setHullTexture(hullTexture);
        player.setTurretTexture(turretTexture);
        player.setShellTexture(shellTexture);
        player.setMap(this.map);
        player.setWindow(window);
        player.setSize((float) 1, (float) 1);
        player.setTankLocation(xPos, yPos);
        player.setHullTurningDistance(3);
        player.setTurretTurningDistance(3);
        player.setMovementSpeed(5);
        player.setInitialDirection(0);
        player.setShellSpeed(10);
        player.setShellRicochetNumber(2);
        player.setFireDelay(500);
        player.enablePlayerControl();

        playerList.add(player);
    }



    /**
     * This is the update method that is called once per frame
     * It updates the enemies the players and the map
     * @return returns true if a new level should be loaded
     */
    public boolean update()
    {
        //Will need to check if an enemy has been killed - then call map.enemyKilled() if they have
        updateEnemies();
        updateMap();

        //If need to load the next level
        if (updatePlayers())
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    /**
     * This method updates all players in a level
     * @return returns true if a new level is to be loaded
     */
    private boolean updatePlayers()
    {
        boolean load = false;

        for (Tank player : playerList)
        {
            if (player.update())
            {
                load = true;
            }
        }
        return load;
    }


    /**
     * Updates all of the enemies within a map
     */
    private void updateEnemies()
    {
        for (Tank enemy : enemyList)
        {
            enemy.update();
        }
    }


    /**
     * This method is to be called when an enemy is killed
     */
    private void enemyKilled(int index)
    {
        this.enemyList.remove(index);

        if (this.enemyList.size() <= 0) //If there are no more enemies unlock the exits
        {
            this.map.unlockExits();
        }
    }



    /**
     * Updates all of the map objects within a map
     */
    private void updateMap() { map.update(); }
}
