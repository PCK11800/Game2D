package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.Tank;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;
import org.jsfml.graphics.Color;

public class ShopScreen extends UIScreen
{

    float screenCenterX, screenCenterY;

    public ShopScreen(Window window, Tank player)
    {
        super(window);
        screenCenterX = window.getWidth() / 2;
        screenCenterY = window.getHeight() / 2;
        initButtons(window, player);
    }

    private void initButtons(Window window, Tank player)
    {


        float screenWidth = window.getWidth();
        float screenHeight = window.getHeight();

        float upgradeButtonWidth = 300;
        float upgradeButtonHeight = 300;

        float continueButtonWidth = 400;
        float continueButtonHeight = 125;

        //Upgrade buttons - Top Row
        addUpgradeButton(upgradeButtonWidth, upgradeButtonHeight, upgradeButtonWidth, upgradeButtonHeight, Textures.MAXHEALTH, Textures.MAXHEALTH_HOVER, Textures.MAXHEALTH_CLICKED, player, "increase_maxhealth", 75);
        addUpgradeButton((screenCenterX - upgradeButtonWidth), upgradeButtonHeight, upgradeButtonWidth, upgradeButtonHeight, Textures.HALFHEALTH, Textures.HALFHEALTH_HOVER, Textures.HALFHEALTH_CLICKED, player, "halfheal", 50);
        addUpgradeButton((screenCenterX + upgradeButtonWidth), upgradeButtonHeight, upgradeButtonWidth, upgradeButtonHeight, Textures.HEALTH, Textures.HEALTH_HOVER, Textures.HEALTH_CLICKED, player, "fullheal", 90);
        addUpgradeButton((screenWidth - upgradeButtonWidth), upgradeButtonHeight, upgradeButtonWidth, upgradeButtonHeight, Textures.SPIKES, Textures.SPIKES_HOVER, Textures.SPIKES_CLICKED, player, "spikes_upgrade", 250);

        //Upgrade buttons - Second
        addUpgradeButton(upgradeButtonWidth, (upgradeButtonHeight * 2) + 100, upgradeButtonWidth, upgradeButtonHeight, Textures.MINIGUN, Textures.MINIGUN_HOVER, Textures.MINIGUN_CLICKED, player, "minigun_upgrade", 200);
        addUpgradeButton((screenCenterX - upgradeButtonWidth), (upgradeButtonHeight * 2) + 100, upgradeButtonWidth, upgradeButtonHeight, Textures.RAILGUN, Textures.RAILGUN_HOVER, Textures.RAILGUN_CLICKED, player, "railgun_upgrade", 300);
        addUpgradeButton((screenCenterX + upgradeButtonWidth), (upgradeButtonHeight * 2) + 100, upgradeButtonWidth, upgradeButtonHeight, Textures.SONIC, Textures.SONIC_HOVER, Textures.SONIC_CLICKED, player, "sonicmode_upgrade", 150);
        addUpgradeButton((screenWidth - upgradeButtonWidth), (upgradeButtonHeight * 2) + 100, upgradeButtonWidth, upgradeButtonHeight, Textures.ARMOUR, Textures.ARMOUR_HOVER, Textures.ARMOUR_CLICKED, player, "armour_upgrade", 200);

        //Continue Button
        addLoadLevelButton(screenWidth - 250, screenHeight - 100, continueButtonWidth, continueButtonHeight, Textures.CONTINUE, Textures.CONTINUE_HOVER, Textures.CONTINUE_CLICKED);

        //Back Button
        //addLoadLevelButton(250, screenHeight - 100, continueButtonWidth, continueButtonHeight, Textures.BACK, Textures.BACK_HOVER, Textures.BACK_CLICKED);

        addMoneyText(screenCenterX - 80, screenHeight - 100, player,30, FontPath.PIXEL, Color.GREEN);
        addHealthText(screenCenterX - 100, screenHeight - 140, player, 30, FontPath.PIXEL, Color.RED);
    }
}
