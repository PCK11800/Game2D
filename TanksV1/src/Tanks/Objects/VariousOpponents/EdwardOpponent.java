package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

/**
 * This class is used to create an instance of a competitor (Edward).
 */
public class EdwardOpponent extends PatrollingOpponent
{
    /**
     * Constructor. Creates a new instance of EdwardOpponent class.
     * @param player tank to attack
     * @param mapGen MapGenerator for the map this Opponent is present in.
     * @param levelNum difficulty setting (changes stats based on point in game this Opponent is spawned)
     */
    public EdwardOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setName("EDWARD");
        setHullTexture(Textures.TANKHULL_EDWARD);
        setTurretTexture(Textures.TANKTURRET_EDWARD);
        setScale();
        if (levelNum == 1) //first difficulty
        {
            setNoticeDistance(2);
            setHealth(400);
        }
        else //second difficulty
        {
            setNoticeDistance(4);
            setHealth(500);
            setFireDelay(450);
            setDamagePerShell(25);
        }
    }
}
