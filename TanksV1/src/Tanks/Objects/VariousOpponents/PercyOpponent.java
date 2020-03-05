package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

/**
 * This class is used to create an instance of a competitor (Percy).
 */
public class PercyOpponent extends ChasingOpponent
{
    /**
     * Constructor. Creates a new instance of PercyOpponent class.
     * @param player tank to attack
     * @param mapGen MapGenerator for the map this Opponent is present in.
     * @param levelNum difficulty setting (changes stats based on point in game this Opponent is spawned)
     */
    public PercyOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setName("PERCY");
        setHullTexture(Textures.TANKHULL_PERCY);
        setTurretTexture(Textures.TANKTURRET_PERCY);
        setNoticeDistance(-1);
        if (levelNum == 1) //first difficulty
        {
            setHealth(400);
            setMovementSpeed(5);
            setFireDelay(500);
        }
        else //second difficulty
        {
            setHealth(500);
            setMovementSpeed(7);
            setFireDelay(500);
        }
        setDamagePerShell(20);
        setRammingDamage(10);

    }
}
