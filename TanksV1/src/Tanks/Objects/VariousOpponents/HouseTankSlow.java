package Tanks.Objects.VariousOpponents;

import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

public class HouseTankSlow extends ChasingOpponent {
    public HouseTankSlow(Tank player, MapGenerator mapGen)
    {
        super(player, mapGen);
        setMovementSpeed(3);
        setHealth(100);
        setDamagePerShell(20);
        setRammingDamage(20);
        setFireDelay(900);
    }
}
