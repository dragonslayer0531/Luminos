package luminoscore.audio;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.util.WaveData;

public class AudioMaster {
	
	private List<Integer> buffers = new ArrayList<Integer>();
		
	public AudioMaster() {
		
	}
	
	public void init() {
		ALC.create();
	}
	
	public void dispose() {
		for(Integer buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		ALC.destroy();
	}
	
	public int loadSource(String source) {
		int buffer = AL10.alGenBuffers();
		buffers.add(buffer);
		
		WaveData data = WaveData.create(source);
		AL10.alBufferData(buffer, data.format, data.data, data.samplerate);
		data.dispose();
		
		return buffer;
	}
	
	public void setListenerPosition(float x, float y, float z) {
		AL10.alListener3f(AL10.AL_POSITION, x, y, z);
	}
	
	public void setListenerVelocity(float dx, float dy, float dz) {
		AL10.alListener3f(AL10.AL_VELOCITY, dx, dy, dz);
	}

}
