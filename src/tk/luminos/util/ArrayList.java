package tk.luminos.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Array based implementation of {@link List}.
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * @param <T>	Type parameter of list
 */
public class ArrayList<T> implements List<T> {
	
	private T[] data;
	private int size;
	
	private static final int DEFAULT_SIZE = 256;
	
	/**
	 * Creates default ArrayList
	 */
	public ArrayList() {
		this(DEFAULT_SIZE);
	}
	
	/**
	 * Creates new ArrayList with given size
	 * 
	 * @param size		initial size of list
	 */
	@SuppressWarnings("unchecked")
	public ArrayList(int size) {
		data = (T[]) new Object[size];
		size = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<T> iterator() {
		return new ArrayListIterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(T obj) {
		if (size == data.length) // checks if array is full
			expand();
		data[size++] = obj; // adds entry to array
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(int index, T obj) {
		if (index < 0 || index > size) // checks if the provided index is out of bounds
			throw new ArrayIndexOutOfBoundsException("Index is invalid.");
		if (size == data.length) // checks if array is full
			expand();
		move(index); // moves entries down a slot
		data[size++] = obj; // adds entry to array
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T get(int index) {
		if (index < 0 || index >= size) // checks if the provided index is out of bounds
			throw new ArrayIndexOutOfBoundsException("Index is invalid");
		return data[index]; // return value at index provided
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(T obj) {
		for (int i = 0; i < size; i++) {
			if (obj.equals(data[i])) // if found, return true
				return true;
		}
		return false; // returns false if array is traversed without finding the entry
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(T obj) {
		for (int i = 0; i < size; i++) {
			if (data[i].equals(obj)) { // loop until found
				closeHole(i); // close hole left at index (i)
				size--; // decrement size
				return true; // return true
			}
		}
		return false; // return false if not found
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T remove(int index) {
		if (index < 0 || index >= size) // check if the provided index is out of bounds
			throw new ArrayIndexOutOfBoundsException("Index is invalid");
		T temp = data[index]; // get the value at the index
		closeHole(index); // close the hole left in the array
		size--; // decrement size
		return temp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Stream<T> stream() {
		return Arrays.stream(data);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void clear() {
		data = (T[]) new Object[DEFAULT_SIZE];
		size = 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	@SuppressWarnings("unchecked")
	private void expand() {
		T[] temp = data; // create copy of array
		data = (T[]) new Object[temp.length * 2]; // recreate data array at 2x capacity
		for (int i = 0; i < size; i++)
			data[i] = temp[i]; // copy contents from data copy back to expanded data array
	}
	
	private void move(int index) {
		for (int i = size; i > index; i++) 
			data[i] = data[i - 1]; // copy the data from (i - 1) to (i)
	}
	
	private void closeHole(int index) {
		for (int i = index; i < size; i++) 
			data[i] = data[i + 1]; // copy the data from (i + 1) to (i)
		data[size] = null; // set last entry to null to remove repeat
	}
	
	private class ArrayListIterator implements Iterator<T> {

		int index = 0;
		
		@Override
		public boolean hasNext() {
			return index < size;
		}

		@Override
		public T next() {
			return data[index++];
		}
		
		@Override
		public void remove() {
			ArrayList.this.remove(index);
		}
		
	}

}
