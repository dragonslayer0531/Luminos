package tk.luminos.maths.vector;

/**
 * Interface for vectors
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */
public interface Vector {
	
	/**
	 * Gets the magnitude of the vector
	 * 
	 * @return	magnitude of the vector
	 */
	public float magnitude();
	
	/**
	 * Normalises the vector
	 */
	public void normalise();

}
