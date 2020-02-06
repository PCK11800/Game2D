package Tanks.Listeners;

import Tanks.Objects.UIScreen;
import org.jsfml.window.Mouse;

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
     * Handles mouse input
     */
    public void handleInput()
    {
        uiScreen.handleMouseMovement(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);

        if(Mouse.isButtonPressed(Mouse.Button.LEFT))
        {
            uiScreen.handleMouseClick(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().y);
        }
    }


}
