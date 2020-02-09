package Tanks.Objects;

import java.util.HashMap;
import java.util.Random;

public class ConfusedOpponent extends Opponent
{
    private Random r = new Random();

    public ConfusedOpponent(Tank player, int[][] grid)
    {
        super(player, grid);
        targetingPlayer = false;
        targetTile = new Integer[]{r.nextInt(mapGrid.length), r.nextInt(mapGrid[0].length)};
    }

    public boolean update()
    {
        super.update();
        if (tileReached)
        {
            Integer[] temp = targetTile;
            while (temp[0].equals(targetTile[0]) && temp[1].equals(targetTile[1]))
            {
                temp = new Integer[]{r.nextInt(mapGrid.length), r.nextInt(mapGrid[0].length)};
            }
            targetTile = temp;
        }
        return false;
    }
}
