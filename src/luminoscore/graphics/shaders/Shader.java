package luminoscore.graphics.shaders;

public class Shader {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/20/2016
	 */
	
	//Constructor Fields
	private String vertexFile, fragmentFile;
	
	/*
	 * @param vertexFile Defines the location of the vertex shader
	 * @param fragmentFile Defines the location of the fragment shader
	 */
	public Shader(String vertexFile, String fragmentFile) {
		this.vertexFile = vertexFile;
		this.fragmentFile = fragmentFile;
	}
	
	//Getter methods
	public String getVertexShader() {
		return vertexFile;
	}
	
	public String getFragmentShader() {
		return fragmentFile;
	}

}
