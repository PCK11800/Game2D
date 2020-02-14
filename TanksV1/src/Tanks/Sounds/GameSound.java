package Tanks.Sounds;

import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class GameSound extends Sound{

    SoundBuffer buffer = new SoundBuffer();

    public GameSound(String soundPath)
    {
        setSoundPath(soundPath);
    }

    public void setSoundPath(String soundPath)
    {
        Path path = FileSystems.getDefault().getPath(soundPath);
        try
        {
            buffer.loadFromFile(path);
            setBuffer(buffer);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
