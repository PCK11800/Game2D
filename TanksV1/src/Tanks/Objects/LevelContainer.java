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


    public LevelContainer(Window w)
    {
        this.window = w;
        this.map = new Map(window);
        this.mapGenerator = new MapGenerator(window, map, 4, 4, 10,100, 100, 20, System.currentTimeMillis());

        initPlayer(Textures.TANKHULL_BLUE, Textures.TANKTURRET_BLUE, Textures.TANKSHELL_DEFAULT);
    }

    //This is primarily for testing as this should really be moved to some other class and the player(s)
    private void initPlayer(String hullTexture, String turretTexture, String shellTexture)
    {
        Tank player = new Tank();
        player.setHullTexture(hullTexture);
        player.setTurretTexture(turretTexture);
        player.setShellTexture(shellTexture);
        player.setMap(this.map);
        player.setWindow(window);
        player.setSize((float) 1, (float) 1);
        player.setTankLocation(800, 500);
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

    public void update()
    {
        //Will need to check if an enemy has been killed - then call map.enemyKilled() if they have
        updatePlayers();
        updateEnemies();
        updateMap();
    }

    private void updatePlayers()
    {
        for (Tank player : playerList)
        {
            player.update();
        }
    }

    private void updateEnemies()
    {
        for (Tank enemy : enemyList)
        {
            enemy.update();
        }
    }

    private void updateMap() { map.update(); }
}
