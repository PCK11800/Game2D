package Tanks.Objects;


import Tanks.ObjectComponents.RotatingObject;
import Tanks.Window.Window;

/**
 * This class represents a button that the user can interact with
 */
public abstract class Button extends RotatingObject
{
    //Instance Variables
    protected Window window;

    private float xPos;
    private float yPos;
    private float width;
    private float height;

    private float xScale = 1;
    private float yScale = 1;
    private float[] scale = ObjectSizeHandler.scaleConstant();

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

        setButton(1, 1);

        this.initialWindowHeight = window.getHeight();
        this.initialWindowWidth = window.getWidth();

        this.currentWindowHeight = window.getHeight();
        this.currentWindowWidth = window.getWidth();
    }

    /**
     * This method is place the button
     * This is the main method to init a button
     * @param xScale the pixel scale on the x-axis
     * @param yScale the pixel scale on the y-axis
     */
    private void setButton(float xScale, float yScale)
    {
        setObjectTexture(this.activeTexture);

        setCenterLocation(this.xPos * xScale, this.yPos * yScale);
        this.xPos = this.xPos * xScale;
        this.yPos = this.yPos * yScale;

        this.width = this.width * xScale;
        this.height = this.height * yScale ;
        setSize(this.width * scale[0], this.height * scale[1]);
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
        float minXPos = (this.xPos - (this.width / 2)) * this.xScale;
        float minYPos = (this.yPos - (this.height / 2)) * this.yScale;
        float maxXPos = (this.xPos + (this.width / 2)) * this.xScale;
        float maxYPos = (this.yPos + (this.height / 2)) * this.yScale;

        if (x >= minXPos && x <= maxXPos)
        {
            if (y >= minYPos && y <= maxYPos)
            {
                return true;
            }
        }

        return false;
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
        if ((this.window.getSize().x != this.initialWindowWidth) || (this.window.getSize().y != this.initialWindowHeight)) // Checks to see if the window size has changed
        {
            this.currentWindowWidth = this.window.getSize().x;
            this.currentWindowHeight = this.window.getSize().y;

            float[] scale = ObjectSizeHandler.scaleConstant();
            this.xScale = scale[0];
            this.yScale = scale[1];
        }

        draw(this.window);
    }

}
