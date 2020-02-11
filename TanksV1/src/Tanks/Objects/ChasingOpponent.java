package Tanks.Objects;

public class ChasingOpponent extends Opponent
{
    public ChasingOpponent(Tank player, int[][] grid)
    {
        super(player, grid);
        targetingPlayer = true;
    }

    public boolean update()
    {
        super.update();
        return false;
    }
}
