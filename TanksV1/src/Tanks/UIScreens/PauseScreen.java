package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;
import org.jsfml.graphics.Color;

public class PauseScreen extends UIScreen
{
    public PauseScreen(Window window)
    {
        super(window);

        float centerX = window.getWidth() / 2;
        float centerY = window.getHeight() / 2;

        //Continue
        addResumeButton(centerX, (centerY - 200), 400, 125, Textures.RESUME, Textures.RESUME_HOVER, Textures.RESUME_CLICKED);
        //Quit
        addQuitButton(centerX, (centerY + 200), 400, 125, Textures.EXITGAME, Textures.EXITGAME_HOVER, Textures.EXITGAME_CLICKED);

        //Text
        addText(centerX - 175, 50, "PAUSED", 60, FontPath.PIXEL, Color.WHITE);
    }
}