package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.Tank;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;

public class ShopScreen extends UIScreen
{
    public ShopScreen(Window window, Tank player)
    {
        super(window);

        initButtons(window, player);
    }

    private void initButtons(Window window, Tank player)
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
        addUpgradeButton(upgradeButtonWidth, upgradeButtonHeight, upgradeButtonWidth, upgradeButtonHeight, Textures.HEALTH, Textures.HEALTH_HOVER, Textures.HEALTH_CLICKED, player, "increase_health");
        addUpgradeButton((screenCenterX - upgradeButtonWidth), upgradeButtonHeight, upgradeButtonWidth, upgradeButtonHeight, Textures.MAXHEALTH, Textures.MAXHEALTH_HOVER, Textures.MAXHEALTH_CLICKED, player, "increase_maxhealth");
        addUpgradeButton((screenCenterX + upgradeButtonWidth), upgradeButtonHeight, upgradeButtonWidth, upgradeButtonHeight, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED, player, "increase_health");
        addUpgradeButton((screenWidth - upgradeButtonWidth), upgradeButtonHeight, upgradeButtonWidth, upgradeButtonHeight, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED, player, "increase_health");

        //Upgrade buttons - Second
        addUpgradeButton(upgradeButtonWidth, (upgradeButtonHeight * 2) + 100, upgradeButtonWidth, upgradeButtonHeight, Textures.MINIGUN, Textures.MINIGUN_HOVER, Textures.MINIGUN_CLICKED, player, "minigun_upgrade");
        addUpgradeButton((screenCenterX - upgradeButtonWidth), (upgradeButtonHeight * 2) + 100, upgradeButtonWidth, upgradeButtonHeight, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED, player, "railgun_upgrade");
        addUpgradeButton((screenCenterX + upgradeButtonWidth), (upgradeButtonHeight * 2) + 100, upgradeButtonWidth, upgradeButtonHeight, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED, player, "player_future");
        addUpgradeButton((screenWidth - upgradeButtonWidth), (upgradeButtonHeight * 2) + 100, upgradeButtonWidth, upgradeButtonHeight, Textures.BRICKBLOCK, Textures.EXIT_LOCKED, Textures.EXIT_UNLOCKED, player, "sonicmode_upgrade");


        //Continue Button
        addLoadLevelButton(screenWidth - 250, screenHeight - 100, continueButtonWidth, continueButtonHeight, Textures.CONTINUE, Textures.CONTINUE_HOVER, Textures.CONTINUE_CLICKED);
    }

}
