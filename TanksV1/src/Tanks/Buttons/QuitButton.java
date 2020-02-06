package Tanks.Buttons;

import Tanks.Objects.Button;
import Tanks.Window.Window;

public class QuitButton extends Button
{
    private Window window;

    public QuitButton(Window window, float x, float y, float width, float height, String activeTexture)
    {
        super(window, x, y, width, height, activeTexture);
        this.window = window;
    }

    protected void performOperation()
    {
        System.out.println("Quiting...");
        window.close();
    }

    public void update()
    {
        draw(this.window);
    }
}
