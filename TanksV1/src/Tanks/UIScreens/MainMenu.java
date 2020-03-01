package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;

public class MainMenu extends UIScreen
{
    public MainMenu(Window window, LeaderboardScreen leaderboard, TutorialScreen tutorial, StoryScreen intro)
    {
        super(window);

        float centerX = window.getWidth() / 2;
        float centerY = window.getHeight() / 2;

        //Play
        addLoadUIScreenButton(centerX, (centerY - 200), 400, 125, Textures.NEWGAME, Textures.NEWGAME_HOVER, Textures.NEWGAME_CLICKED, intro);
        //Leaderboard
        addLoadUIScreenButton(centerX, centerY, 400, 125, Textures.LEADERBOARD, Textures.LEADERBOARD_HOVER, Textures.LEADERBOARD_CLICKED, leaderboard);
        //Tutorial
        addLoadUIScreenButton(centerX, (centerY + 200), 400, 125, Textures.TUTORIAL, Textures.TUTORIAL_HOVER, Textures.TUTORIAL_CLICKED, tutorial);
        //Quit
        addQuitButton(centerX, (centerY + 400), 400, 125, Textures.EXITGAME, Textures.EXITGAME_HOVER, Textures.EXITGAME_CLICKED);
    }
}
