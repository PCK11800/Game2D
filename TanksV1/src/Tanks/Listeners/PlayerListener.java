package Tanks.Listeners;

import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;

import Tanks.Objects.Tank;

public class PlayerListener {
	
	private Tank tank;
	
	public PlayerListener(Tank tank)
	{
		this.tank = tank;
	}
	
	public void handleInput()
	{
		handleKeyboard();
		handleMouse();
	}
	
	public void handleKeyboard()
	{
		if(Keyboard.isKeyPressed(Keyboard.Key.W) && Keyboard.isKeyPressed(Keyboard.Key.A))
		{
			tank.moveForward();
			tank.turnLeft();
		}
		else if(Keyboard.isKeyPressed(Keyboard.Key.W) && Keyboard.isKeyPressed(Keyboard.Key.D))
		{
			tank.moveForward();
			tank.turnRight();
		}
		else if(Keyboard.isKeyPressed(Keyboard.Key.S) && Keyboard.isKeyPressed(Keyboard.Key.A))
		{
			tank.moveBackward();
			tank.turnLeft();
		}
		else if(Keyboard.isKeyPressed(Keyboard.Key.S) && Keyboard.isKeyPressed(Keyboard.Key.D))
		{
			tank.moveBackward();
			tank.turnRight();
		}
		else if(Keyboard.isKeyPressed(Keyboard.Key.W))
		{
			tank.moveForward();
		}
		else if(Keyboard.isKeyPressed(Keyboard.Key.A))
		{
			tank.turnLeft();
		}
		else if(Keyboard.isKeyPressed(Keyboard.Key.S))
		{
			tank.moveBackward();
		}
		else if(Keyboard.isKeyPressed(Keyboard.Key.D))
		{
			tank.turnRight();
		}
	}
	
	public void handleMouse()
	{
		if(Mouse.isButtonPressed(Mouse.Button.LEFT)) {
			tank.shoot();
		}
	}
}
