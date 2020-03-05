package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

/**
 * This class is used to create an instance of the final boss (The Controller).
 */
public class ControllerOpponent extends PatrollingOpponent {
    /**
     * Constructor. Creates a new instance of ControllerOpponent class.
     * @param player tank to attack
     * @param mapGen MapGenerator for the map this Opponent is present in.
     */
    public ControllerOpponent(Tank player, MapGenerator mapGen)
    {
        super(player, mapGen);
        setName("CONTROLLER");
        setHullTexture(Textures.TANKHULL_FUTURE);
        setTurretTexture(Textures.TANKTURRET_FUTURE_DEFAULT);
        setHealth(400);
        setMovementSpeed(3);
        setDamagePerShell(35);
        setFireDelay(500);
        setRammingDamage(30);
        setNoticeDistance(3);
        setScale();
    }
}
