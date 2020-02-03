package Tanks.Objects;

import Tanks.ObjectComponents.MapObject;
import Tanks.ObjectComponents.RotatingObject;
import Tanks.ObjectComponents.TankTurret;
import Tanks.ObjectComponents.Textures;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;


public class Opponent extends Tank {

    private static final int RICOCHET_MAX = 3;
    private int movementCount = 0;
    private double direction = 0;
    private Tank player;
    private float playerXPos;
    private float playerYPos;
    private int ricochetCount = 0;
    private int shotsFired = 0;
    private float[] objectCoords = new float[2];
    private TankTurret clone;
    private String[][] mapGrid;
    private float maxXPos, maxYPos;


    public Opponent(Tank player, int[][] grid)
    {
        super();
        this.player = player;
        mapGrid = new String[grid.length][grid[0].length];
        int j;
        for (int i = 0; i < grid[0].length; i++)
        {
            for (j = 0; j < grid.length; j++)
            {
                mapGrid[j][i] = "";
                if ((grid[j][i] & 1) == 0)
                {
                    mapGrid[j][i] += "T";
                }
                if ((grid[j][i] & 8) == 0)
                {
                    mapGrid[j][i] += "L";
                }
                if (i == grid[0].length - 1)
                {
                    mapGrid[j][i] += "B";
                }
            }
            mapGrid[j - 1][i] += "R";
        }

        for (int i = 0; i < mapGrid[0].length; i++)
        {
            for (j = 0; j < mapGrid.length; j++)
            {
                System.out.println(mapGrid[j][i]);
            }
        }
    }

    public void setMaxPos(float x, float y)
    {
        this.maxXPos = x;
        this.maxYPos = y;
    }


    public boolean update()
    {
        super.update();
        if (movementCount == 0)
        {
            for (int i  = 0; i< 210; i++) {

                rotateTurretRight();
            }
           //move();
            clone = turret.stationaryCopy();

        }
        move();
        clone.update();
        movementCount++;
        playerXPos = player.getXPos();
        playerYPos = player.getYPos();

        if (player.isAlive()) action();

        return false;
    }


    private void action()
    {
        float x1, y1, x2, y2;
        float[] coords;
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
        if (objectCollision(x1, y1, x2, y2, getTurretDir()))
        {
            turret.update();
            shoot();
            clone = turret.stationaryCopy();
        }
        else
        {
            rotateTurretRight();
        }


    }

    private void move()
    {
        //work out which grid space in map opponent is at
        //tile size = 110 (just for testing)
        //max x
        int x, y, playerX, playerY;
        System.out.println(map.getWidth() + "|" + map.getHeight());
        System.out.println(getXPos() + "|" + getYPos());
       // int tempX = map.getWidth() / mapGrid[0].length
        x = (int) Math.floor(getXPos());
        x = (x == 0 ? 0 : (int) Math.floor( x / (map.getWidth() / mapGrid.length)));
        y =  (int) Math.floor(getYPos());
        y = (y == 0 ? 0 : (int) Math.floor(y / (map.getHeight() / mapGrid[0].length)));
        System.out.println(x + "|" + y + "|" + mapGrid.length + "|" + mapGrid[0].length);

        playerX = (int) Math.floor(playerXPos);
        playerX = (playerX == 0 ? 0 : (int) Math.floor( playerX / (map.getWidth() / mapGrid.length)));
        playerY = (int) Math.floor(playerYPos);
        playerY = (playerY == 0 ? 0 : (int) Math.floor(playerY / (map.getHeight() / mapGrid[0].length)));
        System.out.println(playerX + "|" + playerY);
        System.out.println(mapGrid[playerX][playerY]);
        findPath(x, y, playerX, playerY);
    }

