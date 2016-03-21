package luminoscore.audio;

import org.lwjgl.openal.AL10;

import luminoscore.util.math.vector.Vector3f;

public class AudioSource {
	
	private int sourceID;

	public AudioSource() {
		sourceID = AL10.alGenSources();
	}
	
	public void setVolume(float volume) {
		AL10.alSourcef(sourceID, AL10.AL_GAIN, volume);
	}
	
	public void setPitch(float pitch) {
		AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
	}
	
	public void setPosition(Vector3f position) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, position.x, position.y, position.z);
	}
	
	public void setVelocity(Vector3f velocity) {
		AL10.alSource3f(sourceID, AL10.AL_PITCH, velocity.x, velocity.y, velocity.z);
	}
	
	public void setLoop(boolean loop) {
		AL10.alSourcei(sourceID, AL10.AL_LOOPING, loop? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	public void play(int buffer) {
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceID);
	}
	
	public void pause() {
		AL10.alSourcePause(sourceID);
	}
	
	public void continuePlaying() {
		AL10.alSourcePlay(sourceID);
	}
	
	public void stop() {
		AL10.alSourceStop(sourceID);
	}
	
	public void delete() {
		AL10.alSourceStop(sourceID);
		AL10.alDeleteSources(sourceID);
	}
	
	public int getSourceID() {
		return sourceID;
	}

}
