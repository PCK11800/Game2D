package Tanks.UIScreens;

import Tanks.Window.Window;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Text;

public class InGameMonitor {

    private int[] data;
    private int health;
    private Text healthText;
    private Window window;

    public InGameMonitor(Window window)
    {
        this.window = window;
        healthText = new Text();
    }

    private void printHealth(int health)
    {
        this.health = health;
        healthText.setString("Health: " + health);
        healthText.setFont(new GameFont(FontPath.MONTSERRAT));

        if(health >= 66) { healthText.setColor(Color.GREEN); }
        if(health >= 33 && health < 66) { healthText.setColor(Color.YELLOW); }
        else if(health < 33) { healthText.setColor(Color.RED); }

        healthText.setCharacterSize(30);
        healthText.setPosition(100, 0);
    }

    public void updateMonitor(int[] data)
    {
        printHealth(data[0]);
        window.draw(healthText);
    }

}
