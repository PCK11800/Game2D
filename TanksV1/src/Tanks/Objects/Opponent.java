package Tanks.Objects;

import Tanks.ObjectComponents.MapObject;
import Tanks.ObjectComponents.TankTurret;
import Tanks.UIScreens.HealthBar;
import org.jsfml.system.Clock;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;


public class Opponent extends Tank {


    private int movementCount = 0;
    private double direction = 0;
    protected Tank player;
    private float playerXPos;
    private float playerYPos;
    private int ricochetCount = 0;
    private TankTurret clone;
    protected String[][] mapGrid;
    private Stack<Integer[]> movementPath;
    private Integer[] currSpace = new Integer[2];
    protected Clock timer = new Clock();
    protected int pathCalcDelay = 2;
    private int moveDir = 1;
    protected boolean targetingPlayer = true;
    protected Integer[] targetTile = new Integer[2];
    protected boolean tileReached = false;
    private int noticeDistance = 1;
    protected MapGenerator mapGenerator;
    private boolean doesMove = true;
    private HealthBar healthBar;


    /**
     * Constructor. Creates new instance of Opponent.
     * @param player the Tank this opponent will be targeting
     * @param mapGen the map generator
     */
    public Opponent(Tank player, MapGenerator mapGen)
    {
        config("enemy_default");
        this.mapGenerator = mapGen;
        int grid[][] = mapGen.getMap();
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
    }

    public Opponent(Opponent clone)
    {
        this(clone.player, clone.mapGenerator);
    }



    /**
     * Update method called every game loop.
     * @return
     */
    public boolean update()
    {
        super.update();
        if (tileReached) generateMovementPathToTile();
        tileReached = false;

        //first update loop actions
        if (movementCount == 0)
        {
            for (int i  = 0; i< 210; i++) {

                rotateTurretRight();
            }
            if (targetingPlayer)
            {
                generateMovementPathToPlayer();
            }
            else
            {
              generateMovementPathToTile();
            }
            clone = turret.stationaryCopy();
            healthBar = new HealthBar(window, this);
        }
        if (timer.getElapsedTime().asSeconds() > pathCalcDelay && middleOfSpace(hull.getxPos(), hull.getyPos()))
        {
            if (targetingPlayer)
            {
                generateMovementPathToPlayer();
            }
            timer.restart();
        }
        if (player.isAlive())
        {
            if (!action() && doesMove) move();
        }
        clone.update();
        healthBar.update();
        if (movementCount == 0) movementCount++;
        playerXPos = player.getXPos();
        playerYPos = player.getYPos();



        //is player in notice range? if yes, target
        Integer[] playerPos = generateGridPos(player.getXPos(), player.getYPos());
        Integer[] pos = generateGridPos(getXPos(), getYPos());
        int moveCount = 0;
        Stack<Integer[]> pathToPlayer = findPath(pos[0], pos[1], playerPos[0], playerPos[1]);
        while (!pathToPlayer.isEmpty())
        {
            pathToPlayer.pop();
            moveCount++;
        }
        if (moveCount <= noticeDistance)
        {
            targetingPlayer = true;
        }
        else
        {
            targetingPlayer = false;
        }

        return false;
    }

