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
            }

            Collections.sort(scores, new Comparator<Score>() {
                @Override
                public int compare(Score o1, Score o2) {
                    return Integer.compare(o2.getScore(), o1.getScore());
                }
            });

            for (int i = 0; i < 10; i++)
                System.out.println(scores.get(i).getName() + "   " + scores.get(i).getScore());
        }
        catch (IOException e)
        {
            System.out.println(e);
        }

    }

    public void addScore(String name, int score)
    {
        if (score > scores.get(9).getScore())
        {
            scores.set(9, new Score(name, score));
            Collections.sort(scores, new Comparator<Score>() {
                @Override
                public int compare(Score o1, Score o2) {
                    return Integer.compare(o1.getScore(), o2.getScore());
                }
            });
        }
    }

    public String getName(int i)
    {
        String s = scores.get(i-1).getName();
        return s;
    }

    public int getScore(int i)
    {
        int score = scores.get(i-1).getScore();
        return score;
    }
}
