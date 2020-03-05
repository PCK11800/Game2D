package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Opponent;
import Tanks.Objects.Tank;
import Tanks.Sounds.SoundsPath;

/**
 * This class is used to create an instance of a House Tank which remains stationary.
 */
public class HouseTankTurret extends ChasingOpponent
{
    /**
     * Constructor. Creates a new instance of HouseTankTurret class.
     * @param player tank to attack
     * @param mapGen MapGenerator for the map this Opponent is present in.
     */
    public HouseTankTurret(Tank player, MapGenerator mapGen)
    {
        super(player, mapGen);
        //disableMovement();
        setMovementSpeed(1);
        setHealth(100);
        setFireDelay(150);
        setRammingDamage(10);
        setShellRicochetNumber(3);
        setTurretTexture(Textures.TANKTURRET_RED_MACHINEGUN);
        setShellTexture(Textures.TANKSHELL_MACHINEGUN);
        setFiringSound(SoundsPath.MACHINEGUN, 5);
        setScale();
    }
}
