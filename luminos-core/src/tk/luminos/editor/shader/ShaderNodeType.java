package tk.luminos.editor.shader;

public enum ShaderNodeType {
	
	      INPUT(0x00),
	 	 OUTPUT(0x01),
	CALCULATION(0x02);
	
	int type;
	ShaderNodeType(int type) {
		this.type = type;
	}

}
