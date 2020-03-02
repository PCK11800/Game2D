package Tanks.Objects.VariousOpponents;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.MapGenerator;
import Tanks.Objects.Tank;

public class ControllerOpponent extends PatrollingOpponent {
    public ControllerOpponent(Tank player, MapGenerator mapGen)
    {
        super(player, mapGen);
        setName("CONTROLLER");
        setHullTexture(Textures.TANKHULL_FUTURE);
        setTurretTexture(Textures.TANKTURRET_RED);
        setHealth(400);
        setMovementSpeed(3);
        setDamagePerShell(35);
        setFireDelay(500);
        setRammingDamage(30);
        setNoticeDistance(3);
    }
}
