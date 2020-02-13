package Tanks.Objects;


import Tanks.ObjectComponents.RotatingObject;
import Tanks.Window.Window;
import org.jsfml.graphics.FloatRect;

/**
 * This class represents a button that the user can interact with
 */
public abstract class Button extends RotatingObject
{
    //Instace Variables
    protected Window window;
    private FloatRect collider;

    private String activeTexture;
    private String hoveredTexture;
    private String pressedTexture;

    private boolean isHovered = false;



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

        setObjectTexture(activeTexture);
        setCenterLocation(x, y);
        setSize(width, height);

        this.collider = new FloatRect(x - (width / 2), y - (height / 2), width, height); //Left anchor point - height += h/3 as without it there is large area that cannot be pressed that is on the button
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
        return collider.contains(x, y);
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
    public void update() { draw(this.window); }

}
