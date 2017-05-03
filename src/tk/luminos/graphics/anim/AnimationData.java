package tk.luminos.graphics.anim;

public class AnimationData {
	
	public final float length; // seconds
	public final KeyFrameData[] keyFrames;
	
	public AnimationData(float length, KeyFrameData[] keyFrames) {
		this.length = length;
		this.keyFrames = keyFrames;
	}

}
