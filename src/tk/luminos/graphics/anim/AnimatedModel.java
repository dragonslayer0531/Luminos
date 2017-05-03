package tk.luminos.graphics.anim;

import tk.luminos.graphics.Material;
import tk.luminos.graphics.VertexArray;
import tk.luminos.maths.Matrix4;

public class AnimatedModel {
	
	private final VertexArray model;
	private final Material texture;
	
	private final Joint root;
	private final int jointCount;
	
	private Animator animator;
	
	public AnimatedModel(VertexArray model, Material texture, Joint root, int count) {
		this.model = model;
		this.texture = texture;
		this.root = root;
		this.jointCount = count;
		this.animator = new Animator(this);
		root.calculateInverseBindTransform(new Matrix4());
	}
	
	public VertexArray getModel() {
		return model;
	}
	
	public Material getTexture() {
		return texture;
	}
	
	public Joint getRoot() {
		return root;
	}
	
	public void delete() {
		model.delete();
	}
	
	public void doAnimation(Animation anim) {
		animator.doAnimation(anim);
	}
	
	public void update(float frameTime) {
		animator.update(frameTime);
	}
	
	public Matrix4[] getJointTransforms() {
		Matrix4[] jointMatrices = new Matrix4[jointCount];
		addToArray(root, jointMatrices);
		return jointMatrices;
	}
	
	private void addToArray(Joint head, Matrix4[] joints) {
		joints[head.index] = head.getAnimatedTransform();
		for (Joint child : head.children)
			addToArray(child, joints);
	}

}
