package Tanks.Objects;

public class Opponent extends Tank {

    int movementCount = 0;
    double direction = 0;
    public Opponent()
    {
        super();
    }

    public void update()
    {
        super.update();
        if (direction == 0 || movementCount > 10)
        {
            direction = (Math.random() * ((8 - 1) + 1)) + 1;
            movementCount = 0;
        }
        switch ((int) direction)
        {
            case 1:
                moveForward();
                turnLeft();
                break;
            case 2:
                moveForward();
                turnRight();
                break;
            case 3:
                moveBackward();
                turnLeft();
                break;
            case 4:
                moveBackward();
                turnRight();
                break;
            case 5:
                moveForward();
                break;
            case 6:
                turnLeft();
                break;
            case 7:
                moveBackward();
                break;
            case 8:
                turnRight();
                break;
        }
        movementCount++;
    }
}
