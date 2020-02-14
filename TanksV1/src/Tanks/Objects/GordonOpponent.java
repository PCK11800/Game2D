package Tanks.Objects;

import Tanks.ObjectComponents.Textures;

public class GordonOpponent extends PatrollingOpponent
{
    public GordonOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setHullTexture(Textures.TANKHULL_GORDON);
        setTurretTexture(Textures.TANKTURRET_GORDON);
        setNoticeDistance(-1);
        if (levelNum == 1) //first difficulty
        {
            setHealth(70);
            setMovementSpeed(10);
            setFireDelay(600);
        }
        else //second difficulty
        {
            setHealth(100);
            setMovementSpeed(10);
            setFireDelay(400);
        }
        setDamagePerShell(25);


    }
}
