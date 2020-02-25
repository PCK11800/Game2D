package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;
import org.jsfml.graphics.Color;

public class StoryScreen extends UIScreen
{
    public StoryScreen (Window window, String text, ShopScreen shop)
    {
        super(window);

        float width = window.getWidth();
        float height = window.getHeight();

        addLoadUIScreenButton(width - 250, height - 200, 400, 125, Textures.CONTINUE, Textures.CONTINUE_HOVER, Textures.CONTINUE_CLICKED, shop);

        addText(50, 50, text, 30, FontPath.PIXEL, Color.WHITE);
    }



}
