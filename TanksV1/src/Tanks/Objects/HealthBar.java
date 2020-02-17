package Tanks.Objects;

import Tanks.ObjectComponents.RotatingObject;
import Tanks.ObjectComponents.Textures;
import Tanks.Window.Window;

public class HealthBar extends RotatingObject {

    private Tank tank;
    private int startingHealth;
    private Window window;

    public HealthBar(Window window, Tank tank)
    {
        this.tank = tank;
        this.window = window;
        startingHealth = tank.getHealth();
        setObjectTexture(Textures.ENEMYHEALTH_FULL);
        setLocation(tank.getXPos() - (tank.hull.getWidth()) , tank.getYPos() - ((tank.hull.getWidth() / 2) * 3));
        draw(window);
    }

    public void update()
    {
        int health = tank.getHealth();
        setLocation(tank.getXPos() - (tank.hull.getWidth()), tank.getYPos() - ((tank.hull.getWidth() / 2) * 3));
        if (health < startingHealth)
        {
            if (health > (startingHealth / 2) && health <= (startingHealth / 4) * 3)
            {
                setObjectTexture(Textures.ENEMYHEALTH_QUARTERDAMAGE);
            }
            else if (health > (startingHealth / 4) && health <= (startingHealth / 2))
            {
                setObjectTexture(Textures.ENEMYHEALTH_HALFDAMAGE);
            }
            else if (health > 0 && health < (startingHealth / 4))
            {
                setObjectTexture(Textures.ENEMYHEALTH_NEARDEATH);
            }
        }
        draw(window);
    }
}
