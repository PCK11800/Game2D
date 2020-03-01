package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.UIScreen;
import Tanks.Sounds.GameMusicHandler;
import Tanks.Window.Window;
import org.jsfml.graphics.Color;

public class PauseScreen extends UIScreen
{
    public PauseScreen(Window window, GameMusicHandler handler)
    {
        super(window);
        setGameMusicHandler(handler);

        float centerX = window.getWidth() / 2;
        float centerY = window.getHeight() / 2;

        //Continue
        addResumeButton(centerX, (centerY - 200), 400, 125, Textures.RESUME, Textures.RESUME_HOVER, Textures.RESUME_CLICKED);
        //Quit
        addQuitButton(centerX, (centerY + 200), 400, 125, Textures.EXITGAME, Textures.EXITGAME_HOVER, Textures.EXITGAME_CLICKED);

        //Text
        addText(centerX - 175, 50, "PAUSED", 60, FontPath.PIXEL, Color.WHITE);

        //Music
        addSoundText(centerX - 175, window.getHeight() - 50, 60, FontPath.PIXEL, Color.WHITE);
        addSoundButton(centerX + 400, window.getHeight() - 50, 400, 125, "increase_volume", Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
        addSoundButton(centerX - 250, window.getHeight() - 50, 400, 125, "decrease_volume", Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
    }
}