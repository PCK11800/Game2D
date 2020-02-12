package Tanks.ObjectComponents;

import Tanks.Window.Window;

public class DeadTank{

    Window window;
    RotatingObject dead_hull = new RotatingObject();
    RotatingObject dead_turret = new RotatingObject();

    public DeadTank(Window window, float[] deathData)
    {
        this.window = window;
        dead_hull.setObjectTexture(Textures.TANKHULL_DEAD);
        dead_turret.setObjectTexture(Textures.TANKTURRET_DEAD);
        dead_hull.setLocation(deathData[0], deathData[1]);
        dead_turret.setLocation(deathData[2], deathData[3]);
        dead_hull.setSize(deathData[4] * 53, deathData[5] * 75); //dead hull
        dead_turret.setSize(deathData[4] * 53, deathData[5] * 75);
        dead_hull.rotateObject(deathData[6]);
        dead_turret.rotateObject(deathData[7]);
    }

    public void update()
    {
        dead_hull.draw(window);
        dead_turret.draw(window);
    }
}
