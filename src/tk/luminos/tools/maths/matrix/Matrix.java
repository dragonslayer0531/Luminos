package tk.luminos.tools.maths.matrix;

/**
 * 
 * Interface used for matrices
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public interface Matrix {
	
	/**
	 * Sets the identity of the matrix
	 */
	public void setIdentity();
	
	/**
	 * Gets the determinant of the matrix
	 * 
	 * @return	determinant
	 */
	public float determinant();

}
