package tk.luminos.graphics.gameobjects;

import java.util.List;
import java.util.Map;

import tk.luminos.graphics.gameobjects.components.Component;
import tk.luminos.graphics.models.TexturedModel;
import tk.luminos.graphics.terrains.Terrain;
import tk.luminos.maths.vector.Vector3f;

/**
 * 
 * Interface for game objects
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public interface GameObject extends SceneObject {
	
	/**
	 * Gets if the GameObject is renderable
	 * 
	 * @return		Is game object renderable
	 */
	public boolean isRenderable();
	
	/**
	 * Gets the render distance of the GameObject
	 * 
	 * @return		Distance of max rendering
	 */
	public float getRenderDistance(); // TODO FADE OBJECTS BASED ON RENDER DISTANCE
	
	/**
	 * Gets the scale of the GameObject
	 * 
	 * @return		Scale
	 */
	public float getScale();
	
	/**
	 * Gets the model of the GameObject
	 * 
	 * @return		Model of object
	 */
	public TexturedModel getModel();
	
	/**
	 * Gets the position of the GameObject
	 * 
	 * @return		Position of object
	 */
	public Vector3f getPosition();
	
	/**
	 * Gets the rotation of the GameObject
	 * 
	 * @return		Rotation of object
	 */
	public Vector3f getRotation();

	/**
	 * Sets if the GameObject is renderable
	 * 
	 * @param renderable		Should object be renderable
	 */
	public void setRenderable(boolean renderable);
	
	/**
	 * Sets the maximum rendering distance
	 * 
	 * @param renderDistance		Maximum rendering distance
	 */
	public void setRenderDistance(float renderDistance);
	
	/**
	 * Sets the scale of the GameObject
	 * 
	 * @param scale		Scale of game object
	 */
	public void setScale(float scale);
	
	/**
	 * Sets the model of the entity
	 * 
	 * @param model		Model of game object
	 */
	public void setModel(TexturedModel model);
	
	/**
	 * Set position of GameObject
	 * 
	 * @param position	Position of game object
	 */
	public void setPosition(Vector3f position);
	
	/**
	 * Set rotation of GameObject
	 * 
	 * @param rotation	Rotation of game object
	 */
	public void setRotation(Vector3f rotation);
	
	/**
	 * Moves entity across terrains
	 * 
	 * @param terrains		Terrain list of world
	 * @param factor		Factor of movement
	 */
	public void move(List<Terrain> terrains, float factor);
	
	/**
	 * Gets the map of components associated with the GameObject
	 * 
	 * @return		Map of components attached to entity
	 */
	public Map<String, Component> getComponents();
}
