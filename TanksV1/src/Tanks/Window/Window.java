package Tanks.Window;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;
import org.jsfml.window.event.Event;

public class Window extends RenderWindow{

	private int screenWidth, screenHeight;
	private String windowName;
	private int frameRate;
	private Color backgroundColor;
	private int difficulty = 1; //1 = easy, 2 = normal, 3 = hard

	public Window(int screenWidth, int screenHeight, String windowName, int frameRate)
	{
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.windowName = windowName;
		this.frameRate = frameRate;

		openWindow();
		setFrameRate(this.frameRate);
		setMouseCursorVisible(false); //This allows for the use of a custom mouse cursor
	}

	public void openWindow()
	{
		create(new VideoMode(screenWidth, screenHeight), windowName, WindowStyle.FULLSCREEN); //Can set it to .FULLSCREEN
	}

	public String getWindowName()
	{
		return windowName;
	}

	public void setWindowName(String windowName)
	{
		this.windowName = windowName;
	}

	public Color getBackgroundColor()
	{
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}

	public void setFrameRate(int frameRate)
	{
		setFramerateLimit(this.frameRate);
	}

	public void startOfFrame()
	{
		clear(backgroundColor);
	}

	public void endOfFrame()
	{
		display();
		for (Event event : pollEvents( ))
		{
			switch(event.type)
			{
				case CLOSED:
					close();
					break;
			}
		}
	}

	public void setDifficulty(int difficulty) { this.difficulty = difficulty; }

	public int getWidth()
	{
		return screenWidth;
	}

	public int getHeight()
	{
		return screenHeight;
	}

	public int getDifficulty() { return difficulty; }
}
