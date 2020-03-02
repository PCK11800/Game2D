package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;
import org.jsfml.graphics.Color;

import java.awt.*;

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
        float width = window.getSize().x;
        float height =  window.getSize().y;

        float centerX = width / 2;

        //Back button - returns to the main menu
        addLoadUIScreenButton(width - 250, height - 100, 400, 125, Textures.CONTINUE, Textures.CONTINUE_HOVER, Textures.CONTINUE_CLICKED, menu);

        addText(centerX - 350, 100, "GAME OVER!", 80, FontPath.PIXEL, Color.WHITE);
    }
}
