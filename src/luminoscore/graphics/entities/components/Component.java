package luminoscore.graphics.entities.components;

public class Component {
	
	/*
	 * Author: Nick Clark
	 * Created On: 3/16/2016
	 */
	
	//Constructor Field	
	private Object o;
	
	/*
	 * @param o Object
	 * 
	 * Constructor
	 */
	public Component(Object o) {
		this.o = o;
	}
	
	//Getter-Setter Methods
	public Object getComponent() {
		return o;
	}
	
	public void setComponent(Object o) {
		this.o = o;
	}

}
