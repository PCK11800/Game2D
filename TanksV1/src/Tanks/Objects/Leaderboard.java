package Tanks.Objects;

import java.io.*;

public class Leaderboard
{
    private String[] names = new String[10];
    private int[] scores = new int[10];
    private int[] index = {0,1,2,3,4,5,6,7,8,9};
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
        if (score > scores[9])
        {
            int n = 10;
            for (int i = 0; i < n-1; i++)
            {
                for (int j = 0; j < n-1-1; j++)
                {
                    if (scores[j] > scores[j+1])
                    {
                        int tempScore = scores[j];
                        String tempName = names[j];

                        scores[j] = scores[j+1];
                        names[j] = names[j+1];

                        scores[j+1] = tempScore;
                        names[j+1] = tempName;
                    }
                }
            }
        }
        updateLeaderboard();
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
