package Tanks.Objects;

import Tanks.Listeners.PauseListener;
import Tanks.UIScreens.ShopScreen;
import Tanks.Window.Window;

import java.util.ArrayList;
import java.util.Random;

public class GameMode
{
    private ArrayList<LevelContainer> levels = new ArrayList<LevelContainer>();
    private Window window;
    private UIScreenManager uiManager;
    private PauseListener pauseListener;
    private LevelContainer currentLevel;

    private int currentIndex = 0;

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

        setLevels();
        initGameMode();

        this.random = new Random(this.seed);
    }


    /**
     *This method is used to create all of the levels for the single player gameMode
     * IT WOULD BE BETTER TO MAKE A FUNCTION THAT GENERATES LEVELS ON THE FLY RATHER THAN CREATING THEM ALL AT RUNTIME
     */
    public void setLevels()
    {
        Random rand = new Random(this.seed);
        //Round 1
        levels.add(new LevelContainer(this.window, 2, 2, 1, this.seed));
        levels.add(new LevelContainer(this.window, 3, 2, 2, this.seed));
        levels.add(new LevelContainer(this.window, (rand.nextInt(1) + 3), 3, (rand.nextInt(1) + 2), this.seed));

    /*
        //Round 2
        levels.add(new LevelContainer(this.window,  (rand.nextInt(1) + 3),  (rand.nextInt(1) + 2), (rand.nextInt(1) + 3), this.seed));
        levels.add(new LevelContainer(this.window,  (rand.nextInt(2) + 3), (rand.nextInt(2) + 2), (rand.nextInt(1) + 4), this.seed));
        levels.add(new LevelContainer(this.window,  (rand.nextInt(3) + 4), (rand.nextInt(3) + 3), (rand.nextInt(1) + 5), this.seed));

        //Round 3
        levels.add(new LevelContainer(this.window,  (rand.nextInt(3) + 5),  (rand.nextInt(1) + 4), (rand.nextInt(2) + 6), this.seed));
        levels.add(new LevelContainer(this.window,  (rand.nextInt(2) + 6), (rand.nextInt(2) + 4), (rand.nextInt(2) + 7), this.seed));
        levels.add(new LevelContainer(this.window,  (rand.nextInt(2) + 7), (rand.nextInt(3) + 5), (rand.nextInt(2) + 8), this.seed));
        */
        currentLevel = levels.get(0);
    }

    /**
     * This method loads the first level
     */
    public void initGameMode()
    {
        currentLevel.createLevel();
    }

    /**
     * This method updates the level and everything in it.
     * It also handles the logic for loading a new level if it is required
     */
    public void update()
    {
        //Pause
        pauseListener.handlePause();
        if(!paused){
            //Tests to see if you are on a UISCreen - if so update that screen
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
                    //WILL NEED TO CHANGE THIS SO THAT IT ACTUALLY LOADS AFTER EVERY 3 ROUNDS
                    if ((currentIndex % 3) == 0)
                    {
                        uiManager.changeState();
                        uiManager.displayShop();
                    }

                    currentIndex++;

                    currentLevel = levels.get(currentIndex);

                    //This improves ram usage - removes the last maps references, so it is garbage collected
                    levels.remove(currentIndex -1);
                    currentIndex--;

                    currentLevel.createLevel();
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
}
