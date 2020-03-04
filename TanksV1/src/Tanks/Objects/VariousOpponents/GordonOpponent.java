package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

public class GordonOpponent extends PatrollingOpponent
{
    public GordonOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setName("GORDON");
        setHullTexture(Textures.TANKHULL_GORDON);
        setTurretTexture(Textures.TANKTURRET_GORDON);
        setSize((float) 1, (float) 1);
        setNoticeDistance(-1);
        if (levelNum == 1) //first difficulty
        {
            setHealth(300);
            setMovementSpeed(10);
            setFireDelay(600);
        }
        else //second difficulty
        {
            setHealth(400);
            setMovementSpeed(10);
            setFireDelay(400);
        }
        setDamagePerShell(25);


    }
}
