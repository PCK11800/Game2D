package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

public class HenryOpponent extends ChasingOpponent
{
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