    /**
     * Method for detecting if player can be hit by the opponent at any rotation of the opponent's turret given the opponents' current position within the level.
     */
    private boolean action()
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
            return true;
        }
        else
        {
            rotateTurretRight();
            return false;
        }
    }


    /**
     * Method moves opponent to 'target' space in the grid. Returns immediately if opponent already at location.
     */
    private void move()
    {
        if (movementPath.isEmpty())
        {
           if (!targetingPlayer) tileReached = true;
            return;
        }

        direction = hull.getObjectDirection();
        Integer[] nextMove = movementPath.peek();
        currSpace = generateGridPos(hull.getxPos() , hull.getyPos());

        //attempt to get opponent 'unstuck' if they collide with walls
        if (collision())
        {
            moveDir = 0;
            resetCollision();
        }

        //move forward if not in center of a tile
        if (!middleOfSpace(hull.getxPos(), hull.getyPos()))
        {
            if (moveDir == 1)
            {
                moveForward();
            }
            else
            {
                moveBackward();
            }
            clone.setNewPosition(getXPos(), getYPos());
            return;
        }
        moveDir = 1;

        //turn in direction of moment
        if (nextMove[0] != currSpace[0])
        {
            if (nextMove[0] < currSpace[0]) {
                if (hull.getObjectDirection() != 270) // turn left
                {
                    if (direction < 90 || direction > 270) {
                        turnLeft();
                    } else {
                        turnRight();
                    }
                }
                else
                {
                    moveForward();
                    clone.setNewPosition(getXPos(), getYPos());
                }
            }
            else {
                if (hull.getObjectDirection() != 90) //turn right
                {
                    if ((direction > 270 || direction < 90)) {
                        turnRight();
                    } else {
                        turnLeft();
                    }
                }
                else
                {
                    moveForward();
                    clone.setNewPosition(getXPos(), getYPos());
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
                    clone.setNewPosition(getXPos(), getYPos());
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
                    clone.setNewPosition(getXPos(), getYPos());
                }
            }
        }
        else //opponent in next path space
        {
            movementPath.pop();
        }
        clone.setNewPosition(getXPos(), getYPos());
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

    private void generateMovementPathToTile()
    {
        Integer[] pos = generateGridPos(getXPos(), getYPos());
        currSpace[0] = pos[0];
        currSpace[1] = pos[1];
        movementPath = findPath(currSpace[0], currSpace[1], targetTile[0], targetTile[1]);
        movementPath.pop();
    }


    /**
     * Method to determine whether a position is in the (approx) middle of a grid space.
     * @param x the x position
     * @param y the y position
     * @return true if position is in the (approx) middle, false if not
     */
    private boolean middleOfSpace(float x, float y)
    {
        float tileCenterX = (this.mapGenerator.getTileSize() * this.mapGenerator.getXScale()) / 2;
        float tileCenterY = (this.mapGenerator.getTileSize() * this.mapGenerator.getYScale()) / 2;

        x -= (tileCenterX + (this.mapGenerator.getWallShort() * this.mapGenerator.getXScale()));
        y -= (tileCenterY + this.mapGenerator.getOffsetTopY());

        float diffX = x % tileCenterX;
        float diffY = y % tileCenterY;

        //check not between tiles
        int edgeX = Math.round(x / tileCenterX);
        int edgeY = Math.round(y / tileCenterY);

        if ((diffX < 5 || diffX > tileCenterX - 5) && (diffY < 5 || diffY > tileCenterY - 5) && (edgeX % 2 == 0) && (edgeY % 2 == 0))
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
    protected Integer[] generateGridPos(float x, float y)
    {
        int newX, newY;
        newX = (int) Math.floor(x);
        newX = (newX == 0 ? 0 : (int) Math.floor(newX / (map.getWidth() / mapGrid.length)));
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
    protected Stack<Integer[]> findPath(int srcX, int srcY, int destX, int destY)
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
                Integer[] t = prev.peek();
                prev.pop();
                try
                {
                    Integer[] p = prev.peek();
                    x = p[0]; y = p[1];
                }
                catch (EmptyStackException e)
                {
                    //temporary fix for bug where player moves off the map grid before next level loaded
                    //resulting in empty stack exception when Opponent initially tries to find a path to the player
                    //this try-catch should be removed when the door functionality is fixed
                    prev.push(new Integer[]{0, 0});
                    return prev;
                }
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
     * Function to determine if a tank lies on a line between two points.
     * @param x1 start point of line x position
     * @param y1 start point of line y position
     * @param x2 end point of line x position
     * @param y2 end point of line y position
     * @param isSelf indicates whether the tank to checked is the player tank or this opponent (true if self, false if player)
     * @return true if player lies on the line between start and end points, false if not.
     */
    private boolean isTankInFiringLine(float x1, float y1, float x2, float y2, boolean isSelf)
    {
        Float m, c, tankConstant, upperBound, lowerBound;
        //working out equation of line through turret (firing line)
        m = (y1 - y2) / (x1 - x2);
        c =  y2 - (m*x2);

        //constant val for parallel lines between which player tank can be shot at
        upperBound = isSelf ? c + hull.getHeight()/2 : c + player.hull.getHeight()/2;
        lowerBound = isSelf ? c - hull.getHeight()/2 : c - player.hull.getHeight()/2;

        if (m.isInfinite())
        {
            if (x1 == x2)
            {
                if (!isSelf && playerXPos < (x1 + player.hull.getWidth()/2) && playerXPos > (x1 - player.hull.getWidth()/2)) return true;
                if (isSelf && getXPos() < (x1 + hull.getWidth()/2) && getXPos() > (x1 - hull.getWidth()/2)) return true;
            }
            else
            {
                if (playerYPos < (y1 + player.hull.getWidth()/2) && playerYPos > (y1 - player.hull.getWidth()/2)) return true;
                if (isSelf && getXPos() < (y1 + hull.getWidth()/2) && getXPos() > (y1 - hull.getWidth()/2)) return true;
            }
        }

        tankConstant = isSelf ? getYPos() - (m*getXPos()) : (playerYPos - (m*playerXPos));
        return (upperBound > tankConstant && lowerBound < tankConstant); //check player between two parallel lines
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
                            if (playerXPos + (player.hull.getWidth() / 2) > x && playerXPos + (player.hull.getWidth() / 2) < x1 && isTankInFiringLine(x1, y1, x2, y2, false) && !isTankInFiringLine(x1, y1, x2, y2, true)) {
                                return true;
                            } else if (ricochetCount <= shellRicochetNumber) {
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
                            if (playerXPos + (player.hull.getWidth() / 2) > x && playerXPos + (player.hull.getWidth() / 2) < x1 && isTankInFiringLine(x1, y1, x2, y2, false) && !isTankInFiringLine(x1, y1, x2, y2, true)) {
                                return true;
                            } else if (ricochetCount < shellRicochetNumber) {
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
                             if (playerXPos + (player.hull.getWidth() / 2) > x && playerXPos + (player.hull.getWidth() / 2) < x1 && isTankInFiringLine(x1, y1, x2, y2, false) && !isTankInFiringLine(x1, y1, x2, y2, true)) {
                                 return true;
                             } else if (ricochetCount < shellRicochetNumber - 1) {
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
                             if (playerXPos + (player.hull.getWidth() / 2) < y && playerXPos + (player.hull.getWidth() / 2) > y1 && isTankInFiringLine(x1, y1, x2, y2, false) && !isTankInFiringLine(x1, y1, x2, y2, true)) {
                                 return true;
                             } else if (ricochetCount < shellRicochetNumber - 1) {
                                 ricochetCount++;
                                 return canHitPlayer(x, y, newX, newY, 0 - direction);
                             }
                         }
                     }
                 }
            }
            else if (x2 >= x1 && y2 <= y1) //can hit left side or bottom of object
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
                        float coords[] = rotateCoordinates(x, y, newX, newY, 180 - direction);
                        newX = coords[0];
                        newY = coords[1];
                        if (!isObjectInPath(x1, y1, x, y)) {
                            if (playerXPos + (player.hull.getWidth() / 2) < x && playerXPos + (player.hull.getWidth() / 2) > x1 && isTankInFiringLine(x1, y1, x2, y2, false) && !isTankInFiringLine(x1, y1, x2, y2, true)) {
                                return true;
                            } else if (ricochetCount < shellRicochetNumber - 1) {
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
                            if (playerXPos + (player.hull.getWidth() / 2) < x && playerXPos + (player.hull.getWidth() / 2) > x1 && isTankInFiringLine(x1, y1, x2, y2, false) && !isTankInFiringLine(x1, y1, x2, y2, true)) {
                                return true;
                            } else if (ricochetCount < shellRicochetNumber - 1) {
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
                            if (playerXPos + (player.hull.getWidth() / 2) < x && playerXPos + (player.hull.getWidth() / 2) > x1 && isTankInFiringLine(x1, y1, x2, y2, false) && !isTankInFiringLine(x1, y1, x2, y2, true)) {
                                return true;
                            } else if (ricochetCount < shellRicochetNumber - 1) {
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
                            if (playerXPos + (player.hull.getWidth() / 2) < x && playerXPos + (player.hull.getWidth() / 2) > x1 && isTankInFiringLine(x1, y1, x2, y2, false) && !isTankInFiringLine(x1, y1, x2, y2, true)) {
                                return true;
                            } else if (ricochetCount < shellRicochetNumber - 1) {
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
     * @param y1 origin point y posd
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

    public void setNoticeDistance(int dist) { this.noticeDistance = dist; }

    public void setPathCalcDelay(int delay) { this.pathCalcDelay = delay; }

    public void disableMovement() { this.doesMove = false; }
}
