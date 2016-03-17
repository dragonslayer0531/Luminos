package luminoscore.graphics.entities.components;

public class Component {
	
	private Object o;
	
	public Component(Object o) {
		this.o = o;
	}
	
	public Object getComponent() {
		return o;
	}

}
