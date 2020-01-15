package Tanks.Objects;

import Tanks.ObjectComponents.RotatingObject;
import Tanks.ObjectComponents.Textures;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Opponent extends Tank {

    private int movementCount = 0;
    private double direction = 0;
    private Tank player;
    private float playerXPos;
    private float playerYPos;

    public Opponent(Tank player)
    {
        super();
        this.player = player;

    }

    public void update()
    {
        super.update();
        if (movementCount == 0)
        {
            for (int i  = 0; i< 100; i++) {

                rotateTurretRight();
            }



        }
        movementCount++;
        playerXPos = player.getXPos();
        playerYPos = player.getYPos();
        if (player.isAlive() && canHitPlayer()) shoot();
        /*if (direction == 0 || movementCount > 10)
        {
            direction = (Math.random() * ((8 - 1) + 1)) + 1;
            movementCount = 0;
        }
        switch ((int) direction)
        {
            case 1:
                moveForward();
                turnLeft();
                break;
            case 2:
                moveForward();
                turnRight();
                break;
            case 3:
                moveBackward();
                turnLeft();
                break;
            case 4:
                moveBackward();
                turnRight();
                break;
            case 5:
                moveForward();
                break;
            case 6:
                turnLeft();
                break;
            case 7:
                moveBackward();
                break;
            case 8:
                turnRight();
                break;
        }
        movementCount++;*/
    }

    private boolean canHitPlayer()
    {
        float x1, y1, x2, y2, m, c, turnAmount, newx2, newy2, upperBound, lowerBound, playerConstant;

        //return false if player behind direction of turret
        turnAmount = getTurretDir();
        if ( (turnAmount < 180 && playerXPos + player.hull.getWidth()/2 < getXPos()) || (turnAmount > 180 && playerXPos - player.hull.getWidth()/2 > getXPos())) return false;

        //coords of origin
        x1 = turret.getXPos();
        y1 = turret.getYPos();

        //coords of end point of turret
        x2 = x1;
        y2 = y1 - turret.getHeight() / 2;

        //rotating end point of turret around origin (of turret)
        newx2 = (float) (Math.cos(Math.toRadians(turnAmount))) * (x2 - x1) - (float) (Math.sin(Math.toRadians(turnAmount))) * (y2 - y1) + x1;
        newy2 = (float) (Math.sin(Math.toRadians(turnAmount))) * (x2 - x1) + (float) (Math.cos(Math.toRadians(turnAmount))) * (y2 - y1) + y1;
        x2 = newx2;
        y2 = newy2;

        //working out equation of line through turret (firing line)
        m = (y1 - y2) / (x1 - x2);
        c =  y2 - (m*x2);

        //constant val for parallel lines between which player tank can be shot at
        upperBound = c + player.hull.getWidth()/2;
        lowerBound = c - player.hull.getWidth()/2;

        //test rotation
        RotatingObject b = new RotatingObject();
        b.setObjectTexture(Textures.TANKSHELL_DEFAULT);
        b.setCenterLocation(x2, y2);
        window.draw(b);

        playerConstant = (playerYPos - (m*playerXPos));
        return (upperBound > playerConstant && lowerBound < playerConstant);
    }
}
