package Tanks.Buttons;

import Tanks.Objects.Button;
import Tanks.Objects.Tank;
import Tanks.Window.Window;
import org.jsfml.system.Clock;

public class UpgradeButton extends Button
{
    private Tank player;
    private String config;
    private Clock buttonClock = new Clock();

    public UpgradeButton(Window window, float x, float y, float width, float height, String activeTexture, Tank player, String config) //Will need to pass in the tank and config type here
    {
        super(window, x, y, width, height, activeTexture);
        this.window = window;
        this.player = player;
        this.config = config;
    }

    @Override
    public void setPressed()
    {
        if(buttonClock.getElapsedTime().asSeconds() > 1)
        {
            super.setPressed();

            this.player.config(this.config);
            System.out.println("Upgrade: " + this.config);
            buttonClock.restart();
        }
    }
}
