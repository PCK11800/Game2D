package Tanks.UIScreens;

import Tanks.ObjectComponents.RotatingObject;
import Tanks.ObjectComponents.Textures;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;

public class TutorialScreen extends UIScreen
{
    private Window window;
    private RotatingObject page = new RotatingObject();
    private TutorialScreen next;

    public TutorialScreen(Window window, String textPath)
    {
        super(window);
        this.window = window;
        page.setObjectTexture(textPath);
    }

    public TutorialScreen(Window window, String textPath, TutorialScreen next)
    {
        super(window);
        this.window = window;
        this.next = next;
        page.setObjectTexture(textPath);
    }
    /**
     * This function must be called after the main menu has been created
     */
    public void initBackButton(MainMenu menu)
    {
        float width = window.getWidth();
        float height = window.getHeight();
        page.setLocation(0, 0);
        page.setSize(width > page.getWidth() ? page.getWidth() : width - 100, height > page.getHeight() + 250 ? page.getHeight() : height - 200);
        //Back button - returns to the main menu

        if (next != null)
        {
            addLoadUIScreenButton(width - 250, height - 100, 400, 125, Textures.CONTINUE, Textures.CONTINUE_HOVER, Textures.CONTINUE_CLICKED, next);
        }
        else
        {
            addLoadUIScreenButton(250, height - 100, 400, 125, Textures.BACK, Textures.BACK_HOVER, Textures.BACK_CLICKED, menu);
        }
    }

    @Override
    public void update()
    {
        super.update();
        window.draw(page);
    }
}
