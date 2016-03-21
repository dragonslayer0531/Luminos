package luminoscore.audio;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALContext;

import luminoscore.util.math.vector.Vector3f;

public class AudioMaster {

	/*
	 * Author: Nick Clark
	 * Created On: 3/21/2016
	 */
	
	//Data fields
	private ALContext context;
	private List<Integer> buffers;
	
	/*
	 * Constructor
	 */
	public AudioMaster() {
		buffers = new ArrayList<Integer>();
	}
	
	//Initialized OpenAL context
	public void init() {
		context = ALContext.create();
		context.makeCurrent();
	}
	
	//Default set listener data
	public void setListenerData() {
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
	}
	
	/*
	 * @param position Defines listener position
	 * 
	 * Sets listener position
	 */
	public void setListenerData(Vector3f position) {
		AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, position.z);
	}
	
	/*
	 * @param position Defines listener position
	 * @param velocity Defines listener velocity
	 * 
	 * Sets listener position and velocity
	 */
	public void setListenerData(Vector3f position, Vector3f velocity) {
		AL10.alListener3f(AL10.AL_POSITION, position.x, position.y, position.z);
		AL10.alListener3f(AL10.AL_VELOCITY, velocity.x, velocity.y, velocity.z);
	}
	
	/*
	 * @param file Defines file to load
	 * 
	 * Loads a sound file to a AL10 Buffer
	 */
	public int loadSoundFile(String file) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		WaveData waveFile = WaveData.create(file);
		AL10.alBufferData(buffer, waveFile.getFormat(), waveFile.getData(), waveFile.getSamplerate());
		waveFile.dispose();
		return buffer;
	}

	 //Destroys AudioMaster context and buffers
	public void destroy() {
		for(Integer i : buffers) {
			AL10.alDeleteBuffers(i);
		}
		context.destroy();
	}

}
