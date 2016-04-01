package luminoscore.audio;

import org.lwjgl.openal.AL10;

public class AudioSource {

	private int sourceID;
	
	public AudioSource() {
		sourceID = AL10.alGenSources();
	}
	
	public void dispose() {
		stop();
		AL10.alDeleteSources(sourceID);
	}
	
	public void play(int buffer) {
		stop();
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceID);
	}
	
	public void setGain(float gain) {
		AL10.alSourcef(sourceID, AL10.AL_GAIN, gain);
	}
	
	public void setPitch(float pitch) {
		AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
	}
	
	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
	}
	
	public void setVelocity(float dx, float dy, float dz) {
		AL10.alSource3f(sourceID, AL10.AL_VELOCITY, dx, dy, dz);
	}
	
	public void setLooping(boolean loop) {
		AL10.alSourcei(sourceID, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	public void pause() {
		AL10.alSourcePause(sourceID);
	}
	
	public void continuePlay() {
		AL10.alSourcePlay(sourceID);
	}
	
	public void stop() {
		AL10.alSourceStop(sourceID);
	}
	
}
