package Tanks.UIScreens;

import Tanks.ObjectComponents.RotatingObject;
import Tanks.ObjectComponents.Textures;
import Tanks.Objects.Tank;
import Tanks.Window.Window;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.IntRect;

public class HealthBar extends RotatingObject {

    private Tank tank;
    private int startingHealth;
    private Window window;
    private Color healthBarColor;

    public HealthBar(Window window, Tank tank)
    {
        this.tank = tank;
        this.window = window;
        startingHealth = tank.getHealth();
        setObjectTexture(Textures.ENEMYHEALTH_FULL);
        healthBarColor = new Color(0, 255, 0);
        setLocation(tank.getXPos() - (tank.getHull().getWidth()) , tank.getYPos() - ((tank.getHull().getWidth() / 2) * 3));
        setTextureRect(new IntRect(0, 0, 110, 67));
        setSize(100, 67);
        setColor(healthBarColor);
        draw(window);
    }

    public void update()
    {
        setLocation(tank.getXPos() - (tank.getHull().getWidth()), tank.getYPos() - ((tank.getHull().getWidth() / 2) * 3));
        setTextureRect(new IntRect(0, 0, 110 * tank.getHealth()/startingHealth, 67));

        healthBarColor = new Color((255 * (startingHealth - tank.getHealth()))/startingHealth,  (255 * tank.getHealth()) / startingHealth,0);

        setColor(healthBarColor);
        draw(window);
    }
}
