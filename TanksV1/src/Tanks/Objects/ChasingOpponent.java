package Tanks.Objects;

public class ChasingOpponent extends Opponent
{
    public ChasingOpponent(Tank player, MapGenerator mapGen)
    {
        super(player, mapGen);
        targetingPlayer = true;
    }

    public boolean update()
    {
        super.update();
        targetingPlayer = true;
        return false;
    }
}
