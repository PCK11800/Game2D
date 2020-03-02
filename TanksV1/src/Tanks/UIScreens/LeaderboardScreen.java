package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.Leaderboard;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;
import org.jsfml.graphics.Color;

public class LeaderboardScreen extends UIScreen
{
    private Window window;
    private Leaderboard leaderboard;

    public LeaderboardScreen(Window window)
    {
        super(window);
        leaderboard = new Leaderboard();
        this.window = window;
    }

    /**
     * This function must be called after the main menu has been created
     */
    public void initBackButton(MainMenu menu)
    {
        float width = window.getWidth();
        float height = window.getHeight();

        float centerX = width / 2;

        //title text
        addText(centerX - 300, 50, "LEADERBOARD", 60, FontPath.PIXEL, Color.WHITE);

        //title text
        addText(centerX - 175, 300, leaderboard.getString(1), 40, FontPath.MONTSERRAT, Color.WHITE);
        addText(centerX - 175, 350, leaderboard.getString(2), 40, FontPath.MONTSERRAT, Color.WHITE);
        addText(centerX - 175, 400, leaderboard.getString(3), 40, FontPath.MONTSERRAT, Color.WHITE);
        addText(centerX - 175, 450, leaderboard.getString(4), 40, FontPath.MONTSERRAT, Color.WHITE);
        addText(centerX - 175, 500, leaderboard.getString(5), 40, FontPath.MONTSERRAT, Color.WHITE);
        addText(centerX - 175, 550, leaderboard.getString(6), 40, FontPath.MONTSERRAT, Color.WHITE);
        addText(centerX - 175, 600, leaderboard.getString(7), 40, FontPath.MONTSERRAT, Color.WHITE);
        addText(centerX - 175, 650, leaderboard.getString(8), 40, FontPath.MONTSERRAT, Color.WHITE);
        addText(centerX - 175, 700, leaderboard.getString(9), 40, FontPath.MONTSERRAT, Color.WHITE);
        addText(centerX - 175, 750, leaderboard.getString(10), 40, FontPath.MONTSERRAT, Color.WHITE);

        //Back button - returns to the main menu
        addLoadUIScreenButton(width - 250, height - 200, 400, 125, Textures.BACK, Textures.BACK_HOVER, Textures.BACK_CLICKED, menu);
    }

}
