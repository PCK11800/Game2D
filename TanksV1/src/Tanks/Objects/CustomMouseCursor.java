package Tanks.Objects;

import Tanks.ObjectComponents.RotatingObject;
import Tanks.Window.Window;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;

/**
 * This class is used to replace the mouse cursor with a custom mouse cursor
 */
public class CustomMouseCursor extends RotatingObject
{
    private Window window;

    /**
     * The constructor
     * @param window the window the cursor is to be drawn into
     * @param texture the texture of the mouse cursor (e.g. Textures.MOUSE_CURSOR)
     */
    public CustomMouseCursor(Window window, String texture)
    {
        this.window = window;
        setObjectTexture(texture);
    }

    /**
     * This method is used to update the mouse
     * It changes the mouse cursor location and draws it onto the window
     */
    public void update()
    {
        Vector2i mousePos = Mouse.getPosition(this.window);
        setCenterLocation(mousePos.x, mousePos.y);

        draw(this.window);
    }
}
