package Tanks.Objects;

public class Score
{
    private String name;
    private int score;

    /**
     * Creates a new score using a given name and score. The class simply stores the given values so scores can be
     * sorted the arraylist within the leaderboard class.
     */
    public Score(String n, int s)
    {
        name = n;
        score = s;
    }

    public String getName()
    {
        return name;
    }

    public int getScore()
    {
        return score;
    }
}
