package Tanks.Buttons;

import Tanks.Objects.Button;
import Tanks.Window.Window;

public class UpgradeButton extends Button
{
    public UpgradeButton(Window window, float x, float y, float width, float height, String activeTexture) //Will need to pass in the tank and upgrade type here
    {
        super(window, x, y, width, height, activeTexture);
        this.window = window;
    }

    @Override
    public void setPressed()
    {
        super.setPressed();
        System.out.println("Upgrade!");
    }
}
