package Tanks.Upgrades;

import Tanks.Objects.Tank;

public class Minigun
{
    public static void applyUpgrade(Tank t)
    {
        t.setFireDelay(100);
        t.setDamage(0.5);
    }
}
