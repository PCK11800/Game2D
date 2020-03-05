package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

/**
 * This class is used to create an instance of a competitor (Toby).
 */
public class TobyOpponent extends ConfusedOpponent
{
    /**
     * Constructor. Creates a new instance of TobyOpponent class.
     * @param player tank to attack
     * @param mapGen MapGenerator for the map this Opponent is present in.
     * @param levelNum difficulty setting (changes stats based on point in game this Opponent is spawned)
     */
    public TobyOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setName("TOBY");
        setHullTexture(Textures.TANKHULL_TOBY);
        setTurretTexture(Textures.TANKTURRET_TOBY);
        setScale();
        if (levelNum == 1) //first difficulty
        {
            setHealth(500);
            setFireDelay(400);
        }
        else //second difficulty
        {
            setHealth(600);
            setFireDelay(350);
        }
        setDamagePerShell(20);

    }
}
