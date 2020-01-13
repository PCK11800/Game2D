package Tanks.Main;

import java.awt.Dimension;
import java.awt.Toolkit;


import org.jsfml.graphics.Color;

import Tanks.Objects.LevelContainer;
import Tanks.Window.Window;

public class Main {
	
	Window window;
	LevelContainer level;

	
	@SuppressWarnings("unused")
	private void createWindow(int x, int y, String name, int frameRate) 
	{
		window = new Window(x, y, name, frameRate);
		window.setBackgroundColor(Color.WHITE);
	}
	
	private void createFullScreenWindow(int frameRate) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		window = new Window((int)screenSize.getWidth(), (int)screenSize.getHeight(), "Tanks", frameRate);
		window.setBackgroundColor(Color.WHITE);
	}
	
	private void iniGame() 
	{
		//createWindow(1000, 1000, "Tanks", 60);
		createFullScreenWindow(60);

		//In future builds call the main menu here
		level = new LevelContainer(window);
	}
	
	private void loop()
	{
		while(window.isOpen()) {
			window.startOfFrame();

			level.update();

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
