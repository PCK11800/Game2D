package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

public class TobyOpponent extends ConfusedOpponent
{
    public TobyOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setName("TOBY");
        setHullTexture(Textures.TANKHULL_TOBY);
        setTurretTexture(Textures.TANKTURRET_TOBY);
        setSize((float) 1, (float) 1);
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
