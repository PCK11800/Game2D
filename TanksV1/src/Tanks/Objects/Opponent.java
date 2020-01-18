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
            for (int i  = 0; i< 18; i++) {

                rotateTurretRight();
            }


        }
        movementCount++;
        playerXPos = player.getXPos();
        playerYPos = player.getYPos();
        if (player.isAlive() && action()) shoot();


    }

    private void performAction()
    {

    }

    private boolean action()
    {
        float x1, y1, x2, y2, m, c, turnAmount, newx2, newy2, upperBound, lowerBound, playerConstant;

        //return false if player behind direction of turret
        turnAmount = getTurretDir();
        //System.out.println(turnAmount);
        //if ( ((turnAmount < 90 || turnAmount > 270)  && playerYPos + player.hull.getHeight()/2 > getYPos()) || ((turnAmount > 90 && turnAmount < 270) && playerYPos - player.hull.getHeight()/2 < getYPos())) return false;

        //coords of origin
        x1 = turret.getXPos();
        y1 = turret.getYPos();

        //coords of end point of turret
        x2 = x1;
        y2 = y1 - turret.getHeight() / 2;
        float coords[] = rotateCoordinates(x1, y1, x2, y2, getTurretDir());
        x2 = coords[0];
        y2 = coords[1];
        //System.out.println(x1 + "," + y1 + " " + x2 + "," + y2);
        char[] direction = new char[2];
        direction[0] = turnAmount >= 180 ? turnAmount == 180 ? 'D' : 'L' : turnAmount == 0 ? 'U' : 'R';

        System.out.println(objectCollision(x1, y1, x2, y2));
        return isPlayerInFiringLine(x1, y1, x2, y2);
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
        RotatingObject b = new RotatingObject();
        b.setObjectTexture(Textures.TANKSHELL_DEFAULT);
        b.setCenterLocation(x2, y2);
        window.draw(b);
        System.out.println(upperBound + "|" + lowerBound);
        playerConstant = (playerYPos - (m*playerXPos));
        System.out.println(playerConstant);
        return (upperBound > playerConstant && lowerBound < playerConstant);
    }

    private boolean objectCollision(float x1, float y1, float x2, float y2)
    {
        float m, c, x, y, newX, newY, playerConstant, upperBound, lowerBound;
        ArrayList<MapObject> objects = map.getObjectsInMap();
        m = (y1 - y2) / (x1 - x2);
        c =  y2 - (m*x2);
        for (MapObject obj : objects)
        {
            if (x2 <= x1 && y2 <= y1) //can hit right side or bottom
            {
                System.out.println("here1");
                x = obj.getRightBounds();
                y = (m*x) + c;
                if (y > obj.getTopBounds() && y < obj.getBottomBounds())
                {
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 180 - getTurretDir());
                    newX = coords[0];
                    newY = coords[1];
                    System.out.println(x + "|" + y);
                    RotatingObject b = new RotatingObject();
                    b.setObjectTexture(Textures.TANKSHELL_DEFAULT);
                    b.setCenterLocation(x, y);
                    window.draw(b);

                   if (isPlayerInFiringLine(x, y, newX, newY)) shoot();
                    return true;
                }
                y = obj.getBottomBounds();
                x = (y - c) / m;
                if (x < obj.getRightBounds() && x > obj.getLeftBounds())
                {
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 0 - getTurretDir());
                    newX = coords[0];
                    newY = coords[1];
                    System.out.println(x + "|" + y);
                    RotatingObject b = new RotatingObject();
                    b.setObjectTexture(Textures.TANKSHELL_DEFAULT);
                    b.setCenterLocation(x, y);
                    window.draw(b);
                   if (playerYPos + player.hull.getHeight()/2 > y && isPlayerInFiringLine(x, y, newX, newY)) shoot();
                    return true;
                }
            }
            else if (x2 <= x1 && y2 >= y1) //can hit left side or top
            {
                System.out.println("here2");
                x = obj.getRightBounds();
                y = (m*x) + c;
                if (y > obj.getTopBounds() && y < obj.getBottomBounds())
                {
                    System.out.println("hereagain");
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 180 - getTurretDir());
                    newX = coords[0];
                    newY = coords[1];
                    System.out.println(x + "|" + y);
                    RotatingObject b = new RotatingObject();
                    b.setObjectTexture(Textures.TANKSHELL_DEFAULT);
                    b.setCenterLocation(x, y);
                    window.draw(b);
                    if (isPlayerInFiringLine(x, y, newX, newY)) shoot();

                    return true;
                }
                y = obj.getTopBounds();
                x = (y - c) / m;
                if (x < obj.getRightBounds() && x > obj.getLeftBounds())
                {
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 0 - getTurretDir());
                    newX = coords[0];
                    newY = coords[1];
                    System.out.println(x + "|" + y);
                    RotatingObject b = new RotatingObject();
                    b.setObjectTexture(Textures.TANKSHELL_DEFAULT);
                    b.setCenterLocation(x, y);
                    window.draw(b);
                    if (isPlayerInFiringLine(x, y, newX, newY)) shoot();
                    return true;
                }
            }
            else if (x2 >= x1 && y2 <= y1) //can hit left side or bottom
            {
                System.out.println("here3");
                x = obj.getLeftBounds();
                y = (m*x) + c;
                if (y > obj.getTopBounds() && y < obj.getBottomBounds())
                {
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 180 - getTurretDir());
                    newX = coords[0];
                    newY = coords[1];
                    System.out.println(x + "|" + y);
                    RotatingObject b = new RotatingObject();
                    b.setObjectTexture(Textures.TANKSHELL_DEFAULT);
                    b.setCenterLocation(x, y);
                    window.draw(b);
                    if (isPlayerInFiringLine(x, y, newX, newY)) shoot();
                    return true;
                }
                y = obj.getBottomBounds();
                x = (y - c) / m;
                if (x < obj.getRightBounds() && x > obj.getLeftBounds())
                {
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 0 - getTurretDir());
                    newX = coords[0];
                    newY = coords[1];
                    System.out.println(x + "|" + y);
                    RotatingObject b = new RotatingObject();
                    b.setObjectTexture(Textures.TANKSHELL_DEFAULT);
                    b.setCenterLocation(x, y);
                    window.draw(b);
                    if (isPlayerInFiringLine(x, y, newX, newY)) shoot();
                    return true;
                }
            }
            else //left side or top
            {
                System.out.println("here4");
                x = obj.getLeftBounds();
                y = (m*x) + c;
                if (y > obj.getTopBounds() && y < obj.getBottomBounds())
                {
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 180 - getTurretDir());
                    newX = coords[0];
                    newY = coords[1];
                    System.out.println(x + "|" + y);
                    RotatingObject b = new RotatingObject();
                    b.setObjectTexture(Textures.TANKSHELL_DEFAULT);
                    b.setCenterLocation(x, y);
                    window.draw(b);
                    if (isPlayerInFiringLine(x, y, newX, newY)) shoot();
                    return true;
                }
                y = obj.getTopBounds();
                x = (y - c) / m;
                if (x < obj.getRightBounds() && x > obj.getLeftBounds())
                {
                    newX = x;
                    newY = y + 70;
                    float coords[] = rotateCoordinates(x, y, newX, newY, 0 - getTurretDir());
                    newX = coords[0];
                    newY = coords[1];
                    System.out.println(x + "|" + y);
                    RotatingObject b = new RotatingObject();
                    b.setObjectTexture(Textures.TANKSHELL_DEFAULT);
                    b.setCenterLocation(x, y);
                    window.draw(b);
                    if (isPlayerInFiringLine(x, y, newX, newY)) shoot();
                    return true;
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
