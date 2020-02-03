package Tanks.Upgrades;

import Tanks.Objects.Tank;

public class FireRate extends Upgrade
{
    private Tank tank;
    private int upgradeType;

    @Override
    public void applyUpgrade()
    {
        tank.setFireDelay(tank.getFireDelay() + 10);
    }
}
