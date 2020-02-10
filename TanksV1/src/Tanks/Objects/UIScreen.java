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

    //All of the button array Lists
    private ArrayList<Button> quitButtons = new ArrayList<Button>();
    private ArrayList<LoadLevelButton> levelButtons = new ArrayList<LoadLevelButton>();
    private ArrayList<LoadUIScreenButton> uiScreenButtons = new ArrayList<LoadUIScreenButton>();
    private ArrayList<LoadGameModeButton> gameModeButtons = new ArrayList<LoadGameModeButton>();
    private ArrayList<UpgradeButton> upgradeButtons = new ArrayList<UpgradeButton>();


    private UIListener listener;
    private UIScreen linkedScreen; //This is set when a button is pressed

    //Flags
    private boolean loadGameMode = false;
    private boolean loadNextLevel = false;
    private boolean loadLinkedScreen = false;

    /**
     * Constructor
     * @param window the window everything is to be drawn into
     */
    public UIScreen(Window window)
    {
        this.window = window;
        this.listener = new UIListener(this);
    }

    /**
     * Adds a button to the screen that is used to start the game (on the main menu)
     * @param x the x position of the button in pixels (the center of the button)
     * @param y the y position of the button in pixels (the center of the button)
     * @param width the width of the button in pixels
     * @param height the height of the button in pixels
     * @param activeTexture the default (active) texture of the button
     * @param hoveredTexture the texture of the button when the mouse is hovering over it
     * @param pressedTexture the texture of the button when it has been pressed
     */
    public void addGameModeButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture)
    {
        LoadGameModeButton b = new LoadGameModeButton(this.window, x, y, width, height, activeTexture);
        b.setAltTextures(hoveredTexture, pressedTexture);

        gameModeButtons.add(b);
    }

    /**
     * Adds a button to the screen that is used to load the next level (e.g. in the shop)
     * @param x the x position of the button in pixels (the center of the button)
     * @param y the y position of the button in pixels (the center of the button)
     * @param width the width of the button in pixels
     * @param height the height of the button in pixels
     * @param activeTexture the default (active) texture of the button
     * @param hoveredTexture the texture of the button when the mouse is hovering over it
     * @param pressedTexture the texture of the button when it has been pressed
     */
    public void addLoadLevelButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture)
    {
        LoadLevelButton b = new LoadLevelButton(this.window, x, y, width, height, activeTexture);
        b.setAltTextures(hoveredTexture, pressedTexture);

        levelButtons.add(b);
    }

    /**
     * Adds a button to the screen that is used to load into another UIScreen (e.g. the tutorial button on the main menu)
     * @param x the x position of the button in pixels (the center of the button)
     * @param y the y position of the button in pixels (the center of the button)
     * @param width the width of the button in pixels
     * @param height the height of the button in pixels
     * @param activeTexture the default (active) texture of the button
     * @param hoveredTexture the texture of the button when the mouse is hovering over it
     * @param pressedTexture the texture of the button when it has been pressed
     */
    public void addLoadUIScreenButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture, UIScreen linkedScreen)
    {
        LoadUIScreenButton b = new LoadUIScreenButton(this.window, x, y, width, height, activeTexture, linkedScreen);
        b.setAltTextures(hoveredTexture, pressedTexture);

        uiScreenButtons.add(b);
    }

    /**
     * Adds a button to the screen that is used to upgrade the tank in the shop
     * @param x the x position of the button in pixels (the center of the button)
     * @param y the y position of the button in pixels (the center of the button)
     * @param width the width of the button in pixels
     * @param height the height of the button in pixels
     * @param activeTexture the default (active) texture of the button
     * @param hoveredTexture the texture of the button when the mouse is hovering over it
     * @param pressedTexture the texture of the button when it has been pressed
     */
    public void addUpgradeButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture)
    {
        UpgradeButton b = new UpgradeButton(this.window, x, y, width, height, activeTexture);
        b.setAltTextures(hoveredTexture, pressedTexture);

        upgradeButtons.add(b);
    }

    /**
     * Adds a button to the screen that is used to quit the game
     * @param x the x position of the button in pixels (the center of the button)
     * @param y the y position of the button in pixels (the center of the button)
     * @param width the width of the button in pixels
     * @param height the height of the button in pixels
     * @param activeTexture the default (active) texture of the button
     * @param hoveredTexture the texture of the button when the mouse is hovering over it
     * @param pressedTexture the texture of the button when it has been pressed
     */
    public void addQuitButton(float x, float y, float width, float height, String activeTexture, String hoveredTexture, String pressedTexture)
    {
        QuitButton b = new QuitButton(this.window, x, y, width, height, activeTexture);
        b.setAltTextures(hoveredTexture, pressedTexture);

        quitButtons.add(b);
    }


    /**
     * This method is used to draw all of the buttons in a given UI Screen to the screen
     */
    public void update()
    {
        this.listener.handleInput();

        for (Button quitButton : this.quitButtons)
        {
            quitButton.update();
        }

        for (LoadUIScreenButton uiButton: this.uiScreenButtons)
        {
            uiButton.update();
        }

        for (LoadLevelButton lvlButton: this.levelButtons)
        {
            lvlButton.update();
        }

        for (LoadGameModeButton gmButton: this.gameModeButtons)
        {
            gmButton.update();
        }

        for (UpgradeButton upButton: this.upgradeButtons)
        {
            upButton.update();
        }
    }

    //Status functions
    public boolean loadNextLevel() { return this.loadNextLevel; }

    public boolean loadLinkedScreen() { return this.loadLinkedScreen; }

    public boolean loadGameMode() { return this.loadGameMode; }

    /**
     * This is called when the linked screen / next level has been loaded.
     * The resets all of the status / flag functions
     */
    public void resetState()
    {
        this.loadGameMode = false;
        this.loadNextLevel = false;
        this.loadLinkedScreen = false;
    }

    /**
     * This method is used to return the screen that the current UI Screen is linked to (e.g. the tutorial screen is linked to the main menu)
     * @return the screen that is linked to the current UIScreen
     */
    public UIScreen getLinkedScreen() { return this.linkedScreen; }


    /**
     * This method is called by the UI handler when the mouse has been moved
     * The method handles mouse movement and changes the buttons to the correct texture based on the mouse's position
     * @param mouseXPos the x position of the mouse in pixels
     * @param mouseYPos the y position of the mouse in pixels
     */
    public void handleMouseMovement(float mouseXPos, float mouseYPos)
    {
        for (Button quitButton : this.quitButtons)
        {
            if (quitButton.contains(mouseXPos, mouseYPos) && (!quitButton.isHovered()))
            {
                quitButton.setHovered();
                return;
            }

            else if ((!quitButton.contains(mouseXPos, mouseYPos) && quitButton.isHovered()))
            {
                quitButton.setActive();
            }

        }

        for (LoadUIScreenButton uiButton : this.uiScreenButtons)
        {
            if (uiButton.contains(mouseXPos, mouseYPos) && (!uiButton.isHovered()))
            {
                uiButton.setHovered();
                return;
            }

            else if ((!uiButton.contains(mouseXPos, mouseYPos) && uiButton.isHovered()))
            {
                uiButton.setActive();
            }
        }

        for (LoadLevelButton lvlButton : this.levelButtons)
        {
            if (lvlButton.contains(mouseXPos, mouseYPos) && (!lvlButton.isHovered()))
            {
                lvlButton.setHovered();

                return;
            }

            else if ((!lvlButton.contains(mouseXPos, mouseYPos) && lvlButton.isHovered()))
            {
                lvlButton.setActive();
            }
        }

        for (LoadGameModeButton gmButton : this.gameModeButtons)
        {
            if (gmButton.contains(mouseXPos, mouseYPos) && (!gmButton.isHovered()))
            {
                gmButton.setHovered();

                return;
            }

            else if ((!gmButton.contains(mouseXPos, mouseYPos) && gmButton.isHovered()))
            {
                gmButton.setActive();
            }

        }

        for (UpgradeButton upButton : this.upgradeButtons)
        {
            if (upButton.contains(mouseXPos, mouseYPos) && (!upButton.isHovered()))
            {
                upButton.setHovered();

                return;
            }

            else if ((!upButton.contains(mouseXPos, mouseYPos) && upButton.isHovered()))
            {
                upButton.setActive();
            }

        }
    }


    /**
     * This method is called by the UI handler when the mouse has been pressed (left-click)
     * @param mouseXPos the x position of the mouse in pixels
     * @param mouseYPos the y position of the mouse in pixels
     */
    public void handleMouseClick(float mouseXPos, float mouseYPos)
    {
        for (Button quitButton : this.quitButtons)
        {
            if (quitButton.contains(mouseXPos, mouseYPos))
            {
                quitButton.setPressed();

                return;
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

                return;

            }
        }

        for (LoadLevelButton levelButton : this.levelButtons)
        {
            if (levelButton.contains(mouseXPos, mouseYPos))
            {
                levelButton.setPressed();
                resetState();
                this.loadNextLevel = true;

                return;
            }
        }

        for (LoadGameModeButton gmButton : this.gameModeButtons)
        {
            if (gmButton.contains(mouseXPos, mouseYPos))
            {
                gmButton.setPressed();
                resetState();
                this.loadGameMode = true;

                return;
            }
        }

        for (UpgradeButton upButton : this.upgradeButtons)
        {
            if (upButton.contains(mouseXPos, mouseYPos))
            {
                upButton.setPressed();
                resetState();

                //APPLY UPGRADE HERE
                return;
            }
        }
    }
}
