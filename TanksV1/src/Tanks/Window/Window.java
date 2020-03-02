package Tanks.Window;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.window.VideoMode;
import org.jsfml.window.WindowStyle;
import org.jsfml.window.event.Event;

public class Window extends RenderWindow{

	private float screenWidth, screenHeight;
	private String windowName;
	private int frameRate;
	private Color backgroundColor;

	public Window(int screenWidth, int screenHeight, String windowName, int frameRate)
	{
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.windowName = windowName;
		this.frameRate = frameRate;

		openWindow();
		setFrameRate(this.frameRate);
		//setMouseCursorVisible(false); //This allows for the use of a custom mouse cursor
	}

	public void openWindow()
	{
		create(new VideoMode((int) screenWidth, (int) screenHeight), windowName, WindowStyle.DEFAULT); //Can set it to .FULLSCREEN
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

	public float getWidth()
	{
		return screenWidth;
	}

	public float getHeight()
	{
		return screenHeight;
	}
}
