package Tanks.Objects;

import Tanks.Window.Window;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class GameMode
{
    private LinkedList<LevelContainer> levels = new LinkedList<LevelContainer>();
    private Window window;
    private LevelContainer currentLevel;
    private int currentIndex = 0;

    private Random random;
    private long seed;

    /**
     * The constructor
     * @param w the window for everything to be drawn into
     */
    public GameMode(Window w, long seed)
    {
        this.window = w;
        this.seed = seed;
        setLevels();
        initGameMode();

        this.random = new Random(this.seed);
    }


    /**
     *This method is used to create all of the levels in a given game mode.
     * This could be changed in children of this class
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
        if (currentLevel.update())
        {
            currentIndex++;
            //If you have fished / begun a new round
            if (currentIndex % 3 == 0)
            {
                //Load story panel and then load shop, and finally boss level
                System.out.println("LOAD SHOP HERE");
            }
            currentLevel = levels.get(currentIndex);
            currentLevel.createLevel();
        }
    }
}
