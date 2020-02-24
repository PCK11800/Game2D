package Tanks.Listeners;

import Tanks.Objects.GameMode;
import Tanks.UIScreens.FontPath;
import Tanks.UIScreens.GameFont;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Text;
import org.jsfml.system.Clock;
import org.jsfml.window.Keyboard;

public class PauseListener
{

    private GameMode gameMode;
    private Clock pauseClock = new Clock();


    public PauseListener(GameMode gameMode)
    {
        this.gameMode = gameMode;
    }


    public void handlePause()
    {
        if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE))
        {
            if(pauseClock.getElapsedTime().asMilliseconds() >= 100)
            {
                if (gameMode.getStatus())
                {
                    gameMode.unpause();
                }

                else
                {
                    gameMode.pause();
                }
            }
            pauseClock.restart();
        }
    }
}
