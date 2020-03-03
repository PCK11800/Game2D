package Tanks.Main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.GameMode;
import org.jsfml.graphics.Color;

import Tanks.Window.Window;
import org.jsfml.graphics.Image;
import org.jsfml.system.Clock;

public class Main
{
	
	private Window window;
	private GameMode gameMode;

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
		window.setBackgroundColor(Color.BLACK);
	}

	private void iniWindowIcon()
	{
		Path imagePath = FileSystems.getDefault().getPath(Textures.GAMEICON);
		Image image = new Image();
		try{
			image.loadFromFile(imagePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		window.setIcon(image);
	}
	
	private void iniGame() 
	{
		//createWindow(1000, 1000, "Tanks", 60);
		createFullScreenWindow(60);
		iniWindowIcon();

		this.gameMode = new GameMode(this.window, System.nanoTime());
	}
	
	private void loop()
	{
		while(window.isOpen())
		{
			window.startOfFrame();
			gameMode.update();
			window.endOfFrame();
		}
	}

	
	public static void main(String[] args) 
	{
		File lib = new File("TanksV1/src/Tanks/Main/" + System.mapLibraryName("fixXInitThreads"));
		System.load(lib.getAbsolutePath());
		Main main = new Main();
		main.iniGame();
		main.loop();
	}
}
