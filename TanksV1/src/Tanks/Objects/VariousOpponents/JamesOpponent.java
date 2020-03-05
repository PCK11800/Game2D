package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

/**
 * This class is used to create an instance of a competitor (James).
 */
public class JamesOpponent extends ConfusedOpponent
{
    /**
     * Constructor. Creates a new instance of JamesOpponent class.
     * @param player tank to attack
     * @param mapGen MapGenerator for the map this Opponent is present in.
     * @param levelNum difficulty setting (changes stats based on point in game this Opponent is spawned)
     */
    public JamesOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setName("JAMES");
        setHullTexture(Textures.TANKHULL_JAMES);
        setTurretTexture(Textures.TANKTURRET_JAMES);
        if (levelNum == 1) //first difficulty
        {
            setHealth(300);
            setFireDelay(400);
            setDamagePerShell(25);
        }
        else //second difficulty
        {
            setHealth(400);
            setFireDelay(350);
            setDamagePerShell(30);
        }

    }
}
