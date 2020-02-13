package Tanks.Objects;

import Tanks.ObjectComponents.Textures;

public class EdwardOpponent extends PatrollingOpponent
{

    public EdwardOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setHullTexture(Textures.TANKHULL_EDWARD);
        setTurretTexture(Textures.TANKTURRET_EDWARD);
        if (levelNum == 1) //first difficulty
        {
            setNoticeDistance(2);
            setHealth(200);
        }
        else //second difficulty
        {
            setNoticeDistance(4);
            setHealth(300);
            setFireDelay(450);
            setDamagePerShell(25);
        }
    }
}
