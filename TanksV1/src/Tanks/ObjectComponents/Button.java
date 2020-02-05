package Tanks.ObjectComponents;


import Tanks.Window.Window;
import org.jsfml.graphics.FloatRect;

/** This class represents a button that the user can interact with
 *
 */
public class Button extends RotatingObject
{
    private Window window;
    private FloatRect collider;

    private String activeTexture;
    private String hoveredTexture;
    private String pressedTexture;

    private enum State
    {
        ACTIVE,
        HOVERED,
        PRESSED
    }

    private State state = State.ACTIVE;
    

    public Button(Window window, float x, float y, float width, float height, String activeTexture)
    {
        this.window = window;
        this.activeTexture = activeTexture;

        setObjectTexture(activeTexture);
        setCenterLocation(x, y);
        setSize(width, height);

        this.collider = new FloatRect(x - (width / 2), y - (height / 2), width, height + (height/3)); //Left anchor point - height += h/3 as without it there is large area that cannot be pressed that is on the button
    }


    public void setAltTextures(String hoveredTexture, String pressedTexture)
    {
        this.hoveredTexture = hoveredTexture;
        this.pressedTexture = pressedTexture;
    }


    public boolean contains(float x, float y)
    {
        //float rect has a contains method
        return collider.contains(x, y);
    }


    public void setHovered()
    {
        this.state = State.HOVERED;
        setObjectTexture(this.hoveredTexture);
    }

    public void setPressed()
    {
        this.state = State.PRESSED;
        setObjectTexture(this.pressedTexture);

        performOperation();
    }

    public void setActive()
    {
        this.state = State.ACTIVE;
        setObjectTexture(this.activeTexture);
    }


    private void performOperation() //Will pass in a function here
    {
        System.out.println("Button Pressed!");
        //Run function passed in
    }


    public void update()
    {
        draw(window);
    }

}
