package ace.ucv.service.generator;

import java.util.Random;

import ace.ucv.model.Matrix;

public class RandomMatrixGenerator {

    public Matrix generateRandomMatrix(int rows, int cols) {
        Matrix matrix = new Matrix(rows, cols);
        Random rand = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.getData()[i][j] = rand.nextInt(100);  
            }
        }
        return matrix;
    }
}
