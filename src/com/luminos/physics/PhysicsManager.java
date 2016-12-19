package com.luminos.physics;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.luminos.Debug;
import com.luminos.physics.colliders.Collider;

public class PhysicsManager extends Thread {
	
	public static final int UPS_TARGET = 30;
	
	public static boolean forceClose = false;
	
	/*
	 * TODO:
	 * 		Update at fixed interval (i.e. not frame dependent
	 */
	
	private List<Collider> colliders = new ArrayList<Collider>();
	
	/**
	 * Causes thread to run
	 */
	public void run() {
		float upsTime = 1 / UPS_TARGET;
		long lastTime = System.currentTimeMillis();
		while (GLFW.glfwInit() && !forceClose) {
			
			long curTime = System.currentTimeMillis();
			while (curTime - lastTime < upsTime * 1000) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					 Debug.addData(e);
				}
				curTime = System.currentTimeMillis();
			}
			
			this.update();
			lastTime = curTime;
			
		}
	}
	
	/*
	 * Updates the physics of the engine
	 */
	public void update() {
		//TODO:  Update all collider based physics
		for (Collider collider : colliders) {
			collider.getCollisions(colliders);
		}
	}
	
}
