package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.ObjectSizeHandler;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;

public class MainMenu extends UIScreen
{
    public MainMenu(Window window, LeaderboardScreen leaderboard, TutorialScreen tutorial, StoryScreen intro)
    {
        super(window);

        float centerX = window.getSize().x / 2;
        float centerY = window.getSize().y / 2;

        float[] scale = ObjectSizeHandler.scaleConstant();
        //Play
        addLoadUIScreenButton(centerX, (centerY - 200) * scale[1], 400, 125, Textures.NEWGAME, Textures.NEWGAME_HOVER, Textures.NEWGAME_CLICKED, intro);
        //Leaderboard
        addLoadUIScreenButton(centerX, centerY  * scale[1], 400, 125, Textures.LEADERBOARD, Textures.LEADERBOARD_HOVER, Textures.LEADERBOARD_CLICKED, leaderboard);
        //Tutorial
        addLoadUIScreenButton(centerX, (centerY + 200)  * scale[1], 400, 125, Textures.TUTORIAL, Textures.TUTORIAL_HOVER, Textures.TUTORIAL_CLICKED, tutorial);
        //Quit
        addQuitButton(centerX, (centerY + 400)  * scale[1], 400, 125, Textures.EXITGAME, Textures.EXITGAME_HOVER, Textures.EXITGAME_CLICKED);
    }
}