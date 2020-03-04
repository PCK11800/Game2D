package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

public class EdwardOpponent extends PatrollingOpponent
{

    public EdwardOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setName("EDWARD");
        setHullTexture(Textures.TANKHULL_EDWARD);
        setTurretTexture(Textures.TANKTURRET_EDWARD);
        setScale();
        if (levelNum == 1) //first difficulty
        {
            setNoticeDistance(2);
            setHealth(400);
        }
        else //second difficulty
        {
            setNoticeDistance(4);
            setHealth(500);
            setFireDelay(450);
            setDamagePerShell(25);
        }
    }
}
