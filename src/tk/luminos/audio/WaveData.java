package tk.luminos.audio;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_MONO8;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO8;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.BufferUtils;

public class WaveData {
	
	protected final int format;
	protected final int sampleRate;
	protected final int totalBytes;
	protected final int bytesPerFrame;
	protected final ByteBuffer data;
	
	private final AudioInputStream ais;
	private final byte[] byteArray;
	
	private WaveData(AudioInputStream stream) throws IOException {
		this.ais = stream;
		AudioFormat af = stream.getFormat();
		format = getOpenALFormat(af.getChannels(), af.getSampleSizeInBits());
		this.sampleRate = (int) af.getSampleRate();
		this.bytesPerFrame = af.getFrameSize();
		this.totalBytes = (int) (stream.getFrameLength() * this.bytesPerFrame);
		this.data = BufferUtils.createByteBuffer(totalBytes);
		this.byteArray = new byte[totalBytes];
		
		load();
	}
	
	public static WaveData create(String file) throws IOException, FileNotFoundException, UnsupportedAudioFileException {
		InputStream stream = new FileInputStream(file);
		InputStream buffer = new BufferedInputStream(stream);
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(buffer);
		return new WaveData(audioStream);
	}
	
	public void dispose() throws IOException {
		ais.close();
		data.clear();
	}
	
	private static int getOpenALFormat(int channels, int bitsPerSample) {
		if (channels == 1) {
			return bitsPerSample == 8 ? AL_FORMAT_MONO8 : AL_FORMAT_MONO16;
		} else {
			return bitsPerSample == 8 ? AL_FORMAT_STEREO8 : AL_FORMAT_STEREO16;
		}
	}
	
	private ByteBuffer load() throws IOException {
		int bytesRead = ais.read(byteArray, 0, totalBytes);
		data.clear();
		data.put(byteArray, 0, bytesRead);
		data.flip();
		return data;
	}

}
