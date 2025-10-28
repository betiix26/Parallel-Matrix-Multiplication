package ace.ucv.utils;

import ace.ucv.model.Matrix;

public class MatrixUtility {

	public static Matrix add(Matrix matrixA, Matrix matrixB) {
		int[][] result = new int[matrixA.getRows()][matrixA.getCols()];

		for (int i = 0; i < matrixA.getRows(); i++) {
			for (int j = 0; j < matrixA.getCols(); j++) {
				result[i][j] = matrixA.getData()[i][j] + matrixB.getData()[i][j];
			}
		}

		return new Matrix(result, matrixA.getRows(), matrixA.getCols());
	}

	/**
	 * Subtracts the second matrix from the first matrix element-wise.
	 * 
	 * @param matrixA first matrix
	 * @param matrixB second matrix
	 * @return resulting matrix after subtraction
	 */
	public static Matrix subtract(Matrix matrixA, Matrix matrixB) {
		int[][] result = new int[matrixA.getRows()][matrixA.getCols()];

		for (int i = 0; i < matrixA.getRows(); i++) {
			for (int j = 0; j < matrixA.getCols(); j++) {
				result[i][j] = matrixA.getData()[i][j] - matrixB.getData()[i][j];
			}
		}

		return new Matrix(result, matrixA.getRows(), matrixA.getCols());
	}

	/**
	 * Pads a matrix to the next power-of-two dimensions. This is required for
	 * Strassen's algorithm.
	 * 
	 * @param matrix original matrix
	 * @return padded matrix
	 */
	public static Matrix padMatrix(Matrix matrix) {
		int n = matrix.getRows();
		int m = matrix.getCols();
		int newSize = (int) Math.pow(2, Math.ceil(Math.log(Math.max(n, m)) / Math.log(2)));

		if (n == newSize && m == newSize) {
			return matrix;
		}

		int[][] paddedData = new int[newSize][newSize];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				paddedData[i][j] = matrix.getData()[i][j];
			}
		}

		return new Matrix(paddedData, newSize, newSize);
	}

	/**
	 * Splits a square matrix into four equal submatrices (quadrants). Used in
	 * recursive algorithms like Strassen's.
	 * 
	 * @param matrix matrix to split
	 * @return array containing 4 submatrices [top-left, top-right, bottom-left,
	 *         bottom-right]
	 */
	public static Matrix[] split(Matrix matrix) {
		int newSize = matrix.getRows() / 2;
		Matrix[] subMatrices = new Matrix[4];

		int[][] a11 = new int[newSize][newSize]; // top-left
		int[][] a12 = new int[newSize][newSize]; // top-right
		int[][] a21 = new int[newSize][newSize]; // bottom-left
		int[][] a22 = new int[newSize][newSize]; // bottom-right

		for (int i = 0; i < newSize; i++) {
			for (int j = 0; j < newSize; j++) {
				a11[i][j] = matrix.getData()[i][j]; // top-left submatrix
				a12[i][j] = matrix.getData()[i][j + newSize]; // top-right submatrix
				a21[i][j] = matrix.getData()[i + newSize][j]; // bottom-left submatrix
				a22[i][j] = matrix.getData()[i + newSize][j + newSize]; // bottom-right submatrix
			}
		}

		subMatrices[0] = new Matrix(a11, newSize, newSize);
		subMatrices[1] = new Matrix(a12, newSize, newSize);
		subMatrices[2] = new Matrix(a21, newSize, newSize);
		subMatrices[3] = new Matrix(a22, newSize, newSize);

		return subMatrices;
	}

	/**
	 * Combines four submatrices into one larger square matrix. Opposite of split().
	 * 
	 * @param C11  top-left
	 * @param C12  top-right
	 * @param C21  bottom-left
	 * @param C22  bottom-right
	 * @param size size of the final matrix
	 * @return combined matrix
	 */
	public static Matrix combine(Matrix C11, Matrix C12, Matrix C21, Matrix C22, int size) {
		int[][] result = new int[size][size];
		int newSize = size / 2;

		for (int i = 0; i < newSize; i++) {
			for (int j = 0; j < newSize; j++) {
				result[i][j] = C11.getData()[i][j];
				result[i][j + newSize] = C12.getData()[i][j];
				result[i + newSize][j] = C21.getData()[i][j];
				result[i + newSize][j + newSize] = C22.getData()[i][j];
			}
		}

		return new Matrix(result, size, size);
	}

	/**
	 * Crops a padded matrix back to its original dimensions. Useful after Strassen
	 * multiplication to remove extra zeros.
	 * 
	 * @param matrix       padded matrix
	 * @param originalRows original number of rows
	 * @param originalCols original number of columns
	 * @return cropped matrix
	 */
	public static Matrix cropMatrix(Matrix matrix, int originalRows, int originalCols) {
		int[][] croppedData = new int[originalRows][originalCols];
		for (int i = 0; i < originalRows; i++) {
			for (int j = 0; j < originalCols; j++) {
				croppedData[i][j] = matrix.getData()[i][j];
			}
		}
		return new Matrix(croppedData, originalRows, originalCols);
	}
}
