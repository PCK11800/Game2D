package Tanks.Objects;


import Tanks.ObjectComponents.RotatingObject;
import Tanks.Window.Window;
import org.jsfml.graphics.FloatRect;
import org.jsfml.system.Clock;

import java.awt.*;

/**
 * This class represents a button that the user can interact with
 */
public abstract class Button extends RotatingObject
{
    //Instance Variables
    protected Window window;
    private FloatRect collider;

    private float xPos;
    private float yPos;
    private float width;
    private float height;

    private String activeTexture;
    private String hoveredTexture;
    private String pressedTexture;

    private boolean isHovered = false;

    private float initialWindowWidth;
    private float initialWindowHeight;
    private float currentWindowWidth;
    private float currentWindowHeight;

    /**
     * Constructor
     * @param window the window the button is to be drawn into
     * @param x the buttons x position (the center of the button)
     * @param y  the buttons y position (the center of the button)
     * @param width the buttons width in pixels
     * @param height the buttons height in pixels
     * @param activeTexture the buttons default texture
     */
    public Button(Window window, float x, float y, float width, float height, String activeTexture)
    {
        this.window = window;
        this.activeTexture = activeTexture;

        this.xPos = x;
        this.yPos = y;
        this.width = width;
        this.height = height;

        this.activeTexture = activeTexture;

        /*
        setObjectTexture(activeTexture);
        setCenterLocation(x, y);
        setSize(width, height);
         */

        setButton(1, 1);

        this.initialWindowHeight = window.getHeight();
        this.initialWindowWidth = window.getWidth();

        this.currentWindowHeight = window.getHeight();
        this.currentWindowWidth = window.getWidth();

        //this.collider = new FloatRect(x - (width / 2), y - (height / 2), width, height); //Left anchor point - height += h/3 as without it there is large area that cannot be pressed that is on the button
    }

    private void setButton(float xScale, float yScale)
    {
        setObjectTexture(this.activeTexture);
        setCenterLocation(this.xPos * xScale, this.yPos * yScale);
        setSize(this.width *  xScale, this.height * yScale);
    }



    /**
     * This method is used to set the buttons alternate textures - i.e. when the mouse is over the button and when the button has been pressed
     * @param hoveredTexture the texture of the button when the mouse is over the button
     * @param pressedTexture the texture of the button when it has been pressed
     */
    public void setAltTextures(String hoveredTexture, String pressedTexture)
    {
        this.hoveredTexture = hoveredTexture;
        this.pressedTexture = pressedTexture;
    }

    /**
     * This method is used to check whether a given point (the mouse cursor) is within a button
     * @param x the x position of the object to be checked
     * @param y the x position of the object to be checked
     * @return true if it is within the button, false if it isn't
     */
    public boolean contains(float x, float y)
    {
        //float rect has a contains method
        //return this.getLocalBounds().contains(x, y);
        return this.getGlobalBounds().contains(x, y);
        //return collider.contains(x, y);
    }

    /**
     * This method changes the buttons texture to the hovered texture
     */
    public void setHovered()
    {
        this.isHovered = true;
        setObjectTexture(this.hoveredTexture);
    }

    /**
     * This method changes the buttons texture to the pressed texture
     */
    public void setPressed()
    {
        setObjectTexture(this.pressedTexture);
    }

    /**
     * This method changes the buttons texture to the default (active) texture
     */
    public void setActive()
    {
        this.isHovered = false;
        setObjectTexture(this.activeTexture);
    }

    /**
     * Gets if the current button is being hovered over
     * @return returns true if it is, false if not
     */
    public boolean isHovered() { return this.isHovered; }


    /**
     * This method draws the button in the window
     */
    public void update()
    {
        //System.out.println("Current Width:"  + window.getWidth() + " Stored: " + this.currentWindowWidth);
        //System.out.println("Current Height:"  + window.getHeight() + " Stored: " + this.currentWindowHeight);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        if (screenSize.getWidth() != this.currentWindowWidth || screenSize.getHeight() != this.currentWindowHeight)
        {
            System.out.println("THIS IS CALLED");
            float xScale =  (float) screenSize.getWidth() / this.initialWindowWidth;
            float yScale =  (float) screenSize.getHeight() / this.initialWindowHeight;

            this.currentWindowWidth = (float) screenSize.getWidth();
            this.currentWindowHeight = (float) screenSize.getHeight();

            setButton(xScale, yScale);
        }
        draw(this.window);
    }

}
