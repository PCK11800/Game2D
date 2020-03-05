package Tanks.Buttons;

import Tanks.Objects.Button;
import Tanks.Window.Window;

public class DifficultyButton extends Button {

    private int difficulty;
    private boolean selected = false;

    public DifficultyButton(Window window, float x, float y, float width, float height, String activeTexture, int difficulty)
    {
        super(window, x, y, width, height, activeTexture);
        this.difficulty = difficulty;
    }

    @Override
    public void setPressed()
    {
        super.setPressed();
        window.setDifficulty(difficulty);
        selected = true;
    }

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

    public boolean isSelected() { return selected; }

    public void deSelect() {
        selected = false;
        setActive();
    }
}
