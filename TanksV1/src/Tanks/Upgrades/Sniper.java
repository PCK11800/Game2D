package Tanks.Upgrades;

import Tanks.Objects.Tank;

public class Sniper
{
    public static void applyUpgrade(Tank t)
    {
        t.setFireDelay(5000);
        t.setDamage(2);
    }
}
