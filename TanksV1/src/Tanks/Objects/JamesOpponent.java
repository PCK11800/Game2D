package Tanks.Objects;

import Tanks.ObjectComponents.Textures;

public class JamesOpponent extends ConfusedOpponent
{
    public JamesOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setHullTexture(Textures.TANKHULL_JAMES);
        setTurretTexture(Textures.TANKTURRET_JAMES);
        if (levelNum == 1) //first difficulty
        {
            setHealth(100);
            setFireDelay(400);
            setDamagePerShell(25);
        }
        else //second difficulty
        {
            setHealth(100);
            setFireDelay(350);
            setDamagePerShell(30);
        }

    }
}
