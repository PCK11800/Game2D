package Tanks.Objects;

public class EdwardOpponent extends PatrollingOpponent
{

    public EdwardOpponent(Tank player, MapGenerator mapGen, int levelNum)
    {
        super(player, mapGen);
        setNoticeDistance(3);
        if (levelNum == 1) //first difficulty
        {
            setShellRicochetNumber(4);
            setFireDelay(10);
            setHealth(100);
        }
        else //second difficulty
        {
            setShellRicochetNumber(4);
            setFireDelay(5);
            setHealth(120);
        }
    }
}
