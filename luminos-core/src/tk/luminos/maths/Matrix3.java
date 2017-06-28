package tk.luminos.maths;

/**
 * 3x3 Float Matrix
 * 
 * @author Nick Clark
 * @version 1.0
 *
 */

public class Matrix3 {
	
	/**
	 * Matrix entries
	 */
	public float 	m00, m01, m02,
					m10, m11, m12,
					m20, m21, m22;
	
	public final static int SIZE = 3 * 3;
	
	/**
	 * Creates new matrix and sets it as identity matrix
	 */
	public Matrix3() {
		setIdentity();
	}
	
	/**
	 * Sets identity matrix
	 */
	public void setIdentity() {
		this.m00 = 1.0f;
		this.m01 = 0.0f;
		this.m02 = 0.0f;
		this.m10 = 0.0f;
		this.m11 = 1.0f;
		this.m12 = 0.0f;
		this.m20 = 0.0f;
		this.m21 = 0.0f;
		this.m22 = 1.0f;
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
	public static Matrix3 add(Matrix3 left, Matrix3 right, Matrix3 dest) {
		if (dest == null)
			dest = new Matrix3();

		dest.m00 = left.m00 + right.m00;
		dest.m01 = left.m01 + right.m01;
		dest.m02 = left.m02 + right.m02;
		dest.m10 = left.m10 + right.m10;
		dest.m11 = left.m11 + right.m11;
		dest.m12 = left.m12 + right.m12;
		dest.m20 = left.m20 + right.m20;
		dest.m21 = left.m21 + right.m21;
		dest.m22 = left.m22 + right.m22;

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
	public static Matrix3 sub(Matrix3 left, Matrix3 right, Matrix3 dest) {
		if (dest == null)
			dest = new Matrix3();

		dest.m00 = left.m00 - right.m00;
		dest.m01 = left.m01 - right.m01;
		dest.m02 = left.m02 - right.m02;
		dest.m10 = left.m10 - right.m10;
		dest.m11 = left.m11 - right.m11;
		dest.m12 = left.m12 - right.m12;
		dest.m20 = left.m20 - right.m20;
		dest.m21 = left.m21 - right.m21;
		dest.m22 = left.m22 - right.m22;

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
	public static Matrix3 mul(Matrix3 left, Matrix3 right, Matrix3 dest) {
		if (dest == null)
			dest = new Matrix3();

		float m00 =
			left.m00 * right.m00 + left.m10 * right.m01 + left.m20 * right.m02;
		float m01 =
			left.m01 * right.m00 + left.m11 * right.m01 + left.m21 * right.m02;
		float m02 =
			left.m02 * right.m00 + left.m12 * right.m01 + left.m22 * right.m02;
		float m10 =
			left.m00 * right.m10 + left.m10 * right.m11 + left.m20 * right.m12;
		float m11 =
			left.m01 * right.m10 + left.m11 * right.m11 + left.m21 * right.m12;
		float m12 =
			left.m02 * right.m10 + left.m12 * right.m11 + left.m22 * right.m12;
		float m20 =
			left.m00 * right.m20 + left.m10 * right.m21 + left.m20 * right.m22;
		float m21 =
			left.m01 * right.m20 + left.m11 * right.m21 + left.m21 * right.m22;
		float m22 =
			left.m02 * right.m20 + left.m12 * right.m21 + left.m22 * right.m22;

		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;

		return dest;
	}
	
	/**
	 * Linear transformation of {@link Vector3}
	 * 
	 * @param left		Transformation matrix
	 * @param right		Input vector
	 * @param dest		Output vector
	 * @return			If destination vector is null
	 * 						create new vector
	 * 						Then return the output vector
	 */
	public static Vector3 transform(Matrix3 left, Vector3 right, Vector3 dest) {
		if (dest == null)
			dest = new Vector3();

		float x = left.m00 * right.x + left.m10 * right.y + left.m20 * right.z;
		float y = left.m01 * right.x + left.m11 * right.y + left.m21 * right.z;
		float z = left.m02 * right.x + left.m12 * right.y + left.m22 * right.z;

		dest.x = x;
		dest.y = y;
		dest.z = z;

		return dest;
	}
	
	/**
	 * Transposes this matrix
	 * 
	 * @param dest		Destination matrix
	 * @return 			Transpose of this matrix
	 */
	public Matrix3 transpose(Matrix3 dest) {
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
	public static Matrix3 transpose(Matrix3 src, Matrix3 dest) {
		if (dest == null)
		   dest = new Matrix3();
		float m00 = src.m00;
		float m01 = src.m10;
		float m02 = src.m20;
		float m10 = src.m01;
		float m11 = src.m11;
		float m12 = src.m21;
		float m20 = src.m02;
		float m21 = src.m12;
		float m22 = src.m22;

		dest.m00 = m00;
		dest.m01 = m01;
		dest.m02 = m02;
		dest.m10 = m10;
		dest.m11 = m11;
		dest.m12 = m12;
		dest.m20 = m20;
		dest.m21 = m21;
		dest.m22 = m22;
		return dest;
	}
	
	/**
	 * Calculates the determinant of the matrix
	 * 
	 * @return			the determinant of the matrix
	 */
	public float determinant() {
		float f =
			m00 * (m11 * m22 - m12 * m21)
				+ m01 * (m12 * m20 - m10 * m22)
				+ m02 * (m10 * m21 - m11 * m20);
		return f;
	}
	
	/**
	 * Inverts this matrix
	 * 
	 * @return		Inverse of this matrix
	 */
	public Matrix3 invert() {
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
	public static Matrix3 invert(Matrix3 src, Matrix3 dest) {
		float determinant = src.determinant();

		if (determinant != 0) {
			if (dest == null)
				dest = new Matrix3();
			 float determinant_inv = 1f/determinant;

			 float t00 = src.m11 * src.m22 - src.m12* src.m21;
			 float t01 = - src.m10 * src.m22 + src.m12 * src.m20;
			 float t02 = src.m10 * src.m21 - src.m11 * src.m20;
			 float t10 = - src.m01 * src.m22 + src.m02 * src.m21;
			 float t11 = src.m00 * src.m22 - src.m02 * src.m20;
			 float t12 = - src.m00 * src.m21 + src.m01 * src.m20;
			 float t20 = src.m01 * src.m12 - src.m02 * src.m11;
			 float t21 = -src.m00 * src.m12 + src.m02 * src.m10;
			 float t22 = src.m00 * src.m11 - src.m01 * src.m10;

			 dest.m00 = t00*determinant_inv;
			 dest.m11 = t11*determinant_inv;
			 dest.m22 = t22*determinant_inv;
			 dest.m01 = t10*determinant_inv;
			 dest.m10 = t01*determinant_inv;
			 dest.m20 = t02*determinant_inv;
			 dest.m02 = t20*determinant_inv;
			 dest.m12 = t21*determinant_inv;
			 dest.m21 = t12*determinant_inv;
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
	public Matrix3 negate(Matrix3 dest) {
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
	public static Matrix3 negate(Matrix3 src, Matrix3 dest) {
		if (dest == null)
			dest = new Matrix3();

		dest.m00 = -src.m00;
		dest.m01 = -src.m02;
		dest.m02 = -src.m01;
		dest.m10 = -src.m10;
		dest.m11 = -src.m12;
		dest.m12 = -src.m11;
		dest.m20 = -src.m20;
		dest.m21 = -src.m22;
		dest.m22 = -src.m21;
		return dest;
	}
	
	/**
	 * Sets all matrix values to zero
	 * 
	 * @return			Zeroed matrix
	 */
	public Matrix3 setZero() {
		return setZero(this);
	}
	
	/**
	 * Sets all matrix value to zero
	 * 
	 * @param m			Source matrix
	 * @return			Zeroed matrix
	 */
	public static Matrix3 setZero(Matrix3 m) {
		m.m00 = 0.0f;
		m.m01 = 0.0f;
		m.m02 = 0.0f;
		m.m10 = 0.0f;
		m.m11 = 0.0f;
		m.m12 = 0.0f;
		m.m20 = 0.0f;
		m.m21 = 0.0f;
		m.m22 = 0.0f;
		return m;
	}

}
