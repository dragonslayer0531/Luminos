package tk.luminos.utilities.datastructs;

import java.util.List;

interface VectorADT<T> {
	
	public void push(T data);
	public void push(List<T> data);
	public void push(@SuppressWarnings("unchecked") T... data);
	
	public T pop();
	public T peek();
	
	public int size();
	
	public boolean isEmpty();
	public void clear();

}
