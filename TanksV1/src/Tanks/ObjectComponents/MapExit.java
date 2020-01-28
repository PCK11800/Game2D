package Tanks.ObjectComponents;

import Tanks.Window.Window;

/**
 * This class represents the exit of each map.
 * The class has two different behaviours - locked and unlocked.
 * When locked the exit will act the same as a standard wall.
 * However, when unlocked, if the player collides with it the player will load into the next level
 */
public class MapExit extends MapObject
{
    private boolean isLocked = true;

    private String unlockedTexture;


    public MapExit(Window window, float x, float y, float width, float height, String lockedTexture, String unlockedTexture)
    {
        super(window, x, y, width, height, lockedTexture); //The locked texture is assumed to be its default / initial texture
        this.unlockedTexture = unlockedTexture;
        unlockExit();
    }


    public void update()
    {
        draw(window);
    }


    public void unlockExit()
    {
        isLocked = false;
        setObjectTexture(unlockedTexture);
    }


    public boolean getLockedStatus() { return isLocked; }
}