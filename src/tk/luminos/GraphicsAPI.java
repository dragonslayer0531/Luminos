package tk.luminos;

/**
 * Enumeration for all supported graphics APIs
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public enum GraphicsAPI {
	
	OPENGL((short) 0),
	VULKAN((short) 1);
	
	private short id;
	
	/**
	 * Constructor
	 * 
	 * @param id		Enumeration ID
	 */
	private GraphicsAPI(short id) {
		this.id = id;
	}
	
	/**
	 * Gets the id of the enumeration
	 * 
	 * @return	id of the enumeration
	 */
	public short getID() {
		return id;
	}
	
	/**
	 * Gets the GraphicsAPI based on the provided id value
	 * 
	 * @param id		ID value to be probed
	 * @return			GraphicsAPI corresponding to provided ID value
	 */
	public static GraphicsAPI getAPIByID(short id) {
		GraphicsAPI[] apis = GraphicsAPI.values();
		for(GraphicsAPI api : apis) {
			if(api.getID() == id) return api;
		}
		return null;
	}

}
