package Tanks.Objects.VariousOpponents;

import Tanks.Objects.MapGenerator;
import Tanks.Objects.Opponent;
import Tanks.Objects.Tank;

import java.util.HashMap;
import java.util.Random;

public class PatrollingOpponent extends Opponent
{
    private Random r = new Random();
    private HashMap<Integer, Integer[]> corners = new HashMap<Integer, Integer[]>();
    private int target;

    public PatrollingOpponent(Tank player, MapGenerator mapGen)
    {
        super(player, mapGen);
        corners.put(0, new Integer[]{mapGrid.length - 1, mapGrid[0].length - 1});
        corners.put(1, new Integer[]{mapGrid.length - 1, 0});
        corners.put(2, new Integer[]{0, mapGrid[0].length - 1});
        corners.put(3, new Integer[]{0, 0});

        targetingPlayer = false;
        target = r.nextInt(4);
        targetTile = corners.get(target);
    }

    public boolean update()
    {
        super.update();
        if (tileReached)
        {
            int temp = target;
            while (temp == target)
            {
                temp = r.nextInt(4);
            }
            targetTile = corners.get(temp);
            target = temp;
        }
        return false;
    }
}