    private void findPath(int srcX, int srcY, int destX, int destY)
    {

        ArrayList<String> path = new ArrayList<>();
        ArrayList<String> found = new ArrayList<>();
        String temp = "";


        int x = srcX, y = srcY;
        Stack<Integer[]> prev = new Stack<>();
        prev.push(new Integer[]{x, y});
        temp = x + "," + y;
        found.add(temp);
        temp = "";
        while (x != destX || y != destY)
        {
            String current = mapGrid[x][y];
            System.out.println(x + "|" + y);
            System.out.println(current);
            boolean action = false;
            if (!current.contains("T")) {
                temp = Integer.toString(x) + "," + Integer.toString(y-1);
                //System.out.println(found.contains(temp) + "TOP");
                if (!found.contains(temp)) {
                    System.out.println("TOP");
                    path.add(temp);
                    prev.push(new Integer[]{x, y - 1});
                    found.add(temp);
                    y = y - 1;
                    action = true;
                }
            }
            if (!action && !current.contains("L"))
            {
                temp = Integer.toString(x - 1) + "," + Integer.toString(y);
                System.out.println(found.contains(temp) + "LEFT");
                if (!found.contains(temp))
                {
                    System.out.println("LEFT");
                    path.add(temp);
                    prev.push(new Integer[]{x - 1, y});
                    found.add(temp);
                    x = x - 1;
                    action = true;
                }
            }
            if (!action && !current.contains("B"))
            {
                String bottom = mapGrid[x][y + 1];

                if (!bottom.contains("T")) {
                    temp = Integer.toString(x) + "," + Integer.toString(y+1);
                    System.out.println(found.contains(temp) + "BOTTOM");
                    if (!found.contains(temp)) {
                        System.out.println("BOTTOM");
                        path.add(temp);
                        prev.push(new Integer[]{x, y + 1});
                        found.add(temp);
                        y = y + 1;
                        action = true;
                    }
                }
            }
            if (!action && !current.contains("R")) {
                String right = mapGrid[x + 1][y];

                if (!right.contains("L")) {
                    temp = Integer.toString(x + 1) + "," + Integer.toString(y);
                    System.out.println(found.contains(temp) + "RIGHT");
                    if (!found.contains(temp)) {
                        System.out.println("RIGHT");
                        path.add(temp);
                        prev.push(new Integer[]{x + 1, y});
                        found.add(temp);
                        x = x + 1;
                        action = true;
                    }
                }
            }
            if (!action)
            {
                System.out.println("BACK");
                temp = Integer.toString(x) + "," + Integer.toString(y);
                path.remove(temp);
                prev.pop();
                Integer[] p = prev.peek();
                x = p[0]; y = p[1];
            }
            temp = "";
        }
        for (String space : path)
        {
            System.out.println(space);
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

    private boolean isObjectInPath(float x1, float y1, float x2, float y2)
    {
        Line2D line = new Line2D.Float(x1, y1, x2, y2);
        ArrayList<MapObject> objects = map.getObjectsInMap();
        for (MapObject obj : objects)
        {
            Line2D top = new Line2D.Float(obj.getLeftBounds(), obj.getTopBounds(), obj.getRightBounds(), obj.getTopBounds());
            Line2D bottom = new Line2D.Float(obj.getLeftBounds(), obj.getBottomBounds(), obj.getRightBounds(), obj.getBottomBounds());
            Line2D right = new Line2D.Float(obj.getRightBounds(), obj.getTopBounds(), obj.getRightBounds(), obj.getBottomBounds());
            Line2D left = new Line2D.Float(obj.getLeftBounds(), obj.getTopBounds(), obj.getLeftBounds(), obj.getBottomBounds());
            if (line.intersectsLine(top) && line.intersectsLine(left) || line.intersectsLine(top) && line.intersectsLine(right) || line.intersectsLine(top) && line.intersectsLine(bottom) || line.intersectsLine(bottom) && line.intersectsLine(right) || line.intersectsLine(bottom) && line.intersectsLine(left) || line.intersectsLine(right) && line.intersectsLine(left))
            {
                return true;
            }
        }
        return false;
    }


    private boolean objectCollision(float x1, float y1, float x2, float y2, float direction)
    {
        float m, c, x, y, newX, newY;
        ArrayList<MapObject> objects = map.getObjectsInMap();
        m = (y1 - y2) / (x1 - x2);
        c =  y2 - (m*x2);
            if (x2 <= x1 && y2 <= y1) //can hit right side or bottom
            {
                for (MapObject obj : objects)
                {
                    if(((((x1 == obj.getLeftBounds() || x1 == obj.getRightBounds())) && (y1 < obj.getBottomBounds() && y1 > obj.getTopBounds())) || (y1 == obj.getTopBounds() || y1 == obj.getBottomBounds()) && (x1 < obj.getRightBounds() && x1 > obj.getLeftBounds())) || ((x2 > x1) && (x2 > obj.getRightBounds())) || ((x2 < x1) && (x2 < obj.getLeftBounds())) || ((y2 > y1) && (y2 > obj.getBottomBounds())) || ((y2 < y1) && (y2 < obj.getTopBounds())))
                    {
                       continue;
                    }
                    x = obj.getRightBounds();
                    y = (m * x) + c;
                    if (y > obj.getTopBounds() && y < obj.getBottomBounds()) //right
                    {
                        newX = x;
                        newY = y + 70;
                        float coords[] = rotateCoordinates(x, y, newX, newY, 180 - direction);
                        newX = coords[0];
                        newY = coords[1];
                        if (!isObjectInPath(x1, y1, x, y))
                        {
                            if (playerXPos + (player.hull.getWidth() / 2) > x && playerXPos + (player.hull.getWidth() / 2) < x1 && isPlayerInFiringLine(x1, y1, x2, y2)) {
                                return true;
                            } else if (ricochetCount <= RICOCHET_MAX) {
                                ricochetCount++;
                                return objectCollision(x, y, newX, newY, 180 - direction);
                            }
                        }
                    }
                    y = obj.getBottomBounds();
                    x = (y - c) / m;
                    if (x < obj.getRightBounds() && x > obj.getLeftBounds()) //bottom
                    {
                        newX = x;
                        newY = y + 70;
                        float coords[] = rotateCoordinates(x, y, newX, newY, 0 - direction);
                        newX = coords[0];
                        newY = coords[1];
                        if (!isObjectInPath(x1, y1, x, y)) {
                            if (playerXPos + (player.hull.getWidth() / 2) > x && playerXPos + (player.hull.getWidth() / 2) < x1 && isPlayerInFiringLine(x1, y1, x2, y2)) {
                                return true;
                            } else if (ricochetCount < RICOCHET_MAX) {
                                ricochetCount++;
                                return objectCollision(x, y, newX, newY, 0 - direction);
                            }
                        }
                    }
                }
            }
            else if (x2 <= x1 && y2 >= y1) //can hit right side or top
             {
                 for (MapObject obj : objects) {
                     if(((((x1 == obj.getLeftBounds() || x1 == obj.getRightBounds())) && (y1 < obj.getBottomBounds() && y1 > obj.getTopBounds())) || (y1 == obj.getTopBounds() || y1 == obj.getBottomBounds()) && (x1 < obj.getRightBounds() && x1 > obj.getLeftBounds())) || ((x2 > x1) && (x2 > obj.getRightBounds())) || ((x2 < x1) && (x2 < obj.getLeftBounds())) || ((y2 > y1) && (y2 > obj.getBottomBounds())) || ((y2 < y1) && (y2 < obj.getTopBounds())))
                     {
                         continue;
                     }
                     x = obj.getRightBounds();
                     y = (m * x) + c;
                     if (y > obj.getTopBounds() && y < obj.getBottomBounds()) //right side
                     {
                         newX = x;
                         newY = y + 70;
                         float coords[] = rotateCoordinates(x, y, newX, newY, 180 - direction);
                         newX = coords[0];
                         newY = coords[1];
                         if (!isObjectInPath(x1, y1, x, y)) {
                             if (playerXPos + (player.hull.getWidth() / 2) > x && playerXPos + (player.hull.getWidth() / 2) < x1 && isPlayerInFiringLine(x1, y1, x2, y2)) {
                                 return true;
                             } else if (ricochetCount < RICOCHET_MAX) {
                                 ricochetCount++;
                                 return objectCollision(x, y, newX, newY, 180 - direction);
                             }
                         }
                     }
                     y = obj.getTopBounds();
                     x = (y - c) / m;
                     if (x < obj.getRightBounds() && x > obj.getLeftBounds()) //top
                     {
                         newX = x;
                         newY = y + 70;
                         float coords[] = rotateCoordinates(x, y, newX, newY, 0 - direction);
                         newX = coords[0];
                         newY = coords[1];
                         if (!isObjectInPath(x1, y1, x, y)) {
                             if (playerXPos + (player.hull.getWidth() / 2) < y && playerXPos + (player.hull.getWidth() / 2) > y1 && isPlayerInFiringLine(x1, y1, x2, y2)) {
                                 return true;
                             } else if (ricochetCount < RICOCHET_MAX) {
                                 ricochetCount++;
                                 return objectCollision(x, y, newX, newY, 0 - direction);
                             }
                         }
                     }
                 }
            }
            else if (x2 >= x1 && y2 <= y1) //can hit left side or bottom
            {
                //System.out.println("here3");
                for (MapObject obj : objects) {
                    if(((((x1 == obj.getLeftBounds() || x1 == obj.getRightBounds())) && (y1 < obj.getBottomBounds() && y1 > obj.getTopBounds())) || (y1 == obj.getTopBounds() || y1 == obj.getBottomBounds()) && (x1 < obj.getRightBounds() && x1 > obj.getLeftBounds())) || ((x2 > x1) && (x2 > obj.getRightBounds())) || ((x2 < x1) && (x2 < obj.getLeftBounds())) || ((y2 > y1) && (y2 > obj.getBottomBounds())) || ((y2 < y1) && (y2 < obj.getTopBounds())))
                    {
                        continue;
                    }
                    x = obj.getLeftBounds();
                    y = (m * x) + c;
                    if (y > obj.getTopBounds() && y < obj.getBottomBounds()) //left
                    {
                        newX = x;
                        newY = y + 70;
                        float coords[] = rotateCoordinates(x, y, newX, newY, 180 - direction);
                        newX = coords[0];
                        newY = coords[1];
                        if (!isObjectInPath(x1, y1, x, y)) {
                            if (playerXPos + (player.hull.getWidth() / 2) < x && playerXPos + (player.hull.getWidth() / 2) > x1 && isPlayerInFiringLine(x1, y1, x2, y2)) {
                                return true;
                            } else if (ricochetCount < RICOCHET_MAX) {
                                ricochetCount++;
                                return objectCollision(x, y, newX, newY, 180 - direction);
                            }
                        }
                    }
                    y = obj.getBottomBounds();
                    x = (y - c) / m;
                    if (x < obj.getRightBounds() && x > obj.getLeftBounds()) {
                        newX = x;
                        newY = y + 70;
                        float coords[] = rotateCoordinates(x, y, newX, newY, 0 - direction);
                        newX = coords[0];
                        newY = coords[1];
                        if (!isObjectInPath(x1, y1, x, y)) {
                            if (playerXPos + (player.hull.getWidth() / 2) < x && playerXPos + (player.hull.getWidth() / 2) > x1 && isPlayerInFiringLine(x1, y1, x2, y2)) {
                                return true;
                            } else if (ricochetCount < RICOCHET_MAX) {
                                return objectCollision(x, y, newX, newY, 0 - direction);
                            }
                        }
                    }
                }
            }
            else //left side or top
            {
                //System.out.println("here4");
                for (MapObject obj : objects) {
                    if(((((x1 == obj.getLeftBounds() || x1 == obj.getRightBounds())) && (y1 < obj.getBottomBounds() && y1 > obj.getTopBounds())) || (y1 == obj.getTopBounds() || y1 == obj.getBottomBounds()) && (x1 < obj.getRightBounds() && x1 > obj.getLeftBounds())) || ((x2 > x1) && (x2 > obj.getRightBounds())) || ((x2 < x1) && (x2 < obj.getLeftBounds())) || ((y2 > y1) && (y2 > obj.getBottomBounds())) || ((y2 < y1) && (y2 < obj.getTopBounds())))
                    {
                        continue;
                    }
                    x = obj.getLeftBounds();
                    y = (m * x) + c;
                    if (y > obj.getTopBounds() && y < obj.getBottomBounds()) //left
                    {
                        newX = x;
                        newY = y + 70;
                        float coords[] = rotateCoordinates(x, y, newX, newY, 0 - direction);
                        newX = coords[0];
                        newY = coords[1];
                        if (!isObjectInPath(x1, y1, x, y)) {
                            if (playerXPos + (player.hull.getWidth() / 2) < x && playerXPos + (player.hull.getWidth() / 2) > x1 && isPlayerInFiringLine(x1, y1, x2, y2)) {
                                return true;
                            } else if (ricochetCount < RICOCHET_MAX) {
                                ricochetCount++;
                                return objectCollision(x, y, newX, newY, 0 - direction);
                            }
                        }
                    }
                    y = obj.getTopBounds();
                    x = (y - c) / m;
                    if (x < obj.getRightBounds() && x > obj.getLeftBounds()) //top
                    {
                        newX = x;
                        newY = y + 70;
                        float coords[] = rotateCoordinates(x, y, newX, newY, 180 - direction);
                        newX = coords[0];
                        newY = coords[1];
                        if (!isObjectInPath(x1, y1, x, y)) {
                            if (playerXPos + (player.hull.getWidth() / 2) < x && playerXPos + (player.hull.getWidth() / 2) > x1 && isPlayerInFiringLine(x1, y1, x2, y2)) {
                                return true;
                            } else if (ricochetCount < RICOCHET_MAX) {
                                ricochetCount++;
                                return objectCollision(x, y, newX, newY, 180 - direction);
                            }
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
