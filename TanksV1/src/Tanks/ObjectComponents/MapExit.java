package Tanks.ObjectComponents;

import Tanks.Window.Window;

/**
 * This class represents the exit of each map.
 * The class has two different behaviours - locked and unlocked.
 * When locked the exit will act the same as a standard wall.
 * However, when unlocked, if the player collides with it the player will load into the next level
 */
public class MapExit extends RotatingObject
{
    protected Window window;
    private boolean isLocked = true;

    private String lockedTexture;
    private String unlockedTexture;


    public MapExit(Window window, float x, float y, float width, float height, String lockedTexture, String unlockedTexture)
    {
        this.window = window;
        this.lockedTexture = lockedTexture;
        this.unlockedTexture = unlockedTexture;
        setObjectTexture(lockedTexture);
        setLocation(x, y);
        setSize(width, height);
    }

    public void update()
    {
        draw(window);
    }


    public void displayUnlocked()
    {
        setObjectTexture(unlockedTexture);
    }

    public boolean getLockedStatus() { return isLocked; }
    public void setLockedStatus(boolean status) { isLocked = status; }

}
