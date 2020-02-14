package Tanks.Objects;

import Tanks.ObjectComponents.Textures;

public class TobyOpponent extends ConfusedOpponent
{
    public TobyOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setHullTexture(Textures.TANKHULL_TOBY);
        setTurretTexture(Textures.TANKTURRET_TOBY);
        if (levelNum == 1) //first difficulty
        {
            setHealth(500);
            setFireDelay(400);
        }
        else //second difficulty
        {
            setHealth(700);
            setFireDelay(350);
        }
        setDamagePerShell(20);

    }
}
