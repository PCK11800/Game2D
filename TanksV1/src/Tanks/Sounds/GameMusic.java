package Tanks.Sounds;

import org.jsfml.audio.Music;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class GameMusic extends Music {

    public GameMusic(String musicPath)
    {
        setMusicPath(musicPath);
    }

    public void setMusicPath(String musicPath)
    {
        Path path = FileSystems.getDefault().getPath(musicPath);
        try
        {
            openFromFile(path);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
