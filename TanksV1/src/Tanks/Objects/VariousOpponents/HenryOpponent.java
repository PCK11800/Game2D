package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

/**
 * This class is used to create an instance of a competitor (Henry).
 */
public class HenryOpponent extends ChasingOpponent
{
    /**
     * Constructor. Creates a new instance of HenryOpponent class.
     * @param player tank to attack
     * @param mapGen MapGenerator for the map this Opponent is present in.
     * @param levelNum difficulty setting (changes stats based on point in game this Opponent is spawned)
     */
     public HenryOpponent(Tank player, MapGenerator mapGen, int levelNum)
     {
         super(player, mapGen);
         setName("HENRY");
         setHullTexture(Textures.TANKHULL_HENRY);
         setTurretTexture(Textures.TANKTURRET_HENRY);
         if (levelNum == 1) //first difficulty
         {
             setHealth(300);
             setFireDelay(300);
         }
         else //second difficulty
         {
             setHealth(400);
             setFireDelay(300);
             setDamagePerShell(25);
         }

     }
}
