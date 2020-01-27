package Tanks.Upgrades;

import Tanks.Objects.Tank;

public abstract class Upgrade
{
    private Tank tank;
    private int upgradeType;

    public void Upgrade(Tank t)
    {
        this.tank = t;
    }

    public abstract void applyUpgrade();
}