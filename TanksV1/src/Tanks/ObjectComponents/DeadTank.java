package Tanks.ObjectComponents;

import Tanks.Window.Window;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Clock;

public class DeadTank{

    private Window window;
    private RotatingObject dead_hull = new RotatingObject();
    private RotatingObject dead_turret = new RotatingObject();
    private RotatingObject explosion = new RotatingObject();

    private int frame = 0;
    private Clock animClock = new Clock();
    private boolean explodedOnce = false;

    public DeadTank(Window window, float[] deathData)
    {
        this.window = window;
        //Explosion
        explosion.setObjectTexture(Textures.EXPLOSION);
        explosion.setTextureRect(new IntRect(0, 0, 64, 64));
        explosion.setLocation(deathData[0] - (53 * deathData[4]), deathData[1] - (75 * deathData[5]));
        System.out.println(deathData[0] + ", " + deathData[1]);
        System.out.println(explosion.xPos + ", " + explosion.yPos);
        explosion.setSize(800, 500);

        //Dead tank
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

        if(!explodedOnce){
            explosion.draw(window);

            if(animClock.getElapsedTime().asMilliseconds() >= 50)
            {
                animClock.restart();
                frame++;

                if(frame > 31)
                {
                    frame = 0;
                    explodedOnce = true;
                }

                int frameRow = frame / 8;
                int frameCol = frame % 8;
                explosion.setTextureRect(new IntRect(frameCol * 64, frameRow * 64, 64, 64));
            }
        }
    }
}
