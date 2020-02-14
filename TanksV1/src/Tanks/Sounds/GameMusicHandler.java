package Tanks.Sounds;

import org.jsfml.audio.SoundSource;

import java.util.ArrayList;
import java.util.Random;

public class GameMusicHandler {

    private ArrayList<GameMusic> musicList = new ArrayList<>();
    private boolean isBackgroundMusicDonePlaying;
    private GameMusic currentSong;
    private Random rand = new Random();

    public GameMusicHandler() {
        musicListLoad();
        isBackgroundMusicDonePlaying = true;
    }

    //PUT YOUR BACKGROUND MUSIC HERE
    private void musicListLoad() {
        musicList.add(new GameMusic(SoundsPath.MEGALOVANIA));
    }

    public void musicHandler() {
        if (isBackgroundMusicDonePlaying) {
            int randomSelection = rand.nextInt(musicList.size());
            currentSong = musicList.get(randomSelection);
            currentSong.play();
            isBackgroundMusicDonePlaying = false;
        }

        if (currentSong.getStatus() == SoundSource.Status.STOPPED) {
            isBackgroundMusicDonePlaying = true;
        }
    }
}
