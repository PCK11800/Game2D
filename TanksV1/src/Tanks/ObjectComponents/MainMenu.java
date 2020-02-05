package Tanks.ObjectComponents;

import Tanks.Objects.UIScreen;
import Tanks.Window.Window;
import org.jsfml.graphics.Texture;

public class MainMenu extends UIScreen
{
    public MainMenu(Window window)
    {
        super(window);

        float centerX = window.getWidth() / 2;
        float centerY = window.getHeight() / 2;

        //Play
        addButton(centerX, centerY - 200, 400, 100, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
        //Leaderboard
        addButton(centerX, centerY, 400, 100, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
        //Tutorial
        addButton(centerX, centerY + 200, 400, 100, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
        //Quit
        addButton(centerX, centerY + 400, 400, 100, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);

    }
}
