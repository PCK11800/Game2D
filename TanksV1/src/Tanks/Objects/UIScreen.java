package Tanks.Objects;

import Tanks.ObjectComponents.Button;
import Tanks.ObjectComponents.Textures;
import Tanks.Window.Window;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;
import java.awt.MouseInfo;
import java.util.ArrayList;

/**
 * The parent of all UI sreens
 */
public abstract class UIScreen
{
    private Window window;
    private ArrayList<Button> buttons = new ArrayList<Button>();

    public UIScreen(Window window)
    {
        this.window = window;
    }

    public void addButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture)
    {
        Button b = new Button(this.window, x, y, width, height, Textures.BRICKBLOCK);
        b.setAltTextures(hoveredTexture, pressedTexture);

        buttons.add(b);
    }

    /**
     * This is called so quickly that it runs 3 times even though it was pressed once
     */
    public void update()
    {
        float mouseXPos = MouseInfo.getPointerInfo().getLocation().x;
        float mouseYPos = MouseInfo.getPointerInfo().getLocation().y;

        //Vector2i mousePos = Mouse.getPosition();

        boolean mousePressed = Mouse.isButtonPressed(Mouse.Button.LEFT);

        for (Button b : this.buttons)
        {
            if (b.contains(mouseXPos, mouseYPos) && mousePressed)
            {
                b.setPressed();
            }
            else if (b.contains(mouseXPos, mouseYPos))
            {
                b.setHovered();
            }
            else
            {
                b.setActive();
            }
            b.update();
        }

    }


}
