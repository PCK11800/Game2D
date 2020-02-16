package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;

public class ShopScreen extends UIScreen
{
    public ShopScreen(Window window)
    {
        super(window);

        initButtons(window);
    }

    private void initButtons(Window window)
    {
        float screenCenterX = window.getWidth() / 2;
        float screenCenterY = window.getHeight() / 2;

        float screenWidth = window.getWidth();
        float screenHeight = window.getHeight();

        float upgradeButtonWidth = 300;
        float upgradeButtonHeight = 300;

        float continueButtonWidth = 400;
        float continueButtonHeight = 125;

        //Upgrade buttons - Top Row
        addUpgradeButton(upgradeButtonWidth, upgradeButtonHeight, upgradeButtonWidth, upgradeButtonHeight, Textures.HEALTH, Textures.HEALTH_HOVER, Textures.HEALTH_CLICKED);
        addUpgradeButton((screenCenterX - upgradeButtonWidth), upgradeButtonHeight, upgradeButtonWidth, upgradeButtonHeight, Textures.MAXHEALTH, Textures.MAXHEALTH_HOVER, Textures.MAXHEALTH_CLICKED);
        addUpgradeButton((screenCenterX + upgradeButtonWidth), upgradeButtonHeight, upgradeButtonWidth, upgradeButtonHeight, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
        addUpgradeButton((screenWidth - upgradeButtonWidth), upgradeButtonHeight, upgradeButtonWidth, upgradeButtonHeight, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);

        //Upgrade buttons - Second
        addUpgradeButton(upgradeButtonWidth, (upgradeButtonHeight * 2) + 100, upgradeButtonWidth, upgradeButtonHeight, Textures.MINIGUN, Textures.MINIGUN_HOVER, Textures.MINIGUN_CLICKED);
        addUpgradeButton((screenCenterX - upgradeButtonWidth), (upgradeButtonHeight * 2) + 100, upgradeButtonWidth, upgradeButtonHeight, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
        addUpgradeButton((screenCenterX + upgradeButtonWidth), (upgradeButtonHeight * 2) + 100, upgradeButtonWidth, upgradeButtonHeight, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);
        addUpgradeButton((screenWidth - upgradeButtonWidth), (upgradeButtonHeight * 2) + 100, upgradeButtonWidth, upgradeButtonHeight, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED);


        //Continue Button
        addLoadLevelButton(screenWidth - 250, screenHeight - 100, continueButtonWidth, continueButtonHeight, Textures.CONTINUE, Textures.CONTINUE_HOVER, Textures.CONTINUE_CLICKED);
    }

}
