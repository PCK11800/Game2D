package Tanks.Objects;

import Tanks.ObjectComponents.Textures;

public class TankConfigs {

    public void player_default(Tank tank)
    {
        tank.setSize((float) 1, (float) 1);
        tank.setHullTurningDistance(3);
        tank.setTurretTurningDistance(3);
        tank.setMovementSpeed(5);
        tank.setInitialDirection(180);
        tank.setShellSpeed(10);
        tank.setShellRicochetNumber(2);
        tank.setRammingDamage(5);
        tank.setFireDelay(500);
        tank.enablePlayerControl();
        tank.setDamagePerShell(20);
    }

    public void enemy_default(Tank tank)
    {
        tank.setSize((float) 1, (float) 1);
        tank.setHullTurningDistance(3);
        tank.setTurretTurningDistance(3);
        tank.setMovementSpeed(5);
        tank.setInitialDirection(180);
        tank.setShellSpeed(10);
        tank.setShellRicochetNumber(2);
        tank.setRammingDamage(5);
        tank.setFireDelay(500);
        tank.setDamagePerShell(20);
        tank.enableEnemyCollision();
    }

    public void fastshot_upgrade(Tank tank)
    {
        tank.setShellSpeed(15);
        tank.setFireDelay(1000);
        tank.setDamagePerShell(50);
        tank.setShellRicochetNumber(1);
    }

    public void machinegun_upgrade(Tank tank)
    {
        tank.setShellSpeed(10);
        tank.setFireDelay(100);
        tank.setDamagePerShell(10);
        tank.setShellRicochetNumber(2);
    }

    public void sonicmode_upgrade(Tank tank)
    {
        tank.setMovementSpeed(10);
        tank.setHullTurningDistance(6);
    }
}
