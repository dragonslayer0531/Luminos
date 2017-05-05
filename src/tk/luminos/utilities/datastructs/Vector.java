package tk.luminos.utilities.datastructs;

import java.util.List;

/**
 * Vector implementation
 * 
 * @author Nick Clark
 * @version 1.0
 * 
 * @param <T> type of data
 */
public class Vector<T> implements VectorADT<T> {
	
	protected Node<T> head;
	private int size = 0;
	
	/**
	 * Default constructor
	 */
	public Vector() {
		head = null;
	}
	
	/**
	 * Constructor
	 * 
	 * @param data	Adds data to vector
	 */
	public Vector(T data) {
		push(data);
	}
	
	/**
	 * Constructor
	 * 
	 * @param data	Adds data to vector
	 */
	public Vector(@SuppressWarnings("unchecked") T... data) {
		push(data);
	}
	
	/**
	 * Push data to vector
	 * 
	 * @param data		Data to add to structure
	 */
	public void push(T data)
	{
		Node<T> old = head;
		head = new Node<T>(data);
		head.next = old;
		size++;
	}
	
	/**
	 * Push a list of data to vector
	 * 
	 * @param data 		Data to add to structure
	 */
	public void push(List<T> data) {
		for (T t : data)
		{
			push(t);
		}
	}
	
	/**
	 * Pushes a set of data to vector
	 * 
	 * @param data		Data to add to structure
	 */
	public void push(@SuppressWarnings("unchecked") T... data)
	{
		for (T t : data)
		{
			push(t);
		}
	}
	
	/**
	 * Removes and returns item from top of structure
	 * 
	 * @return head node data
	 * @precondition	The structure is not empty
	 */
	public T pop() {
		if (size <= 0)
		{
			throw new EmptyStructException();
		}
		T data = head.data;
		head.last = null;
		head = head.next;
		size--;
		return data;
	}
	
	/**
	 * Returns item from top of structure
	 * 
	 * @return head node data
	 * @precondition	The structure is not empty
	 */
	public T peek() {
		if (size <= 0)
		{
			throw new EmptyStructException();
		}
		return head.data;
	}
	
	/**
	 * Gets the size of the data structure
	 * 
	 * @return size of vector
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Gets if the vector is empty
	 * 
	 * @return is vector empty
	 */
	public boolean isEmpty() {
		return head == null;
	}
	
	/**
	 * Dereferences all entries in vector
	 */
	public void clear() {
		while (!isEmpty())
			pop();
	}
	
	/**
	 * Checks if vector is equal to another object
	 * 
	 * @param obj		Object to check equivalence with
	 * @return 			Are equivalent
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj.getClass() != this.getClass()) 
			return false;
		Vector<T> vec = (Vector<T>) obj;
		if (vec.size() != this.size)
			return false;
		Node<T> node = head;
		Node<T> otherNode = vec.head;
		while (node != null) {
			if (!node.data.equals(otherNode.data))
				return false;
			node = node.next;
			otherNode = otherNode.next;
		}
		return true;
	}

}
