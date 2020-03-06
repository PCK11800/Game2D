package Tanks.Objects;

import java.io.*;
import java.util.*;

public class Leaderboard
{
    private File file;
    private List<Score> scores;

    public Leaderboard()
    {
        try
        {
            //loads the local leaderboard file
            file = new File("TanksV1/Resources/leaderboard.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            scores = new ArrayList<>();

            /**
             * The file should only ever be 10 lines.
             * Reads through all ten lines, parsing each to get a name and a score.
             * If the line is empty the default "EMPTY - 0" score is stored.
             */
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

            //sorts the new array list in descending order
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

    /**
     * Takes a name and a score and looks to add them both to the leaderboard list.
     * If the score is bigger than the lowest score, it's stored in the leaderboard.
     * Once the old score is replaced the arraylist is then sorted again.
     * @param name
     * @param score
     */
    public void addScore(String name, int score)
    {
        if (score > scores.get(9).getScore())
        {
            scores.remove(9);
            scores.add(new Score(name, score));
            Collections.sort(scores, new Comparator<Score>() {
                @Override
                public int compare(Score o1, Score o2) {
                    return Integer.compare(o2.getScore(), o1.getScore());
                }
            });
        }

        update();
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

    /**
     * Updates the leaderboard that's stored locally by creating a new leaderboard.txt file using the scores stored
     * in the array list.
     */
    public void update()
    {
        try
        {
            file = new File("TanksV1/Resources/leaderboard.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < 10; i++)
            {
                if (scores.get(i).getName() != "EMPTY" && scores.get(i).getScore() != 0)
                {
                    writer.write(scores.get(i).getName() + "," + scores.get(i).getScore());
                    writer.newLine();
                }
            }
            writer.close();
        }
        catch (IOException e)
        {
            System.out.println(e);
        }
    }
}
