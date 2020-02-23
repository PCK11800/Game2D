package Tanks.Objects;

import Tanks.Listeners.PlayerListener;
import Tanks.ObjectComponents.DeadTank;
import Tanks.ObjectComponents.TankShell;
import Tanks.ObjectComponents.Textures;
import Tanks.Objects.VariousOpponents.*;
import Tanks.UIScreens.InGameMonitor;
import Tanks.Window.Window;

import java.util.ArrayList;

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
    private int index;
    private boolean loadNextLevel = false;

    private ArrayList<Tank> playerList = new ArrayList<Tank>();
    private ArrayList<Opponent> enemyList = new ArrayList<Opponent>();
    private ArrayList<Opponent> enemyTypes = new ArrayList<Opponent>();;
    private ArrayList<TankShell> shellList = new ArrayList<>();
    private ArrayList<DeadTank> deadTankList = new ArrayList<>();
    private EnemySpawner enemySpawner;

     private InGameMonitor inGameMonitor;

    /**
     * The constructor
     * @param w the window that is to be drawn into
     * @param mapXSize the size of the map in the x axis (measured in "tiles")
     * @param mapYSize the size of the map in the y axis
     * @param  seed the seed for the random generation
     */
    public LevelContainer(Window w, int mapXSize, int mapYSize, int numEnemies, long seed, int index) //Maybe should include a seed here? that is passed to the mapGen
    {
        this.window = w;
        this.map = new Map(window);
        this.mapGenerator = new MapGenerator(window, map, mapXSize, mapYSize, seed);
        this.numEnemies = numEnemies;
        this.index = index;
        this.inGameMonitor = new InGameMonitor(this.window);
        this.seed = seed;
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

        initPlayer(playerList.get(0), playerX, playerY);
        spawnEnemies();
    }


    private void spawnEnemies()
    {
        if (this.index == 0)
        {
            this.enemyTypes = new ArrayList<>();
            this.enemyTypes.add(new HouseTankSlow(this.playerList.get(0), this.mapGenerator));
        }

        else if (this.index == 3)
        {
            this.enemyTypes = new ArrayList<>();
            this.enemyTypes.add(new JamesOpponent(this.playerList.get(0), this.mapGenerator, 1));
            this.enemyTypes.add(new GordonOpponent(this.playerList.get(0), this.mapGenerator, 1));
            this.enemyTypes.add(new EdwardOpponent(this.playerList.get(0), this.mapGenerator, 1));
            this.enemyTypes.add(new HenryOpponent(this.playerList.get(0), this.mapGenerator, 1));
            this.enemyTypes.add(new PercyOpponent(this.playerList.get(0), this.mapGenerator, 1));
            this.enemyTypes.add(new TobyOpponent(this.playerList.get(0), this.mapGenerator, 1));
        }


        else if (this.index == 7)
        {
            this.enemyTypes = new ArrayList<>();
            this.enemyTypes.add(new JamesOpponent(this.playerList.get(0), this.mapGenerator, 2));
            this.enemyTypes.add(new GordonOpponent(this.playerList.get(0), this.mapGenerator, 2));
            this.enemyTypes.add(new EdwardOpponent(this.playerList.get(0), this.mapGenerator, 2));
            this.enemyTypes.add(new HenryOpponent(this.playerList.get(0), this.mapGenerator, 2));
            this.enemyTypes.add(new PercyOpponent(this.playerList.get(0), this.mapGenerator, 2));
            this.enemyTypes.add(new TobyOpponent(this.playerList.get(0), this.mapGenerator, 2));
        }

        else if (this.index == 11)
        {
            this.enemyTypes = new ArrayList<>();
            this.enemyTypes.add(new ControllerOpponent(this.playerList.get(0), this.mapGenerator));
        }

        else
        {
            this.enemyTypes = new ArrayList<>();
            this.enemyTypes.add(new HouseTankSlow(this.playerList.get(0), this.mapGenerator));
            this.enemyTypes.add(new HouseTankFast(this.playerList.get(0), this.mapGenerator));
            this.enemyTypes.add(new HouseTankSniper(this.playerList.get(0), this.mapGenerator));
            this.enemyTypes.add(new HouseTankTurret(this.playerList.get(0), this.mapGenerator));
        }

        int opponentLevel = 1;

        if (this.index > 3)
        {
            opponentLevel = 2;
        }

        this.enemySpawner = new EnemySpawner(this.window, this.enemyList, this.enemyTypes, this.playerList, this.mapGenerator, this.numEnemies, this, opponentLevel);
    }


    /**
     * This method is used to initialise the player - i.e. place them in the map, set their textures, etc.
     */
    private void initPlayer(Tank player, float xPos, float yPos)
    {
        player.setTankLocation(xPos, yPos);
    }

    public void addPlayer(Tank player) {
        this.playerList.add(player);
    }


    /**
     * This is the update method that is called once per frame
     * It updates the enemies the players and the map
     * @return returns true if a new level should be loaded
     */
    public void update()
    {
        //Will need to check if an enemy has been killed - then call map.enemyKilled() if they have
        updateDeadTanks();
        updateShells();
        updateEnemies();
        updateMap();
        updateInGameMonitor();

         updatePlayers();
    }

    public boolean loadNextLevel() { return this.loadNextLevel; }


    /**
     * This method updates all players in a level
     * @return returns true if a new level is to be loaded
     */
    private void updatePlayers()
    {
        this.loadNextLevel = false;

        for (int i = 0; i < playerList.size(); i++)
        {
            Tank player = playerList.get(i);
            if (player.update())
            {
                this.loadNextLevel = true;
            }

            else if(!player.isAlive())
            {
                deadTankList.add(new DeadTank(window, player.getDeathData()));
                inGameMonitor.setCurrentData(0, 0);
                playerList.remove(i);
                playerList.trimToSize();
            }
        }
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
                enemyKilled(i);
                enemyList.trimToSize();

                if(playerList.size() > 0){
                    playerList.get(0).increaseMoney(10);
                }
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

    private void updateInGameMonitor()
    {
        //Health
        if(playerList.size() > 0){
            Tank player = playerList.get(0);
            inGameMonitor.setCurrentData(3, player.getStartingHealth());
            inGameMonitor.setCurrentData(0, player.getHealth());
            inGameMonitor.setCurrentData(2, player.getMoney());
        }
        inGameMonitor.setCurrentData(1, enemyList.size());
        inGameMonitor.updateMonitor();
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
