package Tanks.Objects;

import Tanks.Listeners.PauseListener;
import Tanks.Sounds.GameMusicHandler;
import Tanks.Window.Window;

import java.util.Random;

public class GameMode
{
    private Window window;
    private LevelContainer currentLevel;

    private UIScreenManager uiManager;
    private PauseListener pauseListener;
    private GameMusicHandler gameMusicHandler = new GameMusicHandler();

    private Tank player;
    
    private int levelNum = 0;
    private int maxLevel = 12; //LevelNum starts at 0, so it is no. of levels + 1
    private int[] shopLevels = {2, 6, 10, 3, 7}; // The levels the shop is loaded after - before and after each boss fight

    private Random random;
    private long seed;

    private boolean paused = false;


    /**
     * The constructor
     * @param window the window for everything to be drawn into
     */
    public GameMode(Window window, long seed)
    {
        this.window = window;
        this.uiManager = new UIScreenManager(window);
        this.pauseListener = new PauseListener(this);
        this.seed = seed;

        createLevel();
        initPlayer();
        initGameMode();

        this.random = new Random(this.seed);
    }


    /**
     * This method is used to initialise the player - i.e. place them in the map, set their textures, etc.
     */
    private void initPlayer()
    {
        this.player = new Tank();
        this.player.config("player_default");
        this.player.setLevelContainer(this.currentLevel);
        this.player.setWindow(window);

        this.currentLevel.addPlayer(this.player);
    }


    private void spawnPlayer()
    {
        this.player.resetLoadFlag();
        this.player.setLevelContainer(this.currentLevel);
        this.currentLevel.addPlayer(this.player);
    }


    /**
     * This method dynamically creates every level in the game when a new level is to be loaded
     */
    private void createLevel()
    {
        //Map parameters - with default values
        int mapXSize = 1;
        int mapYSize = 1;
        int numEnemies = 0;

        if (this.levelNum == 0)
        {
            mapXSize = 2;
            mapYSize = 2;
            numEnemies = 1;
        }

        else if (this.levelNum == 1)
        {
            mapXSize = 3;
            mapYSize = 2;
            numEnemies = 2;
        }

        else if (this.levelNum == 2)
        {
            mapXSize = (this.random.nextInt(1) + 3);
            mapYSize = 3;
            numEnemies = (this.random.nextInt(1) + 2);
        }

        else if (this.levelNum == 3)
        {
            mapXSize = (this.random.nextInt(1) + 2);
            mapYSize = (this.random.nextInt(1) + 2);
            numEnemies = 1;

        }

        //Round 2
        else if (this.levelNum == 4)
        {
            mapXSize = (this.random.nextInt(1) + 3);
            mapYSize = (this.random.nextInt(1) + 2);
            numEnemies = (this.random.nextInt(1) + 3);
        }

        else if (this.levelNum == 5)
        {
            mapXSize = (this.random.nextInt(2) + 3);
            mapYSize = (this.random.nextInt(2) + 2);
            numEnemies = (this.random.nextInt(1) + 4);
        }

        else if (this.levelNum == 6)
        {
            mapXSize = (this.random.nextInt(2) + 3);
            mapYSize = (this.random.nextInt(1) + 3);
            numEnemies = (this.random.nextInt(1) + 5);
        }

        else if (this.levelNum == 7)
        {
            mapXSize = (this.random.nextInt(1) + 2);
            mapYSize = (this.random.nextInt(1) + 2);
            numEnemies = 1;
        }

        //Round 3
        else if (this.levelNum == 8)
        {
            mapXSize = (this.random.nextInt(2) + 5);
            mapYSize = (this.random.nextInt(1) + 4);
            numEnemies = (this.random.nextInt(2) + 5);
        }

        else if (this.levelNum == 9)
        {
            mapXSize = (this.random.nextInt(3) + 5);
            mapYSize = (this.random.nextInt(2) + 5);
            numEnemies = (this.random.nextInt(3) + 5);
        }

        else if (this.levelNum == 10)
        {
            mapXSize = (this.random.nextInt(3) + 6);
            mapYSize = (this.random.nextInt(2) + 5);
            numEnemies = (this.random.nextInt(3) + 7);
        }

        else if (this.levelNum == 11)
        {
            mapXSize = (this.random.nextInt(1) + 2);
            mapYSize = (this.random.nextInt(1) + 2);
            numEnemies = 1;
        }

        //Set the map
        System.out.println("LEVEL: " + this.levelNum);
        this.currentLevel = new LevelContainer(this.window, mapXSize, mapYSize, numEnemies, this.seed, this.levelNum);
    }


    /**
     * This method loads the first level
     */
    private void initGameMode()
    {
        currentLevel.createLevel();
    }

    /**
     * This method updates the level and everything in it.
     * It also handles the logic for loading a new level if it is required
     */
    public void update()
    {
        pauseListener.handlePause();
        gameMusicHandler.musicHandler();

        if(paused)
        {
            gameMusicHandler.pause();

            if (!uiManager.isOnPauseScreen())
            {
                //uiManager.setHideUI(false);
                uiManager.displayPauseScreen();
            }

            uiManagerUpdate();
        }

        else
        {
            gameMusicHandler.resume();

            if (uiManager.isOnPauseScreen())
            {
                //uiManager.setHideUI(true);
                uiManager.setOnUIScreen(false);
                uiManager.resumeGame();
            }

            //Tests to see if you are on a UI Screen - if so update that screen
            if (uiManager.isOnUIScreen())
            {
                uiManagerUpdate();
            }

            //In an arena
            else
            {
                if (currentLevel.isPlayerDead())
                {
                    uiManager.changeState();
                    uiManager.displayGameOverScreen();
                }

                else // Player is not dead
                {
                    if (!this.currentLevel.loadNextLevel()) // If not loading a new level
                    {
                        this.currentLevel.update();
                    }

                    else // Load the next level
                    {
                        handleLevelLoading();
                    }
                }
            }
        }
    }


    private void uiManagerUpdate()
    {
        if(uiManager.hideUI())
        {
            uiManager.changeState();
            uiManager.resetFlags();
            this.paused = false;
        }
        else
        {
            uiManager.update();
        }
    }


    private void handleLevelLoading()
    {
        if (isShopLevel()) // The rounds before and after a boss
        {
            // Already been to the shop
            if (uiManager.isInShop())
            {
                uiManager.closeShop();
                loadNextLevel();
            }

            else
            {
                uiManager.changeState();
                uiManager.displayShop(this.player);
            }
        }

        else
        {
            if (this.levelNum + 1 >= this.maxLevel)
            {
                uiManager.changeState();
                uiManager.displayEndScreen();
                this.levelNum = 0;

            }
            else
            {
                loadNextLevel();
            }
        }
    }



    private boolean isShopLevel()
    {
        for (int i : this.shopLevels)
        {
            if (this.levelNum == i)
            {
                return true;
            }
        }

        return false;
    }


    private void loadNextLevel()
    {
        this.levelNum++;

        createLevel();
        spawnPlayer();
        this.currentLevel.createLevel();
    }


    public void pause()
    {
        paused = true;
    }

    public void unpause(){
        paused = false;
    }

    public boolean getStatus(){
        return paused;
    }

    public Window getWindow() { return window; }
}
