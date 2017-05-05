package tk.luminos.graphics.shaders;

/**
 * Uniform variable wrapper for engine.  Variables can either
 * be passed to {@link ShaderProgram} as primitives, math classes,
 * or a Uniform
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Uniform {
	
	private int location;
	private Class<?> type;
	private Object value;
	
	private Uniform(int location, Object value) {
		this.location = location;
		this.value = value;
		this.type = value.getClass();
	}
	
	/**
	 * Creates a new Uniform instance for a given shader and object
	 * 
	 * @param name		Name of variable
	 * @param value		Value of variable
	 * @param program	Program to load to
	 * @return			Uniform that contains information on variable
	 * @throws Exception		Thrown if the relevant {@link ShaderProgram} cannot find the variable
	 */
	public static Uniform loadToShader(String name, Object value, ShaderProgram program) throws Exception {
		program.createUniform(name);
		return new Uniform(program.getLocation(name), value);
	}

	/**
	 * Gets the location of the uniform variable
	 * 
	 * @return	Location of uniform
	 */
	public int getLocation() {
		return location;
	}

	/**
	 * Gets the type of the uniform variable
	 * 
	 * @return	Type of data in uniform
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * Gets the value stored in the uniform variable
	 * 
	 * @return	Value of uniform
	 */
	public Object getValue() {
		return value;
	}

}
