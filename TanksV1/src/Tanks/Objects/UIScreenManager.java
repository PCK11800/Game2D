package Tanks.Objects;

import Tanks.UIScreens.LeaderboardScreen;
import Tanks.UIScreens.MainMenu;
import Tanks.UIScreens.ShopScreen;
import Tanks.UIScreens.TutorialScreen;
import Tanks.Window.Window;


/**
 * There are 5 things a button can do. Only 4 of which this class really needs to handle: loading a new UIScreen, loading the next level or loading a new game (mode).
 */
public class UIScreenManager
{
    private UIScreen currentScreen;

    private Window window;

    private boolean onUIScreen = true; //default true - as you start off on the main menu - used in GameMode
    private boolean hideUI = false; //This is set when then you are on a UI screen but want to move on.
    private boolean loadLevel = false; //if this is false and onUIScreen is true, then you load the gameMode

    private LeaderboardScreen leaderboard;
    private TutorialScreen tutorial;
    private  MainMenu mainMenu;
    private ShopScreen shop;


    public UIScreenManager(Window window) // Will need to pass in the player tank here
    {
        this.window = window;
        initScreens();
    }

    /**
     * This method is used to initialize all of the UIScreens that the game / game mode uses
     * If new screens are to be added they are to be added here
     */
    private void initScreens()
    {
        this.shop = new ShopScreen(this.window);

        this.leaderboard = new LeaderboardScreen(this.window);
        this.tutorial = new TutorialScreen(this.window);

       this.mainMenu = new MainMenu(this.window, this.leaderboard, this.tutorial);

       this.leaderboard.initBackButton(this.mainMenu);
       this.tutorial.initBackButton(this.mainMenu);

        this.currentScreen = this.mainMenu;
    }


    public void displayShop()
    {
        this.currentScreen = this.shop;
    }


    public void changeState()
    {
        if(this.onUIScreen)
        {
            this.onUIScreen = false;
        }
        else
        {
            this.onUIScreen = true;
        }
    }

    public void resetFlags()
    {
        this.hideUI = false;
        this.loadLevel = false;
    }


    public boolean isOnUIScreen() { return this.onUIScreen; }

    public boolean hideUI() { return this.hideUI; }

    public boolean loadLevel() { return this.loadLevel; }


    public void update()
    {
        if (this.onUIScreen) //Only update if you are on a UIScreen - additional prevention
        {
            if (this.currentScreen.loadLinkedScreen())
            {
                System.out.println("WHY AM I HERE?");
                this.currentScreen.resetState();
                this.currentScreen = this.currentScreen.getLinkedScreen();
                this.currentScreen.resetState();
            }

            else if (this.currentScreen.loadGameMode())
            {
                System.out.println("GOT TRYING TO SET STUFF");
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
    }





}
