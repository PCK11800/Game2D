package Tanks.Main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.stream.Collectors;

import Tanks.Objects.Opponent;
import org.jsfml.graphics.Color;

import Tanks.ObjectComponents.Textures;
import Tanks.Objects.Map;
import Tanks.Objects.Tank;
import Tanks.Window.Window;

public class Main {
	
	Window window;
	
	Map testMap;
	
	//ArrayList holding game objects 
	ArrayList<Tank> tankList = new ArrayList<>();
	
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
		drawMapTest();
		drawTankTest();
		drawOpponentTest();
		//after all tanks for current map created
		for (Tank t: tankList)
		{
			testMap.addTank(t);
		}
	}
	
	private void loop()
	{
		while(window.isOpen()) {
			window.startOfFrame();

			updateMap();
			
			window.endOfFrame();
		}
	}
	

	
	private void drawTankTest()
	{
		Tank tank = new Tank();
		tank.setHullTexture(Textures.TANKHULL_GREEN);
		tank.setTurretTexture(Textures.TANKTURRET_GREEN);
		tank.setShellTexture(Textures.TANKSHELL_DEFAULT);
		tank.setMap(testMap);
		tank.setWindow(window);
		tank.setSize(100, 100);
		tank.setTankLocation(800, 500);
		tank.setHullTurningDistance(3);
		tank.setTurretTurningDistance(3);
		tank.setMovementSpeed(5);
		tank.setInitialDirection(180);
		tank.setShellSpeed(10);
		tank.setShellRicochetNumber(2);
		tank.enablePlayerControl();
		
		tankList.add(tank);
		testMap.addTank(tank);
	}

	private void drawOpponentTest()
	{
		Opponent tank = new Opponent();
		tank.setHullTexture(Textures.TANKHULL_GREEN);
		tank.setTurretTexture(Textures.TANKTURRET_GREEN);
		tank.setShellTexture(Textures.TANKSHELL_DEFAULT);
		tank.setMap(testMap);
		tank.setWindow(window);
		tank.setSize(100, 100);
		tank.setTankLocation(800, 500);
		tank.setHullTurningDistance(3);
		tank.setTurretTurningDistance(3);
		tank.setMovementSpeed(5);
		tank.setInitialDirection(180);
		tank.setShellSpeed(10);
		tank.setShellRicochetNumber(2);
		tankList.add(tank);
		testMap.addTank(tank);
	}
	
	private void drawMapTest() {
		testMap = new Map(window);
	}
	
	private void updateMap() {
		testMap.update();
	}
	
	public static void main(String[] args) 
	{
		Main main = new Main();
		main.iniGame();
		main.loop();
	}
}
