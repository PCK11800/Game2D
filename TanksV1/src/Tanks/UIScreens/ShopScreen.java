package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;

public class ShopScreen extends UIScreen
{
    public ShopScreen(Window window)
    {
        super(window);

        float centerX = window.getWidth() / 2;
        float centerY = window.getHeight() / 2;

        //Upgrade buttons
        addUpgradeButton((centerX - 200), (centerY - 200), 300, 300, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
        addUpgradeButton((centerX), (centerY - 200), 300, 300, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
        addUpgradeButton((centerX + 200), (centerY - 200), 300, 300, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
    }
}
