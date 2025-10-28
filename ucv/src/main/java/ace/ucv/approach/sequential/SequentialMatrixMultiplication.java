package ace.ucv.approach.sequential;

import ace.ucv.model.Matrix;

/**
 * Implements classic sequential matrix multiplication. Each element in the
 * result matrix is computed by summing the products of corresponding elements
 * from the row of the first matrix and the column of the second matrix.
 */
public class SequentialMatrixMultiplication {

	/**
	 * Multiplies two matrices in a sequential, single-threaded manner.
	 *
	 * @param matrixA the first matrix
	 * @param matrixB the second matrix
	 * @return the resulting matrix after multiplication
	 * @throws IllegalArgumentException if the number of columns in matrixA does not
	 *                                  match the number of rows in matrixB
	 */
	public Matrix multiply(Matrix matrixA, Matrix matrixB) {
		if (matrixA.getCols() != matrixB.getRows()) {
			throw new IllegalArgumentException(
					"The number of columns in the first matrix must be equal to the number of rows in the second.");
		}

		int rowsA = matrixA.getRows();
		int colsA = matrixA.getCols();
		int colsB = matrixB.getCols();

		// initialize the result matrix with zeros
		int[][] resultData = new int[rowsA][colsB];

		// compute each element of the result matrix
		for (int i = 0; i < rowsA; i++) {
			for (int j = 0; j < colsB; j++) {
				resultData[i][j] = 0; // compute each element of the result matrix
				for (int k = 0; k < colsA; k++) {
					resultData[i][j] += matrixA.getData()[i][k] * matrixB.getData()[k][j];
				}
			}
		}

		// return the computed matrix as a Matrix object
		return new Matrix(resultData, rowsA, colsB);
	}
}
