package tk.luminos.maths.vector;

/**
 * Vector interface
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public interface Vector {
	
	/**
	 * Calculates magnitude of vector
	 * 
	 * @return	Returns magnitude of vector
	 */
	public float magnitude();
	
	/**
	 * Normalizes vector
	 */
	public void normalize();

}
