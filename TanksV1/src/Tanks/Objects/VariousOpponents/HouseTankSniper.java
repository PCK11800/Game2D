package Tanks.Objects.VariousOpponents;

import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

public class HouseTankSniper extends PatrollingOpponent {
    public HouseTankSniper(Tank player, MapGenerator mapGen)
    {
        super(player, mapGen);
        setHealth(50);
        setMovementSpeed(5);
        setShellRicochetNumber(5);
        setDamagePerShell(40);
        setFireDelay(700);
    }
}
