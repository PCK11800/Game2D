package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;

public class MainMenu extends UIScreen
{
    public MainMenu(Window window)
    {
        super(window);

        float centerX = window.getWidth() / 2;
        float centerY = window.getHeight() / 2;

        //Play
        addGameModeButton(centerX, (centerY - 200), 400, 125, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
        //Leaderboard
        addLoadUIScreenButton(centerX, centerY, 400, 125, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
        //Tutorial
        addLoadUIScreenButton(centerX, (centerY + 200), 400, 125, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
        //Quit
        addQuitButton(centerX, (centerY + 400), 400, 125, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
    }
}
