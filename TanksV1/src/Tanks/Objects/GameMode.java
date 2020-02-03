package Tanks.Objects;

import Tanks.Window.Window;
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
        levels.add(new LevelContainer(this.window, 2, 2, 3, this.seed));
        levels.add(new LevelContainer(this.window, 8, 4, 4, this.seed));

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
            currentLevel = levels.get(currentIndex);
            currentLevel.createLevel();
        }
    }
}
