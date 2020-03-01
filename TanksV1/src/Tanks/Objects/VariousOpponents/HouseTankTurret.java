package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Opponent;
import Tanks.Objects.Tank;
import Tanks.Sounds.SoundsPath;

public class HouseTankTurret extends Opponent
{
    public HouseTankTurret(Tank player, MapGenerator mapGen)
    {
        super(player, mapGen);
        disableMovement();
        setHealth(100);
        setFireDelay(600);
        setRammingDamage(10);
        setShellRicochetNumber(3);
        setTurretTexture(Textures.TANKTURRET_RED_MACHINEGUN);
        setShellTexture(Textures.TANKSHELL_MACHINEGUN);
        setFiringSound(SoundsPath.MACHINEGUN, 5);
    }
}
