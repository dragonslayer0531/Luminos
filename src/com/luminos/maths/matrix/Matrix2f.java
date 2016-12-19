package com.luminos.maths.matrix;

import com.luminos.maths.vector.Vector2f;

/**
 * 2x2 Float Matrix
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Matrix2f {
	
	/**
	 * Matrix entries
	 */
	public float 	m00, m01, 
					m10, m11;
	
	/**
	 * Creates new matrix and sets it as identity matrix
	 */
	public Matrix2f() {
		setIdentity();
	}
	
	/**
	 * Sets identity matrix
	 */
	public void setIdentity() {
		this.m00 = 1.0f;
		this.m01 = 0.0f;
		this.m11 = 1.0f;
		this.m10 = 0.0f;
	}
	
	/**
	 * Adds two matrices
	 * 
	 * @param left		Left side matrix
	 * @param right		Right side matrix
	 * @param dest		Destination matrix
	 * @return			If destination matrix is null
	 * 						create new matrix
	 * 						Then, return the sum
	 * 						matrix
	 */
	public static Matrix2f add(Matrix2f left, Matrix2f right, Matrix2f dest) {
		if (dest == null)
			dest = new Matrix2f();

		dest.m00 = left.m00 + right.m00;
		dest.m01 = left.m01 + right.m01;
		dest.m10 = left.m10 + right.m10;
		dest.m11 = left.m11 + right.m11;

		return dest;
	}
	
	/**
	 * Subtracts two matrices
	 * 
	 * @param left		Left side matrix
	 * @param right		Right side matrix
	 * @param dest		Destination matrix
	 * @return			If destination matrix is null
	 * 						create new matrix
	 * 						Then, return the difference
	 * 						matrix
	 */
	public static Matrix2f sub(Matrix2f left, Matrix2f right, Matrix2f dest) {
		if (dest == null)
			dest = new Matrix2f();

		dest.m00 = left.m00 - right.m00;
		dest.m01 = left.m01 - right.m01;
		dest.m10 = left.m10 - right.m10;
		dest.m11 = left.m11 - right.m11;

		return dest;
	}
	
	/**
	 * Multiplies two matrices
	 * 
	 * @param left		Left side matrix
	 * @param right		Right side matrix
	 * @param dest		Destination matrix
	 * @return			If destination matrix is null
	 * 						create new matrix
	 * 						Then return the product
	 * 						matrix
	 */
	public static Matrix2f mul(Matrix2f left, Matrix2f right, Matrix2f dest) {
		if (dest == null)
			dest = new Matrix2f();

		float m00 = left.m00 * right.m00 + left.m10 * right.m01;
		float m01 = left.m01 * right.m00 + left.m11 * right.m01;
		float m10 = left.m00 * right.m10 + left.m10 * right.m11;
		float m11 = left.m01 * right.m10 + left.m11 * right.m11;

		dest.m00 = m00;
		dest.m01 = m01;
		dest.m10 = m10;
		dest.m11 = m11;

		return dest;
	}
	
	/**
	 * Linear transformation of {@link Vector2f}
	 * 
	 * @param left		Transformation matrix
	 * @param right		Input vector
	 * @param dest		Output vector
	 * @return			If destination vector is null
	 * 						create new vector
	 * 						Then return the output vector
	 */
	public static Vector2f transform(Matrix2f left, Vector2f right, Vector2f dest) {
		if (dest == null)
			dest = new Vector2f();

		float x = left.m00 * right.x + left.m10 * right.y;
		float y = left.m01 * right.x + left.m11 * right.y;

		dest.x = x;
		dest.y = y;

		return dest;
	}
	
	/**
	 * Transposes this matrix
	 * 
	 * @param dest		Destination matrix
	 * @return 			Transpose of this matrix
	 */
	public Matrix2f transpose(Matrix2f dest) {
		return transpose(this, dest);
	}
	
	/**
	 * Transposes a matrix
	 * 
	 * @param src		Matrix to transpose
	 * @param dest		Destination matrix
	 * @return			If destination matrix is null
	 * 						create new matrix
	 * 						Then return the output
	 * 						matrix
	 */
	public static Matrix2f transpose(Matrix2f src, Matrix2f dest) {
		if (dest == null)
			dest = new Matrix2f();

		float m01 = src.m10;
		float m10 = src.m01;

		dest.m01 = m01;
		dest.m10 = m10;

		return dest;
	}
	
	/**
	 * Inverts this matrix
	 * 
	 * @return		Inverse of this matrix
	 */
	public Matrix2f invert() {
		return invert(this, this);
	}
	
	/**
	 * Inverts a matrix
	 * 
	 * @param src		Source matrix
	 * @param dest		Destination matrix
	 * @return			If destination matrix is null
	 * 						create new matrix
	 * 						Then return the output
	 * 						matrix
	 */
	public static Matrix2f invert(Matrix2f src, Matrix2f dest) {
		float determinant = src.determinant();
		if (determinant != 0) {
			if (dest == null)
				dest = new Matrix2f();
			float determinant_inv = 1f/determinant;
			float t00 =  src.m11*determinant_inv;
			float t01 = -src.m01*determinant_inv;
			float t11 =  src.m00*determinant_inv;
			float t10 = -src.m10*determinant_inv;

			dest.m00 = t00;
			dest.m01 = t01;
			dest.m10 = t10;
			dest.m11 = t11;
			return dest;
		} else
			return null;
	}
	
	/**
	 * Creates a matrix which negates this matrix
	 * 
	 * @param dest		Destination matrix
	 * @return			Negating matrix
	 */
	public Matrix2f negate(Matrix2f dest) {
		return negate(this, dest);
	}
	
	/**
	 * Creates a negating matrix
	 * 
	 * @param src		Source matrix
	 * @param dest		Destination matrix
	 * @return			If destination matrix is null
	 * 						create new matrix
	 * 						Then return negating matrix
	 */
	public static Matrix2f negate(Matrix2f src, Matrix2f dest) {
		if (dest == null)
			dest = new Matrix2f();

		dest.m00 = -src.m00;
		dest.m01 = -src.m01;
		dest.m10 = -src.m10;
		dest.m11 = -src.m11;

		return dest;
	}
	
	/**
	 * Sets all matrix values to zero
	 * 
	 * @return			Zeroed matrix
	 */
	public Matrix2f setZero() {
		setZero(this);
		return this;
	}
	
	/**
	 * Sets all matrix value to zero
	 * 
	 * @param src		Source matrix
	 * @return			Zeroed matrix
	 */
	public static Matrix2f setZero(Matrix2f src) {
		src.m00 = 0.0f;
		src.m01 = 0.0f;
		src.m10 = 0.0f;
		src.m11 = 0.0f;
		return src;
	}
	
	/**
	 * Calculates the determinant of the matrix
	 * 
	 * @return			the determinant of the matrix
	 */
	public float determinant() {
		return m00 * m11 - m01*m10;
	}

}
