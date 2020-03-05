package Tanks.Objects.VariousOpponents;

import Tanks.Objects.MapGenerator;
import Tanks.Objects.Opponent;
import Tanks.Objects.Tank;

/**
 * This class is used to create an instance of ChasingOpponent - which is primarily used as a superclass,
 * giving its' subclasses the behaviour of chasing the player regardless of whether or not they are within their notice distance or not.
 */
public class ChasingOpponent extends Opponent
{
    /**
     * Constructor. Creates a new instance of ChasingOpponent class.
     * @param player tank to attack
     * @param mapGen MapGenerator for the map this Opponent is present in.
     */
    public ChasingOpponent(Tank player, MapGenerator mapGen)
    {
        super(player, mapGen);
        targetingPlayer = true;
    }

    /**
     * Update method called once per game loop
     * @return
     */
    public boolean update()
    {
        super.update();
        targetingPlayer = true;
        return false;
    }
}
