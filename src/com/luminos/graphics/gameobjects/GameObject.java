package com.luminos.graphics.gameobjects;

import java.util.List;

import com.luminos.graphics.models.TexturedModel;
import com.luminos.graphics.terrains.Terrain;
import com.luminos.tools.maths.vector.Vector3f;

/**
 * 
 * Interface for game objects
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public interface GameObject extends SceneObject {
	
	public boolean isRenderable();
	public float getRenderDistance(); // TODO FADE OBJECTS BASED ON RENDER DISTANCE
	public float getScale();
	public TexturedModel getModel();
	public Vector3f getPosition();
	public Vector3f getRotation();

	public void setRenderable(boolean renderable);
	public void setRenderDistance(float renderDistance);
	public void setScale(float scale);
	public void setModel(TexturedModel model);
	public void setPosition(Vector3f position);
	public void setRotation(Vector3f rotation);
	
	public void move(List<Terrain> terrains, float factor);
}
