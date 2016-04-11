package luminoscore.audio;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.util.WaveData;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Creates audio master
 *
 */

public class AudioMaster {
	
	private List<Integer> buffers = new ArrayList<Integer>();
	
	/**
	 * Constructor
	 */
	public AudioMaster() {
		
	}
	
	/**
	 * Initializes OpenAL Context
	 */
	public void init() {
		ALC.create();
	}
	
	/**
	 * Deletes audio buffers and closes OpenAL Context
	 */
	public void dispose() {
		for(Integer buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		ALC.destroy();
	}
	
	/**
	 * @param source	String containing data of source
	 * @return int		Integer referencing index of Sound Card
	 * 
	 * Loads source to sound card
	 */
	public int loadSource(String source) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		
		WaveData data = WaveData.create(source);
		AL10.alBufferData(buffer, data.format, data.data, data.samplerate);
		data.dispose();
		
		return buffer;
	}
	
	/**
	 * @param x		X Location of listening device
	 * @param y		Y Location of listening device
	 * @param z		Z Location of listening device
	 * 
	 * Sets listener position
	 */
	public void setListenerPosition(float x, float y, float z) {
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
	}
	
	/**
	 * @param dx	X Movement of listening device
	 * @param dy	Y Movement of listening device
	 * @param dz	Z Movement of listening device
	 * 
	 * Sets listener velocity
	 */
	public void setListenerVelocity(float dx, float dy, float dz) {
		AL10.alListener3f(AL10.AL_VELOCITY, dx, dy, dz);
	}

}
