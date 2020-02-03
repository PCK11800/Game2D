package Tanks.Objects;

import Tanks.Window.Window;
import java.util.LinkedList;

public class GameMode
{
    private LinkedList<LevelContainer> levels = new LinkedList<LevelContainer>();
    private Window window;
    private LevelContainer currentLevel;
    private int currentIndex = 0;
    //Maybe create the random object here so that you can seed it here?

    /**
     * The constructor
     * @param w the window for everything to be drawn into
     */
    public GameMode(Window w)
    {
        this.window = w;
        setLevels();
        initGameMode();
    }


    /**
     *This method is used to create all of the levels in a given game mode.
     * This could be changed in children of this class
     */
    public void setLevels()
    {
        levels.add(new LevelContainer(this.window, 3, 3));
        levels.add(new LevelContainer(this.window, 8, 4));

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
