package com.luminos.graphics;

import org.joml.Vector3f;

public class Material {

    private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);

    private Vector3f color;
    
    private float reflectance;

    private Texture texture;
    
    public Material() {
        color = DEFAULT_COLOUR;
        reflectance = 0;
    }
    
    public Material(Vector3f color, float reflectance) {
        this();
        this.color = color;
        this.reflectance = reflectance;
    }

    public Material(Texture texture, float reflectance) {
        this();
        this.texture = texture;
        this.reflectance = reflectance;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public float getReflectance() {
        return reflectance;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public boolean isTextured() {
        return this.texture != null;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}