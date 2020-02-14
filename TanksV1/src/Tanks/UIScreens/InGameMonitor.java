package Tanks.UIScreens;

import Tanks.Window.Window;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Text;

public class InGameMonitor {

    private int[] currentData;
    private Text healthText, enemyText;
    private Window window;

    public InGameMonitor(Window window)
    {
        this.window = window;
        iniTexts();
        currentData = new int[2];
    }

    private void iniTexts()
    {
        healthText = new Text();
        enemyText = new Text();
    }

    private void printHealth(int health)
    {
        healthText.setString("Health: " + health);
        healthText.setFont(new GameFont(FontPath.MONTSERRAT));

        if(health >= 66) { healthText.setColor(Color.GREEN); }
        if(health >= 33 && health < 66) { healthText.setColor(Color.YELLOW); }
        else if(health < 33) { healthText.setColor(Color.RED); }

        if(health < 0) { healthText.setString("Health: " + 0); }
        
        healthText.setCharacterSize(30);
        healthText.setPosition(0, 5);

        window.draw(healthText);
    }

    private void printAmountOfEnemiesLeft(int amount)
    {
        enemyText.setString("Enemies Left: " + amount);
        enemyText.setFont(new GameFont(FontPath.MONTSERRAT));
        enemyText.setColor(Color.WHITE);
        enemyText.setCharacterSize(30);
        enemyText.setPosition(200, 5);

        window.draw(enemyText);
    }

    public void updateMonitor()
    {
        printHealth(currentData[0]);
        printAmountOfEnemiesLeft(currentData[1]);
    }

    public void setCurrentData(int i, int data) { currentData[i] = data; }
    public int[] getCurrentData() { return currentData; }

}
