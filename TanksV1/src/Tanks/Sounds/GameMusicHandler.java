package Tanks.Sounds;

import Tanks.ObjectComponents.RotatingObject;
import org.jsfml.audio.SoundSource;

import java.util.ArrayList;
import java.util.Random;

public class GameMusicHandler {

    private ArrayList<GameMusic> musicList = new ArrayList<>();
    private boolean isBackgroundMusicDonePlaying;
    private GameMusic currentSong;
    private Random rand = new Random();
    private float volume;

    public GameMusicHandler() {
        musicListLoad();
        isBackgroundMusicDonePlaying = true;
        volume = 50;

        int randomSelection = rand.nextInt(musicList.size());
        currentSong = musicList.get(randomSelection);
    }

    //PUT YOUR BACKGROUND MUSIC HERE
    private void musicListLoad() {
        musicList.add(new GameMusic(SoundsPath.NEOSPRINGCORE));
    }

    public void musicHandler() {
        if (isBackgroundMusicDonePlaying) {
            int randomSelection = rand.nextInt(musicList.size());
            currentSong = musicList.get(randomSelection);
            currentSong.setVolume(volume);
            currentSong.play();
            isBackgroundMusicDonePlaying = false;
        }

        if (currentSong.getStatus() == SoundSource.Status.STOPPED) {
            isBackgroundMusicDonePlaying = true;
        }
    }

    public void pause()
    {
        if(currentSong.getStatus() == SoundSource.Status.PLAYING)
        {
            currentSong.pause();
        }
    }

    public void resume()
    {
        if(currentSong.getStatus() == SoundSource.Status.PAUSED)
        {
            currentSong.play();
        }
    }

    public void setVolume(float volume)
    {
        currentSong.setVolume(volume);
        this.volume = volume;
    }

    public float getVolume()
    {
        return currentSong.getVolume();
    }
}
