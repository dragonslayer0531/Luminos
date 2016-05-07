package luminosutils.serialization;

public class LDatabase extends Base {
	
	public static final byte[] HEADER = "LDB".getBytes();
	public static final short VERSION = 0x0100;
	public static final byte CONTAINER_TYPE = ContainerType.DATABASE;

	public int getSize() {
		return 0;
	}

}
