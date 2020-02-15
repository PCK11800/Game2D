package Tanks.ObjectComponents;

import Tanks.Sounds.GameSound;
import Tanks.Sounds.SoundsPath;
import Tanks.Window.Window;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Clock;

import javax.swing.plaf.TextUI;

public class DeadTank{

    private Window window;
    private RotatingObject dead_hull = new RotatingObject();
    private RotatingObject dead_turret = new RotatingObject();
    private RotatingObject explosion = new RotatingObject();
    private RotatingObject fire = new RotatingObject();
    private GameSound explosion_sound = new GameSound(SoundsPath.EXPLOSION);

    public DeadTank(Window window, float[] deathData)
    {
        this.window = window;
        //Explosion
        explosion.setObjectTexture(Textures.EXPLOSION);
        explosion.setTextureRect(new IntRect(0, 0, 64, 64));
        explosion.setLocation(deathData[0] - (53 * deathData[4]), deathData[1] - (75 * deathData[5]));
        explosion.setSize(800, 500);

        //Fire
        fire.setObjectTexture(Textures.FIRE);
        fire.setTextureRect(new IntRect(0, 0, 64, 64));
        fire.setLocation(deathData[0] - (53 * deathData[4]), deathData[1] - (75 * deathData[5]));
        fire.setSize(1000, 600);

        //Dead tank
        dead_hull.setObjectTexture(Textures.TANKHULL_DEAD);
        dead_turret.setObjectTexture(Textures.TANKTURRET_DEAD);
        dead_hull.setLocation(deathData[0], deathData[1]);
        dead_turret.setLocation(deathData[2], deathData[3]);
        dead_hull.setSize(deathData[4] * 53, deathData[5] * 75); //dead hull
        dead_turret.setSize(deathData[4] * 53, deathData[5] * 75);
        dead_hull.rotateObject(deathData[6]);
        dead_turret.rotateObject(deathData[7]);

        explosion_sound.play();
    }

    public void update()
    {
        dead_hull.draw(window);
        dead_turret.draw(window);
        fire_animation();

        if(!explodedOnce){
            explosion_sound.setVolume(50);
            explosion_animation();
        }
    }

    private int frame_explosion = 0;
    private Clock animClock_explosion = new Clock();
    private boolean explodedOnce = false;
    private void explosion_animation()
    {
        explosion.draw(window);

        if(animClock_explosion.getElapsedTime().asMilliseconds() >= 50) {
            animClock_explosion.restart();
            frame_explosion++;

            if (frame_explosion > 31) {
                frame_explosion = 0;
                explodedOnce = true;
            }

            int frameRow = frame_explosion / 8;
            int frameCol = frame_explosion % 8;
            explosion.setTextureRect(new IntRect(frameCol * 64, frameRow * 64, 64, 64));
        }
    }

    private int frame_fire = 0;
    private Clock animClock_fire = new Clock();
    private void fire_animation()
    {
        fire.draw(window);

        if(animClock_fire.getElapsedTime().asMilliseconds() >= 10) {
            animClock_fire.restart();
            frame_fire++;

            if(frame_fire > 59) {
                frame_fire = 0;
            }

            int frameRow = frame_fire / 10;
            int frameCol = frame_fire % 10;
            fire.setTextureRect(new IntRect(frameCol * 64, frameRow * 64, 64, 64));
        }
    }
}
