package Tanks.Main;

import java.awt.Dimension;
import java.awt.Toolkit;


import Tanks.Objects.GameMode;
import org.jsfml.graphics.Color;

import Tanks.Objects.LevelContainer;
import Tanks.Window.Window;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;

public class Main
{
	
	private Window window;
	private GameMode gm;
	private Clock frameClock = new Clock();

	
	@SuppressWarnings("unused")
	private void createWindow(int x, int y, String name, int frameRate) 
	{
		window = new Window(x, y, name, frameRate);
		window.setBackgroundColor(Color.WHITE);
	}
	
	private void createFullScreenWindow(int frameRate)
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window = new Window((int)screenSize.getWidth(), (int)screenSize.getHeight(), "Tanks", frameRate);
		window.setBackgroundColor(Color.WHITE);
	}
	
	private void iniGame() 
	{
		//createWindow(1000, 1000, "Tanks", 60);
		createFullScreenWindow(60);

		//In future builds call the main menu here
		gm = new GameMode(this.window);
	}
	
	private void loop()
	{
		while(window.isOpen())
		{
			window.startOfFrame();
			gm.update();
			window.endOfFrame();
		}
	}

	
	public static void main(String[] args) 
	{
		Main main = new Main();
		main.iniGame();
		main.loop();
	}
}
