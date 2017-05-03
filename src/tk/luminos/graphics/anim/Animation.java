package tk.luminos.graphics.anim;

public class Animation {
	
	private final float length;
	private final KeyFrame[] keyFrames;
	
	public Animation(float lengthInSeconds, KeyFrame[] frames) {
		this.keyFrames = frames;
		this.length = lengthInSeconds;
	}

	public float getLength() {
		return length;
	}

	public KeyFrame[] getKeyFrames() {
		return keyFrames;
	}

}
