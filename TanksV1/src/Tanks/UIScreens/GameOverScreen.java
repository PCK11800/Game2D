package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;

public class GameOverScreen extends UIScreen
{
    private Window window;

    public GameOverScreen(Window window)
    {
        super(window);
        this.window = window;
    }

    /**
     * This function must be called after the main menu has been created
     */
    public void initBackButton(MainMenu menu)
    {
        float width = window.getWidth();
        float height = window.getHeight();

        //Back button - returns to the main menu
        addLoadUIScreenButton(width - 200, height - 110, 400, 125, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED, menu);
    }
}
