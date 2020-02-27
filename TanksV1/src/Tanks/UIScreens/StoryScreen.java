package Tanks.UIScreens;


import Tanks.ObjectComponents.Textures;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;
import org.jsfml.graphics.Color;

public class StoryScreen extends UIScreen {
    private Window window;
    private String section;
    private boolean isSetup = true;
    private String intro = "25/02/2021 : " +
            "It has been 364 days since you lost the title of Tank Champion. " +
            "This time last year, The Controller won the Tank Wars contest by foul means, using illegal" +
            " upgrades to ensure you lost out on the prize. This year you want revenge. " +
            "You are determined to battle The Controller once more and show the world who is the real champion. " +
            "To achieve this, you decide to enter the contest with a new model. A tank capable of beating The Controller for good.\n\n " +
            "A tank named Thomas.";
    private String beforeBattle1 = "26/02/2021 : " +
            "Congratulations. You have successfully fought your way through the House Tanks. Now comes the real challenge... the first Competitor..." +
            "You may want to prepare for this...";

    public StoryScreen(Window window)
    {
        super(window);
        this.window = window;
        initButtons();
    }

    public StoryScreen(Window window, String text, ShopScreen shop)
    {
        super(window);
        this.window = window;
        float width = window.getWidth();
        float height = window.getHeight();
        setText("before_battle_one");
        addLoadUIScreenButton(width - 250, height - 200, 400, 125, Textures.CONTINUE, Textures.CONTINUE_HOVER, Textures.CONTINUE_CLICKED, shop);
    }


    public void update()
    {
        super.update();
    }

    public void setText(String section)
    {
        this.section = section;
        displayText();
    }

    private void displayText()
    {
        float screenWidth = window.getWidth();
        float screenHeight = window.getHeight();
        int ROW_LENGTH = 40;
        char[] text;
  //addText(0, 0, intro, 42, FontPath.PIXEL, Color.BLUE);
        if (section.equals("intro"))
        {
            text = intro.toCharArray();
        }
        else
        {
            text = beforeBattle1.toCharArray();
        }
        int count = 1;
        int pos = 0;
        int row = 0;
        int column;
        int posLastLine = 0;
        int i;
        for (i = 0; i < text.length; i++)
        {
            if (text[i] == ' ' && pos - posLastLine > ROW_LENGTH) {
                row = (count) * 50;
                char[] c = new char[pos - posLastLine];
                for (int j = pos - posLastLine; j > 0; j--)
                {
                    c[(pos - posLastLine) - j] = text[i - j];
                }
                addText(50, row, new String(c), 25, FontPath.PIXEL, Color.BLUE);
                count++;
                posLastLine = pos;
                pos++;
            }
            else
            {
                pos++;
            }
        }
        if (posLastLine < text.length - 1)
        {
            row = (count) * 50;
            char[] c = new char[text.length - posLastLine];
            for (int j = text.length - posLastLine; j > 0; j--) {
                c[(text.length - posLastLine) - j] = text[i - j];
            }
            addText(50, row, new String(c), 25, FontPath.PIXEL, Color.BLUE);
        }
        isSetup = false;
    }


    public void initButtons()
    {
        float screenWidth = window.getWidth();
        float screenHeight = window.getHeight();
        float continueButtonWidth = 400;
        float continueButtonHeight = 125;

        //continue button
        addLoadLevelButton(screenWidth - 250, screenHeight - 100, continueButtonWidth, continueButtonHeight, Textures.CONTINUE, Textures.CONTINUE_HOVER, Textures.CONTINUE_CLICKED);

    }
}
