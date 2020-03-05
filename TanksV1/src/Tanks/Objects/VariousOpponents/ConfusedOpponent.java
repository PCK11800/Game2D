package Tanks.Objects.VariousOpponents;

import Tanks.Objects.MapGenerator;
import Tanks.Objects.Opponent;
import Tanks.Objects.Tank;

import java.util.Random;
/**
 * This class is used to create an instance of ChasingOpponent - which is primarily used as a superclass,
 * giving its' subclasses a random movement style across the map.
 */
public class ConfusedOpponent extends Opponent
{
    private Random r = new Random();

    /**
     * Constructor. Creates a new instance of ConfusedOpponent class.
     * @param player tank to attack
     * @param mapGen MapGenerator for the map this Opponent is present in.
     */
    public ConfusedOpponent(Tank player, MapGenerator mapGen)
    {
        super(player, mapGen);
        targetingPlayer = false;
        targetTile = new Integer[]{r.nextInt(mapGrid.length), r.nextInt(mapGrid[0].length)};
    }

    /**
     * Update method called once per game loop
     * @return
     */
    public boolean update()
    {
        super.update();
        if (tileReached)
        {
            Integer[] temp = targetTile;
            while (temp[0].equals(targetTile[0]) && temp[1].equals(targetTile[1]))
            {
                temp = new Integer[]{r.nextInt(mapGrid.length), r.nextInt(mapGrid[0].length)};
            }
            targetTile = temp;
        }
        return false;
    }
}
