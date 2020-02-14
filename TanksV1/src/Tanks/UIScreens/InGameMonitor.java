package Tanks.UIScreens;

import Tanks.ObjectComponents.RotatingObject;
import Tanks.ObjectComponents.Textures;
import Tanks.Window.Window;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Text;

public class InGameMonitor {

    private int[] currentData;
    private Text healthText, enemyText;
    private Window window;

    //Health Bar
    private RotatingObject healthBar_container = new RotatingObject();
    private RotatingObject healthBar_bar = new RotatingObject();
    private RotatingObject skull = new RotatingObject();

    public InGameMonitor(Window window)
    {
        this.window = window;
        iniComponents();
        currentData = new int[2];
    }

    private void iniComponents()
    {
        healthText = new Text();
        enemyText = new Text();

        healthBar_container.setObjectTexture(Textures.HEALTHBAR_CONTAINER);
        healthBar_bar.setObjectTexture(Textures.HEALTHBAR_BAR);
        skull.setObjectTexture(Textures.SKULL);
        healthBar_container.setLocation(0, 0);
        healthBar_container.setSize(240, 60);
        healthBar_bar.setTextureRect(new IntRect(0, 0, 150, 50));
        healthBar_bar.setLocation(0, 0);
        healthBar_bar.setSize(240, 60);
        skull.setLocation(0, 0);
        skull.setSize(240, 60);
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

    private void printHealthBar(int health)
    {
        healthBar_bar.setTextureRect(new IntRect(0, 0, 20 + (130 * health/100), 50));
        if(health <= 0){
            skull.draw(window);
        }
        else{
            healthBar_container.draw(window);
            healthBar_bar.draw(window);
        }
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
        printHealthBar(currentData[0]);
        //printAmountOfEnemiesLeft(currentData[1]);
    }

    public void setCurrentData(int i, int data) { currentData[i] = data; }
    public int[] getCurrentData() { return currentData; }

}
