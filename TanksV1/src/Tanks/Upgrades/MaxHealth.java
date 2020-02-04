package Tanks.Upgrades;

import Tanks.Objects.Tank;

public class MaxHealth
{
    public static void applyUpgrade(Tank t)
    {
        t.increaseMaxHealth(1);
    }
}
