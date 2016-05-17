package luminoscore.audio;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.util.WaveData;

/**
 * 
 * Creates audio master
 * 
 * @author Nick Clark
 * @version 1.0
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
	 * Deletes audio buffers and closes OpenAL Context
	 */
	public void dispose() {
		for(Integer buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		ALC.destroy();
	}
	
	/**
	 * Loads source to sound card
	 * 
	 * @param source	String containing data of source
	 * @return Integer referencing index of Sound Card
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
	 * Sets listener position
	 * 
	 * @param x		X Location of listening device
	 * @param y		Y Location of listening device
	 * @param z		Z Location of listening device
	 */
	public void setListenerPosition(float x, float y, float z) {
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
	}
	
	/**
	 * Sets listener velocity
	 * 
	 * @param dx	X Movement of listening device
	 * @param dy	Y Movement of listening device
	 * @param dz	Z Movement of listening device
	 */
	public void setListenerVelocity(float dx, float dy, float dz) {
		AL10.alListener3f(AL10.AL_VELOCITY, dx, dy, dz);
	}

}
