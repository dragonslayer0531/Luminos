package tk.luminos.luminoscore.audio;

import org.lwjgl.openal.AL10;

/**
 * 
 * Audio source holder
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class AudioSource {

	private int sourceID;
	
	/**
	 * Constructor
	 * 
	 * @param rolloff				Roll off factor for audio source
	 * @param referenceDistance		Reference distance of audio source
	 * @param maxDistance			Maximum hearing range of audio source
	 */
	public AudioSource(float rolloff, float referenceDistance, float maxDistance) {
		sourceID = AL10.alGenSources();
		AL10.alSourcef(sourceID, AL10.AL_ROLLOFF_FACTOR, rolloff);
		AL10.alSourcef(sourceID, AL10.AL_REFERENCE_DISTANCE, referenceDistance);
		AL10.alSourcef(sourceID, AL10.AL_MAX_DISTANCE, maxDistance);
	}
	
	/**
	 * Deletes audio source
	 */
	public void dispose() {
		stop();
		AL10.alDeleteSources(sourceID);
	}
	
	/**
	 * Plays source
	 * 
	 * @param buffer	Buffer to play
	 */
	public void play(int buffer) {
		stop();
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		continuePlay();
	}
	
	/**
	 * Sets volume of source
	 * 
	 * @param gain	Volume
	 */
	public void setGain(float gain) {
		AL10.alSourcef(sourceID, AL10.AL_GAIN, gain);
	}
	
	/**
	 * Sets pitch of source
	 * 
	 * @param pitch		Pitch
	 */
	public void setPitch(float pitch) {
		AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
	}
	
	/**
	 * Sets source location
	 * 
	 * @param x		X location of source
	 * @param y		Y location of source
	 * @param z		Z location of source
	 */
	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
	}
	
	/**
	 * Sets source velocity
	 * 
	 * @param dx	X movement of source
	 * @param dy	Y movement of source
	 * @param dz	Z movement of source
	 */
	public void setVelocity(float dx, float dy, float dz) {
		AL10.alSource3f(sourceID, AL10.AL_VELOCITY, dx, dy, dz);
	}
	
	/**
	 * Sets if source loops
	 * 
	 * @param loop		Repeat
	 */
	public void setLooping(boolean loop) {
		AL10.alSourcei(sourceID, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	/**
	 * Gets if source is playing
	 * 
	 * @return is source playing
	 */
	public boolean isPlaying() {
		return AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING;
	}
	
	/**
	 * Pauses source
	 */
	public void pause() {
		AL10.alSourcePause(sourceID);
	}
	
	/**
	 * Plays source from pause point
	 */
	public void continuePlay() {
		AL10.alSourcePlay(sourceID);
	}
	
	/**
	 * Stops source
	 */
	public void stop() {
		AL10.alSourceStop(sourceID);
	}
	
}
