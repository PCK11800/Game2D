package Tanks.Buttons;

import Tanks.Objects.Button;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;

public class LoadUIScreenButton extends Button
{
    private UIScreen linkedScreen;

    public LoadUIScreenButton(Window window, float x, float y, float width, float height, String activeTexture, UIScreen linkedScreen)
    {
        super(window, x, y, width, height, activeTexture);
        this.linkedScreen = linkedScreen;
    }

    public UIScreen getLinkedScreen() { return this.linkedScreen; }

}
