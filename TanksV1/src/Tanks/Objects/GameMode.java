package Tanks.Objects;

import Tanks.Listeners.PauseListener;
import Tanks.Sounds.GameMusic;
import Tanks.Sounds.GameMusicHandler;
import Tanks.Sounds.SoundsPath;
import Tanks.UIScreens.InGameMonitor;
import Tanks.UIScreens.ShopScreen;
import Tanks.Window.Window;
import org.jsfml.audio.SoundSource;

import java.util.ArrayList;
import java.util.Random;

public class GameMode
{
    private Window window;
    private LevelContainer currentLevel;

    private UIScreenManager uiManager;
    private PauseListener pauseListener;
    private GameMusicHandler gameMusicHandler = new GameMusicHandler();
    
    private int levelNum = 0;
    private int maxLevel = 9; //LevelNum starts at 0, so it is no. of levels + 1

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
        initGameMode();

        this.random = new Random(this.seed);
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
            //numEnemies = 2;
        }

        else if (this.levelNum == 2)
        {
            mapXSize = (this.random.nextInt(1) + 3);
            mapYSize = 3;
            numEnemies = (this.random.nextInt(1) + 2);
        }

        //Round 2
        else if (this.levelNum == 3)
        {
            mapXSize = (this.random.nextInt(1) + 3);
            mapYSize = (this.random.nextInt(1) + 2);
            numEnemies = (this.random.nextInt(1) + 3);
        }

        else if (this.levelNum == 4)
        {
            mapXSize = (this.random.nextInt(2) + 3);
            mapYSize = (this.random.nextInt(2) + 2);
            numEnemies = (this.random.nextInt(1) + 4);
        }

        else if (this.levelNum == 5)
        {
            mapXSize = (this.random.nextInt(2) + 3);
            mapYSize = (this.random.nextInt(1) + 3);
           numEnemies = (this.random.nextInt(1) + 5);
        }

        //Round 3
        else if (this.levelNum == 6)
        {
            mapXSize = (this.random.nextInt(2) + 5);
            mapYSize = (this.random.nextInt(1) + 4);
            numEnemies = (this.random.nextInt(2) + 5);
        }

        else if (this.levelNum == 7)
        {
            mapXSize = (this.random.nextInt(3) + 5);
            mapYSize = (this.random.nextInt(2) + 5);
            //numEnemies = (this.random.nextInt(3) + 5);
        }

        else if (this.levelNum == 8)
        {
            mapXSize = (this.random.nextInt(3) + 6);
            mapYSize = (this.random.nextInt(2) + 5);
            numEnemies = (this.random.nextInt(3) + 7);
        }

        //Set the map
        this.currentLevel = new LevelContainer(this.window, mapXSize, mapYSize, numEnemies, this.seed);
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
        }
        else
        {
            gameMusicHandler.resume();

            //Tests to see if you are on a UI SCreen - if so update that screen
            if (uiManager.isOnUIScreen())
            {
                uiManager.update();

                if(uiManager.hideUI())
                {
                    uiManager.changeState();
                    uiManager.resetFlags();
                }
            }

            //In an arena
            else
            {
                if (currentLevel.update()) //This runs the method, regardless, but if it returns true do the following
                {
                    this.levelNum++;

                    //WILL NEED TO CHANGE THIS SO THAT IT ACTUALLY LOADS AFTER EVERY 3 ROUNDS
                    if (levelNum == 3 || levelNum == 6)
                    {
                        uiManager.changeState();
                        uiManager.displayShop();
                        return;
                    }


                    if (this.levelNum >= this.maxLevel)
                    {
                        levelNum = 0;
                        uiManager.changeState();
                        uiManager.displayEndScreen();
                    }

                    else
                    {
                        createLevel();
                        currentLevel.createLevel();
                    }

                }
            }
        }
    }

    public void pause(){
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
