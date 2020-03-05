package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

/**
 * This class is used to create an instance of a competitor (Gordon).
 */
public class GordonOpponent extends PatrollingOpponent
{
    /**
     * Constructor. Creates a new instance of GordonOpponent class.
     * @param player tank to attack
     * @param mapGen MapGenerator for the map this Opponent is present in.
     * @param levelNum difficulty setting (changes stats based on point in game this Opponent is spawned)
     */
    public GordonOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setName("GORDON");
        setHullTexture(Textures.TANKHULL_GORDON);
        setTurretTexture(Textures.TANKTURRET_GORDON);
        setScale();
        setNoticeDistance(-1);
        if (levelNum == 1) //first difficulty
        {
            setHealth(300);
            setMovementSpeed(10);
            setFireDelay(600);
        }
        else //second difficulty
        {
            setHealth(400);
            setMovementSpeed(10);
            setFireDelay(400);
        }
        setDamagePerShell(25);


    }
}
