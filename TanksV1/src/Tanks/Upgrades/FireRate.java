package Tanks.Upgrades;

import Tanks.Objects.Tank;

public class FireRate
{
    public static void applyUpgrade(Tank t)
    {
        t.setFireDelay(t.getFireDelay() - 1);
    }
}
