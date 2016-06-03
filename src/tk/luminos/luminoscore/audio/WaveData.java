package tk.luminos.luminoscore.audio;

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

import tk.luminos.luminoscore.Debug;

public class WaveData {
	
	final int format, samplerate, totalBytes, bytesPerFrame;
	final ByteBuffer data;
	
	private final AudioInputStream audioStream;
	private final byte[] dataArray;
	
	private WaveData(AudioInputStream stream) {
		this.audioStream = stream;
		AudioFormat audioFormat = stream.getFormat();
		format = getOpenALFormat(audioFormat.getChannels(), audioFormat.getSampleSizeInBits());
		this.samplerate = (int) audioFormat.getSampleRate();
		this.bytesPerFrame = audioFormat.getFrameSize();
		this.totalBytes = (int) (stream.getFrameLength() * bytesPerFrame);
		this.data = BufferUtils.createByteBuffer(totalBytes);
		this.dataArray = new byte[totalBytes];
		loadData();
	}
	
	private ByteBuffer loadData() {
		try {
			int bytesRead = audioStream.read(dataArray, 0, totalBytes);
			data.clear();
			data.put(dataArray, 0, bytesRead);
			data.flip();
		} catch (IOException e) {
			Debug.addData(e.getMessage());
		}
		return data;
	}
	
	public static WaveData create(String file) {
		FileInputStream fis = null;
		InputStream bufferedInput = null;
		AudioInputStream ais = null;
		try {
			fis = new FileInputStream(file);
			bufferedInput = new BufferedInputStream(fis);
			ais = AudioSystem.getAudioInputStream(bufferedInput);
		} catch (FileNotFoundException e) {
			Debug.addData(e.getMessage());
		} catch (UnsupportedAudioFileException e) {
			Debug.addData(e.getMessage());
		} catch (IOException e) {
			Debug.addData(e.getMessage());
		}
		return new WaveData(ais);
	}
	
	private static int getOpenALFormat(int channels, int bitsPerSample) {
		if(channels == 1) {
			return bitsPerSample == 8 ? AL10.AL_FORMAT_MONO8 : AL10.AL_FORMAT_MONO16;
		} else {
			return bitsPerSample == 8 ? AL10.AL_FORMAT_STEREO8 : AL10.AL_FORMAT_STEREO16;
		}
	}

}
