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
    private UIScreen beforePauseScreen;
    private Window window;

    private boolean onUIScreen = true; //default true - as you start off on the main menu - used in GameMode
    private boolean hideUI = false; //This is set when then you are on a UI screen but want to move on.
    private boolean loadLevel = false; //if this is false and onUIScreen is true, then you load the gameMode
    private boolean inShop = false;
    private boolean onPauseScreen = false;
    private boolean stateBeforePause = false; // False = gameplay, true = on UISCreen
    private GameMode game;

    /**
     * Constructor
     * @param window the window that all UIScreens are to be drawn into
     */
    public UIScreenManager(Window window, GameMode game) // Will need to pass in the player tank here
    {
        this.window = window;
        this.currentScreen = initMainMenu();
        this.game = game;
    }

    /**
     * This method is used to initialize all of the screens related the main menu
     */
    private MainMenu initMainMenu()
    {
        LeaderboardScreen leaderboard = new LeaderboardScreen(this.window);
        TutorialScreen tutorial = new TutorialScreen(this.window);
        StoryScreen story = new StoryScreen(this.window);
        MainMenu mainMenu = new MainMenu(this.window, leaderboard, tutorial, story);

        leaderboard.initBackButton(mainMenu);
        tutorial.initBackButton(mainMenu);
        story.setText("intro");

        return mainMenu;
    }

    /**
     * This method is used to display the shop - it is called in gameMode after x number of rounds
     */
    public void displayShop(Tank player)
    {
        ShopScreen shop = new ShopScreen(this.window, player);
        System.out.println(game.getLevelNum());
        switch (game.getLevelNum())
        {
            case 2:
                this.currentScreen = new StoryScreen(this.window, "before_battle_1", shop);
                break;
            case 3:
                this.currentScreen = new StoryScreen(this.window, "after_battle_1", shop);
                break;
            case 6:
                this.currentScreen = new StoryScreen(this.window, "before_battle_2", shop);
                break;
            case 7:
                this.currentScreen = new StoryScreen(this.window, "after_battle_2", shop);
                break;
            case 10:
                this.currentScreen = new StoryScreen(this.window, "before_battle_3", shop);
                break;
        }
        this.inShop = true;
    }

    public void displayPauseScreen()
    {
        //System.out.println("DISPLAY PAUSE SCREEN CALLED");
        PauseScreen pauseScreen = new PauseScreen(this.window, game.getGameMusicHandler());

        if (this.onUIScreen)
        {
            this.beforePauseScreen = this.currentScreen;
            this.stateBeforePause = true;
            this.hideUI = false;
        }

        else
        {
            this.stateBeforePause = false;
            this.hideUI = false;
        }

        this.currentScreen = pauseScreen;
        this.onPauseScreen = true;
        this.onUIScreen = false;
    }


    /**
     * This method is used to display the end screen when the player either completes the game or dies
     */
    public void displayEndScreen()
    {
        EndScreen endScreen = new EndScreen(this.window);
        endScreen.initBackButton(initMainMenu());
        this.currentScreen = endScreen;

        this.onUIScreen = true;
    }


    public void displayGameOverScreen()
    {
        GameOverScreen goScreen = new GameOverScreen(this.window);
        goScreen.initBackButton(initMainMenu());
        this.currentScreen = goScreen;

        this.onUIScreen = true;
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

    public void closeShop()
    {
        this.inShop = false;
    }

    public void setOnUIScreen(boolean b) {this.onUIScreen = b; }


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

    public boolean isInShop() { return this.inShop; }

    public boolean isOnPauseScreen() { return this.onPauseScreen; }


    public void resumeGame()
    {
        this.onPauseScreen = false;
        //System.out.println("STATE BEFORE: " + this.stateBeforePause);

        if (this.stateBeforePause)
        {
            this.currentScreen = beforePauseScreen;
            this.beforePauseScreen = null;

            this.hideUI = false;
            this.onUIScreen = true;
        }
        else
        {
            currentScreen = null;
            this.hideUI = true;
            this.onUIScreen = false;
        }
        //System.out.println("RESUME GAME CALLED");
    }


    /**
     * This method is used to update the currentUI screen.
     * This method handles all of the loading of UI screens
     */
    public void update()
    {
        if (this.onUIScreen || this.onPauseScreen) //Only update if you are on a UIScreen - additional prevention
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

            else if (this.currentScreen.resumeGame())
            {
                this.currentScreen.resetState();
                this.onUIScreen = false;
                this.hideUI = true;
            }

            this.currentScreen.update();
        }

        //To help improve ram usage - removes references so that they can be garbage collected
        else if (currentScreen != null)
        {
            this.currentScreen = null;
        }
    }

}
