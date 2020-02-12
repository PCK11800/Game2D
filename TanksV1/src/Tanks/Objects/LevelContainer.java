package Tanks.Objects;

import java.util.ArrayList;

import Tanks.ObjectComponents.DeadTank;
import Tanks.ObjectComponents.TankShell;
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
    private ArrayList<TankShell> shellList = new ArrayList<>();
    private ArrayList<DeadTank> deadTankList = new ArrayList<>();
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

        this.enemySpawner = new EnemySpawner(this.window, this.enemyList, this.playerList, this.map, this.mapGenerator, this.numEnemies, this);
        //testing
        //enemyKilled(0);
        unlockExits();
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
        player.config("player_future");
        player.setLevelContainer(this);
        player.setWindow(window);
        player.setTankLocation(xPos, yPos);
        player.config("fastshot_upgrade");

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
        updateShells();
        updateEnemies();
        updateMap();
        updateDeadTanks();

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

        for (int i = 0; i < playerList.size(); i++)
        {
            Tank player = playerList.get(i);
            if (player.update())
            {
                load = true;
            }
            else if(!player.isAlive()){
                deadTankList.add(new DeadTank(window, player.getDeathData()));
                playerList.remove(i);
            }
        }
        return load;
    }


    /**
     * Updates all of the enemies within a map
     */
    private void updateEnemies()
    {
        for (int i = 0; i < enemyList.size(); i++)
        {
            Tank enemy = enemyList.get(i);
            if(!enemy.isAlive()) {
                //Add dead tank
                deadTankList.add(new DeadTank(window, enemy.getDeathData()));
                enemyList.remove(i);
            }

            enemy.update();
        }
    }

    private void updateShells()
    {
        for(int i = 0; i < shellList.size(); i++)
        {
            TankShell shell = shellList.get(i);
            if(!shell.isActive())
            {
                shellList.remove(shell);
                shellList.trimToSize();
            }
            else
            {
                shell.update();
            }
        }
    }

    private void updateDeadTanks()
    {
        for(int i = 0; i < deadTankList.size(); i++)
        {
            deadTankList.get(i).update();
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


    //Testing function
    public void unlockExits()
    {
        this.map.unlockExits();
    }



    /**
     * Updates all of the map objects within a map
     */
    private void updateMap() { map.update(); }

    public Map getMap() {return map;}

    public ArrayList<Opponent> getEnemyList() {return enemyList;}
    public ArrayList<Tank> getPlayerList() {return playerList;}
    public ArrayList<TankShell> getShellList() {return shellList;}
}
