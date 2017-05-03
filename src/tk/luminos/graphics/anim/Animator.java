package tk.luminos.graphics.anim;

import java.util.HashMap;
import java.util.Map;

import tk.luminos.maths.Matrix4;

public class Animator {
	
	private final AnimatedModel model;
	private Animation currentAnimation;
	private float animationTime = 0;
	
	public Animator(AnimatedModel model) {
		this.model = model;
	}
	
	public void doAnimation(Animation animation) {
		this.animationTime = 0;
		this.currentAnimation = animation;
	}
	
	public void update(float frameTime) {
		if (currentAnimation == null)
			return;
		increaseAnimationTime(frameTime);
		Map<String, Matrix4> pose = calculateCurrentAnimationPose();
		applyPose(pose, model.getRoot(), new Matrix4());
	}
	
	private void increaseAnimationTime(float frameTime) {
		animationTime += frameTime;
		if (animationTime > currentAnimation.getLength())
			this.animationTime %= currentAnimation.getLength();
	}
	
	private KeyFrame[] getPreviousAndNextFrames() {
		KeyFrame[] allFrames = currentAnimation.getKeyFrames();
		KeyFrame previousFrame = allFrames[0];
		KeyFrame nextFrame = allFrames[0];
		for (int i = 1; i < allFrames.length; i++) {
			nextFrame = allFrames[i];
			if (nextFrame.getTimeStamp() > animationTime) {
				break;
			}
			previousFrame = allFrames[i];
		}
		return new KeyFrame[] { previousFrame, nextFrame };
	}
	
	private Map<String, Matrix4> calculateCurrentAnimationPose() {
		KeyFrame[] frames = getPreviousAndNextFrames();
		float progression = calculateProgression(frames[0], frames[1]);
		return interpolatePoses(frames[0], frames[1], progression);
	}
	
	private void applyPose(Map<String, Matrix4> currentPose, Joint joint, Matrix4 parentTransform) {
		Matrix4 currentLocalTransform = currentPose.get(joint.name);
		Matrix4 currentTransform = Matrix4.mul(parentTransform, currentLocalTransform, null);
		for (Joint childJoint : joint.children) {
			applyPose(currentPose, childJoint, currentTransform);
		}
		Matrix4.mul(currentTransform, joint.getInvertedBind(), currentTransform);
		joint.setAnimatedTransform(currentTransform);
	}
	
	private float calculateProgression(KeyFrame previousFrame, KeyFrame nextFrame) {
		float totalTime = nextFrame.getTimeStamp() - previousFrame.getTimeStamp();
		float currentTime = animationTime - previousFrame.getTimeStamp();
		return currentTime / totalTime;
	}
	
	private Map<String, Matrix4> interpolatePoses(KeyFrame previousFrame, KeyFrame nextFrame, float progression) {
		Map<String, Matrix4> currentPose = new HashMap<String, Matrix4>();
		for (String jointName : previousFrame.getJointKeyFrames().keySet()) {
			JointTransform previousTransform = previousFrame.getJointKeyFrames().get(jointName);
			JointTransform nextTransform = nextFrame.getJointKeyFrames().get(jointName);
			JointTransform currentTransform = JointTransform.interpolate(previousTransform, nextTransform, progression);
			currentPose.put(jointName, currentTransform.getLocalTransform());
		}
		return currentPose;
	}

}
