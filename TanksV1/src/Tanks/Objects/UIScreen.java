package Tanks.Objects;

import Tanks.Buttons.*;
import Tanks.Listeners.UIListener;
import Tanks.ObjectComponents.Textures;
import Tanks.Window.Window;
import java.util.ArrayList;

/**
 * The parent of all UI screens - e.g. shops, main menus, etc.
 */
public abstract class UIScreen
{
    private Window window;
    private ArrayList<Button> buttons = new ArrayList<Button>();

    private UIListener listener;


    public UIScreen(Window window)
    {
        this.window = window;
        this.listener = new UIListener(this);
    }

    public void addGameModeButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture)
    {
        Button b = new LoadGameModeButton(this.window, x, y, width, height, activeTexture);
        b.setAltTextures(hoveredTexture, pressedTexture);

        buttons.add(b);
    }

    public void addLoadLevelButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture)
    {
        Button b = new LoadLevelButton(this.window, x, y, width, height, activeTexture);
        b.setAltTextures(hoveredTexture, pressedTexture);

        buttons.add(b);
    }

    public void addLoadUIScreenButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture)
    {
        Button b = new LoadUIScreenButton(this.window, x, y, width, height, activeTexture);
        b.setAltTextures(hoveredTexture, pressedTexture);

        buttons.add(b);
    }

    public void addUpgradeButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture)
    {
        Button b = new UpgradeButton(this.window, x, y, width, height, activeTexture);
        b.setAltTextures(hoveredTexture, pressedTexture);

        buttons.add(b);
    }

    public void addQuitButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture)
    {
        Button b = new QuitButton(this.window, x, y, width, height, activeTexture);
        b.setAltTextures(hoveredTexture, pressedTexture);

        buttons.add(b);
    }


    /**
     * This is called so quickly that it runs 3 times even though it was pressed once
     */
    public void update()
    {
        for (Button b : this.buttons)
        {
            this.listener.handleInput();
            b.update();
        }
    }

    public void handleMouseMovement(float mouseXPos, float mouseYPos)
    {
        for (Button b : this.buttons)
        {
            if (b.contains(mouseXPos, mouseYPos))
            {
                b.setHovered();
            }
            else
            {
                b.setActive();
            }
        }
    }


    public void handleMouseClick(float mouseXPos, float mouseYPos)
    {
        for (Button b : this.buttons)
        {
            if (b.contains(mouseXPos, mouseYPos))
            {
                b.setPressed();
            }

            b.update();
        }
    }


}
