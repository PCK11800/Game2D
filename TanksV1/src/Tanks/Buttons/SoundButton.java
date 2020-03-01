package Tanks.Buttons;

import Tanks.Objects.Button;
import Tanks.Objects.Tank;
import Tanks.Sounds.GameMusicHandler;
import Tanks.Window.Window;
import org.jsfml.system.Clock;

public class SoundButton extends Button
{
    private Clock buttonClock = new Clock();
    private String type;
    private Tank player;
    private GameMusicHandler handler;

    public SoundButton(Window window, float x, float y, float width, float height, String activeTexture, String type, GameMusicHandler handler, Tank player)
    {
        super(window, x, y, width, height, activeTexture);
        this.type = type;
        this.handler = handler;
        this.player = player;
    }

    @Override
    public void setPressed()
    {
        if(buttonClock.getElapsedTime().asMilliseconds() > 10)
        {
            super.setPressed();

            if(type.equals("increase_volume"))
            {
                if(handler.getVolume() < 100) { handler.setVolume(handler.getVolume() + 1); }
                System.out.println("Volume Increased");
            }
            else if(type.equals("decrease_volume"))
            {
                if(handler.getVolume() > 0) { handler.setVolume(handler.getVolume() - 1); }
                System.out.println("Volume Decreased");
            }
            else {
                System.out.println("No such function");
            }

            buttonClock.restart();
        }
    }
}
