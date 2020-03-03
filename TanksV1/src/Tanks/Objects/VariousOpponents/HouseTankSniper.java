package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;
import Tanks.Sounds.SoundsPath;

public class HouseTankSniper extends PatrollingOpponent {
    public HouseTankSniper(Tank player, MapGenerator mapGen)
    {
        super(player, mapGen);
        setHealth(50);
        setMovementSpeed(5);
        setShellRicochetNumber(5);
        setDamagePerShell(40);
        setFireDelay(2000);
        setTurretTexture(Textures.TANKTURRET_RED_RAILGUN);
        setShellTexture(Textures.TANKSHELL_FAST);
        setFiringSound(SoundsPath.RAILGUN, 80);
    }
}
