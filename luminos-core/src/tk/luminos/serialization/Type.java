package tk.luminos.serialization;

public class Type {

	public static final byte UNKNOWN 	= 0;
	public static final byte BYTE 		= 1;
	public static final byte SHORT 		= 2;
	public static final byte CHAR 		= 3;
	public static final byte INTEGER	= 4;
	public static final byte LONG	 	= 5;
	public static final byte FLOAT		= 6;
	public static final byte DOUBLE		= 7;
	public static final byte BOOLEAN	= 8;
	
	public static int getSize(byte type) {
		switch (type) {
		case BYTE:		return Byte.BYTES;
		case SHORT:		return Short.BYTES;
		case CHAR:		return Character.BYTES;
		case INTEGER:	return Integer.BYTES;
		case LONG:		return Long.BYTES;
		case FLOAT:		return Float.BYTES;
		case DOUBLE:	return Double.BYTES;
		case BOOLEAN:	return Byte.BYTES;
		}
		assert(false);
		return 0;
	}
	
}
