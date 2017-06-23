package tk.luminos.serialization;

public interface Serializable<T extends DBBase> {
	
	public T serialize(String name);

}
