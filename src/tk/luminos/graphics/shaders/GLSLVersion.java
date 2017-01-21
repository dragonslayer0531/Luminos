package tk.luminos.graphics.shaders;

public enum GLSLVersion {
	
	VERSION110("#version 110"),
	VERSION120("#version 120"),
	VERSION130("#version 130"),
	VERSION140("#version 140"),
	VERSION150("#version 150"),
	VERSION330("#version 330"),
	VERSION400("#version 400"),
	VERSION410("#version 410"),
	VERSION420("#version 420"),
	VERSION430("#version 430"),
	VERSION440("#version 440"),
	VERSION450("#version 450");
	
	private String name;
	private GLSLVersion(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the header for shaders of the provided GLSL version
	 * 
	 * @param version		GLSL Version
	 * @param core			Uses GLSL core
	 * @return				Header of shaders
	 */
	public static String getHeader(GLSLVersion version, boolean core) {
		if (core) 
			return version.name + " core";
		return version.name;
	}

}
