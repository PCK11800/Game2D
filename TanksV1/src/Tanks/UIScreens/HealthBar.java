package Tanks.UIScreens;

import Tanks.ObjectComponents.RotatingObject;
import Tanks.ObjectComponents.Textures;
import Tanks.Objects.Tank;
import Tanks.Window.Window;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Text;

public class HealthBar extends RotatingObject {

    private Tank tank;
    private int startingHealth;
    private Window window;
    private Color healthBarColor;
    private Text text = null;
    private int offset = 0;

    public HealthBar(Window window, Tank tank, String name)
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

        //name
        if (!name.equals("")) {
            text = new Text();
            text.setPosition(tank.getXPos() - ((tank.getHull().getWidth() / 3) * 2), (float) (tank.getYPos() - (tank.getHull().getWidth() * 1.4)));
            text.setFont(new GameFont(FontPath.PIXEL));
            text.setCharacterSize(16);
            text.setColor(Color.WHITE);
            text.setString(name);
            window.draw(text);
            if (name.length() > 5) offset = 6 * (name.length() - 5);
        }

        draw(window);

    }

    public void update()
    {
        setLocation(tank.getXPos() - (tank.getHull().getWidth()), tank.getYPos() - ((tank.getHull().getWidth() / 2) * 3));
        setTextureRect(new IntRect(0, 0, 110 * tank.getHealth()/startingHealth, 67));

        healthBarColor = new Color((255 * (startingHealth - tank.getHealth()))/startingHealth,  (255 * tank.getHealth()) / startingHealth,0);

        setColor(healthBarColor);
        draw(window);
        if (text != null)
        {
            text.setPosition(tank.getXPos() - ((tank.getHull().getWidth() / 3) * 2) - offset , (float) (tank.getYPos() - (tank.getHull().getWidth() * 1.4)));
            window.draw(text);
        }
    }
}
