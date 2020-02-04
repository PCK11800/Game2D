package Tanks.Upgrades;

import Tanks.Objects.Tank;

public class Heal
{
    public static void applyUpgrade(Tank t)
    {
        t.increaseHealth(-1);
    }
}
