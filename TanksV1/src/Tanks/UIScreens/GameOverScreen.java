package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.Leaderboard;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;
import org.jsfml.graphics.Color;

public class GameOverScreen extends UIScreen
{
    private Window window;
    private Leaderboard leaderboard;
    private String name;
    private int score;

    public GameOverScreen(Window window)
    {
        super(window);
        Leaderboard leaderboard = new Leaderboard();
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
        addText(centerX - 300, 50, "GAME OVER", 60, FontPath.PIXEL, Color.WHITE);

        //Back button - returns to the main menu
        addLoadUIScreenButton(width - 200, height - 110, 400, 125, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED, menu);
    }
}
