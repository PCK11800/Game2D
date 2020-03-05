package Tanks.Objects.VariousOpponents;

import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

/**
 * This class is used to create an instance of a House Tank which is slower than the others.
 */
public class HouseTankSlow extends ChasingOpponent {
    /**
     * Constructor. Creates a new instance of HouseTankSlow class.
     * @param player tank to attack
     * @param mapGen MapGenerator for the map this Opponent is present in.
     */
    public HouseTankSlow(Tank player, MapGenerator mapGen)
    {
        super(player, mapGen);
        setMovementSpeed(3);
        setHealth(100);
        setDamagePerShell(20);
        setRammingDamage(20);
        setFireDelay(900);
    }
}
