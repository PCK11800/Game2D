package Tanks.Buttons;

import Tanks.Objects.Button;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;

public class LoadUIScreenButton extends Button
{
    private Window window;
    private UIScreen linkedScreen;

    public LoadUIScreenButton(Window window, float x, float y, float width, float height, String activeTexture, UIScreen linkedScreen)
    {
        super(window, x, y, width, height, activeTexture);
        this.window = window;
        this.linkedScreen = linkedScreen;
    }


    protected void performOperation()
    {
        System.out.println("Load UI Screen");
    }

    public UIScreen getLinkedScreen() { return this.linkedScreen; }


    public void update()
    {
        draw(this.window);
    }
}
