package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

public class PercyOpponent extends ChasingOpponent
{
    public PercyOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setName("PERCY");
        setHullTexture(Textures.TANKHULL_PERCY);
        setTurretTexture(Textures.TANKTURRET_PERCY);
        setSize((float) 1, (float) 1);
        setNoticeDistance(-1);
        if (levelNum == 1) //first difficulty
        {
            setHealth(400);
            setMovementSpeed(5);
            setFireDelay(500);
        }
        else //second difficulty
        {
            setHealth(500);
            setMovementSpeed(7);
            setFireDelay(500);
        }
        setDamagePerShell(20);
        setRammingDamage(10);

    }
}
