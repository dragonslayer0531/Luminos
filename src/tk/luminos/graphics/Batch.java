package tk.luminos.graphics;

public class Batch<T> {
	
	private T[] data;
	private int length = 0;
	
	@SuppressWarnings("unchecked")
	public Batch() {
		data = (T[]) new Object[0];
	}
	
	public void add(T t) {
		resize(length + 1);
		data[length - 1] = t;
	}
	
	public T remove() {
		T t = data[length - 1];
		resize(length - 1);
		return t;
	}
	
	@SuppressWarnings("unchecked")
	public T[] toArray() {
		T[] array = (T[]) new Object[length];
		System.arraycopy(data, 0, array, 0, length);
		return array;
	}
	
	@SuppressWarnings("unchecked")
	private void resize(int newSize) {
		T[] newData = (T[]) new Object[newSize];
		for (int i = 0; i < length; i++) {
			newData[i] = data[i];
		}
		data = newData;
		length++;
	}

}
