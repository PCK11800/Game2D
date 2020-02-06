package Tanks.Buttons;

import Tanks.Objects.Button;
import Tanks.Window.Window;

public class LoadGameModeButton extends Button
{
    private Window window;

    public LoadGameModeButton(Window window, float x, float y, float width, float height, String activeTexture)
    {
        super(window, x, y, width, height, activeTexture);
        this.window = window;
    }


    protected void performOperation()
    {
        System.out.println("Load game mode");
    }

    public void update()
    {
        draw(this.window);
    }
}
