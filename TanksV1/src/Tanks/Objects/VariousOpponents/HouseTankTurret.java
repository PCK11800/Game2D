package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Opponent;
import Tanks.Objects.Tank;

public class HouseTankTurret extends Opponent
{
    public HouseTankTurret(Tank player, MapGenerator mapGen)
    {
        super(player, mapGen);
        disableMovement();
        setHealth(100);
        setFireDelay(100);
        setRammingDamage(10);
        setShellRicochetNumber(3);
    }
}
