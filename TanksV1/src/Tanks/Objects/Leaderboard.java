package Tanks.Objects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Leaderboard
{
    private File file;
    private List<Score> scores;

    public Leaderboard()
    {
        try
        {
            file = new File("TanksV1/Resources/leaderboard.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            scores = new ArrayList<>();

            for (int i = 0; i < 10; i++)
            {
                String line = br.readLine();

                if (line != null)
                {
                    String[] lines = line.split(",");
                    scores.add(new Score(lines[0], Integer.parseInt(lines[1])));
                }

                else
                {
                    scores.add(new Score("EMPTY", 0));
                }
                System.out.println(scores.get(i).getName() + "   " + scores.get(i).getScore());
            }
        }
        catch (IOException e)
        {
            System.out.println(e);
        }

    }

    public String getString(int i)
    {
        String s = "test";
        return s;
    }
}
