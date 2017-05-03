package tk.luminos.graphics.anim;

import java.util.ArrayList;
import java.util.List;

import tk.luminos.maths.Matrix4;

public class Joint {
	
	public final int index; // Joint ID
	public final String name;
	public final List<Joint> children = new ArrayList<Joint>();
	private Matrix4 animatedTransform = new Matrix4();
	private final Matrix4 bindLocalTransform;
	private Matrix4 invertedBindTransform = new Matrix4();
	
	public Joint(int index, String name, Matrix4 bindLocalTransform) {
		this.index = index;
		this.name = name;
		this.bindLocalTransform = bindLocalTransform;
	}
	
	public void addChild(Joint child) {
		this.children.add(child);
	}
	
	public Matrix4 getAnimatedTransform() {
		return animatedTransform;
	}
	
	public Matrix4 getInvertedBind() {
		return invertedBindTransform;
	}
	
	public void calculateInverseBindTransform(Matrix4 parentBindTransform) {
		Matrix4 bindTrans = Matrix4.mul(parentBindTransform, bindLocalTransform, null);
		Matrix4.invert(bindTrans, invertedBindTransform);
		for (Joint child : children)
			child.calculateInverseBindTransform(bindTrans);
	}

	public void setAnimatedTransform(Matrix4 currentTransform) {
		this.animatedTransform = currentTransform;
	}

}
