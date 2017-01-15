package tk.luminos.graphics.shaders;

public class Uniform {
	
	private int location;
	private Class<?> type;
	private Object value;
	
	private Uniform(int location, Object value) {
		this.location = location;
		this.value = value;
		this.type = value.getClass();
	}
	
	public static Uniform loadToShader(String name, Object value, ShaderProgram program) {
		program.createUniform(name);
		return new Uniform(program.getLocation(name), value);
	}

	public int getLocation() {
		return location;
	}

	public Class<?> getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

}
