package Tanks.ObjectComponents;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;

import Tanks.Window.Window;

public class RotatingObject extends Sprite {

	protected float xPos, yPos;
	protected float width, height;
	protected float xCenter, yCenter;
	protected float objectDirection;
	protected Texture objectTexture;
	
	public void setObjectTexture(String texturePath) 
	{
		Path imagePath = FileSystems.getDefault().getPath("..", texturePath);
		objectTexture = new Texture();
		try {
			objectTexture.loadFromFile(imagePath);
			objectTexture.setSmooth(true);
			setTexture(objectTexture);
			
			width = getTextureWidth();
			height = getTextureHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Moves the centroid to the center of the object.
	 */
	private void centerObject() 
	{
		this.xCenter = getTextureWidth() / 2;
		this.yCenter = getTextureHeight() / 2;
		setOrigin(xCenter, yCenter);
	}
	
	/**
	 * Rotates the object to a specific direction.
	 * Direction is from 0 to 360 starting north.
	 * @param objectDirection
	 */
	public void rotateObject(float objectDirection) 
	{
		centerObject();
		this.objectDirection = objectDirection;
		
		if(this.objectDirection >= 360) {
			this.objectDirection = this.objectDirection - 360;
		}
		
		else if(this.objectDirection <= 0) {
			this.objectDirection = this.objectDirection + 360;
		}
		setRotation(objectDirection);
	}
	
	public void setCenterLocation(float xPos, float yPos) 
	{
		this.xPos = xPos;
		this.yPos = yPos;
		centerObject();
		setPosition(xPos, yPos);
	}
	
	public void setLocation(float xPos, float yPos) 
	{
		this.xPos = xPos;
		this.yPos = yPos;
		setPosition(xPos, yPos);
	}
	
	public void setSize(float width, float height) 
	{
		float widthMultiplyer = width/this.width;
		float heightMultiplyer = height/this.height;
		
		this.width = widthMultiplyer * this.width;
		this.height = heightMultiplyer * this.height;
		
		setScale(widthMultiplyer, heightMultiplyer);
	}
	
	
	public void draw(Window w) 
	{
		w.draw(this);
	}
	
	public float getTextureWidth() 
	{
		return getTexture().getSize().x;
	}

	public float getTextureHeight() 
	{
		return getTexture().getSize().y;
	}
	
	public float getWidth() 
	{
		return width;
	}
	
	public float getHeight() 
	{
		return height;
	}
	
	public float getxPos() 
	{
		return xPos;
	}

	public float getyPos() 
	{
		return yPos;
	}
	
	public float getTopBounds() 
	{
		return getGlobalBounds().top;
	}
	
	public float getRightBounds() 
	{
		return getGlobalBounds().left + getWidth();
	}
	
	public float getLeftBounds() 
	{
		return getGlobalBounds().left;
	}
	
	public float getBottomBounds() 
	{
		return getGlobalBounds().top + getHeight();
	}
}
