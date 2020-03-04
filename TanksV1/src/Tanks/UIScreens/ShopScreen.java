package Tanks.UIScreens;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.Tank;
import Tanks.Objects.UIScreen;
import Tanks.Window.Window;
import org.jsfml.graphics.Color;

public class ShopScreen extends UIScreen
{

    private float screenCenterX, screenCenterY;
    private float screenWidth;
    private float screenHeight;

    private float upgradeButtonWidth = 300;
    private float upgradeButtonHeight = 300;

    private float continueButtonWidth = 400;
    private float continueButtonHeight = 125;

    public ShopScreen(Window window, Tank player)
    {
        super(window);
        screenCenterX = window.getSize().x / 2;
        screenCenterY = window.getSize().y / 2;

        screenWidth = window.getSize().x;
        screenHeight = window.getSize().y;

        initButtons(window, player);
    }

    private void initButtons(Window window, Tank player)
    {

        //Upgrade buttons - Top Row
        addUpgradeButton(getXPos(0), upgradeButtonHeight / 2, upgradeButtonWidth, upgradeButtonHeight, Textures.MAXHEALTH, Textures.MAXHEALTH_HOVER, Textures.MAXHEALTH_CLICKED, player, "increase_maxhealth", 75);
        addUpgradeButton(getXPos(1), upgradeButtonHeight / 2, upgradeButtonWidth, upgradeButtonHeight, Textures.HALFHEALTH, Textures.HALFHEALTH_HOVER, Textures.HALFHEALTH_CLICKED, player, "halfheal", 50);
        addUpgradeButton(getXPos(2), upgradeButtonHeight / 2, upgradeButtonWidth, upgradeButtonHeight, Textures.HEALTH, Textures.HEALTH_HOVER, Textures.HEALTH_CLICKED, player, "fullheal", 90);
        addUpgradeButton(getXPos(3), upgradeButtonHeight / 2, upgradeButtonWidth, upgradeButtonHeight, Textures.SPIKES, Textures.SPIKES_HOVER, Textures.SPIKES_CLICKED, player, "spikes_upgrade", 250);

        //Upgrade buttons - Second
        addUpgradeButton(getXPos(0), (upgradeButtonHeight * 2), upgradeButtonWidth, upgradeButtonHeight, Textures.MINIGUN, Textures.MINIGUN_HOVER, Textures.MINIGUN_CLICKED, player, "minigun_upgrade", 200);
        addUpgradeButton(getXPos(1), (upgradeButtonHeight * 2), upgradeButtonWidth, upgradeButtonHeight, Textures.RAILGUN, Textures.RAILGUN_HOVER, Textures.RAILGUN_CLICKED, player, "railgun_upgrade", 300);
        addUpgradeButton(getXPos(2), (upgradeButtonHeight * 2), upgradeButtonWidth, upgradeButtonHeight, Textures.SONIC, Textures.SONIC_HOVER, Textures.SONIC_CLICKED, player, "sonicmode_upgrade", 150);
        addUpgradeButton(getXPos(3), (upgradeButtonHeight * 2), upgradeButtonWidth, upgradeButtonHeight, Textures.ARMOUR, Textures.ARMOUR_HOVER, Textures.ARMOUR_CLICKED, player, "armour_upgrade", 200);

        //Continue Button
        addLoadLevelButton(screenWidth - 250, screenHeight - 100, continueButtonWidth, continueButtonHeight, Textures.CONTINUE, Textures.CONTINUE_HOVER, Textures.CONTINUE_CLICKED);


        addMoneyText(screenCenterX - 80, screenHeight - 100, player,30, FontPath.PIXEL, Color.GREEN);
        addHealthText(screenCenterX - 100, screenHeight - 140, player, 30, FontPath.PIXEL, Color.RED);
    }


    private float getXPos(int i)
    {
        float xScale = getWindow().getSize().x / getWindow().getWidth();
        float xOffset = 50;
        float buttonPadding = 100;

        switch (i)
        {
            case 0:
                return xScale * (xOffset + (upgradeButtonWidth * i) + (upgradeButtonWidth / 2));

            case 1:
                return xScale * (xOffset + buttonPadding + (upgradeButtonWidth * i) + (upgradeButtonWidth / 2));

            case 2:
                return  xScale * (this.getWindow().getSize().x - (xOffset + buttonPadding + upgradeButtonWidth + (upgradeButtonWidth / 2)));

            case 3:
                return xScale * (this.getWindow().getSize().x - (xOffset + (upgradeButtonWidth / 2)));

            default:
                return xOffset + upgradeButtonWidth;
        }
    }
}
