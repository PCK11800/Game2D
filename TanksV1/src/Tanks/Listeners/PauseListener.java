package Tanks.Listeners;

import Tanks.Objects.GameMode;
import Tanks.UIScreens.FontPath;
import Tanks.UIScreens.GameFont;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Text;
import org.jsfml.system.Clock;
import org.jsfml.window.Keyboard;

public class PauseListener {

    private GameMode gameMode;
    private Clock pauseClock = new Clock();
    private Text pauseText;

    public PauseListener(GameMode gameMode)
    {
        this.gameMode = gameMode;
        iniPauseText();
    }

    public void handlePause()
    {
        if(gameMode.getStatus()){
            gameMode.getWindow().draw(pauseText);
        }
        if (Keyboard.isKeyPressed(Keyboard.Key.ESCAPE)) {
            if(pauseClock.getElapsedTime().asMilliseconds() >= 100) {
                if (gameMode.getStatus()) {
                    gameMode.unpause();
                } else {
                    gameMode.pause();
                }
            }
            pauseClock.restart();
        }
    }

    public void iniPauseText(){
        pauseText = new Text();
        pauseText.setPosition(10, 20);
        pauseText.setFont(new GameFont(FontPath.PIXEL));
        pauseText.setCharacterSize(30);
        pauseText.setColor(Color.WHITE);
        pauseText.setString("GAME IS PAUSED. ESC TO CONTINUE.");
    }
}
