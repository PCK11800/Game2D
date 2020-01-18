package Tanks.Objects;

import Tanks.ObjectComponents.MapObject;
import Tanks.ObjectComponents.RotatingObject;
import Tanks.ObjectComponents.Textures;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;


public class Opponent extends Tank {

    private static final int RICOCHET_MAX = 3;
    private int movementCount = 0;
    private double direction = 0;
    private Tank player;
    private float playerXPos;
    private float playerYPos;
    private int ricochetCount = 0;
    private int shotsFired = 0;
    private boolean isObjectInFiringLine = false;

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
            for (int i  = 0; i< 210; i++) {

                rotateTurretRight();
            }


        }
        movementCount++;
        playerXPos = player.getXPos();
        playerYPos = player.getYPos();

        if (player.isAlive()) action();


    }
    

    private void action()
    {
        float x1, y1, x2, y2;
        float[] coords;
        float turnAmount = getTurretDir();
        //coords of origin
        x1 = turret.getXPos();
        y1 = turret.getYPos();

        //coords of end point of turret
        x2 = x1;
        y2 = y1 - turret.getHeight() / 2;
        coords = rotateCoordinates(x1, y1, x2, y2, getTurretDir());
        x2 = coords[0];
        y2 = coords[1];

        ricochetCount = 0;
        isObjectInFiringLine = false;
        if (objectCollision(x1, y1, x2, y2, getTurretDir()))
        {
            shoot();
        }
        else if ((turnAmount <= 180 && playerXPos > x1) || (turnAmount > 180 && playerXPos < x1) && !isObjectInFiringLine)
        {
            if (isPlayerInFiringLine(x1, y1, x2, y2))
            {
                shoot();
            }
            else
            {
                rotateTurretRight();
            }
        }
        else
        {
            rotateTurretRight();
        }


    }

    private boolean isPlayerInFiringLine(float x1, float y1, float x2, float y2)
    {
        Float m, c, playerConstant, upperBound, lowerBound;
        //working out equation of line through turret (firing line)
        m = (y1 - y2) / (x1 - x2);
        c =  y2 - (m*x2);

        //constant val for parallel lines between which player tank can be shot at
        upperBound = c + player.hull.getHeight()/2;
        lowerBound = c - player.hull.getHeight()/2;

        if (m.isInfinite())
        {
            if (x1 == x2)
            {
                if (playerXPos < (x1 + player.hull.getWidth()/2) && playerXPos > (x1 - player.hull.getWidth()/2)) return true;
            }
            else
            {
                if (playerYPos < (y1 + player.hull.getWidth()/2) && playerYPos > (y1 - player.hull.getWidth()/2)) return true;
            }
        }
        //test rotation
        playerConstant = (playerYPos - (m*playerXPos));
        return (upperBound > playerConstant && lowerBound < playerConstant);
    }

    private boolean objectCollision(float x1, float y1, float x2, float y2, float direction)
    {
        float m, c, x, y, newX, newY, playerConstant, upperBound, lowerBound;
        ArrayList<MapObject> objects = map.getObjectsInMap();
        m = (y1 - y2) / (x1 - x2);
        c =  y2 - (m*x2);
        for (MapObject obj : objects)
        {

             if(((((x1 == obj.getLeftBounds() || x1 == obj.getRightBounds())) && (y1 < obj.getBottomBounds() && y1 > obj.getTopBounds())) || (y1 == obj.getTopBounds() || y1 == obj.getBottomBounds()) && (x1 < obj.getRightBounds() && x1 > obj.getLeftBounds())) || ((x2 > x1) && (x2 > obj.getRightBounds())) || ((x2 < x1) && (x2 < obj.getLeftBounds())) || ((y2 > y1) && (y2 > obj.getBottomBounds())) || ((y2 < y1) && (y2 < obj.getTopBounds())))
            {
                //do nothing - objects out of fire line
            }
            else if (x2 <= x1 && y2 <= y1) //can hit right side or bottom
            {
                x = obj.getRightBounds();
                y = (m*x) + c;
                if (y > obj.getTopBounds() && y < obj.getBottomBounds()) //right
                {
                    isObjectInFiringLine = true;
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 180 - direction);
                    newX = coords[0];
                    newY = coords[1];
                    if (playerXPos + player.hull.getWidth()/2 > x && isPlayerInFiringLine(x, y, newX, newY))
                    {
                       return true;
                    }
                    else if (ricochetCount < RICOCHET_MAX)
                    {
                        ricochetCount++;
                        return objectCollision(x, y, newX, newY, 180 - direction);
                    }
                }
                y = obj.getBottomBounds();
                x = (y - c) / m;
                if (x < obj.getRightBounds() && x > obj.getLeftBounds()) //bottom
                {
                    isObjectInFiringLine = true;
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 0 - direction);
                    newX = coords[0];
                    newY = coords[1];
                    if (playerYPos + player.hull.getHeight()/2 > y && isPlayerInFiringLine(x, y, newX, newY))
                    {
                        return true;
                    }
                    else if (ricochetCount < RICOCHET_MAX)
                    {
                        ricochetCount++;
                        return objectCollision(x, y, newX, newY, 180 - direction);
                    }

                }
            }
            else if (x2 <= x1 && y2 >= y1) //can hit right side or top
             {
                x = obj.getRightBounds();
                y = (m*x) + c;
                if (y > obj.getTopBounds() && y < obj.getBottomBounds()) //right side
                {
                    isObjectInFiringLine = true;
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 180 - direction);
                    newX = coords[0];
                    newY = coords[1];
                    if (playerXPos + player.hull.getWidth()/2 > x && isPlayerInFiringLine(x, y, newX, newY))
                    {
                        return true;
                    }
                    else if (ricochetCount < RICOCHET_MAX)
                    {
                        ricochetCount++;
                        return objectCollision(x, y, newX, newY, 180 - direction);
                    }
                }
                y = obj.getTopBounds();
                x = (y - c) / m;
                if (x < obj.getRightBounds() && x > obj.getLeftBounds()) //top
                {
                    isObjectInFiringLine = true;
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 0 - direction);
                    newX = coords[0];
                    newY = coords[1];
                    if (playerYPos + player.hull.getHeight()/2 < y && isPlayerInFiringLine(x, y, newX, newY))
                    {
                        return true;
                    }
                    else if (ricochetCount < RICOCHET_MAX)
                    {
                        ricochetCount++;
                        return objectCollision(x, y, newX, newY, 180 - direction);
                    }
                }
            }
            else if (x2 >= x1 && y2 <= y1) //can hit left side or bottom
            {
                x = obj.getLeftBounds();
                y = (m*x) + c;
                if (y > obj.getTopBounds() && y < obj.getBottomBounds()) //left
                {
                    isObjectInFiringLine = true;
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 180 - direction);
                    newX = coords[0];
                    newY = coords[1];
                    if (playerXPos + player.hull.getWidth() / 2 > x && isPlayerInFiringLine(x, y, newX, newY))
                    {
                        return true;
                    }
                    else if (ricochetCount < RICOCHET_MAX)
                    {
                        ricochetCount++;
                        return objectCollision(x, y, newX, newY, 180 - direction);
                    }
                }
                y = obj.getBottomBounds();
                x = (y - c) / m;
                if (x < obj.getRightBounds() && x > obj.getLeftBounds())
                {
                    isObjectInFiringLine = true;
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 0 - direction);
                    newX = coords[0];
                    newY = coords[1];
                    if (playerYPos + player.hull.getHeight()/2 > y && isPlayerInFiringLine(x, y, newX, newY))
                    {
                        return true;
                    }
                    else if (ricochetCount < RICOCHET_MAX)
                    {
                        ricochetCount++;
                        return objectCollision(x, y, newX, newY, 180 - direction);
                    }
                }
            }
            else //left side or top
            {
                //System.out.println("here4");
                x = obj.getLeftBounds();
                y = (m*x) + c;
                if (y > obj.getTopBounds() && y < obj.getBottomBounds()) //left
                {
                    isObjectInFiringLine = true;
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY,  0 - direction);
                    newX = coords[0];
                    newY = coords[1];
                    if (playerXPos + player.hull.getWidth() / 2 < x && isPlayerInFiringLine(x, y, newX, newY))
                    {
                        return true;
                    }
                    else if (ricochetCount < RICOCHET_MAX)
                    {
                        ricochetCount++;
                        return objectCollision(x, y, newX, newY, 180 - direction);
                    }
                }
                y = obj.getTopBounds();
                x = (y - c) / m;
                if (x < obj.getRightBounds() && x > obj.getLeftBounds()) //top
                {
                    isObjectInFiringLine = true;
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 180 - direction);
                    newX = coords[0];
                    newY = coords[1];
                    if (playerYPos + player.hull.getHeight()/2 < y && isPlayerInFiringLine(x, y, newX, newY))
                    {
                        return true;
                    }
                    else if (ricochetCount < RICOCHET_MAX)
                    {
                        ricochetCount++;
                         return objectCollision(x, y, newX, newY, 180 - direction);
                    }
                }
            }
        }
        return false;
    }


    private float[] rotateCoordinates(float x1, float y1, float x2, float y2, float turnAmount)
    {
        float newx2, newy2;
        newx2 = (float) (Math.cos(Math.toRadians(turnAmount))) * (x2 - x1) - (float) (Math.sin(Math.toRadians(turnAmount))) * (y2 - y1) + x1;
        newy2 = (float) (Math.sin(Math.toRadians(turnAmount))) * (x2 - x1) + (float) (Math.cos(Math.toRadians(turnAmount))) * (y2 - y1) + y1;
        x2 = newx2;
        y2 = newy2;
        return (new float[] {x2, y2});
    }


}
