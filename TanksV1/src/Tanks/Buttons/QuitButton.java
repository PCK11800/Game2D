package Tanks.Buttons;

import Tanks.Objects.Button;
import Tanks.Window.Window;

public class QuitButton extends Button
{
    public QuitButton(Window window, float x, float y, float width, float height, String activeTexture)
    {
        super(window, x, y, width, height, activeTexture);
    }


    @Override
    public void setPressed()
    {
        super.setPressed();
        System.out.println("Quiting...");
        this.window.close();
    }
}
