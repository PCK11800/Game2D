package Tanks.Objects;

import Tanks.UIScreens.ShopScreen;
import Tanks.Window.Window;

import java.util.ArrayList;
import java.util.Random;

public class GameMode
{
    private ArrayList<LevelContainer> levels = new ArrayList<LevelContainer>();
    private Window window;
    private UIScreenManager uiManager;
    private LevelContainer currentLevel;

    private int currentIndex = 0;

    private Random random;
    private long seed;

    /**
     * The constructor
     * @param window the window for everything to be drawn into
     */
    public GameMode(Window window, long seed)
    {
        this.window = window;
        this.uiManager = new UIScreenManager(window);
        this.seed = seed;

        setLevels();
        initGameMode();

        this.random = new Random(this.seed);
    }


    /**
     *This method is used to create all of the levels for the single player gameMode
     */
    public void setLevels()
    {
        Random rand = new Random(this.seed);
        //Round 1
        levels.add(new LevelContainer(this.window, 2, 2, 1, this.seed));
        levels.add(new LevelContainer(this.window, 3, 2, 2, this.seed));
        levels.add(new LevelContainer(this.window, (rand.nextInt(1) + 3), 3, (rand.nextInt(1) + 2), this.seed));

        //Round 2
        levels.add(new LevelContainer(this.window,  (rand.nextInt(1) + 3),  (rand.nextInt(1) + 2), (rand.nextInt(1) + 3), this.seed));
        levels.add(new LevelContainer(this.window,  (rand.nextInt(2) + 3), (rand.nextInt(2) + 2), (rand.nextInt(1) + 4), this.seed));
        levels.add(new LevelContainer(this.window,  (rand.nextInt(3) + 4), (rand.nextInt(3) + 3), (rand.nextInt(1) + 5), this.seed));

        //Round 3
        levels.add(new LevelContainer(this.window,  (rand.nextInt(3) + 5),  (rand.nextInt(1) + 4), (rand.nextInt(2) + 6), this.seed));
        levels.add(new LevelContainer(this.window,  (rand.nextInt(2) + 6), (rand.nextInt(2) + 4), (rand.nextInt(2) + 7), this.seed));
        levels.add(new LevelContainer(this.window,  (rand.nextInt(2) + 7), (rand.nextInt(3) + 5), (rand.nextInt(2) + 8), this.seed));

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
        if (uiManager.isOnUIScreen())
        {
            uiManager.update();

            if(uiManager.hideUI())
            {
                uiManager.changeState();
                uiManager.resetFlags();
            }
        }

        else
        {
            if (currentLevel.update()) //This runs the method, regardless, but if it returns true do the following
            {
                currentIndex++;

                //PROBLEM HERE! - IT WILL ONLY EVER LOAD THE FIRST SHOP -
                if ((currentIndex+1) % 3 == 0)
                {
                    uiManager.changeState();
                    uiManager.displayShop();
                }

                currentLevel = levels.get(currentIndex);
                currentLevel.createLevel();

            }
        }
    }
}
