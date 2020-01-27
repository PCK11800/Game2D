package Tanks.Upgrades;

import Tanks.Objects.Tank;

public class Heal extends Upgrade
{
    private Tank tank;
    private int upgradeType;

    @Override
    public void applyUpgrade()
    {
        tank.increaseHealth(1);
    }
}
