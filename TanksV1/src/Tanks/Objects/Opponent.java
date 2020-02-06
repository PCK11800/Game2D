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

    private final int RICOCHET_MAX = 3;
    private int movementCount = 0;
    private double direction = 0;
    private Tank player;
    private float playerXPos;
    private float playerYPos;
    private int ricochetCount = 0;
    private TankTurret clone;
    private String[][] mapGrid;
    private Stack<Integer[]> movementPath;
    private Integer[] currSpace = new Integer[2];
    private float gridSpaceWidth, gridSpaceHeight;


    /**
     * Constructor. Creates new instance of Opponent.
     * @param player the Tank this opponent will be targeting
     * @param grid the map grid of the level this opponent exists on
     */
    public Opponent(Tank player, int[][] grid)
    {
        super();
        this.player = player;
        mapGrid = new String[grid.length][grid[0].length];
        int j;

        //generate grid of obstacles in the current level
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


    /**
     * Update method called every game loop. First loop includes pathfinding the route from current position to player.
     * @return
     */
    public boolean update()
    {
        super.update();
        if (movementCount == 0)
        {
            for (int i  = 0; i< 210; i++) {

                rotateTurretRight();
            }
           generateMovementPathToPlayer();
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

    /**
     * Method for detecting if player can be hit by the opponent at any rotation of the opponent's turret given the opponents' current position within the level.
     */
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
        if (canHitPlayer(x1, y1, x2, y2, getTurretDir()))
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

    /**
     * Method moves opponent to 'target' space in the grid. Returns immediately if opponent already at location.
     */
    private void move()
    {
        if (movementPath.isEmpty()) return;
        direction = hull.getObjectDirection();
        Integer[] nextMove = movementPath.peek();
        currSpace = generateGridPos(getXPos() , getYPos());
        gridSpaceWidth = map.getWidth() / mapGrid.length;
        gridSpaceHeight = map.getHeight() / mapGrid[0].length;
       // System.out.println(map.getWidth() + "||" + gridSpaceWidth + "{}{}" + map.getHeight() + "||" + map.getHeight());
        /*
        Integer[] borderCheck1 = generateGridPos(getXPos() - (gridSpaceWidth/6) - 10, getYPos());
        Integer[] borderCheck2 = generateGridPos(getXPos() + (gridSpaceWidth/6) + 10, getYPos());
        Integer[] borderCheck3 = generateGridPos(getXPos(), getYPos() - (gridSpaceHeight/5));
        Integer[] borderCheck4 = generateGridPos(getXPos(), getYPos() + (gridSpaceHeight/5));
        if (borderCheck1[0] != borderCheck2[0] || borderCheck2[0] != borderCheck3[0] || borderCheck3[0] != borderCheck4[0] || borderCheck1[1] != borderCheck2[1] || borderCheck2[1] != borderCheck3[1] || borderCheck3[1] != borderCheck4[1])
        {
            moveForward();
            turret.update();
            clone = turret.stationaryCopy();
            return;
        }*/
        if (!middleOfSpace(getXPos(), getYPos()))
        {
            moveForward();
            turret.update();
            clone = turret.stationaryCopy();
            return;
        }
        if (nextMove[0] != currSpace[0])
        {
            if (nextMove[0] < currSpace[0]) {
                if (hull.getObjectDirection() != 270) // turn left
                {
                   // System.out.println(direction);
                    if (direction < 90 || direction > 270) {
                        turnLeft();
                    } else {
                        turnRight();
                    }
                }
                else
                {
                    moveForward();
                    turret.update();
                    clone = turret.stationaryCopy();
                }
            }
            else {
                if (hull.getObjectDirection() != 90) //turn right
                {
                    if ((direction < 270 || direction > 90)) {
                        turnRight();
                    } else {
                        turnLeft();
                    }
                }
                else
                {
                    moveForward();
                    turret.update();
                    clone = turret.stationaryCopy();
                }
            }
        }
        else if (nextMove[1] != currSpace[1])
        {
            if (nextMove[1] > currSpace[1])
            {
                if (hull.getObjectDirection() != 180) //turn down
                {
                    if (direction > 180) {
                        turnLeft();
                    } else {
                        turnRight();
                    }
                }
                else
                {
                    moveForward();
                    turret.update();
                    clone = turret.stationaryCopy();
                }
            }
            else
            {
                if (hull.getObjectDirection() != 0) //turn up
                {
                    if (direction < 180) {
                        turnLeft();
                    } else {
                        turnRight();
                    }
                }
                else
                {
                    moveForward();
                    turret.update();
                    clone = turret.stationaryCopy();
                }
            }
        }
        else //opponent in next path space
        {
            movementPath.pop();
        }
        turret.update();
        clone = turret.stationaryCopy();
    }

    /**
     * Method for generating the movement path between the opponent and the player.
     */
    private void generateMovementPathToPlayer()
    {
        int x, y, playerX, playerY;

        Integer[] pos = generateGridPos(getXPos(), getYPos());
        x = pos[0];
        y = pos[1];

        Integer[] playerPos = generateGridPos(playerXPos, playerYPos);
        playerX = playerPos[0]; playerY = playerPos[1];
        currSpace[0] = x;
        currSpace[1] = y;
        movementPath = findPath(x, y, playerX, playerY);
        movementPath.pop(); //remove current space position from path
    }


    /**
     * Method to determine whether a position is in the (approx) middle of a grid space.
     * @param x the x position
     * @param y the y position
     * @return true if position is in the (approx) middle, false if not
     */
    private boolean middleOfSpace(float x, float y)
    {
        float newX, newY;
        newX = (float) Math.floor(x);
        newX = newX / (map.getWidth() / mapGrid.length);
        int temp = (int) Math.floor(newX);
        float xResult = newX - temp;
        newY =  (float) Math.floor(y);
        newY = newY / (map.getHeight() / mapGrid[0].length);
        temp = (int) Math.floor(newY);
        float yResult = newY - temp;
        if (xResult < 0.6 && xResult > 0.4 && yResult < 0.6 && yResult > 0.4)
        {
            return true;
        }
        return false;
    }

    /**
     * Function to generate the coordinates on the map grid given the window coordinates of a position on screen
     * @param x window x position
     * @param y window y position
     * @return Integer array, index 0 being x coord, index 1 being y coord
     */
    private Integer[] generateGridPos(float x, float y)
    {
        int newX, newY;
        newX = (int) Math.floor(x);
        newX = (newX == 0 ? 0 : (int) Math.floor( newX / (map.getWidth() / mapGrid.length)));
        newY =  (int) Math.floor(y);
        newY = (newY == 0 ? 0 : (int) Math.floor(newY / (map.getHeight() / mapGrid[0].length)));
        return new Integer[]{newX, newY};
    }


    /**
     * Function to find path between two points on grid.     *
     * @param srcX starting point X coord
     * @param srcY starting point Y coord
     * @param destX destination point X coord
     * @param destY destination point Y coord
     * @return Stack of coords mapping out the path between the starting point and end point such that calling .pop() repeatedly on the stack until empty will provide the full route
     * excluding the starting point.
     */
    private Stack<Integer[]> findPath(int srcX, int srcY, int destX, int destY)
    {
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
            boolean action = false;
            if (!current.contains("T")) {
                temp = Integer.toString(x) + "," + Integer.toString(y-1);
                if (!found.contains(temp)) {
                    prev.push(new Integer[]{x, y - 1});
                    found.add(temp);
                    y = y - 1;
                    action = true;
                }
            }
            if (!action && !current.contains("L"))
            {
                temp = Integer.toString(x - 1) + "," + Integer.toString(y);
                if (!found.contains(temp))
                {
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
                    if (!found.contains(temp)) {
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
                    if (!found.contains(temp)) {
                        prev.push(new Integer[]{x + 1, y});
                        found.add(temp);
                        x = x + 1;
                        action = true;
                    }
                }
            }
            if (!action)
            {
                temp = Integer.toString(x) + "," + Integer.toString(y);
                prev.pop();
                Integer[] p = prev.peek();
                x = p[0]; y = p[1];
            }
            temp = "";
        }

        prev = reverseStack(prev);
        return prev;
    }


    /**
     * Function to reverse a Stack of integer arrays
     * @param s stack to reverse
     * @return reversed stack
     */
    private Stack<Integer[]> reverseStack(Stack<Integer[]> s)
    {
        if (s.isEmpty())
        {
            return s;
        }
        Integer[] bottom = popBottom(s);
        reverseStack(s);
        s.push(bottom);
        return s;
    }

    /**
     * Function to return the 'bottom' of stack (first item that was added)
     * @param s stack (of integer arrays)
     * @return bottom item of stack
     */
    private Integer[] popBottom(Stack<Integer[]> s)
    {
        Integer[] top = s.pop();
        if (s.isEmpty())
        {
            return top;
        }
        else
        {
            Integer[] bottom = popBottom(s);
            s.push(top);
            return bottom;
        }
    }

    /**
     * Function to determine if player lies on a line between two points.
     * @param x1 start point of line x position
     * @param y1 start point of line y position
     * @param x2 end point of line x position
     * @param y2 end point of line y position
     * @return true if player lies on the line between start and end points, false if not.
     */
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

        playerConstant = (playerYPos - (m*playerXPos));
        return (upperBound > playerConstant && lowerBound < playerConstant); //check player between two parallel lines
    }


    /**
     * Function to determine if a MapObject lies on the line between two points
     * @param x1 start point of line x position
     * @param y1 start point of line y position
     * @param x2 end point of line x position
     * @param y2 end point of line y position
     * @return true if MapObject lies on line, false if not
     */
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


    /**
     * Function to determine if player can be shot from opponent's current position.
     * Calls itself recursively until the max number of ricochets for a tank shell fired from this opponent has been reached.
     * @param x1 start x position of shell
     * @param y1 start y position of shell
     * @param x2 x position used to create equation for line of fire from (x1, y1) (will be a point that has been rotated around (x1, y1))
     * @param y2 y position use to create equation for line of fire from (x1, y1) (will be a point that has been rotated around (x1, y1))
     * @param direction rotation angle (in degrees) between (x1, y1) and (x2, y2)
     * @return true if player can be shot from current position, false if not
     */
    private boolean canHitPlayer(float x1, float y1, float x2, float y2, float direction)
    {
        float m, c, x, y, newX, newY;
        ArrayList<MapObject> objects = map.getObjectsInMap();
        m = (y1 - y2) / (x1 - x2);
        c =  y2 - (m*x2);
            if (x2 <= x1 && y2 <= y1) //can hit right side or bottom of object
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
                                return canHitPlayer(x, y, newX, newY, 180 - direction);
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
                                return canHitPlayer(x, y, newX, newY, 0 - direction);
                            }
                        }
                    }
                }
            }
            else if (x2 <= x1 && y2 >= y1) //can hit right side or top of object
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
                                 return canHitPlayer(x, y, newX, newY, 180 - direction);
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
                                 return canHitPlayer(x, y, newX, newY, 0 - direction);
                             }
                         }
                     }
                 }
            }
            else if (x2 >= x1 && y2 <= y1) //can hit left side or bottom of object
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
                                return canHitPlayer(x, y, newX, newY, 180 - direction);
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
                                return canHitPlayer(x, y, newX, newY, 0 - direction);
                            }
                        }
                    }
                }
            }
            else //left side or top of object
            {
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
                                return canHitPlayer(x, y, newX, newY, 0 - direction);
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
                                return canHitPlayer(x, y, newX, newY, 180 - direction);
                            }
                        }
                    }
                }
            }

        return false;
    }


    /**
     * Function to rotate coordinates around an origin by a certain amount
     * @param x1 origin point x pos
     * @param y1 origin point y pos
     * @param x2 point to rotate x pos
     * @param y2 point to rotate y pos
     * @param turnAmount amount to turn
     * @return float array of rotated coords (index 0 = x pos, index 1 = y pos)
     */
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
