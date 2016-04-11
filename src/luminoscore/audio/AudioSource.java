package luminoscore.audio;

import org.lwjgl.openal.AL10;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Audio source holder
 *
 */

public class AudioSource {

	private int sourceID;
	
	/**
	 * Constructor
	 */
	public AudioSource() {
		sourceID = AL10.alGenSources();
	}
	
	/**
	 * Deletes audio source
	 */
	public void dispose() {
		stop();
		AL10.alDeleteSources(sourceID);
	}
	
	/**
	 * @param buffer	Buffer to play
	 * 
	 * Plays source
	 */
	public void play(int buffer) {
		stop();
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceID);
	}
	
	/**
	 * @param gain	Volume
	 * 
	 * Sets volume of source
	 */
	public void setGain(float gain) {
		AL10.alSourcef(sourceID, AL10.AL_GAIN, gain);
	}
	
	/**
	 * @param pitch		Pitch
	 * 
	 * Sets pitch of source
	 */
	public void setPitch(float pitch) {
		AL10.alSourcef(sourceID, AL10.AL_PITCH, pitch);
	}
	
	/**
	 * @param x		X location of source
	 * @param y		Y location of source
	 * @param z		Z location of source
	 * 
	 * Sets source location
	 */
	public void setPosition(float x, float y, float z) {
		AL10.alSource3f(sourceID, AL10.AL_POSITION, x, y, z);
	}
	
	/**
	 * @param dx	X movement of source
	 * @param dy	Y movement of source
	 * @param dz	Z movement of source
	 * 
	 * Sets source velocity
	 */
	public void setVelocity(float dx, float dy, float dz) {
		AL10.alSource3f(sourceID, AL10.AL_VELOCITY, dx, dy, dz);
	}
	
	/**
	 * @param loop		Repeat
	 * 
	 * Sets if source loops
	 */
	public void setLooping(boolean loop) {
		AL10.alSourcei(sourceID, AL10.AL_LOOPING, loop ? AL10.AL_TRUE : AL10.AL_FALSE);
	}
	
	/**
	 * @return boolean	Active source
	 * 
	 * Gets if source is playing
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
