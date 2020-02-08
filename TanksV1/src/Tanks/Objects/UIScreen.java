package Tanks.Objects;

import Tanks.Buttons.*;
import Tanks.Listeners.UIListener;
import Tanks.Window.Window;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * The parent of all UI screens - e.g. shops, main menus, etc.
 */
public abstract class UIScreen
{
    private Window window;
    private ArrayList<Button> buttons = new ArrayList<Button>();
    private ArrayList<LoadLevelButton> levelButtons = new ArrayList<LoadLevelButton>();
    private ArrayList<LoadUIScreenButton> uiScreenButtons = new ArrayList<LoadUIScreenButton>();
    private ArrayList<LoadGameModeButton> gameModeButtons = new ArrayList<LoadGameModeButton>();
    private ArrayList<UpgradeButton> upgradeButtons = new ArrayList<UpgradeButton>();


    private UIListener listener;
    private UIScreen linkedScreen; //This is set when a button is pressed

    private boolean loadGameMode = false;
    private boolean loadNextLevel = false;
    private boolean loadLinkedScreen = false;


    public UIScreen(Window window)
    {
        this.window = window;
        this.listener = new UIListener(this);
    }

    public void addGameModeButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture)
    {
        LoadGameModeButton b = new LoadGameModeButton(this.window, x, y, width, height, activeTexture);
        b.setAltTextures(hoveredTexture, pressedTexture);

        gameModeButtons.add(b);
    }

    public void addLoadLevelButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture)
    {
        LoadLevelButton b = new LoadLevelButton(this.window, x, y, width, height, activeTexture);
        b.setAltTextures(hoveredTexture, pressedTexture);

        levelButtons.add(b);
    }

    public void addLoadUIScreenButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture, UIScreen linkedScreen)
    {
        LoadUIScreenButton b = new LoadUIScreenButton(this.window, x, y, width, height, activeTexture, linkedScreen);
        b.setAltTextures(hoveredTexture, pressedTexture);

        uiScreenButtons.add(b);
    }

    public void addUpgradeButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture)
    {
        UpgradeButton b = new UpgradeButton(this.window, x, y, width, height, activeTexture);
        b.setAltTextures(hoveredTexture, pressedTexture);

        upgradeButtons.add(b);
    }

    public void addQuitButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture)
    {
        QuitButton b = new QuitButton(this.window, x, y, width, height, activeTexture);
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

        for (LoadUIScreenButton uiButton: this.uiScreenButtons)
        {
            this.listener.handleInput();
            uiButton.update();
        }

        for (LoadLevelButton lvlButton: this.levelButtons)
        {
            this.listener.handleInput();
            lvlButton.update();
        }

        for (LoadGameModeButton gmButton: this.gameModeButtons)
        {
            this.listener.handleInput();
            gmButton.update();
        }

        for (UpgradeButton upButton: this.upgradeButtons)
        {
            this.listener.handleInput();
            upButton.update();
        }

    }

    //Status functions
    public boolean loadNextLevel() { return this.loadNextLevel; }

    public boolean loadLinkedScreen() { return this.loadLinkedScreen; }

    public boolean loadGameMode() { return this.loadGameMode; }

    /**
     * This is called when the linked screen / next level has been loaded
     */
    public void resetState()
    {
        this.loadGameMode = false;
        this.loadNextLevel = false;
        this.loadLinkedScreen = false;
    }

    public UIScreen getLinkedScreen() { return this.linkedScreen; }


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


        for (LoadUIScreenButton uiButton : this.uiScreenButtons)
        {
            if (uiButton.contains(mouseXPos, mouseYPos))
            {
                uiButton.setHovered();
            }
            else
            {
                uiButton.setActive();
            }
        }

        for (LoadLevelButton lvlButton : this.levelButtons)
        {
            if (lvlButton.contains(mouseXPos, mouseYPos))
            {
                lvlButton.setHovered();
            }
            else
            {
                lvlButton.setActive();
            }
        }

        for (LoadGameModeButton gmButton : this.gameModeButtons)
        {
            if (gmButton.contains(mouseXPos, mouseYPos))
            {
                gmButton.setHovered();
            }
            else
            {
                gmButton.setActive();
            }
        }

        for (UpgradeButton upButton : this.upgradeButtons)
        {
            if (upButton.contains(mouseXPos, mouseYPos))
            {
                upButton.setHovered();
            }
            else
            {
                upButton.setActive();
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
        }

        for (LoadUIScreenButton uiButton : this.uiScreenButtons)
        {
            if (uiButton.contains(mouseXPos, mouseYPos))
            {
                uiButton.setPressed();

                resetState();
                this.loadLinkedScreen = true;
                this.linkedScreen = uiButton.getLinkedScreen();
            }
        }

        for (LoadLevelButton levelButton : this.levelButtons)
        {
            if (levelButton.contains(mouseXPos, mouseYPos))
            {
                levelButton.setPressed();
                resetState();
                this.loadNextLevel = true;
            }
        }

        for (LoadGameModeButton gmButton : this.gameModeButtons)
        {
            if (gmButton.contains(mouseXPos, mouseYPos))
            {
                gmButton.setPressed();
                resetState();
                this.loadGameMode = true;

            }
        }

        for (UpgradeButton upButton : this.upgradeButtons)
        {
            if (upButton.contains(mouseXPos, mouseYPos))
            {
                upButton.setPressed();
                resetState();

                //APPLY UPGRADE HERE
            }
        }
    }
}
