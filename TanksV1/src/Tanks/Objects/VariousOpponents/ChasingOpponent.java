package Tanks.Objects.VariousOpponents;

import Tanks.Objects.MapGenerator;
import Tanks.Objects.Opponent;
import Tanks.Objects.Tank;

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
