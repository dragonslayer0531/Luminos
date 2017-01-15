package tk.luminos.loaders;

import static org.lwjgl.openal.AL10.AL_FORMAT_MONO16;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.stb.STBVorbisInfo;

public class AudioLoader {

	protected int loadSoundBuffer(String file) throws Exception {
		int id = alGenBuffers();
		Loader.sounds.add(id);
		
		STBVorbisInfo info = STBVorbisInfo.malloc();
		ShortBuffer pcm = readVorbis(file, 32 * 1024, info);
		alBufferData(id, info.channels() == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm, info.sample_rate());
		
		return id;
	}

	private ShortBuffer readVorbis(String resource, int bufferSize, STBVorbisInfo info) throws Exception {
		ByteBuffer vorbis;
		vorbis = sourceToByteBuffer(resource, bufferSize);
		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = STBVorbis.stb_vorbis_open_memory(vorbis, error, null);
		if (decoder == NULL)
			throw new Exception("Audio source could not be loaded: Error Code" + error.get(0));
		
		STBVorbis.stb_vorbis_get_info(decoder, info);
		int channels = info.channels();
		int lengthSamples = STBVorbis.stb_vorbis_stream_length_in_samples(decoder);
		ShortBuffer pcm = BufferUtils.createShortBuffer(lengthSamples);
		pcm.limit(STBVorbis.stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
		
		return pcm;			
	}

	private static ByteBuffer sourceToByteBuffer(String resource, int bufferSize) throws IOException {
		ByteBuffer buffer;
		Path path = Paths.get(resource);
		if (Files.isReadable(path)) {
			SeekableByteChannel fc = Files.newByteChannel(path);
			buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
			while (fc.read(buffer) != -1) ;
		} else {
			InputStream source = new FileInputStream(resource);
			ReadableByteChannel rbc = Channels.newChannel(source);
			buffer = BufferUtils.createByteBuffer(bufferSize);
			while (true) {
				int bytes = rbc.read(buffer);
				if (bytes == -1) {
					break;
				}
				if (buffer.remaining() == 0) {
					buffer = resizeBuffer(buffer, buffer.capacity() * 2);
				}
			}
		}
		buffer.flip();
		return buffer;
	}

	private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
		ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
		buffer.flip();
		newBuffer.put(buffer);
		return newBuffer;

	}

}
