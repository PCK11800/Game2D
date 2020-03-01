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
        float musicVolumeControl_x = centerX;
        float musicVolumeControl_y = centerY - 20;
        addText(musicVolumeControl_x - 170, musicVolumeControl_y - 60, "Music Volume", 30, FontPath.PIXEL, Color.WHITE);
        addSoundText(musicVolumeControl_x - 85, musicVolumeControl_y - 10, 30, FontPath.PIXEL, Color.WHITE);
        addSoundButton(musicVolumeControl_x + 120, musicVolumeControl_y, 53, 53, "increase_music_volume", Textures.RIGHT_ARROW, Textures.RIGHT_ARROW_HOVERED, Textures.RIGHT_ARROW_CLICKED);
        addSoundButton(musicVolumeControl_x - 120, musicVolumeControl_y, 53, 53, "decrease_music_volume", Textures.LEFT_ARROW, Textures.LEFT_ARROW_HOVERED, Textures.LEFT_ARROW_CLICKED);

        //Game Volume

    }
}