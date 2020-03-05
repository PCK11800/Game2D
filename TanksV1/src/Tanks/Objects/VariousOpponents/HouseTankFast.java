package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

/**
 * This class is used to create an instance of a House Tank which is faster than the others.
 */
public class HouseTankFast extends ConfusedOpponent
{
    /**
     * Constructor. Creates a new instance of HouseTankFast class.
     * @param player tank to attack
     * @param mapGen MapGenerator for the map this Opponent is present in.
     */
    public  HouseTankFast(Tank player, MapGenerator mapGen)
    {
        super(player, mapGen);
        setHullTexture(Textures.TANKHULL_RED);
        setTurretTexture(Textures.TANKTURRET_BLUE);
        setScale();
        setHealth(50);
        setFireDelay(1000);
        setDamagePerShell(10);
        setRammingDamage(10);
        setMovementSpeed(10);
        setNoticeDistance(-1);
        setPathCalcDelay(2);
        setHullTurningDistance(5);
    }
}
