package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

public class PercyOpponent extends ChasingOpponent
{
    public PercyOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setHullTexture(Textures.TANKHULL_PERCY);
        setTurretTexture(Textures.TANKTURRET_PERCY);
        setNoticeDistance(-1);
        if (levelNum == 1) //first difficulty
        {
            setHealth(100);
            setMovementSpeed(5);
            setFireDelay(500);
        }
        else //second difficulty
        {
            setHealth(150);
            setMovementSpeed(7);
            setFireDelay(500);
        }
        setDamagePerShell(20);
        setRammingDamage(10);

    }
}
