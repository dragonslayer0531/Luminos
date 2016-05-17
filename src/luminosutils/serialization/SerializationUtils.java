package luminosutils.serialization;

import java.nio.ByteBuffer;

/**
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * Holds utility data for the serialization of primitives
 *
 */

public class SerializationUtils {

	/**
	 * Writes bytes from a source array to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param src		Array to read from
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, byte[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			dest[pointer++] = src[i];
		return pointer;
	}
	
	/**
	 * Writes characters from a source array to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param src		Array to read from
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, char[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	
	/**
	 * Writes shorts from a source array to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param src		Array to read from
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, short[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	
	/**
	 * Writes integers from a source array to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param src		Array to read from
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, int[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	
	/**
	 * Writes longs from a source array to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param src		Array to read from
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, long[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	
	/**
	 * Writes floats from a source array to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param src		Array to read from
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, float[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	
	/**
	 * Writes doubles from a source array to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param src		Array to read from
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, double[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	
	/**
	 * Writes booleans from a source array to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param src		Array to read from
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, boolean[] src) {
		assert(dest.length > pointer + src.length);
		for (int i = 0; i < src.length; i++)
			pointer = writeBytes(dest, pointer, src[i]);
		return pointer;
	}
	
	/**
	 * Writes a source byte to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param value		Byte to write to destination array
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, byte value) {
		assert(dest.length > pointer + Type.getSize(Type.BYTE));
		dest[pointer++] = value;
		return pointer;
	}
	
	/**
	 * Writes a source short to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param value		Short to write to destination array
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, short value) {
		assert(dest.length > pointer + Type.getSize(Type.SHORT));
		dest[pointer++] = (byte)((value >> 8) & 0xff);
		dest[pointer++] = (byte)((value >> 0) & 0xff);
		return pointer;
	}
	
	/**
	 * Writes a source character to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param value		Character to write to destination array
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, char value) {
		assert(dest.length > pointer + Type.getSize(Type.CHAR));
		dest[pointer++] = (byte)((value >> 8) & 0xff);
		dest[pointer++] = (byte)((value >> 0) & 0xff);
		return pointer;
	}
	
	/**
	 * Writes a source integer to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param value		Integer to write to destination array
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, int value) {
		assert(dest.length > pointer + Type.getSize(Type.INTEGER));
		dest[pointer++] = (byte)((value >> 24) & 0xff);
		dest[pointer++] = (byte)((value >> 16) & 0xff);
		dest[pointer++] = (byte)((value >> 8) & 0xff);
		dest[pointer++] = (byte)((value >> 0) & 0xff);
		return pointer;
	}
	
	/**
	 * Writes a source long to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param value		Long to write to destination array
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, long value) {
		assert(dest.length > pointer + Type.getSize(Type.LONG));
		dest[pointer++] = (byte)((value >> 56) & 0xff);
		dest[pointer++] = (byte)((value >> 48) & 0xff);
		dest[pointer++] = (byte)((value >> 40) & 0xff);
		dest[pointer++] = (byte)((value >> 32) & 0xff);
		dest[pointer++] = (byte)((value >> 24) & 0xff);
		dest[pointer++] = (byte)((value >> 16) & 0xff);
		dest[pointer++] = (byte)((value >> 8) & 0xff);
		dest[pointer++] = (byte)((value >> 0) & 0xff);
		return pointer;
	}
	
	/**
	 * Writes a source float to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param value		Float to write to destination array
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, float value) {
		assert(dest.length > pointer + Type.getSize(Type.FLOAT));
		int data = Float.floatToIntBits(value);
		return writeBytes(dest, pointer, data);
	}
	
	/**
	 * Writes a source double to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param value		Byte to write to destination array
	 * @return			Double pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, double value) {
		assert(dest.length > pointer + Type.getSize(Type.DOUBLE));
		long data = Double.doubleToLongBits(value);
		return writeBytes(dest, pointer, data);
	}
	
	/**
	 * Writes a source boolean to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param value		Boolean to write to destination array
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, boolean value) {
		assert(dest.length > pointer + Type.getSize(Type.BOOLEAN));
		dest[pointer++] = (byte)(value ? 1 : 0);
		return pointer;
	}
	
	/**
	 * Writes a source string to a destination byte array
	 * 
	 * @param dest		Array to write to
	 * @param pointer	Integer describing where to start writing
	 * @param value		String to write to destination array
	 * @return			Final pointer position
	 */
	public static int writeBytes(byte[] dest, int pointer, String string) {
		pointer = writeBytes(dest, pointer, (short) string.length());
		return writeBytes(dest, pointer, string.getBytes());
	}
	
