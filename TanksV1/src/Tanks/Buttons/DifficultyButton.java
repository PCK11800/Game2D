package Tanks.Buttons;

import Tanks.Objects.Button;
import Tanks.Window.Window;

public class DifficultyButton extends Button {

    private Window.Difficulty difficulty;
    private boolean selected = false;

    /**
     * Constructor. Creates instance of DifficultyButton.
     * @param window window to create button in.
     * @param x x position of button
     * @param y y position of button
     * @param width width of button (in pixels)
     * @param height height of button (in pixels)
     * @param activeTexture texture of button when not hovered over or clicked
     * @param difficulty difficulty which this instance sets the game window to
     */
    public DifficultyButton(Window window, float x, float y, float width, float height, String activeTexture, Window.Difficulty difficulty)
    {
        super(window, x, y, width, height, activeTexture);
        this.difficulty = difficulty;
    }

    /**
     * Method for changing difficulty and updating texture when button is pressed.
     */
    @Override
    public void setPressed()
    {
        super.setPressed();
        window.setDifficulty(difficulty);
        selected = true;
    }

    /**
     * Update method.
     */
    @Override
    public void update()
    {
        if(window.getDifficulty() == difficulty){
            super.setPressed();
            selected = true;
        }

        super.update();
        if (selected) setPressed();
    }

    /**
     * Accessor method for getting selected status of this DifficulyButton
     * @return true if selected, false if not
     */
    public boolean isSelected() { return selected; }

    /**
     * Mutator method to deselect a currently selected DifficultyButton (does nothing if button not already selected)
     */
    public void deSelect() {
        selected = false;
        setActive();
    }
}
