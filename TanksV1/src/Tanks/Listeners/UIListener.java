package Tanks.Listeners;

import Tanks.Objects.UIScreen;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;
import org.jsfml.window.Window;

import java.awt.*;

/**
 * This class is used in UIScreens to listen out for mouse presses
 */
public class UIListener
{
    private UIScreen uiScreen;

    public UIListener(UIScreen screen)
    {
        this.uiScreen = screen;
        handleInput();
    }

    /**
     * This method handles the players mouse inputs
     * It handles both movement and left-clicks
     */
    public void handleInput()
    {
        Vector2i mousePos = Mouse.getPosition(uiScreen.getWindow()); // This gets the mouse position relative to the window
        float mousePosX = mousePos.x;
        float mousePosY = mousePos.y;


        if(Mouse.isButtonPressed(Mouse.Button.LEFT))
        {
            uiScreen.handleMouseClick(mousePosX, mousePosY);
        }
        else
        {
            uiScreen.handleMouseMovement(mousePosX, mousePosY);
        }
    }
}
