package Tanks.Objects;

import java.io.*;

public class Leaderboard
{
    private String[] names = new String[10];
    private int[] scores = new int[10];
    private File file;

    public Leaderboard()
    {
        try
        {
            file = new File("TanksV1/Resources/leaderboard.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            for (int i = 0; i < 10; i++)
            {
                String line = br.readLine();

                if (line != null)
                {
                    String[] lines = line.split(",");
                    names[i] = lines[0];
                    scores[i] = Integer.parseInt(lines[1]);
                }

                else
                {
                    names[i] = "EMPTY";
                    scores[i] = 0;
                }
            }
            for (int i = 0; i < 10; i++)
                System.out.println((i+1) + ". " + names[i] + "   " + scores[i]);
        }
        catch (IOException e)
        {
            System.out.println(e);
        }

    }

    public void addToLeaderboard(String name, int score)
    {

    }

    public void updateLeaderboard()
    {

    }

    public String getString(int i)
    {
        String s = i + ". " + names[i-1] + "   " + scores[i-1];
        return s;
    }
}
