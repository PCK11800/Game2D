package Tanks.Objects;

import Tanks.UIScreens.*;
import Tanks.Window.Window;


/**
 * This class handles all UI events, UIScreen loading, etc.
 * There are 5 things a button can do. Only 4 of which this class really needs to handle: loading a new UIScreen, loading the next level or loading a new game (mode).
 */
public class UIScreenManager
{
    private UIScreen currentScreen;
    private Window window;

    private boolean onUIScreen = true; //default true - as you start off on the main menu - used in GameMode
    private boolean hideUI = false; //This is set when then you are on a UI screen but want to move on.
    private boolean loadLevel = false; //if this is false and onUIScreen is true, then you load the gameMode

    /**
     * Constructor
     * @param window the window that all UIScreens are to be drawn into
     */
    public UIScreenManager(Window window) // Will need to pass in the player tank here
    {
        this.window = window;
        this.currentScreen = initMainMenu();
    }

    /**
     * This method is used to initialize all of the screens related the main menu
     */
    private MainMenu initMainMenu()
    {
        LeaderboardScreen leaderboard = new LeaderboardScreen(this.window);
        TutorialScreen tutorial = new TutorialScreen(this.window);

        MainMenu mainMenu = new MainMenu(this.window, leaderboard, tutorial);

        leaderboard.initBackButton(mainMenu);
        tutorial.initBackButton(mainMenu);

        return mainMenu;
    }

    /**
     * This method is used to display the shop - it is called in gameMode after x number of rounds
     */
    public void displayShop(Tank player)
    {
        this.currentScreen = new ShopScreen(this.window, player);
    }

    /**
     * This method is used to display the end screen when the player either completes the game or dies
     */
    public void displayEndScreen()
    {
        EndScreen endScreen = new EndScreen(this.window);
        endScreen.initButtonEnd(initMainMenu());
        this.currentScreen = endScreen;
    }


    /**
     * This method changes the state of the UI manager - i.e. from showing UI to not showing UI
     */
    public void changeState()
    {
        if(this.onUIScreen)
        {
            this.onUIScreen = false;
            this.currentScreen = null;
        }
        else
        {
            this.onUIScreen = true;
        }
    }

    /**
     * This method is used to rest the flags that are used in gameMode to determine whether or not UI is to be shown
     */
    public void resetFlags()
    {
        this.hideUI = false;
        this.loadLevel = false;
    }

    /**
     * Returns the onUIScreen flag
     * @return true if it is, false if not
     */
    public boolean isOnUIScreen() { return this.onUIScreen; }

    /**
     * Returns the hideUI flag
     * @return true if it is, false if not
     */
    public boolean hideUI() { return this.hideUI; }


    /**
     * This method is used to update the currentUI screen.
     * This method handles all of the loading of UI screens
     */
    public void update()
    {
        if (this.onUIScreen) //Only update if you are on a UIScreen - additional prevention
        {
            if (this.currentScreen.loadLinkedScreen())
            {
                this.currentScreen.resetState();
                this.currentScreen = this.currentScreen.getLinkedScreen();
                this.currentScreen.resetState();
            }

            else if (this.currentScreen.loadGameMode())
            {
                this.currentScreen.resetState();
                this.hideUI = true;
                this.loadLevel = false;
            }

            else if (this.currentScreen.loadNextLevel())
            {
                this.currentScreen.resetState();
                this.hideUI = true;
                this.loadLevel = true;
            }

            this.currentScreen.update();
        }

        //To help improve ram usage - removes references so that they can be garbage collected+
        else if (currentScreen != null)
        {
            this.currentScreen = null;
        }
    }

}
