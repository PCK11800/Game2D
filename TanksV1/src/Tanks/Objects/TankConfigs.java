package Tanks.Objects;

import Tanks.ObjectComponents.Textures;
import Tanks.Sounds.SoundsPath;

public class TankConfigs {

    public void player_default(Tank tank)
    {
        tank.setHullTexture(Textures.TANKHULL_GREEN);
        tank.setTurretTexture(Textures.TANKTURRET_GREEN);
        tank.setShellTexture(Textures.TANKSHELL_DEFAULT);
        tank.setFiringSound(SoundsPath.TANKFIRING, 20);
        tank.setMovingSound(SoundsPath.TANKMOVING, 10);
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
        tank.setHullTexture(Textures.TANKHULL_RED);
        tank.setTurretTexture(Textures.TANKTURRET_RED);
        tank.setShellTexture(Textures.TANKSHELL_DEFAULT);
        tank.setFiringSound(SoundsPath.TANKFIRING, 20);
        tank.setMovingSound(SoundsPath.TANKMOVING, 5);
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
        //tank.enableEnemyCollision();
    }

    public void player_future(Tank tank)
    {
        tank.setHullTexture(Textures.TANKHULL_FUTURE);
        tank.setTurretTexture(Textures.TANKTURRET_FUTURE_DEFAULT);
        tank.setShellTexture(Textures.TANKSHELL_DEFAULT);
        tank.setFiringSound(SoundsPath.TANKFIRING, 80);
        tank.setMovingSound(SoundsPath.TANKMOVING, 10);
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

    public void railgun_upgrade(Tank tank)
    {
        tank.setTurretTexture(Textures.TANKTURRET_GREEN_RAILGUN);
        tank.setSize((float) 1, (float) 1);
        tank.turret.setSize((float) 1 * 53, (float) 1 * 75);
        tank.setShellTexture(Textures.TANKSHELL_FAST);
        tank.setFiringSound(SoundsPath.RAILGUN, 80);
        tank.setShellSpeed(15);
        tank.setFireDelay(1000);
        tank.setDamagePerShell(50);
        tank.setShellRicochetNumber(1);
    }

    public void machinegun_upgrade(Tank tank)
    {
        tank.setTurretTexture(Textures.TANKTURRET_GREEN_MACHINEGUN);
        tank.setShellTexture(Textures.TANKSHELL_MACHINEGUN);
        tank.setFiringSound(SoundsPath.MACHINEGUN, 5);
        tank.setShellSpeed(10);
        tank.setFireDelay(250);
        tank.setDamagePerShell(10);
        tank.setShellRicochetNumber(2);
    }

    public void sonicmode_upgrade(Tank tank)
    {
        tank.setMovementSpeed(10);
        tank.setHullTurningDistance(6);
    }

    private void halfTextureFix(Tank tank){
        //Put all the textures that somehow get split in half here
        tank.setTurretTexture(Textures.TANKTURRET_FUTURE_RAILGUN);
    }
}
