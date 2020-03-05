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

        //column headings
        addText(centerX - 480, 200, "Rank", 40, FontPath.PIXEL, Color.WHITE);
        addText(centerX - 80, 200, "Name", 40, FontPath.PIXEL, Color.WHITE);
        addText(centerX + 320, 200, "Score", 40, FontPath.PIXEL, Color.WHITE);

        //leaderboard scores
        for (int i = 1; i <= 10; i++)
        {
            addText(centerX - 480, 200 + i * 50, String.valueOf(i), 40, FontPath.PIXEL, Color.WHITE);
            addText(centerX - 80, 200 + i * 50, leaderboard.getName(i), 40, FontPath.PIXEL, Color.WHITE);
            addText(centerX + 320, 200 + i * 50, String.valueOf(leaderboard.getScore(i)), 40, FontPath.PIXEL, Color.WHITE);
        }

        //Back button - returns to the main menu
        addLoadUIScreenButton(width - 250, height - 65, 400, 125, Textures.BACK, Textures.BACK_HOVER, Textures.BACK_CLICKED, menu);
    }

}
