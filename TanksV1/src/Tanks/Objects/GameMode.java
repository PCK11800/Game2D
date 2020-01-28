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

    public GameMode(Window w)
    {
        this.window = w;
        setStages();
        initGameMode();
    }

    public void setStages()
    {
        levels.add(new LevelContainer(this.window, 3, 3));
        levels.add(new LevelContainer(this.window, 8, 4));

        currentLevel = levels.get(0);
    }

    public void initGameMode()
    {
        currentLevel.createLevel();
    }

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
