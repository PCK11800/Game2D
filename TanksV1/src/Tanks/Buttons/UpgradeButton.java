package Tanks.Buttons;

import Tanks.Objects.Button;
import Tanks.Objects.Tank;
import Tanks.Window.Window;
import org.jsfml.graphics.Color;
import org.jsfml.system.Clock;

public class UpgradeButton extends Button
{
    private Tank player;
    private String config;
    private Clock buttonClock = new Clock();
    private int cost;
    private Color color;

    public UpgradeButton(Window window, float x, float y, float width, float height, String activeTexture, Tank player, String config, int cost) //Will need to pass in the tank and config type here
    {
        super(window, x, y, width, height, activeTexture);
        this.window = window;
        this.player = player;
        this.config = config;
        this.cost = cost;
        this.color = getColor();

        if(this.player.installedUpgrades.contains(this.config))
        {
            this.setColor(Color.RED);
        }
    }

    @Override
    public void setPressed()
    {
        if(buttonClock.getElapsedTime().asSeconds() > 1)
        {
            super.setPressed();

            if(this.player.getMoney() < cost && !this.player.installedUpgrades.contains(this.config)){
                System.out.println("Not enough money!");
            }
            else if(this.player.installedUpgrades.contains(this.config))
            {
                if(this.config.equals("minigun_upgrade") || this.config.equals("railgun_upgrade"))
                {
                    this.player.config("defaultgun_upgrade");
                    this.player.installedUpgrades.remove(this.config);
                    this.player.installedUpgrades.trimToSize();
                    this.setColor(this.color);
                }
                else{
                    System.out.println("Already got this upgrade!");
                }
            }
            else{
                this.player.decreaseMoney(cost);
                this.player.config(this.config);
                System.out.println("Upgrade: " + this.config);

                if(!this.config.equals("increase_maxhealth") && !this.config.equals("fullheal") && !this.config.equals("halfheal")){
                    this.player.installedUpgrades.add(this.config);
                    this.setColor(Color.RED);
                }
            }

            buttonClock.restart();
        }
    }
}
