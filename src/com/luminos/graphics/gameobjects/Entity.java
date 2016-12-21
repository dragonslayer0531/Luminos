package com.luminos.graphics.gameobjects;

import java.util.List;

import com.luminos.graphics.models.TexturedModel;
import com.luminos.graphics.render.MasterRenderer;
import com.luminos.graphics.terrains.Terrain;
import com.luminos.maths.vector.Vector3f;

public class Entity implements GameObject {
	
	public static int GRAVITY = -50;
	
	private boolean renderable = true;
	private float scale = 1;
	private float renderDistance = MasterRenderer.FAR_PLANE;
	private Vector3f position = new Vector3f(0, 0, 0);
	private Vector3f rotation = new Vector3f(0, 0, 0);
	private TexturedModel model = null;
	
	public Entity() {}
	
	public Entity(Vector3f position, Vector3f rotation, float scale, TexturedModel model) {
		this.scale = scale;
		this.position = position;
		this.rotation = rotation;
		this.model = model;
	}

	@Override
	public Vector3f getPosition() {
		return position;
	}

	@Override
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	@Override
	public Vector3f getRotation() {
		return rotation;
	}
	
	@Override
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	@Override
	public float getScale() {
		return scale;
	}
	
	@Override
	public void setScale(float scale) {
		this.scale = scale;
	}

	@Override
	public boolean isRenderable() {
		return renderable;
	}

	@Override
	public void setRenderable(boolean renderable) {
		this.renderable = renderable;
	}
	
	@Override
	public TexturedModel getModel() {
		return model;
	}
	
	@Override
	public void setModel(TexturedModel model) {
		this.model = model;
	}
	
	@Override
	public float getRenderDistance() {
		return renderDistance;
	}

	@Override
	public void setRenderDistance(float renderDistance) {
		this.renderDistance = renderDistance;
	}

	@Override
	public void move(List<Terrain> terrains, float factor) {
		//Do Nothing
	}
	
}