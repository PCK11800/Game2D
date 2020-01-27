package Tanks.Upgrades;

import Tanks.Objects.Tank;

public class MaxHealth extends Upgrade
{
    private Tank tank;
    private int upgradeType;

    @Override
    public void applyUpgrade()
    {
        tank.increaseMaxHealth(1);
    }
}
