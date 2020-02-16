package Tanks.UIScreens;

import org.jsfml.graphics.Font;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class GameFont extends Font {

    public GameFont(String fontPath)
    {
        setGameTextPath(fontPath);
    }

    public void setGameTextPath(String fontPath)
    {
        Path path = FileSystems.getDefault().getPath(fontPath);
        try
        {
            loadFromFile(path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
