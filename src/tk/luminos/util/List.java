package tk.luminos.util;

import java.util.stream.Stream;

/**
 * Interface for all list classes
 * 
 * @author Nick Clark
 * @version 1.0
 *
 * @param <T>	type of object stored
 */
public interface List<T> extends Iterable<T> {
	
	/**
	 * Adds an object to the list
	 * 
	 * @param obj 		Object to add
	 */
	public void add(T obj);
	
	/**
	 * Adds an object to the list at a given position.  If the index is out of
	 * bounds, the method throws an {@link ArrayIndexOutOfBounds} exception.
	 * 
	 * @param index		Index to add at
	 * @param obj		Object to add
	 */
	public void add(int index, T obj);
	
	/**
	 * Gets the object at a specified index.  If the index is out of
	 * bounds, the method throws an {@link ArrayIndexOutOfBounds} exception.
	 * 
	 * @param index		index to retreive
	 * @return 			Object at index
	 */
	public T get(int index);
	
	/**
	 * Checks if the object is in the list
	 * 
	 * @param obj		Object to check
	 * @return 			If object is in list
	 */
	public boolean contains(T obj);
	
	/**
	 * Removes the given object from the list.  If the object is not in the
	 * list, it is guaranteed to return false.
	 * 
	 * @param obj		Object to remove
	 * @return			If the object was successfully removed
	 */
	public boolean remove(T obj);
	
	/**
	 * Removes the object at the given index from the list.  If the index is out of
	 * bounds, the method throws an {@link ArrayIndexOutOfBounds} exception.
	 * 
	 * @param index		Index of object to remove
	 * @return 			Object at that instance
	 */
	public T remove(int index);
	
	/**
	 * Gets the size of the list
	 * 
	 * @return			Size of the list
	 */
	public int size();
	
	/**
	 * Creates and returns a new stream
	 * 
	 * @return			Stream of contents of list
	 */
	public Stream<T> stream();
	
	/**
	 * Clears the list's contents
	 */
	public void clear();
	
	/**
	 * Checks if the list is empty
	 * 
	 * @return			If the list is empty
	 */
	public boolean isEmpty();

}
