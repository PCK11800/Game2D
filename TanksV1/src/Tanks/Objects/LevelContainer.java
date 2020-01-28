package Tanks.Objects;

import java.util.ArrayList;

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
    private MapGenerator mapGenerator;

    private ArrayList<Tank> playerList = new ArrayList<Tank>();
    private ArrayList<Tank> enemyList = new ArrayList<Tank>(); //This should be changed once enemies are properly implemented


    /**
     * THe constructor
     * @param w the window that is to be drawn into
     * @param mapXSize the size of the map in the x axis (measured in "tiles")
     * @param mapYSize the size of the map in the y axis
     */
    public LevelContainer(Window w, int mapXSize, int mapYSize) //Maybe should include a seed here? that is passed to the mapGen
    {
        this.window = w;
        this.map = new Map(window);
        this.mapGenerator = new MapGenerator(window, map, mapXSize, mapYSize, System.currentTimeMillis());

        //this.mapGenerator.createMap();
        //initPlayer(Textures.TANKHULL_BLUE, Textures.TANKTURRET_BLUE, Textures.TANKSHELL_DEFAULT);
    }


    /**
     * This method is used to initialise the player - i.e. place them in the map, set their textures, etc.
     * @param hullTexture the texture for the tanks hull
     * @param turretTexture the texture for the tanks turret
     * @param shellTexture the texture for the tanks shell / projectile
     */
    private void initPlayer(String hullTexture, String turretTexture, String shellTexture)
    {
        Tank player = new Tank();
        player.setHullTexture(hullTexture);
        player.setTurretTexture(turretTexture);
        player.setShellTexture(shellTexture);
        player.setMap(this.map);
        player.setWindow(window);
        player.setSize((float) 1, (float) 1);
        player.setTankLocation(100, 150);
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


    private void initEnemy(String hullTexture, String turretTexture, String shellTexture)
    {
        Opponent player = new Opponent(playerList.get(0));
        player.setHullTexture(hullTexture);
        player.setTurretTexture(turretTexture);
        player.setShellTexture(shellTexture);
        player.setMap(this.map);
        player.setWindow(window);
        player.setSize((float) 1, (float) 1);
        player.setTankLocation(100, 450);
        player.setHullTurningDistance(3);
        player.setTurretTurningDistance(3);
        player.setMovementSpeed(5);
        player.setInitialDirection(0);
        player.setShellSpeed(10);
        player.setShellRicochetNumber(2);
        player.setFireDelay(500);

        enemyList.add(player);
    }


    /**
     * This is used to create / initialise the map. It places all of the map objects within the level as well as the players & enemies
     */
    public void createLevel()
    {
       this.mapGenerator.createMap();
       initPlayer(Textures.TANKHULL_BLUE, Textures.TANKTURRET_BLUE, Textures.TANKSHELL_DEFAULT);
       initEnemy(Textures.TANKHULL_BLUE, Textures.TANKTURRET_BLUE, Textures.TANKSHELL_DEFAULT);
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
     * Updates all of the map objects within a map
     */
    private void updateMap() { map.update(); }
}
