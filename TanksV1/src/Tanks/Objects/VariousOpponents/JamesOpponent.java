package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

public class JamesOpponent extends ConfusedOpponent
{
    public JamesOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setName("JAMES");
        setHullTexture(Textures.TANKHULL_JAMES);
        setTurretTexture(Textures.TANKTURRET_JAMES);
        setSize((float) 1, (float) 1);
        if (levelNum == 1) //first difficulty
        {
            setHealth(300);
            setFireDelay(400);
            setDamagePerShell(25);
        }
        else //second difficulty
        {
            setHealth(400);
            setFireDelay(350);
            setDamagePerShell(30);
        }

    }
}
