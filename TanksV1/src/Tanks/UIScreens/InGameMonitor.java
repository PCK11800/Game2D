package Tanks.UIScreens;

import Tanks.ObjectComponents.RotatingObject;
import Tanks.ObjectComponents.Textures;
import Tanks.Window.Window;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Text;

public class InGameMonitor {

    private int[] currentData;
    private Text enemyText;
    private Window window;

    //Health Bar
    private RotatingObject healthBar_container = new RotatingObject();
    private RotatingObject healthBar_bar = new RotatingObject();
    private RotatingObject skull = new RotatingObject();

    //Enemy tanks left
    private RotatingObject enemytankicon = new RotatingObject();

    public InGameMonitor(Window window)
    {
        this.window = window;
        iniComponents();
        currentData = new int[2];
    }

    private void iniComponents()
    {
        iniHealthBar();
        iniEnemyLeft();
    }

    private void iniHealthBar()
    {
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

    private void iniEnemyLeft()
    {
        enemytankicon.setObjectTexture(Textures.ENEMYTANKICON);
        enemytankicon.setLocation(240, 0);
        enemytankicon.setSize(240, 60);
        enemyText = new Text();
        enemyText.setPosition(290, 20);
        enemyText.setFont(new GameFont(FontPath.PIXEL));
        enemyText.setCharacterSize(15);
        enemyText.setColor(Color.WHITE);
    }
    private void printEnemyLeft(int enemyLeft)
    {
        enemyText.setString("X" + enemyLeft);
        enemytankicon.draw(window);
        window.draw(enemyText);
    }

    public void updateMonitor()
    {
        printHealthBar(currentData[0]);
        printEnemyLeft(currentData[1]);
    }

    public void setCurrentData(int i, int data) { currentData[i] = data; }
    public int[] getCurrentData() { return currentData; }

}