	/**
	 * Reads a byte from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to read
	 * @return			Byte of data read
	 */
	public static byte readByte(byte[] src, int pointer) {
		return src[pointer];
	}
	
	/**
	 * Reads a byte array from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to start reading from
	 * @param dest		Array to write to
	 */
	public static void readBytes(byte[] src, int pointer, byte[] dest) {
		for (int i = 0; i < dest.length; i++)
			dest[i] = src[pointer + i];
	}
	
	/**
	 * Reads a short array from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to start reading from
	 * @param dest		Array to write to
	 */
	public static void readShorts(byte[] src, int pointer, short[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = readShort(src, pointer);
			pointer += Type.getSize(Type.SHORT);
		}
	}
	
	/**
	 * Reads a character array from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to start reading from
	 * @param dest		Array to write to
	 */
	public static void readChars(byte[] src, int pointer, char[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = readChar(src, pointer);
			pointer += Type.getSize(Type.CHAR);
		}
	}
	
	/**
	 * Reads a integer array from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to start reading from
	 * @param dest		Array to write to
	 */
	public static void readInts(byte[] src, int pointer, int[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = readInt(src, pointer);
			pointer += Type.getSize(Type.INTEGER);
		}
	}
	
	/**
	 * Reads a long array from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to start reading from
	 * @param dest		Array to write to
	 */
	public static void readLongs(byte[] src, int pointer, long[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = readLong(src, pointer);
			pointer += Type.getSize(Type.LONG);
		}
	}
	
	/**
	 * Reads a float array from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to start reading from
	 * @param dest		Array to write to
	 */
	public static void readFloats(byte[] src, int pointer, float[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = readFloat(src, pointer);
			pointer += Type.getSize(Type.FLOAT);
		}
	}
	
	/**
	 * Reads a double array from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to start reading from
	 * @param dest		Array to write to
	 */
	public static void readDoubles(byte[] src, int pointer, double[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = readDouble(src, pointer);
			pointer += Type.getSize(Type.DOUBLE);
		}
	}
	
	/**
	 * Reads a boolean array from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to start reading from
	 * @param dest		Array to write to
	 */
	public static void readBooleans(byte[] src, int pointer, boolean[] dest) {
		for (int i = 0; i < dest.length; i++) {
			dest[i] = readBoolean(src, pointer);
			pointer += Type.getSize(Type.BOOLEAN);
		}
	}
	
	/**
	 * Reads a short from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to read from
	 * @return			Short equivalent of the data read
	 */
	public static short readShort(byte[] src, int pointer) {
		return ByteBuffer.wrap(src, pointer, 2).getShort();
	}
	
	/**
	 * Reads a character from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to read from
	 * @return			Character equivalent of the data read
	 */
	public static char readChar(byte[] src, int pointer) {
		return ByteBuffer.wrap(src, pointer, 2).getChar();
	}
	
	/**
	 * Reads a integer from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to read from
	 * @return			Integer equivalent of the data read
	 */
	public static int readInt(byte[] src, int pointer) {
		return ByteBuffer.wrap(src, pointer, 4).getInt();
	}
	
	/**
	 * Reads a long from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to read from
	 * @return			Long equivalent of the data read
	 */
	public static long readLong(byte[] src, int pointer) {
		return ByteBuffer.wrap(src, pointer, 8).getLong();
	}
	
	/**
	 * Reads a float from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to read from
	 * @return			Float equivalent of the data read
	 */
	public static float readFloat(byte[] src, int pointer) {
		return Float.intBitsToFloat(readInt(src, pointer));
	}
	
	/**
	 * Reads a double from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to read from
	 * @return			Double equivalent of the data read
	 */
	public static double readDouble(byte[] src, int pointer) {
		return Double.longBitsToDouble(readLong(src, pointer));
	}
	
	/**
	 * Reads a boolean from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to read from
	 * @return			Boolean equivalent of the data read
	 */
	public static boolean readBoolean(byte[] src, int pointer) {
		assert(src[pointer] == 0 || src[pointer] == 1);
		return src[pointer] != 0;
	}
	
	/**
	 * Reads a string from a source array
	 * 
	 * @param src		Array to read from
	 * @param pointer	Integer describing where to read from
	 * @return			String equivalent of the data read
	 */
	public static String readString(byte[] src, int pointer, int length) {
		return new String(src, pointer, length);
	}
}
