package luminoscore.audio;

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
import org.lwjgl.openal.AL10;

public class WaveData {
	
	//Sound wave data
	private int format;
	private int samplerate;
	private int totalBytes;
	private int bytesPerFrame;
	private ByteBuffer data;
	private byte[] dataArray;
	
	//Constructor data
	private AudioInputStream as;
	
	/*
	 * @param as AudioInputStream defining the file
	 * 
	 * Constructor (private)
	 */
	private WaveData(AudioInputStream as) {
		this.as = as;
		AudioFormat audioFormat = as.getFormat();
		this.format = getOpenALFormat(audioFormat.getChannels(), audioFormat.getSampleSizeInBits());
		this.samplerate = (int) audioFormat.getSampleRate();
		this.bytesPerFrame = audioFormat.getFrameSize();
		this.totalBytes = (int) (as.getFrameLength() * bytesPerFrame);
		this.data = BufferUtils.createByteBuffer(totalBytes);
		this.dataArray = new byte[totalBytes];
		loadData();
	}
	
	 //Disposes of AudioInputStream
	public void dispose() {
		try {
			as.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		data.clear();
	}
	
	/*
	 * @param channels Defines the number of channels in the file
	 * @param bitsPerSample Defins the number of bits in a sample
	 * @return int
	 * 
	 * Returns the OpenAL format of the audio file
	 */
	private int getOpenALFormat(int channels, int bitsPerSample) {
		if(channels == 1) {
			return bitsPerSample == 8 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_MONO16;
		} else {
			return bitsPerSample == 8 ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_STEREO16;
		}
	}
	
	//Loads ddata from audio input stream to a bytebuffer
	private ByteBuffer loadData() {
		try {
			int bytesRead = as.read(dataArray, 0, totalBytes);
			data.clear();
			data.put(dataArray, 0, bytesRead);
			data.flip();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/*
	 * @param file Defines the file to be read
	 * @return WaveData
	 * 
	 * Creates WaveData from file
	 */
	public static WaveData create(String file) {
		try {
			InputStream stream = new FileInputStream(file);
			InputStream bufferedInput = new BufferedInputStream(stream);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedInput);
			WaveData wavStream = new WaveData(audioStream);
			return wavStream;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	//Getter Methods
	public int getFormat() {
		return format;
	}

	public int getSamplerate() {
		return samplerate;
	}

	public int getTotalBytes() {
		return totalBytes;
	}

	public int getBytesPerFrame() {
		return bytesPerFrame;
	}

	public ByteBuffer getData() {
		return data;
	}

	public AudioInputStream getAs() {
		return as;
	}

	public byte[] getDataArray() {
		return dataArray;
	}

}
