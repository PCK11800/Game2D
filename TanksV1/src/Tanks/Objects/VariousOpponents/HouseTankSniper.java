package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;
import Tanks.Sounds.SoundsPath;

/**
 * This class is used to create an instance of a House Tank which has increased shell damage.
 */
public class HouseTankSniper extends PatrollingOpponent {
    /**
     * Constructor. Creates a new instance of HouseTankSniper class.
     * @param player tank to attack
     * @param mapGen MapGenerator for the map this Opponent is present in.
     */
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
